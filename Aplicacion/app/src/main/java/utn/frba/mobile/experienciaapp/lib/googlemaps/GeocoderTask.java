package utn.frba.mobile.experienciaapp.lib.googlemaps;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.lib.errors.ErrorTreatment;

public class GeocoderTask extends AsyncTask<String, Void, List<Address>> {
        public Activity activity;

        public GeocoderTask(Activity activity){
            super();
            this.activity = activity;
        }
        @Override
        protected List<Address> doInBackground (String...locationName){
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(activity.getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                ErrorTreatment.TreatExeption(e);
            }
            return addresses;
        }

        @Override
        protected void onPostExecute (List<Address> addresses) {
            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(activity.getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Adding Markers on Google Map for each matching address
            for (int i = 0; i < addresses.size(); i++) {

                Address address = (Address) addresses.get(i);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();


                String addressText = address.getFeatureName() + ", " + address.getAdminArea() + ", " +  address.getCountryName();

                if(activity instanceof ReciveAdress){
                    ((ReciveAdress) activity).ReciveAdress(address);
                }

            }
        }
}