package com.example.opprf_psi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
//        System.loadLibrary("boost_thread");
//        System.loadLibrary("boost_system");
//        System.loadLibrary("boost_filesystem");
//        System.loadLibrary("boost_date_time");
//        System.loadLibrary("boost_chrono");
//        System.loadLibrary("gmp");
//        System.loadLibrary("gmpxx");
//        System.loadLibrary("boost_atomic");



        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("native-lib");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
