package com.quebec.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.quebec.app.R;

public class EventAddDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_adding);
    }

    public void saveDetails(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
