package com.example.pruebam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class InicioActivity extends Activity {
	
	//LISTJSON
	
	
	//LISTJSON
	 
	 
    public static final String PREFERENCE_NAME = "preferencias";
    public static final String TAG_NOMBRES = "nombres";
    public static final String TAG_TOKEN = "token";
    SharedPreferences prefs;
    Editor edit;
    String nom = "defecto";
    TextView tv;

    DrawerLayout drawerLayout;
    ListView listView;
    String[] opciones = { "Dashboard y prospectos", "Cerrar Sesion"};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        
     

        prefs = getSharedPreferences(PREFERENCE_NAME, 0);
        edit = prefs.edit();
        nom = prefs.getString(TAG_TOKEN, "");

        tv = (TextView) findViewById(R.id.textView2);
        tv.setText(nom);

         ListView listView = (ListView) findViewById(R.id.list_view);
         DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        listView.setAdapter(new ArrayAdapter<Object>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                opciones));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //Toast.makeText(InicioActivity.this, "Item: " + opciones[arg2] + arg2,
                //        Toast.LENGTH_SHORT).show();
                //drawerLayout.closeDrawers();

                if (arg2==1){


                	//edit.putString(TAG_TOKEN, "");
                    edit.putString(TAG_NOMBRES, "");

                    edit.commit();

                    Intent intent = new Intent(InicioActivity.this,
                            SplashActivity.class);
                    startActivity(intent);
                    finish();



                }
            }

        });


        // Mostramos el botón en la barra de la aplicación
        //getActionBar().setDisplayHomeAsUpEnabled(true);



    }



	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (drawerLayout.isDrawerOpen(listView)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(listView);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {

            case KeyEvent.KEYCODE_BACK:

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setMessage("Desea desloguearse?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                edit.putString(TAG_NOMBRES, "");

                                edit.commit();

                                Intent intent = new Intent(InicioActivity.this,
                                        SplashActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                dialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();

                return true;
        }
        event.getAction();

        return super.onKeyDown(keyCode, event);
    }
    
    
    
    
    ///////////////
    



}