package de.rwth.pulsuhr.pulsuhr;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by steffen on 16.06.16.
 */
public class PulsHistoryFragment extends Fragment implements View.OnClickListener{

    private View myView;
    private RecyclerView recyclerView;
    private MeasurementViewAdapter adapter;
    private SqlHelper myDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.pulse_history_fragment, container, false);
        recyclerView = (RecyclerView)myView.findViewById(R.id.measurementList);
        myDB = new SqlHelper(getActivity());
        Cursor cursor = myDB.showMeasurement();
        TextView tvInfo = (TextView)myView.findViewById(R.id.tvInfo);
        if (cursor.getCount()!=0){
            tvInfo.setVisibility(myView.INVISIBLE);
        }
        else{
            tvInfo.setVisibility(myView.VISIBLE);
        }
        adapter = new MeasurementViewAdapter(getActivity(),myDB);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return myView;
    }

    public void onClick(View v){
    }

    public void makeAlertMessage(String title, String message ){
        AlertDialog.Builder builer = new AlertDialog.Builder(getActivity());
        builer.setCancelable(true);
        builer.setTitle(title);
        builer.setMessage(message);
        builer.show();
    }

}
