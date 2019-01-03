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
import java.util.List;

public class WeightFragment extends Fragment {

    private static ArrayList<ProgressObject> list_month = new ArrayList<>();
    private View view;
    private static GraphView graph;
    private static LineGraphSeries<DataPoint> weightSeries = new LineGraphSeries<>();

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.weight_fragment, container, false);

        graph = (GraphView) view.findViewById(R.id.graph);

        weightSeries.setTitle("Your weight");
        graph.getViewport().setMaxX(31);
        graph.getViewport().setMinX(1);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxY(80);
        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("januari");
        graph.addSeries(weightSeries);
        graph.getLegendRenderer().setVisible(true);
        return view;
    }

    public static void setPoList(List<ProgressObject> poList) {

        String arr[];
        for (ProgressObject o : poList) {
            arr = o.getDate().split(",");
            arr = arr[1].split(" ");
            if (arr[1].equals("januari")) {
                System.out.println("added");
                list_month.add(o);
            }
        }

        for(int i = 0; i<list_month.size();i++) {
            weightSeries.appendData(new DataPoint(i+1, list_month.get(i).getWeight()), true, list_month.size());
        }
    }

}
