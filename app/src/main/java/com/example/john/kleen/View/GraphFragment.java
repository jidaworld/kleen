package com.example.john.kleen.View;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.kleen.Model.ProgressObject;
import com.example.john.kleen.Model.SaveData;
import com.example.john.kleen.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class GraphFragment extends Fragment {

    private int avrSteps = 0;
    ArrayList<ProgressObject> yearList = new ArrayList<>();
    ArrayList<ProgressObject> monthList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_fragment, container, false);

        TextView textView = view.findViewById(R.id.text_view_graph);
        GraphView graph_year = (GraphView) view.findViewById(R.id.graph_year);

        LineGraphSeries<DataPoint> stepSeries_year = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> goalSeries_year = new LineGraphSeries<>();

        for(int i = 0; i<yearList.size();i++){
            stepSeries_year.appendData(new DataPoint(i+1, yearList.get(i).getSteps()), true, yearList.size());
            goalSeries_year.appendData(new DataPoint(i+1, yearList.get(i).getStep_goal()), true, yearList.size());
            avrSteps += yearList.get(i).getSteps();
        }
        avrSteps /= yearList.size();

        TextView stepsAvr = view.findViewById(R.id.stepavr);
        stepsAvr.setText("Average steps per day: "+ avrSteps);
        GridLabelRenderer gridLabel = graph_year.getGridLabelRenderer();
        graph_year.getViewport().setMaxX(yearList.size());
        graph_year.getViewport().setXAxisBoundsManual(true);
        graph_year.getViewport().setMaxY(15000);
        graph_year.getViewport().setMinX(1);
        graph_year.getViewport().setMinY(0);
        graph_year.getViewport().setYAxisBoundsManual(true);
        gridLabel.setHorizontalAxisTitle("2018");

        stepSeries_year.setColor(Color.BLACK);
        stepSeries_year.setTitle("steps");
        goalSeries_year.setColor(Color.RED);
        goalSeries_year.setTitle("goal");
        graph_year.addSeries(stepSeries_year);
        graph_year.addSeries(goalSeries_year);
        graph_year.getLegendRenderer().setVisible(true);

        GraphView graph_month = (GraphView) view.findViewById(R.id.graph_month);
        LineGraphSeries<DataPoint> stepSeries_month = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> goalSeries_month = new LineGraphSeries<>();
        for(int i = 0;i<monthList.size();i++){
            stepSeries_month.appendData(new DataPoint(i, monthList.get(i).getSteps()), true, monthList.size());
            goalSeries_month.appendData(new DataPoint(i, monthList.get(i).getStep_goal()), true, monthList.size());
        }
        graph_month.getViewport().setMaxX(monthList.size());
        graph_month.getViewport().setXAxisBoundsManual(true);
        graph_month.getViewport().setMaxY(15000);
        graph_month.getViewport().setMinX(1);
        graph_month.getViewport().setMinY(0);
        graph_month.getViewport().setYAxisBoundsManual(true);
        GridLabelRenderer gridLabel_month = graph_month.getGridLabelRenderer();
        gridLabel_month.setHorizontalAxisTitle(monthList.get(0).getDate().getMonth().name());
        graph_month.addSeries(stepSeries_month);
        graph_month.addSeries(goalSeries_month);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<ProgressObject> list = new SaveData(this.getActivity()).read();
        for(ProgressObject o : list){
            if(o.getDate().getYear() == 2018){
                yearList.add(o);
            }
            if(o.getDate().getMonth() == LocalDate.of(2018,12,1).getMonth()){
                monthList.add(o);
            }
        }

    }

}
