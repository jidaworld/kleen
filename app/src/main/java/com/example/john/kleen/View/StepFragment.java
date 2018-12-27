package com.example.john.kleen.View;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.kleen.R;

import static android.content.Context.MODE_PRIVATE;

public class StepFragment extends Fragment {

    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private static TextView textView;

    private String save = "saveName";
    private int stepDisplay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.step_fragment, container, false);

        textView = view.findViewById(R.id.text_steps);
        Log.i("BundleDebug","onCreateView");
        if(getArguments()!=null){
            stepDisplay = getArguments().getInt(ARG_PARAM2);
        }
        textView.setText(""+stepDisplay);
        return view;
    }

    public static StepFragment newInstance(int steps){
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, steps);
        fragment.setArguments(args);
        return fragment;
    }

    public static void updateStepCounter(int steps){
        if(textView!=null) {
            textView.setText("" + steps);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
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

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(save, MODE_PRIVATE);
        stepDisplay = sharedPreferences.getInt("steps",0);
        updateStepCounter(stepDisplay);
    }
}
