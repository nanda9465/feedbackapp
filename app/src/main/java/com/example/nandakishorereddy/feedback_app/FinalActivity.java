package com.example.nandakishorereddy.feedback_app;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.app.Activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


public class FinalActivity extends AppCompatActivity  {
    Button btn1;
    ArrayList<String> final_question_list = new ArrayList<>();
    ArrayList<String> final_answer_list = new ArrayList<>();
    private String uname;
	private String url_login;
    private String client_name;						 				   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Bundle b = getIntent().getExtras();
        final_question_list = b.getStringArrayList("final Questions list");
        final_answer_list = b.getStringArrayList("final answer list");
        uname = b.getString("uname");
		client_name = b.getString("client_name");
        url_login = b.getString("url_login");										 								 
        System.out.println(final_question_list);
        System.out.println(final_answer_list);

       /* txtinput = (TextView) findViewById(R.id.txt_input);
        txtOutput = (TextView) findViewById(R.id.txt_output);*/
        btn1 = (Button)findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setEnabled(false);
                // Log.d("myTag", "var value "+var);
                Log.d("myTag", "Inside button 1 click ");
                Submit();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               /* Log.d("myTag", "Inside intent ");
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                Log.d("myTag", "after intent ");*/
                finish();

            }
        });


    }

    public void Submit() {
        Log.d("my new tag", "Submitt");

        if(isConnected()){

           // Toast.makeText(getBaseContext(), "Connected to server", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getBaseContext(), "Not Connected check the internet settings", Toast.LENGTH_LONG).show();
        }

        new HttpAsyncTask().execute(url_login);
        //new HttpAsyncTask().execute("http://10.0.2.2:8082/myservlet/Login_servlet");

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
           // Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "THANKS FOR THE FEEDBACK !", Toast.LENGTH_LONG).show();
        }
    }


    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            JSONArray array2 = new JSONArray();

            for (int i = 0; i < final_question_list.size(); i++) {

                array.put(final_question_list.get(i));

            }
            for (int i = 0; i < final_answer_list.size(); i++) {
               /* System.out.println(final_question_list.get(i));
                System.out.println(final_answer_list.get(i));

                jsonObject.put(final_question_list.get(i), final_answer_list.get(i));*/
                array2.put(final_answer_list.get(i));

            }

            jsonObject.put("Questions", array);
            jsonObject.put("answers", array2);
            jsonObject.put("uname", uname);
			jsonObject.put("client_name", client_name);										   
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();


            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
    }

}
