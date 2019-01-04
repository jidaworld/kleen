package com.example.john.kleen.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.john.kleen.Controller.StepCounterService;
import com.example.john.kleen.Model.Util.BusStation;
import com.example.john.kleen.Model.WeightCalories;
import com.example.john.kleen.R;

import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

public class StepFragment extends Fragment {

    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private static TextView textView;

    private String save = "saveName";
    private int stepDisplay;
    private TextView infoText;
    private StringTokenizer token;
    private String text = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_fragment, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        infoText = view.findViewById(R.id.info);
        textView = view.findViewById(R.id.text_steps);
        loadData();

        if (getArguments() != null) {
            stepDisplay = getArguments().getInt(ARG_PARAM2);
        }
        textView.setText("" + stepDisplay);

        final Button button_goal = view.findViewById(R.id.goal_button);
        button_goal.setOnClickListener((v) -> {
            String goal = ((EditText) view.findViewById(R.id.goal_input)).getText().toString();
            if (!goal.equals("")) {
                int step_goal = Integer.parseInt(goal);

                BusStation.getBus().post(new WeightCalories(0,0,step_goal));

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("goal_save", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("goal", step_goal);
                editor.apply();
                token = new StringTokenizer(infoText.getText().toString(), ".");
                token.nextToken();
                text = "Current goal: " + step_goal + "." + token.nextToken();
                infoText.setText(text);
            }
        });

        final Button button_weight = view.findViewById(R.id.weight_button);
        button_weight.setOnClickListener((v) -> {
            String weight = ((EditText) view.findViewById(R.id.weight_input)).getText().toString();
            if (!weight.equals("")) {
                int weight_int = Integer.parseInt(weight);
                BusStation.getBus().post(new WeightCalories(weight_int,0,0));

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("weight_save", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("weight", weight_int);
                editor.apply();
                token = new StringTokenizer(infoText.getText().toString(), ".");
                text = token.nextToken() + ". You weigh " + weight_int + " kg";
                infoText.setText(text);
            }
        });

        final Button button_start = view.findViewById(R.id.start_button);
        button_start.setOnClickListener((v)-> {
            startFService(view);
        });

        final Button button_stop = view.findViewById(R.id.stop_button);
        button_stop.setOnClickListener((v)-> {
            stopFService(view);
        });

        return view;
    }

    public static StepFragment newInstance(int steps) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, steps);
        fragment.setArguments(args);
        return fragment;
    }

    public static void updateStepCounter(int steps) {
        if (textView != null) {
            textView.setText("" + steps);
        }
    }

    public void startFService(View v){
        Intent intent = new Intent(getActivity(), StepCounterService.class);
        intent.putExtra("startFService","");
        ContextCompat.startForegroundService(getActivity(), intent);
    }

    public void stopFService(View v){
        Intent intent = new Intent(getActivity(), StepCounterService.class);
        intent.putExtra("stopFService","");
        getActivity().stopService(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stepDisplay = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setStepDisplay(int stepDisplay) {
        this.stepDisplay = stepDisplay;
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(save, MODE_PRIVATE);
        stepDisplay = sharedPreferences.getInt("steps", 0);
        updateStepCounter(stepDisplay);

        sharedPreferences = getActivity().getSharedPreferences("goal_save", MODE_PRIVATE);
        int savedGoal = sharedPreferences.getInt("goal", 0);
        sharedPreferences = getActivity().getSharedPreferences("weight_save", MODE_PRIVATE);
        int weightGoal = sharedPreferences.getInt("weight", 0);

        text = "Current goal: " + savedGoal + ". You weigh " + weightGoal + " kg";
        if (infoText != null) {
            infoText.setText(text);
        }
    }
}
