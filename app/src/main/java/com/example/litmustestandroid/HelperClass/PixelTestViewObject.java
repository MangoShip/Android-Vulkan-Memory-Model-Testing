package com.example.litmustestandroid.HelperClass;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PixelTestViewObject {

    public String testName;
    public String testMode;

    public LinearLayout progressLayout;
    public LinearLayout tuningConfigLayout;
    public TextView tuningCurrentConfigNumber;
    public TextView currentIterationNumber;

    public Button explorerDefaultButton;
    public Button explorerStressButton;
    public Button tuningButton;
    public Button[] buttons;

    public ResultButton explorerDefaultResultButton;
    public ResultButton explorerStressResultButton;
    public ResultButton tuningResultButton;
    public ResultButton[] resultButtons;
}
