package com.example.litmustestandroid.DialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.litmustestandroid.MainActivity;
import com.example.litmustestandroid.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConformanceTuningResultDialogFragment extends DialogFragment {

    Context context;
    String configNum;
    ArrayList<ConformanceResultCase> conformanceResultCases;

    private TextView testName;
    private Button closeButton;
    private RecyclerView resultRV;

    public ConformanceTuningResultDialogFragment(Context ct, String configNum, ArrayList<ConformanceResultCase> conformanceResultCases) {
        this.configNum = configNum;
        this.context = ct;
        this.conformanceResultCases = conformanceResultCases;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conformance_tuning_result_dialog_layout, container, false);

        testName = view.findViewById(R.id.conformanceTuningResultTestName);
        testName.setText("Test " + configNum + " Result");

        resultRV = view.findViewById(R.id.conformanceTuningResultRecyclerView);

        ConformanceTestResultAdapter conformanceTestResultAdapter = new ConformanceTestResultAdapter((MainActivity)context, conformanceResultCases);
        resultRV.setAdapter(conformanceTestResultAdapter);
        resultRV.setLayoutManager(new LinearLayoutManager((MainActivity)context));
        resultRV.addItemDecoration(new DividerItemDecoration((MainActivity)context, LinearLayoutManager.VERTICAL));

        closeButton = view.findViewById(R.id.conformanceTuningResultCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

        @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
