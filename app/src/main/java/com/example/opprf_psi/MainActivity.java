package com.example.opprf_psi;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nativeLogging();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spadapter = ArrayAdapter.createFromResource(this, 
                R.array.function_choices, android.R.layout.simple_spinner_item);
        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spadapter);
        final Button button_encContext = findViewById(R.id.buttonclear);
        button_encContext.setOnClickListener(clickClearListener);

        final Button button_runpsi = findViewById(R.id.buttonrun);
        button_runpsi.setOnClickListener(clickRunListener);
        // Example of a call to a native method

    }

    private View.OnClickListener clickClearListener = new View.OnClickListener() {
        public void onClick(View v) {
            final TextView encodedContext = findViewById(R.id.editTextEncodedContext);
            encodedContext.setText("");
        }
    };

    private View.OnClickListener clickRunListener = new View.OnClickListener() {
        public void onClick(View v) {
            final TextView tv = findViewById(R.id.textViewOUTPUT);
            EditText etencContext = findViewById(R.id.editTextEncodedContext);
            String encodedContext = etencContext.getText().toString();
            int neles;
            int oneles;
            int bitlen;
            float epsilon;
            String ipaddr ;
            int port;
            int nthreads;
            int threshold;
            int nmegabins;
            int polys;
            int nfuns;
            int payloadbl;
            int psi_type;
            if (!encodedContext.isEmpty()) {
                String[] contextElements = encodedContext.split(";");
                if (contextElements.length != 13) {
                    tv.append("Encoded Context is not complete!\n");
                    return;
                }
                tv.append("Setting encoded Context...\n");
                neles = Integer.parseInt(contextElements[0]);
                oneles = Integer.parseInt(contextElements[1]);
                bitlen = Integer.parseInt(contextElements[2]);
                epsilon = Float.parseFloat(contextElements[3]);
                ipaddr = contextElements[4];
                port = Integer.parseInt(contextElements[5]);
                nthreads = Integer.parseInt(contextElements[6]);
                threshold = Integer.parseInt(contextElements[7]);
                nmegabins = Integer.parseInt(contextElements[8]);
                polys = Integer.parseInt(contextElements[9]);
                nfuns = Integer.parseInt(contextElements[10]);
                payloadbl = Integer.parseInt(contextElements[11]);
                psi_type = Integer.parseInt(contextElements[12]);


            } else {

                tv.append("Setting Context...\n");
                EditText etneles = findViewById(R.id.editTextneles);
                neles = Integer.parseInt(etneles.getText().toString());
                EditText etoneles = findViewById(R.id.editTextotherneles);
                oneles = Integer.parseInt(etoneles.getText().toString());
                EditText etbitlen = findViewById(R.id.editTextbitlen);
                bitlen = Integer.parseInt(etbitlen.getText().toString());
                EditText etepsilon = findViewById(R.id.editTextepsilon);
                epsilon = Float.parseFloat(etepsilon.getText().toString());
                EditText etipaddr = findViewById(R.id.editTextaddress);
                ipaddr = etipaddr.getText().toString();
                EditText etport = findViewById(R.id.editTextport);
                port = Integer.parseInt(etport.getText().toString());
                EditText etnthreads = findViewById(R.id.editTextnthreads);
                nthreads = Integer.parseInt(etnthreads.getText().toString());
                EditText etthreshold = findViewById(R.id.editTextthreshold);
                threshold = Integer.parseInt(etthreshold.getText().toString());
                EditText etmegabins = findViewById(R.id.editTextnmegabins);
                nmegabins = Integer.parseInt(etmegabins.getText().toString());
                EditText etpolys = findViewById(R.id.editTextpolysize);
                polys = Integer.parseInt(etpolys.getText().toString());
                EditText etnfuns = findViewById(R.id.editTextnfuns);
                nfuns = Integer.parseInt(etnfuns.getText().toString());
                EditText etpayloadbl = findViewById(R.id.editTextPayloadbl);
                payloadbl = Integer.parseInt(etpayloadbl.getText().toString());
                Spinner sptype = findViewById(R.id.spinner);
                psi_type = sptype.getSelectedItemPosition();
            }
            nativeSetContext(neles, oneles, bitlen, epsilon, ipaddr, port, nthreads, threshold, nmegabins, polys, nfuns, payloadbl, psi_type);
            final Button thisbutton = (Button)findViewById(R.id.buttonrun);
            final TextView sumout = (TextView) findViewById(R.id.sumout);
            thisbutton.setEnabled(false);
            tv.append("Running PSI!\n");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final int output = nativeRun();
                    final String cont_output = nativeGetSomeContext();
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.append("PSI run returned: "+output+"\n");
                            tv.append(cont_output);
                            thisbutton.setEnabled(true);
                            GradientDrawable sum_background = (GradientDrawable) sumout.getBackground();
                            sum_background.setColor(getSumColor(output));
                            sumout.setText(""+output);
                        }
                    });
                }
            }).start();


        }
    };

    public int getSumColor(int sum){
        if (sum == 0) {
            return Color.GRAY;
        } else if (sum < 10 ) {
            return Color.GREEN;
        } else if (sum < 100 ) {
            return Color.YELLOW;
        } else if (sum >= 200 ) {
            return Color.RED;
        } else {
            return Color.BLACK;
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void nativeSetContext(int neles, int oneles, int bitlen, float epsilon, String ipaddr, int port, int nthreads, int threshold, int megabins, int polys, int nfuns, int payloadbl, int psi_type);
    public native int nativeRun();
    public native int nativeLogging();
    public native String nativeGetSomeContext();
}
