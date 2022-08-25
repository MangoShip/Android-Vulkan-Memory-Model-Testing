package com.example.litmustestandroid.HelperClass;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConformanceTestViewObject {

    public Button startButton;

    public LinearLayout progressLayout;
    public LinearLayout configLayout;
    public TextView currentTestName;
    public TextView currentConfigNumber;
    public TextView currentIterationNumber;

    public LinearLayout explorerResultLayout;
    public LinearLayout tuningResultLayout;

    public boolean newExplorer = true;
    public boolean newTuning = true;
}
