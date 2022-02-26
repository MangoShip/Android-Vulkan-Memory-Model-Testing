#include <jni.h>
#include <string>
#include <stdlib.h>
#include <vector>
#include <thread>
#include <iostream>
#include <fstream>
#include <sstream>
#include <set>
#include <chrono>
#include <android/log.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

// Printing log tool for Android Studio's Logcat
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__))

// The corw1 litmus test checks SC-per-location by ensuring a read and a write to the same address
// cannot be re-ordered on one thread.

namespace corw1 {

    using namespace std;

    constexpr char *TAG = "MainActivityCorw1";
    constexpr char *SHADER_NAME = "corw1.spv";
    constexpr char *OUTPUT_NAME = "corw1_output.txt";

    const int minWorkgroups = 4;
    const int maxWorkgroups = 36;
    const int minWorkgroupSize = 1;
    const int maxWorkgroupSize = 64;
    const int shufflePct = 100;
    const int barrierPct = 85;
    const int numMemLocations = 1;
    const int testMemorySize = 1024;
    const int numOutputs = 1;
    const int scratchMemorySize = 4096;
    const int memStride = 64;
    const int memStressPct = 100;
    const int memStressIterations = 1000;
    const int memStressPattern = 1;
    const int preStressPct = 100;
    const int preStressIterations = 100;
    const int preStressPattern = 3;
    const int stressLineSize = 256;
    const int stressTargetLines = 2;
    const int gpuDeviceId = 7857;
    const char* testName = "corw1";
    const char* weakBehaviorStr = "r0: 1";
    const int testIterations = 1000;
    int weakBehavior = 0;
    int nonWeakBehavior = 0;
    const int sampleInterval = 1000;

    class LitmusTester {

    private:
        typedef enum StressAssignmentStrategy {ROUND_ROBIN, CHUNKING} StressAssignmentStrategy;
        StressAssignmentStrategy stressAssignmentStrategy = ROUND_ROBIN;

    public:
        void run(ofstream &outputFile, string testFile) {
            outputFile << "Starting " << testName << " litmus test run \n";

            // Get instance and device
            auto instance = easyvk::Instance(true);
            auto device = getDevice(&instance, outputFile);

            outputFile << "Weak behavior to watch for: " << weakBehaviorStr << "\n";
            outputFile << "Sampling output approximately every " << sampleInterval
                       << " iterations\n";

            // setup devices, memory, and parameters
            auto testData = easyvk::Buffer(device, testMemorySize);
            auto memLocations = easyvk::Buffer(device, numMemLocations);
            auto results = easyvk::Buffer(device, numOutputs);
            auto shuffleIds = easyvk::Buffer(device, maxWorkgroups*maxWorkgroupSize);
            auto barrier = easyvk::Buffer(device, 1);
            auto scratchpad = easyvk::Buffer(device, scratchMemorySize);
            auto scratchLocations = easyvk::Buffer(device, maxWorkgroups);
            auto stressParams = easyvk::Buffer(device, 7);
            std::vector<easyvk::Buffer> testBuffers = {testData, memLocations, results, shuffleIds, barrier, scratchpad, scratchLocations, stressParams};

            // Start timer
            std::chrono::time_point<std::chrono::system_clock> start, end;
            start = std::chrono::system_clock::now();

            // Run tests
            for (int i = 0; i < testIterations; i++) {
                // Set up program with shader file
                auto program = easyvk::Program(device, testFile.c_str(), testBuffers);

                // Get number and size of work group
                int numWorkgroups = setNumWorkgroups();
                int workgroupSize = setWorkgroupSize();

                clearMemory(testData, testMemorySize);
                setMemLocations(memLocations);
                clearMemory(results, numOutputs);
                setShuffleIds(shuffleIds, numWorkgroups, workgroupSize);
                clearMemory(barrier, 1);
                clearMemory(scratchpad, scratchMemorySize);
                setScratchLocations(scratchLocations, numWorkgroups);
                setStressParams(stressParams);

                program.setWorkgroups(numWorkgroups);
                program.setWorkgroupSize(workgroupSize);
                program.prepare();
                program.run();

                // Checking result
                checkResult(testData, results, memLocations, outputFile);

                // Destroy program
                program.teardown();
            }
            // End timer
            end = std::chrono::system_clock::now();
            std::chrono::duration<double> elapsed_seconds = end - start;

            // Output time duration
            outputFile << "elapsed time: " << elapsed_seconds.count() << "s\n";
            outputFile << "iterations per second: " << testIterations / elapsed_seconds.count()
                       << " \n";

            // Destroy buffer, device, and instance
            for (easyvk::Buffer buffer : testBuffers) {
                buffer.teardown();
            }
            device.teardown();
            instance.teardown();
        }

