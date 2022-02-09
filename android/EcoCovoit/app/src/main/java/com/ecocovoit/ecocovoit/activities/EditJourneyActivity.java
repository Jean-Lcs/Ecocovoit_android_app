package com.ecocovoit.ecocovoit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ecocovoit.ecocovoit.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditJourneyActivity extends AppCompatActivity {

    private EditText editTextDepart;
    private EditText editTextArrivee;
    private EditText editTextDate;
    private EditText editTextHeure;
    private Button btnDate;
    private Button btnHeure;
    private Button btnSauvegarder;

    private Calendar calendar;

    public static final String EXTRA_JOURNEY_TYPE = "extra_journey_type";
    public static final int JOURNEY_TYPE_CAR_RIDE = 0;
    public static final int JOURNEY_TYPE_COVOIT = 1;
    public static final String TAG = "ERROR_COCOVOIT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journey);
        editTextDepart = this.findViewById(R.id.edittxt_editjourneyactivity_depart);
        editTextArrivee = this.findViewById(R.id.edittxt_editjourneyactivity_arrivee);
        editTextDate = this.findViewById(R.id.edittxt_editjourneyactivity_date);
        editTextHeure = this.findViewById(R.id.edittxt_editjourneyactivity_heure);
        btnDate = this.findViewById(R.id.btn_editjourneyactivity_date);
        btnHeure = this.findViewById(R.id.btn_editjourneyactivity_heure);
        btnSauvegarder = this.findViewById(R.id.btn_editjourneyactivity_sauvegarder);

        calendar = new GregorianCalendar();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker(EditJourneyActivity.this);
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                calendar.set(year,month,day);
            }
        });
        btnHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    TimePicker timePicker = new TimePicker(EditJourneyActivity.this);
                    int hour = timePicker.getHour();
                    int minute = timePicker.getMinute();
                    calendar.set(Calendar.HOUR,hour);
                    calendar.set(Calendar.MINUTE,minute);
                }
            }
        });
        btnSauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                int type = intent.getExtras().getInt(EXTRA_JOURNEY_TYPE);
                if (type==JOURNEY_TYPE_CAR_RIDE){
                    sendCarRideRequest();
                }
                else if (type==JOURNEY_TYPE_COVOIT){
                    sendCovoitRequest();
                }
                else{
                    Log.i(TAG, "Wrong Type");
                }
            }
        });
    }
    private void sendCarRideRequest(){

    }
    private void sendCovoitRequest(){

    }
}