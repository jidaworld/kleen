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

public class WeightFragment extends Fragment {

    ArrayList<ProgressObject> list_month = new ArrayList<>();

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.weight_fragment, container, false);

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> weightSeries = new LineGraphSeries<>();

        for(int i = 0; i<list_month.size();i++) {
            weightSeries.appendData(new DataPoint(i+1, list_month.get(i).getWeight()), true, list_month.size());
        }

        weightSeries.setTitle("Your weight");
        graph.getViewport().setMaxX(list_month.size());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxY(80);
        graph.getViewport().setMinY(50);
        graph.getViewport().setYAxisBoundsManual(true);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(list_month.get(0).getDate().getMonth().name());
        graph.addSeries(weightSeries);
        graph.getLegendRenderer().setVisible(true);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<ProgressObject> list = new SaveData(this.getActivity()).read();
        for(ProgressObject o : list) {
            if(o.getDate().getMonth().getValue() == 12){
                list_month.add(o);
            }
        }
    }

}
