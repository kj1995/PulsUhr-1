package de.rwth.pulsuhr.pulsuhr;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by steffen on 16.06.16.
 */
public class MeasurePulsFragment extends Fragment implements View.OnClickListener{
    View myView;
    private LineGraphSeries<DataPoint> dataPoints;
    private int LastXValue = 0;
    private PulsUhr pulsUhr;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.measure_pulse_fragment, container, false);

        //Buttons
        Button btnStartM = (Button) myView.findViewById(R.id.btnStartMeasurement);
        btnStartM.setOnClickListener(this);
        Button btnSaveM = (Button) myView.findViewById(R.id.btnSaveMeasurement);
        btnSaveM.setOnClickListener(this);

        PulsUhr pulsUhr = new PulsUhr(getActivity(), "98:D3:31:90:41:88");

        //Graphview
        GraphView graph = (GraphView) myView.findViewById(R.id.graph);
        dataPoints = new LineGraphSeries<DataPoint>();
        addDataPoint(1);addDataPoint(4);addDataPoint(4);addDataPoint(1);addDataPoint(1);addDataPoint(1);addDataPoint(6);
        addDataPoint(4);addDataPoint(1);addDataPoint(1);addDataPoint(1);addDataPoint(3);addDataPoint(5);addDataPoint(4);
        addDataPoint(1);addDataPoint(6);addDataPoint(1);addDataPoint(1);addDataPoint(1);addDataPoint(1);addDataPoint(1);
        graph.addSeries(dataPoints);

        return myView;
    }

    //test
    public void addDataPoint(int receivedPoint)
    {
        LastXValue++;
        dataPoints.appendData(new DataPoint(LastXValue, receivedPoint), true, 480);
    }

    public void onClick(View v) {
        final SqlHelper myDB = new SqlHelper(getActivity());
        switch (v.getId())
        {
            case R.id.btnStartMeasurement:
                pulsUhr.connect();
                pulsUhr.startMeasurement();
                break;
            case R.id.btnSaveMeasurement:
                //test
                final String Pulse = "180";
                final String Measurement = "1";
                final EditText Comment = (EditText)myView.findViewById(R.id.editText);
                final RatingBar bar = (RatingBar)myView.findViewById(R.id.ratingBar);
                boolean isSuccessfull = myDB.addMeasurement(Measurement,Pulse,Comment.getText().toString(), ((float) bar.getRating()));
                if(isSuccessfull)
                    Toast.makeText(getActivity(),"Messung gespeichert",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(),"Messung nicht gespeichert",Toast.LENGTH_SHORT).show();
                break;

        }
    }


}