        // Go through instance's available devices and determine which device to use
        easyvk::Device getDevice(easyvk::Instance* instance, ofstream &outputFile) {
            int idx = 0;
            if (gpuDeviceId != -1) {
                int j = 0;
                for (easyvk::Device _device : instance->devices()) {
                    if (_device.properties().deviceID == gpuDeviceId) {
                        idx = j;
                        break;
                    }
                    j++;
                    _device.teardown();
                }
            }
            easyvk::Device device = instance->devices().at(idx);
            outputFile << "Using device " << device.properties().deviceName << "\n";
            return device;
        }

        // Checks how many weak and non weak behaviors produced from test
        void checkResult(easyvk::Buffer &testData, easyvk::Buffer &results, easyvk::Buffer &memLocations, ofstream &outputFile) {
            if (rand() % sampleInterval == 1) {
                outputFile << "r0: " << results.load(0) << "\n";
            }
            if (results.load(0) == 1) { // instruction re-ordered, weak behavior
                weakBehavior++;
            } else {
                nonWeakBehavior++;
            }
        }

        void clearMemory(easyvk::Buffer &gpuMem, int size) {
            for (int i = 0; i < size; i++) {
                gpuMem.store(i, 0);
            }
        }

        void setShuffleIds(easyvk::Buffer &ids, int numWorkgroups, int workgroupSize) {
            // initialize identity mapping
            for (int i = 0; i < numWorkgroups*workgroupSize; i++) {
                ids.store(i, i);
            }
            if (percentageCheck(shufflePct)) {
                // shuffle workgroups
                for (int i = numWorkgroups - 1; i >= 0; i--) {
                    int x = rand() % (i + 1);
                    if (workgroupSize > 1) {
                        // swap and shuffle invocations within a workgroup
                        for (int j = 0; j < workgroupSize; j++) {
                            uint32_t temp = ids.load(i*workgroupSize + j);
                            ids.store(i*workgroupSize + j, ids.load(x*workgroupSize + j));;
                            ids.store(x*workgroupSize + j, temp);
                        }
                        for (int j = workgroupSize - 1; j > 0; j--) {
                            int y = rand() % (j + 1);
                            uint32_t temp = ids.load(i * workgroupSize + y);
                            ids.store(i * workgroupSize + y, ids.load(i * workgroupSize + j));
                            ids.store(i * workgroupSize + j, temp);
                        }
                    } else {
                        uint32_t temp = ids.load(i);
                        ids.store(i, ids.load(x));
                        ids.store(x, temp);
                    }
                }
            }
        }

        void setMemLocations(easyvk::Buffer &locations) {
            std::set<int> usedRegions;
            int numRegions = testMemorySize / memStride;
            for (int i = 0; i < numMemLocations; i++) {
                int region = rand() % numRegions;
                while(usedRegions.count(region))
                    region = rand() % numRegions;
                int locInRegion = rand() % (memStride);
                locations.store(i, (region * memStride) + locInRegion);
                usedRegions.insert(region);
            }
        }

