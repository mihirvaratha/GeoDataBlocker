package nplproject.sem6.geodatablocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static nplproject.sem6.geodatablocker.R.id.btnLocation;
import static nplproject.sem6.geodatablocker.SelectLocation.MyPREFERENCES;

/**
 * Created by ANKIT on 04-04-2017.
 */

public class LocationFragment extends Fragment {
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.location_fragment, container, false);
        return view;
    }
    @Override
    public void onPause(){
        super.onPause();

    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        mGoogleApiClient.connect();
//
//    }

    @Override
    public void onStart(){
        super.onStart();
        TextView tvhpn,tvhpa,tvwpn,tvwpa;
        tvhpn = (TextView) view.findViewById(R.id.tvhpn);
        tvhpa = (TextView) view.findViewById(R.id.tvhpa);
        tvwpn = (TextView) view.findViewById(R.id.tvwpn);
        tvwpa = (TextView) view.findViewById(R.id.tvwpa);
        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String hpn = sharedpreferences.getString("hpn","");
        String hpa = sharedpreferences.getString("hpa","");
        String wpn = sharedpreferences.getString("wpn","");
        String wpa = sharedpreferences.getString("wpa","");
        if(hpn.length()>0){
            tvhpn.setText(hpn);
            tvhpa.setText(hpa);
            tvwpn.setText(wpn);
            tvwpa.setText(wpa);
        }
        Button btnLocation = (Button) view.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getContext(), SelectLocation.class));
                Intent intent = new Intent(getActivity(), SelectLocation.class);
                startActivity(intent);
            }
        });


    }

}
