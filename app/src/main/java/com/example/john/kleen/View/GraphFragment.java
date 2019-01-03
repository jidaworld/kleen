package com.example.john.kleen.View;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.kleen.DB.CallBack;
import com.example.john.kleen.DB.DBHandler;
import com.example.john.kleen.Model.ProgressObject;
import com.example.john.kleen.Model.SaveData;
import com.example.john.kleen.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

public class GraphFragment extends Fragment {

    private static int avrSteps = 0;
    private static ArrayList<ProgressObject> yearList = new ArrayList<>();
    private static ArrayList<ProgressObject> monthList = new ArrayList<>();
    private static List<ProgressObject> poList = new ArrayList<>();
    private View view;
    private TextView textView;
    private static TextView stepsAvr;
    private static GraphView graph_year;
    private static GraphView graph_month;
    private static LineGraphSeries<DataPoint> stepSeries_year = new LineGraphSeries<>();
    private static LineGraphSeries<DataPoint> goalSeries_year = new LineGraphSeries<>();
    private static LineGraphSeries<DataPoint> stepSeries_month = new LineGraphSeries<>();
    private static LineGraphSeries<DataPoint> goalSeries_month = new LineGraphSeries<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.graph_fragment, container, false);

        textView = view.findViewById(R.id.text_view_graph);
        graph_year = (GraphView) view.findViewById(R.id.graph_year);
        graph_month = (GraphView) view.findViewById(R.id.graph_month);
        stepsAvr = view.findViewById(R.id.stepavr);

        GridLabelRenderer gridLabel = graph_year.getGridLabelRenderer();
        graph_year.getViewport().setMaxX(30);
        graph_year.getViewport().setXAxisBoundsManual(true);
        graph_year.getViewport().setMaxY(1000);
        graph_year.getViewport().setMinX(0);
        graph_year.getViewport().setMinY(0);
        graph_year.getViewport().setYAxisBoundsManual(true);
        gridLabel.setHorizontalAxisTitle("2018");

        graph_month.getViewport().setMaxX(31);
        graph_month.getViewport().setXAxisBoundsManual(true);
        graph_month.getViewport().setMaxY(15000);
        graph_month.getViewport().setMinX(1);
        graph_month.getViewport().setMinY(0);
        graph_month.getViewport().setYAxisBoundsManual(true);
        GridLabelRenderer gridLabel_month = graph_month.getGridLabelRenderer();
        gridLabel_month.setHorizontalAxisTitle("januari");

        stepsAvr.setText("Average steps per day: " + avrSteps);
        graph_year.getLegendRenderer().setVisible(true);

        graph_year.addSeries(stepSeries_year);
        graph_year.addSeries(goalSeries_year);
        graph_month.addSeries(stepSeries_month);
        graph_month.addSeries(goalSeries_month);
        return view;
    }


    public static void setPoList(List<ProgressObject> poList) {
        String arr[];
        for (ProgressObject o : poList) {
            arr = o.getDate().split(",");
            if (arr[2].equals(" 2019")) {
                yearList.add(o);
            }
            arr = arr[1].split(" ");
            if (arr[1].equals("januari")) {
                monthList.add(o);
            }
        }

        for (int i = 0; i < yearList.size(); i++) {
            stepSeries_year.appendData(new DataPoint(i, yearList.get(i).getSteps()), false, yearList.size());
            goalSeries_year.appendData(new DataPoint(i, yearList.get(i).getStep_goal()), false, yearList.size());
            avrSteps += yearList.get(i).getSteps();
        }

        avrSteps /= yearList.size();
        stepsAvr.setText("Average steps per day: " + avrSteps);

        for (int i = 0; i < monthList.size(); i++) {
            stepSeries_month.appendData(new DataPoint(i+1, monthList.get(i).getSteps()), false, monthList.size());
            goalSeries_month.appendData(new DataPoint(i+1, monthList.get(i).getStep_goal()), false, monthList.size());
        }

        stepSeries_year.setColor(Color.BLACK);
        stepSeries_year.setTitle("steps");
        goalSeries_year.setColor(Color.RED);
        goalSeries_year.setTitle("goal");
        graph_year.getLegendRenderer().setVisible(true);

    }
}
