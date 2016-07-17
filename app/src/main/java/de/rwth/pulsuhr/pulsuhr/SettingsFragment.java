package de.rwth.pulsuhr.pulsuhr;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by steffen on 16.06.16.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.settings_fragment, container, false);

        Button btnResetDB = (Button)myView.findViewById(R.id.btnResetDB);
        btnResetDB.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {
        final SqlHelper myDB = new SqlHelper(getActivity());
        switch (v.getId()){
            case R.id.btnResetDB :
                int succesful = myDB.deleteMeasurment(null);
                if(succesful > 0){
                    Toast.makeText(getActivity(),"Datenbank zurückgesetzt !",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
