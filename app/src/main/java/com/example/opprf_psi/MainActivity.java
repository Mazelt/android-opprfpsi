package com.example.opprf_psi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("native-lib");

    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_benchmark:
                return true;

            case R.id.action_contact_tracing:
                Intent intentToContactTracing = new Intent(this, ContactTracingActivity.class);
                intentToContactTracing.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentToContactTracing);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nativeLogging();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spadapter = ArrayAdapter.createFromResource(this, 
                R.array.function_choices, android.R.layout.simple_spinner_item);
        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spadapter);

        final Button button_runpsi = findViewById(R.id.buttonrun);
        button_runpsi.setOnClickListener(clickRunListener);
        // Example of a call to a native method

    }

    private View.OnClickListener clickRunListener = new View.OnClickListener() {
        public void onClick(View v) {
            final TextView tv = findViewById(R.id.textViewOUTPUT);
            tv.append("Setting Context...\n");
            tv.append("Running PSI!\n");
            EditText etneles = findViewById(R.id.editTextneles);
            int neles = Integer.parseInt(etneles.getText().toString());
            EditText etoneles = findViewById(R.id.editTextotherneles);
            int oneles = Integer.parseInt(etoneles.getText().toString());
            EditText etbitlen = findViewById(R.id.editTextbitlen);
            int bitlen = Integer.parseInt(etbitlen.getText().toString());
            EditText etepsilon = findViewById(R.id.editTextepsilon);
            float epsilon = Float.parseFloat(etepsilon.getText().toString());
            EditText etipaddr = findViewById(R.id.editTextaddress);
            String ipaddr = etipaddr.getText().toString();
            EditText etport = findViewById(R.id.editTextport);
            int port = Integer.parseInt(etport.getText().toString());
            EditText etnthreads = findViewById(R.id.editTextnthreads);
            int nthreads = Integer.parseInt(etnthreads.getText().toString());
            EditText etthreshold = findViewById(R.id.editTextthreshold);
            int threshold = Integer.parseInt(etthreshold.getText().toString());
            EditText etmegabins = findViewById(R.id.editTextnmegabins);
            int nmegabins = Integer.parseInt(etmegabins.getText().toString());
            EditText etpolys = findViewById(R.id.editTextpolysize);
            int polys = Integer.parseInt(etpolys.getText().toString());
            EditText etnfuns = findViewById(R.id.editTextnfuns);
            int nfuns = Integer.parseInt(etnfuns.getText().toString());
            EditText etpayloadbl = findViewById(R.id.editTextPayloadbl);
            int payloadbl = Integer.parseInt(etpayloadbl.getText().toString());
            Spinner sptype = findViewById(R.id.spinner);
            int psi_type = sptype.getSelectedItemPosition();
            nativeSetContext(neles, oneles, bitlen, epsilon, ipaddr, port, nthreads, threshold, nmegabins, polys, nfuns, payloadbl, psi_type);
            final Button thisbutton = (Button)findViewById(R.id.buttonrun);
            thisbutton.setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String output = nativeRun();
                    final String cont_output = nativeGetSomeContext();
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.append(output);
                            tv.append(cont_output);
                            thisbutton.setEnabled(true);

                        }
                    });
                }
            }).start();


        }
    };



    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void nativeSetContext(int neles, int oneles, int bitlen, float epsilon, String ipaddr, int port, int nthreads, int threshold, int megabins, int polys, int nfuns, int payloadbl, int psi_type);
    public native String nativeRun();
    public native int nativeLogging();
    public native String nativeGetSomeContext();
}
