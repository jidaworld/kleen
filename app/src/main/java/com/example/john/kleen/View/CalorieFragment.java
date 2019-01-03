package com.example.john.kleen.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.john.kleen.Model.ProgressObject;
import com.example.john.kleen.Model.SaveData;
import com.example.john.kleen.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalorieFragment extends Fragment {

    private static ArrayList<ProgressObject> list_month = new ArrayList<>();
    private static GraphView graph;
    private View view;
    private static LineGraphSeries<DataPoint> calorieSeries = new LineGraphSeries<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calorie_fragment, container, false);
        graph = (GraphView) view.findViewById(R.id.graph);

        calorieSeries.setTitle("Calories burned");
        graph.getViewport().setMaxX(31);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxY(700);
        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("januari");
        graph.getLegendRenderer().setVisible(true);

        graph.addSeries(calorieSeries);

        return view;
    }

    public static void setPoList(List<ProgressObject> poList) {

        String arr[];
        for (ProgressObject o : poList) {
            arr = o.getDate().split(",");
            arr = arr[1].split(" ");
            if (arr[1].equals("januari")) {
                list_month.add(o);
            }
        }

        for (int i = 0; i < list_month.size(); i++) {
            calorieSeries.appendData(new DataPoint(i + 1, list_month.get(i).getCalories()), true, list_month.size());
        }

    }
}
