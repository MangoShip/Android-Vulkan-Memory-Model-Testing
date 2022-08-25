# Android Vulkan Memory Model Testing
This app is focused on using litmus tests to showcase the allowed behaviors on Vulkan memory model on Android devices. This work is originated from [WebGPU Memory Model Testing](https://gpuharbor.ucsc.edu/webgpu-mem-testing/).

## Requirement
This app is required to be run on devices with 8.0+ and GPU that supports Vulkan 1.1.

## Download
Visit this [link](https://drive.google.com/drive/folders/1B1Kn9Dmfet_Q7UW7Az6kt66jw9kdjAEL?usp=sharing) to download the APK file for Pixel CoRR Testing version of this app.

## Result Gallery
Visit this [link](https://mangoship.github.io/Android-Vulkan-Result-Gallery/) to see a table of collected data using this app on various Android devices' GPU.

## About the app
Once you first launch the app, make sure to accept giving permission to let the app to read and write files in the storage. This permission is required since the app needs to use shader files to run litmus tests and write test output to a text file.

By clicking the navigation drawer on the top-left side of the screen, you will be able to see the full list of litmus tests like the screenshot below.
<img src="https://raw.githubusercontent.com/MangoShip/Android-Vulkan-Memory-Model-Testing/main/screenshot/screenshot0.PNG" width="200" height="408">

Similiar to [WebGPU Memory Model Testing](https://gpuharbor.ucsc.edu/webgpu-mem-testing/) page, this app provides four categories of litmus tests.
- **Weak Memory Tests**: These test the semantics of memory when two threads access multiple memory locations concurrently, specifically whether hardware is allowed to re-order cetain combinations of reads and writes to different memory locations.
- **Coherence Tests**: These test the semantics of memory when a single memory location is accessed concurrently, ensuring coherency is respected.
- **Atomicity**: This test checks that an atomic read-modify-write instruction is, indeed, atomic.
- **Barrier Tests**: These test the implementation of Vulkan's fence synchronization primitives, ensuring memory is properly synchronized with respect to the barrier.

Each test page contains a brief description of the program under test, as well as pseudocode showing the instructions executed by each thread. The psuedocode also shows where each thread executes relative to the other and calls out the behavior of interest.

Each test page provides two mode for testing: explorer and tuning. 

### Explorer Mode
In explorer mode, you will be able to manually change parameters to induce interesting behaviors. For more information on each test parameters, visit the [WebGPU Memory Model Testing](https://gpuharbor.ucsc.edu/webgpu-mem-testing/) page. There will be parameter presets that you will be able to quickly load "Default" or "Stress" parameters. There also will be shader option where you can select shader variant. Not selecting any shader option will run test with default shader. For more information on shader variants, visit the [WebGPU Memory Model Testing](https://gpuharbor.ucsc.edu/webgpu-mem-testing/) page and click on variant option in test page to see the difference on the shader variants. Finally, you can click "Start" button to start running the test.

![explorer_gif](https://media.giphy.com/media/jaeIHZ5VpqWYscViiu/giphy.gif)

While the test is running, you will be able to see progress of the current running test below the "Tuning" button. 

![explorer_progress_gif](https://media.giphy.com/media/7wjFjf2FLa4vaYk4eH/giphy.gif)

After the test has completed running, you will be able to see the result by clicking the "Result" button.

![explorer_result_gif](https://media.giphy.com/media/go92Jo0FyzGiyKyuez/giphy.gif)

### Tuning Mode
In tuning mode, you can choose a number of configuration to run and a number of iterations to run for. Each configuration generates a random parameter set and run the test. You can also set a random seed to generate same parameters among different devices. (Empty random seed will result in random parameters) You can also specify the range of number and size of workgroups to produce the random parameter. This was to prevent spending time running tests with number and size of workgroups that will produce all sequential behaviors and no interleaved and weak behaviors. The tuning mode allows users to generate data showing what parameter combinations are most effective at revealing weak behaivors or even bugs. Finally, you can click "Start" button to start running the test. 

![tuning_gif](https://media.giphy.com/media/Z76tLqk8qW7AWERgFT/giphy.gif)

While test is running, you will be able to see progress of the current running test below the "Tuning" button. 

![tuning_progress_gif](https://media.giphy.com/media/DLDkTca03CsEW3Tf95/giphy.gif)

After test has completed running, you will be able to see the result by clicking the "Result" button.

![tuning_result_gif](https://media.giphy.com/media/odto4MU8CKiMTKQsJM/giphy.gif)

### Running Multiple Tests
If you want to run multiple tests with certain parameters at once, you can visit the "Running Multiple Tests" page by opening up the navigation drawer. Here, you will be able to select which tests that you want to run, and a test type.("Explorer" or "Tuning") Once you have finished making selections, clicking "Start" will start running selected tests. Similar to other testing mode, you will be able to see the test progress under the "Start" button. Once all the test has been completed, the result will be visible under the "Start" button.

![explorer_multi_gif](https://media.giphy.com/media/U5oIY9UgxPV6aFtICi/giphy.gif)
![tuning_multi_gif](https://media.giphy.com/media/on7l9zz168X7tq9VUD/giphy.gif)

### Sending Results (Temporary)
After using the "Running Multiple Tests" function, you can send the result to the developer by clicking the "Send Result" button. **The current version of this feature is temporary, so it can be changed in the future** After clicking the button, there will be a pop-up of other applications like "Gmail" that you can select. Currently, "Gmail" is the most preferable application that you can use the send results to the developer. Other email applications may not work as intented. 

![sending_result_gif](https://media.giphy.com/media/4cx0uqY4Cv1TbTr5rZ/giphy.gif)

#### Issues with sending result
Some Android devices may run into issue with sending result feature due to Google Play being disabled, unable to open "Gmail" application through another app, and etc. If this happens, there is an alternative way for you to retrieve the result file. Download [X-plore](https://play.google.com/store/apps/details?id=com.lonelycatgames.Xplore&hl=en_US&gl=US) or any application that lets you look into the devices' storage. After downloading X-plore, head over to  `"/storage/emulated/0/Android/data/com.example.litmusandroid/files/DCIM/"` where you will find a json file called `"litmustest_multitest_explorer_result.json"` (result file for explorer) or `"litmustest_multitest_tuning_result.json"` (result file for tuning). You can transfer this file to somewhere else like Google Drive then email to `mingun0108@gmail.com` with the file attached to send the result file to the developer.
