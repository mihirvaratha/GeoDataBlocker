package nplproject.sem6.geodatablocker;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.R.attr.fragment;
import static android.content.Context.MODE_PRIVATE;
import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;
import static nplproject.sem6.geodatablocker.AppListAdapter.selectedAppList;


public class AppsFragment extends android.support.v4.app.ListFragment {
    public View view;
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private AppListAdapter listadaptor = null;


    public AppsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apps_fragment, container, false);

//        ListView listView = (ListView) view.findViewById(R.id.list);
        packageManager = getActivity().getPackageManager();

        new LoadApplications().execute();


        // Inflate the layout for this fragment

        //To force stop app(requires root permission)
//        try {
//            Process suProcess = Runtime.getRuntime().exec("su");
//            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
//            os.writeBytes("adb shell" + "\n");
//            os.flush();
//            os.writeBytes("am force-stop com.bsb.hike" + "\n");
//            os.flush();
////            Toast.makeText(getActivity(), "grantedddd", Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            Toast.makeText(getActivity(), "Root Permission Not Found"+e.getMessage(), Toast.LENGTH_LONG).show();
//        }
        Button button_done = (Button) view.findViewById(R.id.btnBlockApp);
        button_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "stored inside array " + selectedAppList, Toast.LENGTH_SHORT).show();
                SharedPreferences sharedpreferences_package = getActivity().getPreferences(Context.MODE_PRIVATE);;
                String json = new Gson().toJson(selectedAppList);
                SharedPreferences.Editor editor = sharedpreferences_package.edit();
                editor.putString("APP_PACKAGE", json);
                editor.commit();
                Toast.makeText(getContext(), "Commited Data to shared pref", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }





    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app = applist.get(position);
        try {
            Intent intent = packageManager
                    .getLaunchIntentForPackage(app.packageName);

            if (null != intent) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            Collections.sort(applist, new ApplicationInfo.DisplayNameComparator(packageManager));
            listadaptor = new AppListAdapter(getActivity(),
                    R.layout.app_row, applist);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    @Override
    public void onStart(){
        super.onStart();

    }
}
