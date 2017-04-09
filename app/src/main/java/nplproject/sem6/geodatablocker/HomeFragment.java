package nplproject.sem6.geodatablocker;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static nplproject.sem6.geodatablocker.AppListAdapter.selectedAppListCounter;
import static nplproject.sem6.geodatablocker.SelectLocation.HomePlaceName;
import static nplproject.sem6.geodatablocker.SelectLocation.MyPREFERENCES;

/**
 * Created by ANKIT on 04-04-2017.
 */

public class HomeFragment extends Fragment {
    public View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        GPSTracker gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 2s
                    GPSTracker gps = new GPSTracker(getActivity());
                    String currentLatitude = String.valueOf(gps.getLatitude());
                    currentLatitude=currentLatitude.substring(0,currentLatitude.indexOf(".")+3);
                    String currentLongitude = String.valueOf(gps.getLongitude());
                    currentLongitude=currentLongitude.substring(0,currentLongitude.indexOf(".")+3);
                    Toast.makeText(getContext(),"Current Latitude:"+currentLatitude+"\nCurrent Longitude:"+currentLongitude,Toast.LENGTH_LONG).show();
                }
            }, 2000);
        }





//        longitude=longitude.substring(10,longitude.indexOf(".")+3);

//        Toast.makeText(getContext(),"Latitude:"+gps.getLatitude(),Toast.LENGTH_LONG).show();
        Button btnStart = (Button) view.findViewById(R.id.btnStart);
        Button btnStop = (Button) view.findViewById(R.id.btnStop);
        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String hpn = sharedpreferences.getString("hpn","");
        String wpn = sharedpreferences.getString("wpn","");
        String hplat = sharedpreferences.getString("hplat","");
        String hplon = sharedpreferences.getString("hplon","");
        String wplat = sharedpreferences.getString("wplat","");
        String wplon = sharedpreferences.getString("wplon","");
//        Toast.makeText(getActivity(),"hp:Latitude:"+hplat+"\nhp:Longitude:"+wplat,Toast.LENGTH_LONG).show();
//        Toast.makeText(getActivity(),"wp:Latitude:"+wplat+"\nwp:Longitude:"+wplon,Toast.LENGTH_LONG).show();


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
                if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("LOCATION IS OFF");  // GPS not found
                    builder.setMessage("Switch On Location?"); // Want to enable?
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(),"Turn On Location To Use Application",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.create().show();
                    return;
                }
                else {
                    addNotification();
                    Toast.makeText(getContext(),"Service Started",Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Service Stopped",Toast.LENGTH_SHORT).show();
                NotificationManager notifManager= (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notifManager.cancelAll();

                SharedPreferences sharedpreferences_package = getActivity().getPreferences(Context.MODE_PRIVATE);;
                String json = sharedpreferences_package.getString("APP_PACKAGE", "");
                ArrayList<String> foo = (ArrayList<String>) new Gson().fromJson(json,
                        new TypeToken<ArrayList<String>>() {
                        }.getType());
                for (int i=0; i<selectedAppListCounter;i++){
                    String temp = foo.get(i);
                    Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(temp);
                    startActivity(LaunchIntent);
                    Toast.makeText(getActivity(), "Started in background"+temp, Toast.LENGTH_SHORT).show();
                }
//                Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.bsb.hike");
//                startActivity(LaunchIntent);
                Intent thisapp = getActivity().getPackageManager().getLaunchIntentForPackage("nplproject.sem6.geodatablocker");
                startActivity(thisapp);

            }
        });

    }
    private void addNotification() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.icon)
                        .setOngoing(true)
                        .setContentTitle("Geo-Data Blocker is Running");
//                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
//        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        String hpn = sharedpreferences.getString("hpn","");
//        String wpn = sharedpreferences.getString("wpn","");
//        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getContext())
//                .setSmallIcon(R.drawable.icon)
//                .setOngoing(true);
//        NotificationCompat.InboxStyle inboxStyle =
//                new NotificationCompat.InboxStyle();
//        String[] events = {"Home Place:"+hpn,"Work Place:"+wpn};
//        inboxStyle.setBigContentTitle("Geo-Data Blocker is Running");
//        for (int i=0; i < events.length; i++) {
//
//            inboxStyle.addLine(events[i]);
//        }
//        mBuilder.setStyle(inboxStyle);
//        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, mBuilder.build());
//    }
}
