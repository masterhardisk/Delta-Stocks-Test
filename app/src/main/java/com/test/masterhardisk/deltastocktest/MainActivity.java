package com.test.masterhardisk.deltastocktest;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codemybrainsout.placesearch.PlaceSearchDialog;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.test.masterhardisk.deltastocktest.Utils.Config;
import com.test.masterhardisk.deltastocktest.Utils.GetLocation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button chargeButton;
    private EditText whenEditText, whereEditText;
    private String timeStamp = "";
    private GetLocation getLocation;
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPermission();

        getLocation = new GetLocation(this);


        config = new Config(this);

        setUp();

    }


    private void setUp(){

        chargeButton = findViewById(R.id.button1);
        whenEditText = findViewById(R.id.whenEditText);
        whereEditText = findViewById(R.id.whereEditText);


        chargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handleButton();
            }
        });
    }


    private void handleButton(){

        pickerDate();
    }


    private void pickerDate(){
        final Calendar c = Calendar.getInstance();
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mMonth = c.get(Calendar.MONTH);
        int mYear = c.get(Calendar.YEAR);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                String str_date = i2+"-"+(i1+1)+"-"+i;

                pickerHour(str_date);

            }

        }, mYear, mMonth, mDay);

        datePickerDialog.show();


    }

    private void pickerHour(final String date){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String time = date + " " +i+":"+i1;

                whenEditText.setText(time);

                pickerCity();

            }
        }, hour, minute, true);

        timePickerDialog.setTitle("Selecciona la hora");

        timePickerDialog.show();
    }

    private void pickerCity(){
        PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog.Builder(this)
                .setHintText("Introduce la ciudad")
                .setNegativeText("CANCELAR")
                .setPositiveText("ACEPTAR")
                .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                    @Override
                    public void locationName(String locationName) {
                        whereEditText.setText(locationName);

                        showDataUser();
                    }
                })
                .build();
        placeSearchDialog.show();

    }


    private void showDataUser(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.user_data);
        dialog.setCanceledOnTouchOutside(true);

        TextView longitude = dialog.findViewById(R.id.longitude);
        TextView latitude = dialog.findViewById(R.id.latitude);
        TextView city = dialog.findViewById(R.id.city);
        TextView cp = dialog.findViewById(R.id.cp);

        longitude.setText(config.getUserLongitude());
        latitude.setText(config.getUserLatitude());
        city.setText(config.getUserCity());
        cp.setText(config.getUserCP());

        dialog.show();

    }


    public void setPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    111);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 111){
            getLocation = new GetLocation(this);
        }
    }
}