        /** Sets the stress regions and the location in each region to be stressed. Uses the stress assignment strategy to assign
         * workgroups to specific stress locations.
         */
        void setScratchLocations(easyvk::Buffer &locations, int numWorkgroups) {
            std::set <int> usedRegions;
            int numRegions = scratchMemorySize / stressLineSize;
            for (int i = 0; i < stressTargetLines; i++) {
                int region = rand() % numRegions;
                while(usedRegions.count(region))
                    region = rand() % numRegions;
                int locInRegion = rand() % (stressLineSize);
                switch (stressAssignmentStrategy) {
                    case ROUND_ROBIN:
                        for (int j = i; j < numWorkgroups; j += stressTargetLines) {
                            locations.store(j, (region * stressLineSize) + locInRegion);
                        }
                        break;
                    case CHUNKING:
                        int workgroupsPerLocation = numWorkgroups/stressTargetLines;
                        for (int j = 0; j < workgroupsPerLocation; j++) {
                            locations.store(i*workgroupsPerLocation + j, (region * stressLineSize) + locInRegion);
                        }
                        if (i == stressTargetLines - 1 && numWorkgroups % stressTargetLines != 0) {
                            for (int j = 0; j < numWorkgroups % stressTargetLines; j++) {
                                locations.store(numWorkgroups - j - 1, (region * stressLineSize) + locInRegion);
                            }
                        }
                        break;
                }
            }
        }

        // Set random number of work group size
        int setWorkgroupSize() {
            if (minWorkgroupSize == maxWorkgroupSize) {
                return minWorkgroupSize;
            } else {
                int size = rand() % (maxWorkgroupSize - minWorkgroupSize);
                return minWorkgroupSize + size;
            }
        }

        // Set random number of work group
        int setNumWorkgroups() {
            if (minWorkgroups == maxWorkgroups) {
                return minWorkgroups;
            } else {
                int size = rand() % (maxWorkgroups - minWorkgroups);
                return minWorkgroups + size;
            }
        }

        /**
         * 0: barrier
         * 1: memory stress
         * 2: memory stress iterations
         * 3: memory stress pattern
         * 4: pre-stress
         * 5: pre-stress iterations
         * 6: pre-stress pattern
         */
        void setStressParams(easyvk::Buffer &params) {
            if (percentageCheck(barrierPct)) {
                params.store(0, 1);
            } else {
                params.store(0, 0);
            }
            if (percentageCheck(memStressPct)) {
                params.store(1, 1);
            } else {
                params.store(1, 0);
            }
            params.store(2, memStressIterations);
            params.store(3, memStressPattern);
            if (percentageCheck(preStressPct)) {
                params.store(4, 1);
            } else {
                params.store(4, 0);
            }
            params.store(5, preStressIterations);
            params.store(6, preStressPattern);
        }

        bool percentageCheck(int percentage) {
            return rand() % 100 < percentage;
        }
    };

    /*
     * Functions below this point are used for running litmus test on Android App
     */
    jint runTest(std::string filePath) {
        srand (time(NULL));
        LitmusTester app;
        // Get output file and shader file.
        std::ofstream outputFile(filePath + "/" + OUTPUT_NAME);
        std::string testFile = filePath + "/" + SHADER_NAME;

        // Run test
        try {
            app.run(outputFile, testFile);
            // Get weak behavior and non weak behavior
            outputFile << "weak behavior: " << weakBehavior << "\n";
            outputFile << "non weak behavior: " << nonWeakBehavior << "\n";
        }
        catch (const std::runtime_error& e) {
            outputFile << e.what() << "\n";
            outputFile.close();
            return 1;
        }
        outputFile.close();
        return 0;
    }

    std::string getFileDirFromJava(JNIEnv *env, jobject obj);
    std::string readOutput(std::string filePath);

    void readFileFromResRaw(JNIEnv *env, jobject obj) {
        LOGD("Get file path via JNI");
        std::string filePath = getFileDirFromJava(env, obj);

        runTest(filePath);
        LOGD(
                "%s/%s:\n%s",
                filePath.c_str(),
                OUTPUT_NAME,
                readOutput(filePath + "/" + OUTPUT_NAME).c_str());
    }

    std::string getFileDirFromJava(JNIEnv *env, jobject obj) {
        jclass clazz = env->GetObjectClass(obj);
        jmethodID method = env->GetMethodID(clazz, "getFileDir", "()Ljava/lang/String;");
        jobject ret = env->CallObjectMethod(obj, method);

        jstring jFilePath = (jstring) ret;

        return std::string(env->GetStringUTFChars(jFilePath, nullptr));
    }

    std::string readOutput(std::string filePath) {
        std::ifstream ifs(filePath);
        std::stringstream ss;

        ss << ifs.rdbuf();

        return ss.str();
    }
}