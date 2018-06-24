package com.an_ant_on_the_sun.distancetraveled.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.an_ant_on_the_sun.distancetraveled.R;

public class AboutActivity extends AppCompatActivity {
    private TextView textViewAbout;
    private Button buttonRateApp;
    private Button buttonSupportDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onRateAppButtonClick(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.an_ant_on_the_sun.distancetraveled"));
        startActivity(browserIntent);
    }

    public void onSupportDeveloperButtonClick(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://yasobe.ru/na/na_poezdku_v_kitai"));
        startActivity(browserIntent);
    }
}
