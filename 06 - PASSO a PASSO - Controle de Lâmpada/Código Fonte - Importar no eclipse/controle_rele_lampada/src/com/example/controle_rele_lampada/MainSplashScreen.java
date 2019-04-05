package com.example.controle_rele_lampada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
 
public class MainSplashScreen extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash_screen);
         
// METHOD 1     
         
         /****** Create Thread that will sleep for 5 seconds *************/        
        Thread background = new Thread() {
            public void run() {
                 
                try {
                    // Thread will sleep for 7 seconds
                    sleep(1*3000);
                     
                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),Controle_rele_lampada.class);
                    startActivity(i);
                     
                    //Remove activity
                    finish();
                     
                } catch (Exception e) {
                 
                }
            }
        };
         
        // start thread
        background.start();
    }
     
    @Override
    protected void onDestroy() {
         
        super.onDestroy();
         
    }
}