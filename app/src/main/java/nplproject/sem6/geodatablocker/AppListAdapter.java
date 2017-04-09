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
    public static ArrayList<String> selectedAppList = new ArrayList<String>();
    public static int selectedAppListCounter = 0;
    public static int[] temp = new int[1000];
    public static int tempCounter=0;
    public boolean[] thumbnailsselection = new boolean[1000];


    static class ViewHolder {
        ImageView appicon;
        TextView app_name;
        TextView app_paackage;
        CheckBox checkBox;
    }


    public AppListAdapter(Context context, int textViewResourceId, List<ApplicationInfo> appsList) {
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
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();

        if (null == view){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.app_row, null);

            viewHolder.appicon = (ImageView) view.findViewById(R.id.app_icon);
            viewHolder.app_name = (TextView) view.findViewById(R.id.app_name);
            viewHolder.app_paackage = (TextView) view.findViewById(R.id.app_paackage);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.cb_app);

            view.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ApplicationInfo data = appsList.get(position);

//            view = layoutInflater.inflate(R.layout.app_row, null);
//            viewHolder.appicon = (ImageView) view.findViewById(R.id.app_icon);
//            viewHolder.app_name = (TextView) view.findViewById(R.id.app_name);
//            viewHolder.app_paackage = (TextView) view.findViewById(R.id.app_paackage);

//            ApplicationInfo data = appsList.get(position);
        viewHolder.appicon.setImageDrawable(data.loadIcon(packageManager));
        viewHolder.app_name.setText(data.loadLabel(packageManager));
        viewHolder.app_paackage.setText(data.packageName);

        viewHolder.checkBox.setId(position);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int id = cb.getId();
                ApplicationInfo data = appsList.get(id);
                if (checkList.get(id))
                {
                    cb.setChecked(false);
//                    thumbnailsselection[id] = false;
                    checkList.set(id,false);
                    int i;
                    for (i=0; i<tempCounter;i++)
                        if (temp[i] == id)
                            break;

                    selectedAppList.remove(i);
                    --selectedAppListCounter;
                }
                else
                {
                    cb.setChecked(true);
//                    thumbnailsselection[id] = true;
                    checkList.set(id,true);
                    selectedAppList.add(data.packageName);
                    temp[selectedAppListCounter] = id;
                    selectedAppListCounter++;
                }
                Toast.makeText(getContext(), "inside array " + selectedAppList, Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.checkBox.setChecked(checkList.get(position));

//        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_app);
//        for (int i=0;i<appsList.size();i++){
//            if (checkList.get(position) == true)
//                viewHolder.checkBox.setChecked(true);
//
//        }
//        Toast.makeText(getContext(), "blah " + thumbnailsselection, Toast.LENGTH_SHORT).show();



//        if (null != data) {
//        TextView appName = (TextView) view.findViewById(R.id.app_name);
//        TextView packageName = (TextView) view.findViewById(R.id.app_paackage);
//        ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
//
//        checkBox.setChecked(checkList.get(position)); // set the status as we stored it
//        checkBox.setTag(Integer.valueOf(position)); // set the tag so we can identify the correct row in the listener
//        checkBox.setOnCheckedChangeListener(mListener); // set the listener

//        ApplicationInfo data = appsList.get(position);
//        appName.setText(data.loadLabel(packageManager));
//        packageName.setText(data.packageName);
//        iconview.setImageDrawable(data.loadIcon(packageManager));
//        }


        return view;
    }

//    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {
//
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            checkList.set((Integer)buttonView.getTag(),isChecked); // get the tag so we know the row and store the status
////            Toast.makeText(getContext(), "selected" + checkList, Toast.LENGTH_SHORT).show();
////            selectedAppList.add();
//
//            ApplicationInfo data = appsList.get((Integer)buttonView.getTag());
//            if (isChecked){
//                selectedAppList.add(data.packageName);
//                temp[selectedAppListCounter] = (Integer)buttonView.getTag();
//                selectedAppListCounter++;
//            }
//            if (!isChecked){
//                int i;
//                for (i=0; i<tempCounter;i++)
//                    if (temp[i] == (Integer)buttonView.getTag())
//                        break;
//
//                selectedAppList.remove(i);
//                --selectedAppListCounter;
//
//            }
//
//            Toast.makeText(getContext(), "inside array " + selectedAppList, Toast.LENGTH_SHORT).show();
//
//        }
//    };
}

