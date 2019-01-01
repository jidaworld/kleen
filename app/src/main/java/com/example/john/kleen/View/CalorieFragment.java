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

public class CalorieFragment extends Fragment {

    ArrayList<ProgressObject> list_month = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.calorie_fragment, container, false);


        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> calorieSeries = new LineGraphSeries<>();
        for(int i = 0; i<list_month.size(); i++){
            calorieSeries.appendData(new DataPoint(i+1, list_month.get(i).getCalories()), true, list_month.size());
        }

        calorieSeries.setTitle("Calories burned");
        graph.addSeries(calorieSeries);
        graph.getViewport().setMaxX(list_month.size());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxY(700);
        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(list_month.get(0).getDate().getMonth().name());
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
