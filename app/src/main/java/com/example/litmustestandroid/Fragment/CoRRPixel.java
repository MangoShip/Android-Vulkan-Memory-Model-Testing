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

import com.example.litmustestandroid.HelperClass.ResultButton;
import com.example.litmustestandroid.HelperClass.TestViewObject;
import com.example.litmustestandroid.MainActivity;
import com.example.litmustestandroid.R;

import org.jetbrains.annotations.NotNull;

public class CoRRPixel extends Fragment {

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_corr_pixel, container, false);

        TextView description = fragmentView.findViewById(R.id.corrPixel_description);
        description.setText(getResources().getString(R.string.corr_description));

        TestViewObject testViewObject = new TestViewObject();

        testViewObject.testName = "corr";

        testViewObject.tuningProgressLayout = fragmentView.findViewById(R.id.corrPixel_progressLayout);
        testViewObject.tuningCurrentConfigNumber = fragmentView.findViewById(R.id.corrPixel_currentConfigNumber);
        testViewObject.tuningCurrentIterationNumber = fragmentView.findViewById(R.id.corrPixel_currentIterationNumber);
        testViewObject.tuningProgressLayout.setVisibility(View.GONE);

        testViewObject.tuningButton = fragmentView.findViewById(R.id.corrPixel_startButton);
        testViewObject.tuningResultButton = new ResultButton(fragmentView.findViewById(R.id.corrPixel_resultButton));

        // Initial button state
        testViewObject.tuningResultButton.button.setEnabled(false);
        testViewObject.tuningResultButton.button.setBackgroundColor(getResources().getColor(R.color.lightgray));

        testViewObject.tuningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).startCoRRTest("corr", testViewObject);
            }
        });
        testViewObject.tuningResultButton.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).tuningTestResult("corr", "Single");
            }
        });

        return fragmentView;
    }
}

