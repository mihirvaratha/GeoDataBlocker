package nplproject.sem6.geodatablocker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ANKIT on 04-04-2017.
 */

public class AppsFragment extends Fragment {
    public View view;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.apps_fragment, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
