package nplproject.sem6.geodatablocker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import android.support.v7.app.AppCompatActivity;

public class SelectLocation extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    public static final String MyPREFERENCES = "GeoDataBlockerPrefs" ;
    public static final String HomePlaceName = "hpn";
    public static final String HomePlaceAddress = "hpa";
    public static final String WorkPlaceName = "wpn";
    public static final String WorkPlaceAddress = "wpa";
    public static final String HomePlaceLatitude = "hplat";
    public static final String HomePlaceLongitude = "hplon";
    public static final String WorkPlaceLatitude = "wplat";
    public static final String WorkPlaceLongitude = "wplon";

    SharedPreferences sharedpreferences;
    int hpFlag=0;
    int wpFlag=0;
    public View view;
    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mIdTextView;
    private TextView mPhoneTextView;
    private TextView mWebTextView;
    private TextView mAttTextView;
    private TextView tvlat,tvlon;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    public SelectLocation() {
        // Required empty public constructor
    }
    @Override
    public void onPause(){
        super.onPause();
        mGoogleApiClient.stopAutoManage(SelectLocation.this);
        mGoogleApiClient.disconnect();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Button btnSetHomePlace = (Button)findViewById(R.id.btnSetHomePlace);
        Button btnSetWorkPlace = (Button)findViewById(R.id.btnSetWorkPlace);
        CardView cardView = (CardView) findViewById(R.id.cvlocation);
        cardView.setVisibility(View.INVISIBLE);
        btnSetHomePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameTextView.getText().toString();
                String address = mAddressTextView.getText().toString();
                String latitude = tvlat.getText().toString();
                String longitude = tvlon.getText().toString();
                latitude=latitude.substring(9,latitude.indexOf(".")+3);
                longitude=longitude.substring(10,longitude.indexOf(".")+3);
//                double latitude = Float.valueOf(tvlat.getText().toString());
//                double roundOffLat = Math.floor(latitude * 1000.0) / 1000.0;
//                Toast.makeText(SelectLocation.this,"Latitude:"+latitude,Toast.LENGTH_LONG).show();


//                float roundOffLong = Math.floor(lon * 1000.0) / 1000.0;
                if(TextUtils.isEmpty(name)) {
                    mAutocompleteTextView.setError("Required");
                    mAutocompleteTextView.requestFocus();
                    return;
                }
                else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(HomePlaceName, name);
                    editor.putString(HomePlaceAddress, address);
                    editor.putString(HomePlaceLatitude,latitude);
                    editor.putString(HomePlaceLongitude,longitude);
                    editor.commit();
//                    mNameTextView.setText("");
//                    mAddressTextView.setText("");
                    mAutocompleteTextView.setText("");
                    CardView cardView = (CardView) findViewById(R.id.cvlocation);
                    cardView.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Home Place Successfully Set!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btnSetWorkPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameTextView.getText().toString();
                String address = mAddressTextView.getText().toString();
                String latitude = tvlat.getText().toString();
                String longitude = tvlon.getText().toString();
                latitude=latitude.substring(9,latitude.indexOf(".")+3);
                longitude=longitude.substring(10,longitude.indexOf(".")+3);
                if(TextUtils.isEmpty(name)) {
                    mAutocompleteTextView.setError("Required");
                    mAutocompleteTextView.requestFocus();
                    return;
                }
                else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(WorkPlaceName, name);
                    editor.putString(WorkPlaceAddress, address);
                    editor.putString(WorkPlaceLatitude,latitude);
                    editor.putString(WorkPlaceLongitude,longitude);
                    editor.commit();
                    CardView cardView = (CardView) findViewById(R.id.cvlocation);
                    cardView.setVisibility(View.INVISIBLE);

//                    mNameTextView.setText("");
//                    mAddressTextView.setText("");
                    mAutocompleteTextView.setText("");
                    Toast.makeText(getApplicationContext(), "Work Place Successfully Set!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(SelectLocation.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(SelectLocation.this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mNameTextView = (TextView) findViewById(R.id.name);
        mAddressTextView = (TextView) findViewById(R.id.address);
        tvlat = (TextView) findViewById(R.id.tvlat);
        tvlon = (TextView) findViewById(R.id.tvlon);
//        mIdTextView = (TextView) view.findViewById(R.id.place_id);
//        mPhoneTextView = (TextView) view.findViewById(R.id.phone);
//        mWebTextView = (TextView) view.findViewById(R.id.web);
//        mAttTextView = (TextView) view.findViewById(R.id.att);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(SelectLocation.this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            LatLng latlng = place.getLatLng();
            double lat=latlng.latitude;
            double lon=latlng.longitude;
            CardView cardView = (CardView) findViewById(R.id.cvlocation);
            cardView.setVisibility(View.VISIBLE);
//            saveLatLng(roundOffLat,roundOffLong);
//            Toast.makeText(SelectLocation.this,"Latitude:"+roundOffLat+"\nLongitude:"+latlng.longitude,Toast.LENGTH_LONG).show();
            CharSequence attributions = places.getAttributions();
            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            tvlat.setText("Latitude: "+lat);
            tvlon.setText("Longitude: "+lon);
//            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
//            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
//            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }
    };

//    private void saveLatLng(double roundOffLat, double roundOffLong) {
//            Toast.makeText(SelectLocation.this,"Latitude:"+roundOffLat,Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(SelectLocation.this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

}
