package com.example.john.kleen.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.john.kleen.Model.ProgressObject;
import com.example.john.kleen.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class CalorieFragment extends Fragment {

    private static ArrayList<ProgressObject> list_month = new ArrayList<>();
    private static GraphView graph;
    private static double maxY = 0;
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
        graph.getViewport().setMaxY(maxY*1.25);
        System.out.println(maxY + " calorie max");
        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("januari");
        graph.getLegendRenderer().setVisible(true);

        graph.addSeries(calorieSeries);

        return view;
    }

    public static void setPoList(List<ProgressObject> poList) {

        if(list_month.isEmpty()) {
            String arr[];

            for (ProgressObject o : poList) {
                arr = o.getDate().split(",");
                arr = arr[1].split(" ");
                if (arr[1].equals("1")) {
                    list_month.add(o);
                }
            }

            for(ProgressObject o : list_month) {
                if(maxY < ((double)o.getSteps())*0.05) {
                    maxY = o.getSteps() * 0.05;
                }
                arr = o.getDate().split(",");
                calorieSeries.appendData(new DataPoint(Integer.parseInt(arr[0]), o.getSteps()*0.05), true, list_month.size());
            }
            System.out.println(maxY);

            //graph.getViewport().setMaxY(maxY*1.25);
        }
    }
}
