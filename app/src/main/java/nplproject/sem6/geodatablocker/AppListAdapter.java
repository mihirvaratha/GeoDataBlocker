package nplproject.sem6.geodatablocker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihir on 08-04-2017.
 */

public class AppListAdapter extends ArrayAdapter<ApplicationInfo> {
    private List<ApplicationInfo> appsList = null;
    private Context context;
    private PackageManager packageManager;
    private ArrayList<Boolean> checkList = new ArrayList<Boolean>();

    public AppListAdapter(Context context, int textViewResourceId,
                              List<ApplicationInfo> appsList) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.appsList = appsList;
        packageManager = context.getPackageManager();

        for (int i = 0; i < appsList.size(); i++) {
            checkList.add(false);
        }

    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.app_row, null);
        }

        ApplicationInfo data = appsList.get(position);
        if (null != data) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView packageName = (TextView) view.findViewById(R.id.app_paackage);
            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);

            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_app);
            checkBox.setTag(Integer.valueOf(position)); // set the tag so we can identify the correct row in the listener
            checkBox.setChecked(checkList.get(position)); // set the status as we stored it
            checkBox.setOnCheckedChangeListener(mListener); // set the listener


            appName.setText(data.loadLabel(packageManager));
            packageName.setText(data.packageName);
            iconview.setImageDrawable(data.loadIcon(packageManager));
        }
        return view;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkList.set((Integer)buttonView.getTag(),isChecked); // get the tag so we know the row and store the status
            Toast.makeText(getContext(), "yaha pe se selected array me daal de", Toast.LENGTH_SHORT).show();
        }
    };
}

