package com.example.nandakishorereddy.feedback_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private int var = 0;
    // public String[] Questions;
   // private static String url_login = "http://10.0.2.2:8082/myservlet/Login_servlet";
    JSONParser jParser = new JSONParser();

    JSONObject json;
	@InjectView(R.id.input_server) EditText _serverText;
    @InjectView(R.id.input_client) EditText _clientText;																										
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;

    ArrayList<String> Questions_list = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Log.d(TAG, "On button Click");
                login();
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");
        var = 0;
        if (!validate()) {
            onLoginFailed();
            return;
        }
        if(isConnected()){

            // Toast.makeText(getBaseContext(), "Connected to server", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getBaseContext(), "Not Connected check the internet settings", Toast.LENGTH_LONG).show();
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        //   String email = _emailText.getText().toString();
        //  String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        new Login().execute();

        // params.add(new BasicNameValuePair("u",username));
        // params.add(new BasicNameValuePair("p",pass));

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // Log.d(TAG, " os handler");
                        if (var == 2) {
                            // On complete call either onLoginSuccess or onLoginFailed
                            Log.d(TAG, " inside var 2");
                            onLoginFailed();
                        }

                        if (var == 3){

                            DBERROR();
                        }
                        if (var == 4){

                            Timedout();
                        }

                        progressDialog.dismiss();
                    }
                }, 2000);
    }


    private class Login extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            // Getting username and password from user input
            //   String username = uname.getText().toString();
            //   String pass = password.getText().toString();
            String server_id = _serverText.getText().toString();
            String client_name = _clientText.getText().toString();													  
            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // params.add(new BasicNameValuePair("u",username));
            // params.add(new BasicNameValuePair("p",pass));
            params.add(new BasicNameValuePair("u",email));
            params.add(new BasicNameValuePair("p",password));
			String url_login = "http://"+server_id+":8090/myservlet/Login_servlet";
            //json = null;																	   																 
            json = jParser.makeHttpRequest(url_login, "GET", params);

            System.out.println(json);
            // System.out.println(json.getClass().getName());
            String s=null;


            Log.d(TAG, "Login executee");
            if (json != null) {
                try {
                    s = json.getString("info");
                    Log.d("Msg", json.getString("info"));
                    if (s.equals("success")) {
                        var = 1;
                        //  ArrayList<String> Questions_list=new ArrayList<String>();
                        JSONObject obj = new JSONObject(String.valueOf(json));
                        JSONArray arr = obj.getJSONArray("Questions");
                        Questions_list = new ArrayList<String>();
                        for (int i = 0; i < arr.length(); i++) {

                            //Questions[i] = arr.getString(i);
                            Questions_list.add(arr.getString(i));

                        }
                       // onLoginSuccess();
                        finish(Questions_list);

                    }
                    if (s.equals("fail")) {

                        var = 2;
                        Log.d(TAG, "failure ");
                        //  onLoginFailed();
                        //finish();
                    }
                    if (s.equals("DB Connection Error")) {

                        var = 3;
                        Log.d(TAG, "DB Connection Error ");
                        //  onLoginFailed();
                        //finish();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            else{

                var = 4;
                Log.d(TAG, " Connection Timed out ");

            }

            return null;
        }

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        // moveTaskToBack(true);
        Log.d(TAG, "  On Back pressed ");
        _loginButton.setEnabled(true);
        _emailText.setText("");
        _passwordText.setText("");

    }

 /*   public void onLoginSuccess() {
        // Log.d(TAG, "Login  Sucesss");
        // _loginButton.setEnabled(true);
        finish();

    }*/
 public boolean isConnected(){
     ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
     NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
     if (networkInfo != null && networkInfo.isConnected())
         return true;
     else
         return false;
 }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed Re enter username and password ", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }
    public void Timedout() {
        Toast.makeText(getBaseContext(), "Timed Out ", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public void DBERROR() {
        Toast.makeText(getBaseContext(), " DB Connection Error ", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        //Log.d(TAG, "Inside validate");
        boolean valid = true;
        String server_id = _serverText.getText().toString();
        String email = _emailText.getText().toString();
        String client_name = _clientText.getText().toString();	  
        String password = _passwordText.getText().toString();
        
		if (server_id.isEmpty()) {
            _serverText.setError("enter a valid Server IP");
            valid = false;
        } else {
            _serverText.setError(null);
        }

        if (client_name.isEmpty()) {
            _serverText.setError("enter a Client Name");
            valid = false;
        } else {
            _serverText.setError(null);
        }
        if (email.isEmpty()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 12) {
            _passwordText.setError("between 4 and 12 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    public void finish(ArrayList<String> Questions_list)
    {
		String server_id = _serverText.getText().toString();
        String client_name = _clientText.getText().toString();
        String url_login = "http://"+server_id+":8090/myservlet/Login_servlet";												
         // Log.d(TAG, " Inside finish");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("Questions list", Questions_list);
        intent.putExtra("uname", _emailText.getText().toString() );
		intent.putExtra("url", url_login);
        intent.putExtra("client_name", client_name);								  
        //intent.putStringArrayListExtra("Questions list", Questions_list);
        startActivity(intent);

    }
}

