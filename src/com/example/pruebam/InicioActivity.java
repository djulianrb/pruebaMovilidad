package com.example.pruebam;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.widget.DrawerLayout;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class InicioActivity extends ListActivity {
	
	//LISTJSON
	private Context context;
	private static String url = "http://mapsgoogle.herobo.com/prueba.json";

	private static final String TAG_VTYPE = "id";
	private static final String TAG_VCOLOR = "name";
	private static final String TAG_FUEL = "surname";
	private static final String TAG_TREAD = "telephone";
	private static final String TAG_OPERATOR = "approvedOperators";
	private static final String TAG_NAME = "name";
	private static final String TAG_POINTS = "experiencePoints";

	ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
	
	ListView lv ;
	
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
        
       
        
        new ProgressTask(InicioActivity.this).execute();
     

        prefs = getSharedPreferences(PREFERENCE_NAME, 0);
        edit = prefs.edit();
        nom = prefs.getString(TAG_TOKEN, "");

        tv = (TextView) findViewById(R.id.textView2);
        tv.setText(nom);

         ListView listView = (ListView) findViewById(R.id.list_view);
         DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         
         lv = (ListView)findViewById(android.R.id.list);

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
    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;

		private ListActivity activity;

		// private List<Message> messages;
		public ProgressTask(ListActivity activity) {
			this.activity = activity;
			context = activity;
			dialog = new ProgressDialog(context);
		}

		/** progress dialog to show user that the backup is processing. */

		/** application context. */
		//private Context context;

		protected void onPreExecute() {
			this.dialog.setMessage("Progress start");
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			ListAdapter adapter = new SimpleAdapter(context, jsonlist,
					R.layout.list_item, new String[] { TAG_VTYPE, TAG_VCOLOR,
							TAG_FUEL, TAG_TREAD }, new int[] {
							R.id.vehicleType, R.id.vehicleColor, R.id.fuel,
							R.id.treadType });
			
			setListAdapter(adapter);

			// selecting single ListView item
			 lv = getListView();
			
		}

		protected Boolean doInBackground(final String... args) {
			try {
				
				com.example.JSONInicio.JSONParser jParser = new com.example.JSONInicio.JSONParser();

				// getting JSON string from URL
				JSONArray json = jParser.getJSONFromUrl(url);

				for (int i = 0; i < json.length(); i++) {

					try {
						JSONObject c = json.getJSONObject(i);
						String vtype = c.getString(TAG_VTYPE);

						String vcolor = c.getString(TAG_VCOLOR);
						String vfuel = c.getString(TAG_FUEL);
						String vtread = c.getString(TAG_TREAD);

						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_VTYPE, vtype);
						map.put(TAG_VCOLOR, vcolor);
						map.put(TAG_FUEL, vfuel);
						map.put(TAG_TREAD, vtread);
						jsonlist.add(map);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Toast.makeText(getApplicationContext(), "NO SE PUDO CARGAR LA LISTA DE PROSPECTOS", Toast.LENGTH_LONG).show();
					}
				}


				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
			

		}

	}



}