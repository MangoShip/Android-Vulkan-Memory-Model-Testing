package com.example.litmustestandroid.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litmustestandroid.HelperClass.ConformanceTestViewObject;
import com.example.litmustestandroid.HelperClass.TestCase;
import com.example.litmustestandroid.MainActivity;
import com.example.litmustestandroid.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConformanceTest  extends Fragment {

    private Button explorerButton, tuningButton, defaultParamButton, stressParamButton, explorerSendResultButton, tuningSendResultButton;
    private String testMode = "ConformanceExplorer";

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_conformance_test, container, false);

        // Reset MainActivity's conformanceShaders
        for(int i = 0; i < ((MainActivity)getActivity()).conformanceShaders.size(); i++) {
            ((MainActivity)getActivity()).conformanceShaders.replaceAll((k, v) -> v = false);
        }

        TextView note = fragmentView.findViewById(R.id.conformance_test_note);
        note.setText(getResources().getString(R.string.conformance_test_note));

        TextView description = fragmentView.findViewById(R.id.conformance_test_description);
        description.setText(getResources().getString(R.string.conformance_test_description));

        ConformanceTestViewObject conformanceTestViewObject = new ConformanceTestViewObject();

        // Set progress layout to be invisible in default
        conformanceTestViewObject.progressLayout = fragmentView.findViewById(R.id.conformance_test_progressLayout);
        conformanceTestViewObject.progressLayout.setVisibility(View.GONE);

        // Set progress config layout to be invisible in default
        conformanceTestViewObject.configLayout = fragmentView.findViewById(R.id.conformance_test_currentConfigLayout);
        conformanceTestViewObject.configLayout.setVisibility(View.GONE);

        // Set explorer result layout to be invisible in default
        conformanceTestViewObject.explorerResultLayout = fragmentView.findViewById(R.id.conformance_test_explorerResultLayout);
        conformanceTestViewObject.explorerResultLayout.setVisibility(View.GONE);

        // Set tuning result layout to be invisible in default
        conformanceTestViewObject.tuningResultLayout = fragmentView.findViewById(R.id.conformance_test_tuningResultLayout);
        conformanceTestViewObject.tuningResultLayout.setVisibility(View.GONE);

        RecyclerView conformanceTestExplorerResultRV = fragmentView.findViewById(R.id.conformanceTestExplorerResultRV);
        RecyclerView conformanceTestTuningResultRV = fragmentView.findViewById(R.id.conformanceTestTuningResultRV);

        TextView testList = fragmentView.findViewById(R.id.conformanceTestList);
        LinearLayout testListLayout = fragmentView.findViewById(R.id.conformanceTestListLayout);
        testList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(testListLayout.getVisibility() == View.GONE) {
                    testListLayout.setVisibility(View.VISIBLE);
                }
                else {
                    testListLayout.setVisibility(View.GONE);
                }
            }
        });

        TextView explorerParameterList = fragmentView.findViewById(R.id.conformanceTestExplorerParameter);
        LinearLayout explorerParameterLayout = fragmentView.findViewById(R.id.conformanceTestExplorerParameterLayout);
        LinearLayout explorerParameterItemLayout = fragmentView.findViewById(R.id.conformanceTestExplorerParameterItemLayout);
        explorerParameterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(explorerParameterItemLayout.getVisibility() == View.GONE) {
                    explorerParameterItemLayout.setVisibility(View.VISIBLE);
                }
                else {
                    explorerParameterItemLayout.setVisibility(View.GONE);
                }
            }
        });

        TextView tuningParameterList = fragmentView.findViewById(R.id.conformanceTestTuningParameter);
        LinearLayout tuningParameterLayout = fragmentView.findViewById(R.id.conformanceTestTuningParameterLayout);
        LinearLayout tuningParameterItemLayout = fragmentView.findViewById(R.id.conformanceTestTuningParameterItemLayout);
        tuningParameterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tuningParameterItemLayout.getVisibility() == View.GONE) {
                    tuningParameterItemLayout.setVisibility(View.VISIBLE);
                }
                else {
                    tuningParameterItemLayout.setVisibility(View.GONE);
                }
            }
        });

        explorerButton = fragmentView.findViewById(R.id.conformanceTestExplorerButton);
        tuningButton = fragmentView.findViewById(R.id.conformanceTestTuningButton);

        explorerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explorerButton.setBackgroundColor(getResources().getColor(R.color.teal_200));
                tuningButton.setBackgroundColor(getResources().getColor(R.color.lightgray));
                explorerParameterLayout.setVisibility(View.VISIBLE);
                explorerParameterItemLayout.setVisibility(View.VISIBLE);

                if(!conformanceTestViewObject.newExplorer) {
                    conformanceTestViewObject.explorerResultLayout.setVisibility(View.VISIBLE);
                }

                conformanceTestViewObject.tuningResultLayout.setVisibility(View.GONE);
                tuningParameterLayout.setVisibility(View.GONE);
                testMode = "ConformanceExplorer";
            }
        });

        tuningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuningButton.setBackgroundColor(getResources().getColor(R.color.teal_200));
                explorerButton.setBackgroundColor(getResources().getColor(R.color.lightgray));
                tuningParameterLayout.setVisibility(View.VISIBLE);
                tuningParameterItemLayout.setVisibility(View.VISIBLE);

                if(!conformanceTestViewObject.newTuning) {
                    conformanceTestViewObject.tuningResultLayout.setVisibility(View.VISIBLE);
                }

                conformanceTestViewObject.explorerResultLayout.setVisibility(View.GONE);
                explorerParameterLayout.setVisibility(View.GONE);
                testMode = "ConformanceTuning";
            }
        });

        View[] testCheckBoxViews = new View[20];
        testCheckBoxViews[0] = fragmentView.findViewById(R.id.message_passing_coherency_checkBox);
        testCheckBoxViews[1] = fragmentView.findViewById(R.id.store_coherency_checkBox);
        testCheckBoxViews[2] = fragmentView.findViewById(R.id.read_coherency_checkBox);
        testCheckBoxViews[3] = fragmentView.findViewById(R.id.load_buffer_coherency_checkBox);
        testCheckBoxViews[4] = fragmentView.findViewById(R.id.store_buffer_coherency_checkBox);
        testCheckBoxViews[5] = fragmentView.findViewById(R.id.write_22_coherency_checkBox);
        testCheckBoxViews[6] = fragmentView.findViewById(R.id.message_passing_barrier_checkBox);
        testCheckBoxViews[7] = fragmentView.findViewById(R.id.store_barrier_checkBox);
        testCheckBoxViews[8] = fragmentView.findViewById(R.id.read_barrier_checkBox);
        testCheckBoxViews[9] = fragmentView.findViewById(R.id.load_buffer_barrier_checkBox);
        testCheckBoxViews[10] = fragmentView.findViewById(R.id.store_buffer_barrier_checkBox);
        testCheckBoxViews[11] = fragmentView.findViewById(R.id.write_22_barrier_checkBox);
        testCheckBoxViews[12] = fragmentView.findViewById(R.id.corr_default_checkBox);
        testCheckBoxViews[13] = fragmentView.findViewById(R.id.coww_default_checkBox);
        testCheckBoxViews[14] = fragmentView.findViewById(R.id.cowr_default_checkBox);
        testCheckBoxViews[15] = fragmentView.findViewById(R.id.corw2_default_checkBox);
        testCheckBoxViews[16] = fragmentView.findViewById(R.id.corr_rmw_checkBox);
        testCheckBoxViews[17] = fragmentView.findViewById(R.id.coww_rmw_checkBox);
        testCheckBoxViews[18] = fragmentView.findViewById(R.id.cowr_rmw_checkBox);
        testCheckBoxViews[19] = fragmentView.findViewById(R.id.corw2_rmw_checkBox);

        CheckBox selectAllCheckBox = fragmentView.findViewById((R.id.selectAllCheckBox));
        selectAllCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectAllCheckBox.isChecked()) {
                    for(int i = 0; i < testCheckBoxViews.length; i++) {
                        ((CheckBox)testCheckBoxViews[i]).setChecked(true);
                        ((MainActivity)getActivity()).conformanceTestCheckBoxesListener(testCheckBoxViews[i]);
                    }
                }
                else {
                    for(int i = 0; i < testCheckBoxViews.length; i++) {
                        ((CheckBox)testCheckBoxViews[i]).setChecked(false);
                        ((MainActivity)getActivity()).conformanceTestCheckBoxesListener(testCheckBoxViews[i]);
                    }
                }
            }
        });

        TestCase currTest = ((MainActivity)getActivity()).findTestCase("corr");
        int basic_parameters = getResources().getIdentifier(currTest.paramPresetNames[0], "raw", getActivity().getPackageName());
        int stress_parameters = getResources().getIdentifier(currTest.paramPresetNames[1], "raw", getActivity().getPackageName());

        EditText[] conformanceTestExplorerParameters = new EditText[18];
        conformanceTestExplorerParameters[0] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerTestIteration); // testIteration
        conformanceTestExplorerParameters[1] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerTestingWorkgroups); // testingWorkgroups
        conformanceTestExplorerParameters[2] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerMaxWorkgroups); // maxWorkgroups
        conformanceTestExplorerParameters[3] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerMinWorkgroupSize); // minWorkgroupSize
        conformanceTestExplorerParameters[4] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerMaxWorkgroupSize); // maxWorkgroupSize
        conformanceTestExplorerParameters[5] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerShufflePct); // shufflePct
        conformanceTestExplorerParameters[6] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerBarrierPct); // barrierPct
        conformanceTestExplorerParameters[7] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerScratchMemorySize); // scratchMemorySize
        conformanceTestExplorerParameters[8] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerMemoryStride); // memStride
        conformanceTestExplorerParameters[9] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerMemoryStressPct); // memStressPct
        conformanceTestExplorerParameters[10] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerMemoryStressIterations); // memStressIterations
        conformanceTestExplorerParameters[11] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerMemoryStressPattern); // memStressPattern
        conformanceTestExplorerParameters[12] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerPreStressPct); // preStressPct
        conformanceTestExplorerParameters[13] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerPreStressIterations); // preStressIterations
        conformanceTestExplorerParameters[14] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerPreStressPattern); // preStressPattern
        conformanceTestExplorerParameters[15] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerStressLineSize); // stressLineSize
        conformanceTestExplorerParameters[16] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerStressTargetLines); // stressTargetLines
        conformanceTestExplorerParameters[17] = (EditText) fragmentView.findViewById(R.id.conformanceTestExplorerStressAssignmentStrategy); // stressAssignmentStrategy

        EditText[] conformanceTestTuningParameters = new EditText[7];
        conformanceTestTuningParameters [0] = (EditText) fragmentView.findViewById(R.id.conformanceTestTuningConfigNum); // testConfigNum
        conformanceTestTuningParameters [1] = (EditText) fragmentView.findViewById(R.id.conformanceTestTuningTestIteration); // testIteration
        conformanceTestTuningParameters [2] = (EditText) fragmentView.findViewById(R.id.conformanceTestTuningRandomSeed); // randomSeed
        conformanceTestTuningParameters [3] = (EditText) fragmentView.findViewById(R.id.conformanceTestTuningTestingWorkgroups); // testingWorkgroups
        conformanceTestTuningParameters [4] = (EditText) fragmentView.findViewById(R.id.conformanceTestTuningMaxWorkgroups); // maxWorkgroups
        conformanceTestTuningParameters [5] = (EditText) fragmentView.findViewById(R.id.conformanceTestTuningMinWorkgroupSize); // minWorkgroupSize
        conformanceTestTuningParameters [6] = (EditText) fragmentView.findViewById(R.id.conformanceTestTuningMaxWorkgroupSize); // maxWorkgroupSize

        ((MainActivity)getActivity()).loadParameters(conformanceTestExplorerParameters, basic_parameters);

        defaultParamButton = fragmentView.findViewById(R.id.conformanceTestDefaultParamButton);
        stressParamButton = fragmentView.findViewById(R.id.conformanceTestStressParamButton);
        explorerSendResultButton = fragmentView.findViewById(R.id.conformanceTestExplorerSendResultButton);
        tuningSendResultButton = fragmentView.findViewById(R.id.conformanceTestTuningSendResultButton);

        conformanceTestViewObject.startButton = fragmentView.findViewById(R.id.conformance_test_startButton);
        conformanceTestViewObject.currentTestName = fragmentView.findViewById(R.id.conformance_test_currentTestName);
        conformanceTestViewObject.currentConfigNumber = fragmentView.findViewById(R.id.conformance_test_currentConfigNumber);
        conformanceTestViewObject.currentIterationNumber = fragmentView.findViewById(R.id.conformance_test_currentIterationNumber);

        defaultParamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultParamButton.setBackgroundColor(getResources().getColor(R.color.teal_200));
                stressParamButton.setBackgroundColor(getResources().getColor(R.color.lightgray));
                ((MainActivity)getActivity()).loadParameters(conformanceTestExplorerParameters, basic_parameters);
            }
        });

        stressParamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stressParamButton.setBackgroundColor(getResources().getColor(R.color.teal_200));
                defaultParamButton.setBackgroundColor(getResources().getColor(R.color.lightgray));
                ((MainActivity)getActivity()).loadParameters(conformanceTestExplorerParameters, stress_parameters);
            }
        });

        explorerSendResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open dialog for sending result
                ((MainActivity)getActivity()).sendResultEmail(testMode);
            }
        });

        tuningSendResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open dialog for sending result
                ((MainActivity)getActivity()).sendResultEmail(testMode);
            }
        });

        conformanceTestViewObject.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(testMode.equals("ConformanceExplorer")) {
                    ((MainActivity)getActivity()).conformanceExplorerTestBegin(conformanceTestExplorerParameters,  conformanceTestViewObject, conformanceTestExplorerResultRV);
                }
                else { // Tuning
                    ((MainActivity)getActivity()).conformanceTuningTestBegin(conformanceTestTuningParameters,  conformanceTestViewObject, conformanceTestTuningResultRV);
                }
            }
        });

        return fragmentView;
    }
}
