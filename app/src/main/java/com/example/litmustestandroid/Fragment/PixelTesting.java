package com.example.litmustestandroid.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.litmustestandroid.HelperClass.PixelTestViewObject;
import com.example.litmustestandroid.HelperClass.ResultButton;
import com.example.litmustestandroid.HelperClass.TestViewObject;
import com.example.litmustestandroid.MainActivity;
import com.example.litmustestandroid.R;

import org.jetbrains.annotations.NotNull;

public class PixelTesting extends Fragment {

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_pixel_testing, container, false);

        TextView description = fragmentView.findViewById(R.id.pixelTesting_description);
        description.setText(getResources().getString(R.string.pixel_testing_description));

        PixelTestViewObject testViewObject = new PixelTestViewObject();

        testViewObject.testName = "message_passing";

        testViewObject.progressLayout = fragmentView.findViewById(R.id.pixelTesting_progressLayout);
        testViewObject.progressLayout.setVisibility(View.GONE);


        testViewObject.tuningConfigLayout = fragmentView.findViewById(R.id.pixelTesting_configLayout);
        testViewObject.tuningCurrentConfigNumber = fragmentView.findViewById(R.id.pixelTesting_currentConfigNumber);
        testViewObject.currentIterationNumber = fragmentView.findViewById(R.id.pixelTesting_currentIterationNumber);

        testViewObject.explorerDefaultButton = fragmentView.findViewById(R.id.pixelTesting_explorerDefault_startButton);
        testViewObject.explorerStressButton = fragmentView.findViewById(R.id.pixelTesting_explorerStress_startButton);
        testViewObject.tuningButton = fragmentView.findViewById(R.id.pixelTesting_tuning_startButton);

        testViewObject.explorerDefaultResultButton = new ResultButton(fragmentView.findViewById(R.id.pixelTesting_explorerDefault_resultButton));
        testViewObject.explorerStressResultButton = new ResultButton(fragmentView.findViewById(R.id.pixelTesting_explorerStress_resultButton));
        testViewObject.tuningResultButton = new ResultButton(fragmentView.findViewById(R.id.pixelTesting_tuning_resultButton));

        // Initial button state
        testViewObject.explorerDefaultResultButton.button.setEnabled(false);
        testViewObject.explorerDefaultResultButton.button.setBackgroundColor(getResources().getColor(R.color.lightgray));
        testViewObject.explorerStressResultButton.button.setEnabled(false);
        testViewObject.explorerStressResultButton.button.setBackgroundColor(getResources().getColor(R.color.lightgray));
        testViewObject.tuningResultButton.button.setEnabled(false);
        testViewObject.tuningResultButton.button.setBackgroundColor(getResources().getColor(R.color.lightgray));

        testViewObject.explorerDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testViewObject.buttons = new Button[]{testViewObject.explorerDefaultButton, testViewObject.explorerStressButton, testViewObject.tuningButton};
                testViewObject.resultButtons = new ResultButton[]{testViewObject.explorerDefaultResultButton, testViewObject.explorerStressResultButton, testViewObject.tuningResultButton};
                testViewObject.testMode = "ExplorerDefault";
                ((MainActivity)getActivity()).startPixelTest(testViewObject.testName, testViewObject);
            }
        });

        testViewObject.explorerDefaultResultButton.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testViewObject.testMode = "ExplorerDefault";
                ((MainActivity)getActivity()).displayPixelTestResult(testViewObject.testMode);
            }
        });

        testViewObject.explorerStressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testViewObject.buttons = new Button[]{testViewObject.explorerStressButton, testViewObject.explorerDefaultButton, testViewObject.tuningButton};
                testViewObject.resultButtons = new ResultButton[]{testViewObject.explorerStressResultButton, testViewObject.explorerDefaultResultButton, testViewObject.tuningResultButton};
                testViewObject.testMode = "ExplorerStress";
                ((MainActivity)getActivity()).startPixelTest(testViewObject.testName, testViewObject);
            }
        });

        testViewObject.explorerStressResultButton.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testViewObject.testMode = "ExplorerStress";
                ((MainActivity)getActivity()).displayPixelTestResult(testViewObject.testMode);
            }
        });

        testViewObject.tuningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testViewObject.buttons = new Button[]{testViewObject.tuningButton, testViewObject.explorerDefaultButton, testViewObject.explorerStressButton,};
                testViewObject.resultButtons = new ResultButton[]{testViewObject.tuningResultButton, testViewObject.explorerDefaultResultButton, testViewObject.explorerStressResultButton};
                testViewObject.testMode = "Tuning";
                ((MainActivity)getActivity()).startPixelTest(testViewObject.testName, testViewObject);
            }
        });
        testViewObject.tuningResultButton.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testViewObject.testMode = "Tuning";
                ((MainActivity)getActivity()).tuningTestResult(testViewObject.testName, "Single");
            }
        });

        return fragmentView;
    }
}

