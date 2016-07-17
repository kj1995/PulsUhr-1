package de.rwth.pulsuhr.pulsuhr;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Blob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by kajan on 17.07.2016.
 */
public class MeasurementViewAdapter extends RecyclerView.Adapter<MeasurementViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private View view;
    private SqlHelper myDB;
    private static Context appContext;
    private LineGraphSeries<DataPoint> dataPoints;
    private int LastXValue = 0;


    public MeasurementViewAdapter(Context context, SqlHelper db) {
        inflater = LayoutInflater.from(context);
        appContext = context;
        this.myDB = db;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.measurement_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Cursor cursor = myDB.showMeasurement();
        cursor.moveToPosition(position);
        if (cursor.getCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
            builder.setCancelable(true);
            builder.setTitle("Achtung !");
            builder.setMessage("Noch keine Messungen vorhanden !");
            builder.show();
        } else {
            Log.d("Test","onBind... called "+position);
            LastXValue = 0;
            String dateAndTime = new SimpleDateFormat(" dd.MM.yyyy    HH.mm").format(new Timestamp(Long.valueOf(cursor.getString(0))));
            dataPoints = new LineGraphSeries<DataPoint>();
            holder.tvTimestamp.setText("Messung vom : " + dateAndTime);
            holder.tvPulse.setText(cursor.getString(2));
            holder.tvComment.setText(" " + cursor.getString(3) + "\n Bewertung : " + cursor.getString(4));
            byte[] bMeasurement = cursor.getBlob(1);
            int count;
            for(count = 0; count < bMeasurement.length; count++){
                int dp = (int) bMeasurement[count];
                addDataPoint(dp);
            }
            holder.graph.addSeries(dataPoints);

        }
    }

    @Override
    public int getItemCount() {

        Cursor cursor = myDB.showMeasurement();
        return cursor.getCount();

    }

    public void addDataPoint(int receivedPoint) {
        LastXValue++;
        dataPoints.appendData(new DataPoint(LastXValue, receivedPoint), true, 480);
    }

    public void delete(int position){
        myDB.deleteMeasurment(String.valueOf(position));
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        TextView tvComment, tvPulse, tvTimestamp;
        GraphView graph;

        public MyViewHolder(View itemView) {

            super(itemView);

            tvComment = (TextView) itemView.findViewById(R.id.listComment);
            tvPulse = (TextView) itemView.findViewById(R.id.listPulse);
            tvTimestamp = (TextView) itemView.findViewById(R.id.listTimestamp);
            graph = (GraphView) itemView.findViewById(R.id.listGraph);
            graph.setOnLongClickListener(this);
        }
        //test
        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
            builder.setCancelable(false);
            builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    delete(getLayoutPosition());
                }
            });
            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setTitle("Löschen");
            builder.setMessage("Messung unwiderruflich löschen ?");
            builder.show();
            return true;
        }
    }
}

