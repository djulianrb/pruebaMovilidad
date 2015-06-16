package com.example.pruebam;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 
 * Login Activity Class 
 *
 */
public class LoginActivity extends Activity {
	public static final String TAG_NOMBRES = "nombres";
	public static final String TAG_TOKEN = "token";
    public static final String PREFERENCE_NAME = "preferencias";
    SharedPreferences prefs;
    Editor edit;
    String nom,tok = "";
    String email;
    String password;
    
    SQLiteDatabase db;
  
	
	
	// Progress Dialog Object
	ProgressDialog prgDialog;
	// Error Msg TextView Object
	TextView errorMsg;
	// Email Edit View Object
	EditText emailET;
	// Passwprd Edit View Object
	EditText pwdET;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		 prefs = getSharedPreferences(PREFERENCE_NAME, 0);
	     edit = prefs.edit();
	     nom = prefs.getString(TAG_NOMBRES, "");
	     tok = prefs.getString(TAG_TOKEN, "");
	     Log.i("nombre",""+nom);
	     
	     if(nom.length()!=0){


	            Intent intent = new Intent(this, InicioActivity.class);
	            startActivity(intent);
	        }
	     
	   //Abrimos la base de datos 'DBUsuarios' en modo escritura
	     UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(this, "DBUsuarios", null, 1);

	     db = usdbh.getWritableDatabase();
		
		
		// Find Error Msg Text View control by ID
		errorMsg = (TextView)findViewById(R.id.login_error);
		// Find Email Edit View control by ID
		emailET = (EditText)findViewById(R.id.loginEmail);
		// Find Password Edit View control by ID
		pwdET = (EditText)findViewById(R.id.loginPassword);
		// Instantiate Progress Dialog object
		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
	}
	
	/**
	 * Method gets triggered when Login button is clicked
	 * 
	 * @param view
	 */
	public void loginUser(View view){
		// Get Email Edit View Value
		 email = emailET.getText().toString();
		// Get Password Edit View Value
		password = pwdET.getText().toString();
		// Instantiate Http Request Param Object
		RequestParams params = new RequestParams();
		// When Email Edit View and Password Edit View have values other than Null
		if(Utility.isNotNull(email) && Utility.isNotNull(password)){
			// When Email entered is Valid
			if(Utility.validate(email)){
				// Put Http parameter username with value of Email Edit View control
				params.put("email", email);
				// Put Http parameter password with value of Password Edit Value control
				params.put("password", password);
				// Invoke RESTful Web Service with Http parameters
				try {
					invokeWS(params);
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			} 
			// When Email is invalid
			else{
				Toast.makeText(getApplicationContext(), "INGRESA UN EMAIL VALIDO", Toast.LENGTH_LONG).show();
			}
		} 
		// When any of the Edit View control left blank
		else{
			Toast.makeText(getApplicationContext(), "LLENA TODOS LOS CAMPOS", Toast.LENGTH_LONG).show();
		}
		
	}
	
	/**
	 * Method that performs RESTful webservice invocations
	 * 
	 * @param params
	 */
	
	
	
	
	public void invokeWS(RequestParams params){
		// Show Progress Dialog
		try {
			prgDialog.show();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
			
			
		
		 // Make RESTful webservice call using AsyncHttpClient object
		 AsyncHttpClient client = new AsyncHttpClient();
		 Log.i("salio","salio");
		 

         client.get("http://directodevelopment.igapps.co/application/login",params ,new AsyncHttpResponseHandler() {
        	 // When the response returned by REST has Http response code '200'
        	 
        	
        	 
             @Override
             public void onSuccess(String response) {
            	 // Hide Progress Dialog
            	 Log.i("RESPUESTA",""+response);
            	 prgDialog.hide();
                 try {
                	 	 // JSON Object
                         JSONObject obj = new JSONObject(response);
                         // When the JSON response has status boolean value assigned with true
                         if(obj.getBoolean("success")){
                        	 
                        	 //////////////
                             Log.i("USUARIO:", obj.getString("authToken") + "");
                             edit.putString(TAG_NOMBRES, obj.getString("authToken")+"");
                             edit.putString(TAG_TOKEN, obj.getString("authToken")+"");
                             edit.commit();
                             //////////////
                             //BD
                             ContentValues nuevoRegistro = new ContentValues();
                             nuevoRegistro.put("codigo", cryptWithMD5(password));
                             nuevoRegistro.put("nombre", email);
                             db.insert("Usuarios", null, nuevoRegistro);

                          
                             
                             //////////////
                        	 
                        	 Toast.makeText(getApplicationContext(), "ESTAS LOGUEADO!", Toast.LENGTH_LONG).show();
                        	 // Navigate to Home screen
                        	 navigatetoHomeActivity();
                         } 
                         // Else display error message
                         else{
                        	 errorMsg.setText(obj.getString("error_msg"));
                        	 Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                         }
                 } catch (JSONException e) {
                     // TODO Auto-generated catch block
                     Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                     e.printStackTrace();
                     
                 }
             }
             // When the response returned by REST has Http response code other than '200'
             @Override
             public void onFailure(int statusCode, Throwable error,
                 String content) {
            	 
            	 Log.i("despues del catch",""+content);
                 // Hide Progress Dialog 
                prgDialog.hide();
                 // When Http response code is '404'
                
                     Toast.makeText(getApplicationContext(), "Dispositivo no conectado a internet", Toast.LENGTH_SHORT).show();
                 
                     Cursor c = db.rawQuery("SELECT codigo, nombre FROM Usuarios", null);
                     c.moveToFirst();

                     do {
                         try {
                             String nom = c.getString(1);
                             String con = c.getString(0);

                             
							if(email.equals(nom) && cryptWithMD5(password).equals(con)){

                                 

                                

                                 Intent intent = new Intent(LoginActivity.this,
                                         InicioActivity.class);
                                 startActivity(intent);
                                 finish();

                                 break;

                             } else{

                                 
                                 Toast.makeText(getApplicationContext(), "Usuario o Contraseñas incorrectas", Toast.LENGTH_SHORT).show();
                             }

                         } catch (Exception e) {
                             Log.i("", "" + e);
                         }





                     } while(c.moveToNext()); 
                     
                     
                     
                     
             }
         });}

	
	
	/**
	 * Method which navigates from Login Activity to Home Activity
	 */
	public void navigatetoHomeActivity(){
		Intent homeIntent = new Intent(getApplicationContext(),InicioActivity.class);
		homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(homeIntent);
	}
	
	private static MessageDigest md;

	   public String cryptWithMD5(String pass){
	    try {
	        md = MessageDigest.getInstance("MD5");
	        byte[] passBytes = pass.getBytes();
	        md.reset();
	        byte[] digested = md.digest(passBytes);
	        StringBuffer sb = new StringBuffer();
	        for(int i=0;i<digested.length;i++){
	            sb.append(Integer.toHexString(0xff & digested[i]));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException ex) {
	    	Toast.makeText(getApplicationContext(), "Fallo en contraseña", Toast.LENGTH_SHORT).show();
	    }
	        return null;


	   }
	

	
}