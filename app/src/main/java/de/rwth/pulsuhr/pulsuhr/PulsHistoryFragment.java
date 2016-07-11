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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by steffen on 16.06.16.
 */
public class PulsHistoryFragment extends Fragment implements View.OnClickListener{
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.pulse_history_fragment, container, false);
        Button btnShowM= (Button) myView.findViewById(R.id.btnShow);
        btnShowM.setOnClickListener(this);
        return myView;
    }

    public void onClick(View v){
        final SqlHelper myDB = new SqlHelper(getActivity());
        switch(v.getId())
        {
            case R.id.btnShow:
                //test
                Cursor cursor = myDB.showMeasurement();
                if(cursor.getCount()==0){
                    makeAlertMessage("Fehler","Keine Messungen");
                }
                else {
                    StringBuffer buffer = new StringBuffer();
                    while (cursor.moveToNext()) {
                        String dateAndTime = new SimpleDateFormat("HH.mm   dd.MM.yyyy ").format(new Timestamp(Long.valueOf(cursor.getString(0))));
                        buffer.append("Timestamp : " + dateAndTime + "\n");
                        buffer.append("Messung : " + cursor.getString(1) + "\n");
                        buffer.append("Puls : " + cursor.getString(2) + "\n");
                        buffer.append("Kommentar : " + cursor.getString(3) + "\n");
                        buffer.append("Bewertung : " + cursor.getString(4) + "\n\n");
                    }
                    makeAlertMessage("Messungen", buffer.toString());
                    break;
                }
        }
    }

    public void makeAlertMessage(String title, String message ){
        AlertDialog.Builder builer = new AlertDialog.Builder(getActivity());
        builer.setCancelable(true);
        builer.setTitle(title);
        builer.setMessage(message);
        builer.show();
    }

}
