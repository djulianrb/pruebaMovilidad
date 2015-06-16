package com.example.pruebam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

    private static final int SEGUNDOS=3000;

    //private final int NOTIFICATION_ID = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash);


        Handler handler = new Handler();
        handler.postDelayed(IniciarMenu(), SEGUNDOS);



    }


    private Runnable IniciarMenu()
    {
        Runnable res=new Runnable(){
            public void run(){
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        return res;
    }
}
