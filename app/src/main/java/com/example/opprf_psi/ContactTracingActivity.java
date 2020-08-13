package com.example.opprf_psi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;


public class ContactTracingActivity extends AppCompatActivity {
    int sum = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_tracing);
        final TextView sumout = (TextView) findViewById(R.id.sumout);
        Button buttonct = (Button) findViewById(R.id.buttonct);
        buttonct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                sum = r.nextInt(51);
                GradientDrawable ct_background = (GradientDrawable) sumout.getBackground();
                ct_background.setColor(getSumColor(sum));
                sumout.setText(""+nativeRun2());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_benchmark:
                finish();
                return true;

            case R.id.action_contact_tracing:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int getSumColor(int sum){
        if (sum == 0) {
            return Color.GRAY;
        } else if (sum < 10 ) {
            return Color.GREEN;
        } else if (sum < 30 ) {
            return Color.YELLOW;
        } else if (sum >= 30 ) {
            return Color.RED;
        } else {
            return Color.BLACK;
        }
    }

    public native int nativeRun2();
}

