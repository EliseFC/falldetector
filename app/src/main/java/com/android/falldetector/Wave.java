package com.android.falldetector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Wave#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wave extends Fragment {

    private LineGraphSeries<DataPoint> mSeries;
    private int lastX = 0;
    private static final int MAX_NUM_DATAPOINTS = 50;

    public Wave() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Wave.
     */
    public static Wave newInstance() {
        Wave fragment = new Wave();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Wave", "------> onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wave, container, false);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        mSeries = new LineGraphSeries<>(generateInitData());
        graph.addSeries(mSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(MAX_NUM_DATAPOINTS);

        return rootView;
    }

    public void updateView(float ax, float ay, float az, float accelValue) {
        TextView x = (TextView)getView().findViewById(R.id.ax);
        x.setText("ax = " + ax);
        TextView y = (TextView)getView().findViewById(R.id.ay);
        y.setText("ay = " + ay);
        TextView z = (TextView)getView().findViewById(R.id.az);
        z.setText("az = " + az);
        mSeries.appendData(new DataPoint(lastX++, accelValue), true, MAX_NUM_DATAPOINTS);
    }

    private DataPoint[] generateInitData() {
        DataPoint[] values = new DataPoint[MAX_NUM_DATAPOINTS];
        for (int i = 0; i < MAX_NUM_DATAPOINTS; i++) {
            double x = i;
            double y = 0;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
            lastX = i;
        }
        return values;
    }
}
