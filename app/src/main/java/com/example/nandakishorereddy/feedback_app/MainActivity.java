package com.example.nandakishorereddy.feedback_app;

import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private final int SPEECH_RECOGNITION_CODE = 1;
    private TextView txtinput;
    private TextView mic_text;
    private TextView txtOutput;
    private ImageButton btnMicrophone;
    private EditText editText;
    private String outputstring;
    private int var =0;
    private String uname;
	private String url_login;
    private String client_name;						 						   
    Button btn1, btn2;
   // private String[] question;
    ArrayList<String> final_question_list = new ArrayList<>();
    ArrayList<String> final_answer_list = new ArrayList<>();
  /*  private String question1 = "How do you feel about the Team ?";
    private String question2 = " Who is ur favourite actor ?";
   
 */

    private int step ,length=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtinput = (TextView) findViewById(R.id.txt_input);
        txtOutput = (TextView) findViewById(R.id.txt_output);
        mic_text = (TextView) findViewById(R.id.mic_text);
        txtOutput.setVisibility(View.GONE);
        editText = (EditText)findViewById(R.id.editText);
        editText.setVisibility(View.INVISIBLE);
        btn1 = (Button)findViewById(R.id.button1);
        btn2 = (Button)findViewById(R.id.button2);
        Bundle b = getIntent().getExtras();
        step = 0;
        ArrayList<String> Questions_list = null;
        if (b != null) {
            Questions_list = b.getStringArrayList("Questions list");
            uname = b.getString("uname");
			url_login = b.getString("url");
            client_name = b.getString("client_name");							   
            Toast.makeText(getApplicationContext(), "Welcome\t\t"+uname, Toast.LENGTH_LONG).show();
            Log.d("myTag", "var value " + Questions_list);
            length = Questions_list.size();
           // System.out.println(Questions_list);
        }
       // length = Questions_list.size();

        final String[] question = Questions_list.toArray(new String[Questions_list.size()]);

        txtinput.setText(question[step]);
        btnMicrophone = (ImageButton) findViewById(R.id.btn_mic);
        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // var++;
               // Log.d("myTag", "var value " + var);
                startSpeechToText();

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // var++;
               // Log.d("myTag", "var value "+var);
                Log.d("myTag", "Inside button 1 click ");
                samequestion(question[step]);
                btnMicrophone.setVisibility(View.VISIBLE);
                mic_text.setVisibility(View.VISIBLE);
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                editText.setVisibility(View.INVISIBLE);

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // var++;
                Log.d("myTag", "Inside button 2 click ");
                Log.d("myTag", "value"+step);
                if ( step ==length-1){
                        changeactivity();
                     }
                else{
                    changequestion(question[step+1]);
                btnMicrophone.setVisibility(View.VISIBLE);
                    mic_text.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);}

            }
        });

    }
    /**
     * Start speech to text intent. This opens up Google Speech Recognition API dialog box to listen the speech input.
     * */
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "START Speaking...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Callback for speech recognition activity
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    Log.d("myTag", "inside result");
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    outputstring = result.get(0);
                    txtOutput.setText(outputstring);
                    editText.setText(outputstring);
                    //changequestion();
                    editText.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    btn2.setVisibility(View.VISIBLE);
                    btnMicrophone.setVisibility(View.INVISIBLE);
                    mic_text.setVisibility(View.GONE);
                }
                break;
            }
        }

      /*  switch (var) {
            case 1: {
                Log.d("myTag", "inside case"+var);
                out_question1 = question1;
                out_answer1 = outputstring;
                Log.d("myTag", out_question1+" Your answer "+out_answer1);
                if (out_answer1 != null)
                {
                    Log.d("myTag", "inside answer to the Question ");

                    Toast.makeText(getApplicationContext(), "YOU JUST SAID",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), out_answer1,
                            Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //changing the question
                    changequestion(question2);

                }
                else
                {
                    Log.d("myTag", "inside answeris NULL ");
                    Toast.makeText(getApplicationContext(), "Please Provide ur Feedback By clicking on the Mic ",
                            Toast.LENGTH_SHORT).show();
                    var--;
                }

                break;
            }
            case 2: {
                Log.d("myTag", "inside case"+var);
                out_question2 = question2;
                out_answer2 = outputstring;
                Log.d("myTag", out_question2+"answer to "+out_answer2);
                changequestion(question3);
                break;
            }
            case 3: {
                Log.d("myTag", "inside case"+var);
                out_question3 = question3;
                out_answer3 = outputstring;
                changequestion(question4);
                break;
            }
            case 4: {
                Log.d("myTag", "inside case"+var);
                out_question4 = question4;
                out_answer4 = outputstring;
                changequestion(question5);
                break;
            }
            case 5: {
                Log.d("myTag", "inside case"+var);
                out_question5 = question5;
                out_answer5 = outputstring;
                changequestion(question5);
                break;
            }

        }*/

        Log.d("myTag", "inside after result");

    }


    public void samequestion(String question)
    {

        Log.d("myTag", "changing Question");
        txtinput.setText(question);
        txtOutput.setText("");

    }


    public void changequestion(String question)
    {

        Log.d("myTag", "value"+step);
        String final_question = txtinput.getText().toString();
        String final_answer = txtOutput.getText().toString();
        final_question_list.add(final_question);
        final_answer_list.add(final_answer);

        Log.d("myTag", "changing Question");
        Log.d("myTag", final_question);
        Log.d("myTag", final_answer);
        txtinput.setText(question);
        txtOutput.setText("");
        step++;

    }

    public void changeactivity()
    {

        Log.d("myTag", "value"+step);
        String final_question = txtinput.getText().toString();
        String final_answer = txtOutput.getText().toString();
        final_question_list.add(final_question);
        final_answer_list.add(final_answer);
        System.out.println(final_answer_list);
        System.out.println(final_question_list);


        Intent intent = new Intent(this, FinalActivity.class);
       // EditText editText = (EditText) findViewById(R.id.editText);
       // String message = editText.getText().toString();
        intent.putExtra("final Questions list", final_question_list);
        intent.putExtra("final answer list", final_answer_list);
        intent.putExtra("uname", uname);
		intent.putExtra("url_login", url_login);
        intent.putExtra("client_name", client_name);																				
        startActivity(intent);
        finish();

    }


}



