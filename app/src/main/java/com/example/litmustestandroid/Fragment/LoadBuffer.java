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
import com.example.litmustestandroid.MainActivity;
import com.example.litmustestandroid.R;

import org.jetbrains.annotations.NotNull;

public class LoadBuffer extends Fragment {

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_load_buffer, container, false);

        TextView description = fragmentView.findViewById(R.id.load_buffer_description);
        description.setText(getResources().getString(R.string.load_buffer_description));

        Button explorerButton = fragmentView.findViewById(R.id.load_buffer_explorerButton);
        Button tuningButton = fragmentView.findViewById(R.id.load_buffer_tuningButton);

        ResultButton explorerResultButton = new ResultButton(fragmentView.findViewById(R.id.load_buffer_explorerResultButton));
        ResultButton tuningResultButton = new ResultButton(fragmentView.findViewById(R.id.load_buffer_tuningResultButton));

        // Initial button state
        explorerResultButton.button.setEnabled(false);
        tuningResultButton.button.setEnabled(false);
        explorerResultButton.button.setBackgroundColor(getResources().getColor(R.color.lightgray));
        tuningResultButton.button.setBackgroundColor(getResources().getColor(R.color.lightgray));

        explorerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openExploreMenu("load_buffer", new Button[]{explorerButton, tuningButton}, new ResultButton[]{explorerResultButton, tuningResultButton});
            }
        });
        explorerResultButton.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).explorerTestResult("load_buffer");
            }
        });
        tuningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).openTuningMenu("load_buffer", new Button[]{tuningButton, explorerButton}, new ResultButton[]{tuningResultButton, explorerResultButton});
            }
        });
        tuningResultButton.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).tuningTestResult("load_buffer");
            }
        });

        return fragmentView;
    }
}
