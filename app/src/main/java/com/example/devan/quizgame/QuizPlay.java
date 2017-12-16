package com.example.devan.quizgame;

/*****************************************************
 This is a Quiz Game for android.

 Quiz Game
 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001

This is the play game acitivity. It consists of a question and two buttons.

 The user can press True or tilt phone left for answering true.
 The user can press False or tilt phone right for answering false.

 It also shows the running score.
 ******************************************************/


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class QuizPlay extends AppCompatActivity implements SensorEventListener
{
    //declare running score variable which is used to keep the running score
    int runningscore = 0;

    //declare randomnum which will pick questions randomly
    int randomnum;

    //list of custom MyQuestions
    List<MyQuestions> myquestions ;

    //create SensorManager and Senor objects
    private SensorManager sensorManager;
    private Sensor sensor;

    //used to get current orientation for sensor purposes
    private float Rot[] = new float[9];
    private float orientation[] = new float[9];

    //declare float array for output form low pass filter
    protected float[] gravSensorVals;

    //read MyQuestions object
    MyQuestions readRecord;

    //make separate folder on the internal storage (project)
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/project";

    //this is the File path - it is in the project folder
    File file = new File(path + "/QuizGameQuestions.txt");

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_play);


        //Directory Path
        File dir = new File(path);

        //if directory does not exist, create
        if(!dir.exists())
        {
            dir.mkdirs();
        }

        else
        {
            //if no there is nothing bundled onto intent, or in this case, if the intent came from QuizGame
            //this is the first time QuizPlay was active
            if(getIntent().getExtras() == null)
            {
                //initialize the myquestions as new arraylist
                myquestions = new ArrayList<>();
                //read from the text file
                myquestions = readfromfile(file);
            }
            //if something was bundled onto the activity, or in this case, if the intent was from QuizPlay itself
            //the running scores and myquestions are retrieved from the intent
            //this was implemented so 1. the same questions are not chosen due to randomness
            //2. More importantly, for efficiency becuase now we do not need to keep reading external storage every time activity is loaded
            else if(getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();
                myquestions = (ArrayList<MyQuestions>) extras.getSerializable("ARRAYLIST");
                runningscore = extras.getInt("runscore");
            }
        }

        //initializing Sensor Manager and sensor type
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //declare n
        int n = myquestions.size();

        //if the user gets through all the questions we end the game by passing intent to EndGame
        //this is done to prevent app crashing in case our fairly small database is used up entirely
        if( n == 0)
        {
            //send intent to EndGame
            //bundle intent with the running score
            Intent intent = new Intent(QuizPlay.this, EndGame.class);
            Bundle bundle = new Bundle();
            bundle.putInt("runscore", runningscore);
            intent.putExtras(bundle);
            QuizPlay.this.finish();
            startActivity(intent);
        }
        else {
            //create new Random class object, rand
            Random rand = new Random();

            //initialize randomnum with a value between 1 and size of myquestions array
            //gets a number from 0 < randomnum < myquestions
            randomnum = rand.nextInt(n);

            //set the playquestion and playrunning score TextViews
            TextView playquestion = (TextView) findViewById(R.id.txtQuestion);
            TextView playrunningscore = (TextView) findViewById(R.id.txtRunningScore);

            //these will be used to display the running questions and score
            playquestion.setText(myquestions.get(randomnum).Question);
            playrunningscore.setText(Integer.toString(runningscore));
        }
    }

    //low pass filter from Slides - Android Sensor by Prof. John Cole
    final float ALPHA = 0.25f; // if ALPHA = 1 OR 0, no filter applies.
    protected float[] lowPass(float[] input, float[] output )
    { if ( output == null )
        return input;
        for ( int i=0; i<input.length; i++ ) { output[i] = output[i] + ALPHA * (input[i] - output[i]); }
        return output; }

    //returns null
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    //override the onSensorChanged from SensorManager
    @Override
    public void onSensorChanged(SensorEvent event) {

        //if the event was due to accelerometer
        //pass event.values which is returned by the sensor, to low pass filter.
        //collect the filtered data in gravSensorVals
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravSensorVals = lowPass( event.values, gravSensorVals );

        //get orientation
        SensorManager.getOrientation(Rot, orientation);

        //if gravSensorVals is not null, meaning some usable data was received from sensor
        if (gravSensorVals != null )
        {
            //get the x, y and z values from sensor
            float x = gravSensorVals[0];
            float y = gravSensorVals[1];
            float z = gravSensorVals[2];

            //if the device is in portrait
            if (Math.abs(x) > Math.abs(y))
            {

                if(getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
                {
                    if (x < 0)
                    {
                        //if device was tilted, disable the sensor, as we will not be using sensor again till activity is refreshed
                        sensorManager.unregisterListener(this);
                        //call onClickFalse
                        //this is equivalent to pressing the false button
                        onClickFalse();
                    }

                    if (x > 0)
                    {   //if device was tilted, disable the sensor, as we will not be using sensor again till activity is refreshed
                        sensorManager.unregisterListener(this);
                        //call onClickTrue
                        //this is equivalent to pressing the true button
                        onclickTrue();
                    }
                }

            }
            //if device is in landscape
            else if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            {

                if( y > 0){
                    //if device was tilted, disable the sensor, as we will not be using sensor again till activity is refreshed
                    sensorManager.unregisterListener(this);
                    //call onClickFalse
                    //this is equivalent to pressing the false button
                    onClickFalse();
                }
                if(y < 0){
                    //if device was tilted, disable the sensor, as we will not be using sensor again till activity is refreshed
                    sensorManager.unregisterListener(this);
                    //call onClickTrue
                    //this is equivalent to pressing the true button
                    onclickTrue();
                }
            }
            if (x > (-2) && x < (2) && y > (-2) && y < (2) && z < (2) && z > (-2))
            {
                //unregister sensor
                    sensorManager.unregisterListener(this);
            }
        }

    }

    //method onclickTrue
    //triggered when either the device is tilted left or true button is pressed
    //if answer is correct, send intent back to QuizPlay
    //else send to EndGame
    public void onclickTrue()
    {
        if(myquestions.get(randomnum).Answer.equalsIgnoreCase("true"))
        {
            //if answer was true and we pressed true
            //remove the current question from list
            myquestions.remove(randomnum);

            //increase running score by 10
            runningscore = runningscore + 10;

            //send intent to QuizPlay
            Intent intent = new Intent(QuizPlay.this, QuizPlay.class);
            //Bundle with the myquestions so we do not have to read it again
            //and running score
            Bundle bundle = new Bundle();
            bundle.putInt("runscore", runningscore);
            bundle.putSerializable("ARRAYLIST",(Serializable)myquestions);
            intent.putExtras(bundle);
            QuizPlay.this.finish();
            startActivity(intent);
        }
        else
        {
            //if answer was false and we pressed true, end game
            //send intent bundled with just running score to EndGame
            Intent intent = new Intent(QuizPlay.this, EndGame.class);
            Bundle bundle = new Bundle();
            bundle.putInt("runscore", runningscore);
            intent.putExtras(bundle);
            QuizPlay.this.finish();
            startActivity(intent);
        }
    }

    //method onclickFalse
    //triggered when either the device is tilted left or true button is pressed
    //if answer is correct, send intent back to QuizPlay
    //else send to EndGame
    public void onClickFalse()
    {
        //    sensorManager.unregisterListener(this);
        if(myquestions.get(randomnum).Answer.equalsIgnoreCase("false"))
        {
            //if answer was false and we pressed false
            //remove the current question from list
            myquestions.remove(randomnum);

            //increase running score by 10
            runningscore = runningscore + 10;

            //send intent to QuizPlay
            Intent intent = new Intent(QuizPlay.this, QuizPlay.class);
            //Bundle with the myquestions so we do not have to read it again
            //and running score
            Bundle bundle = new Bundle();
            bundle.putInt("runscore", runningscore);
            bundle.putSerializable("ARRAYLIST",(Serializable)myquestions);
            intent.putExtras(bundle);
            QuizPlay.this.finish();
            startActivity(intent);
        }
        else
        {
            //if answer was true and we pressed false, end game
            //send intent bundled with just running score to EndGame
            Intent intent = new Intent(QuizPlay.this, EndGame.class);
            Bundle bundle = new Bundle();
            bundle.putInt("runscore", runningscore);
            intent.putExtras(bundle);
            QuizPlay.this.finish();
            startActivity(intent);
        }
    }

    //onclickTrueButton method
    //triggered when true button is pressed
    public void onClickTrueButton(View view)
    {
        onclickTrue();
    }

    //onClickFalseButton method
    //triggered when false button is pressed
    public void onClickFalseButton(View view)
    {
        onClickFalse();
    }


    // override activity's onResume method
    //register the sensor here
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //override the onBackPressed method
    //instead of going back to previous activity exit to main screen.
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(QuizPlay.this, QuizGame.class));
        finish();

    }


    // override activity's onResume method
    //unregister the sensor here
    @Override
    protected void onPause() {
        super.onPause();
        //unregister Sensor listener
        sensorManager.unregisterListener(this);
    }

    //readfromfile method which reads the file line by line
    //takes input file
    //returns List of MyObjects
    public List<MyQuestions> readfromfile(File file)
    {
        myquestions = new ArrayList<>();

        //initiate FileInputStream
        FileInputStream fis = null;

        //try with catch block checks FileNotFoundException
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        //Initiate new Input StreamReader
        InputStreamReader isr = new InputStreamReader(fis);

        //Initiate new BufferedReader
        BufferedReader br = new BufferedReader(isr);

        //String line which is a temp variable for lines
        String line;
        try
        {
            //while there are more lines, read line
            while ((line=br.readLine()) != null)
            {
                //parses the line line
                //returns string array
                //parameters 1. delimiter 2. for passing null values as well we put negative integer
                String[] data = line.split("\t", -1);

                //read into readRecord which is a MyObject
                readRecord = new MyQuestions();
                readRecord.Question = data[0] ;
                readRecord.Answer = data[1];

                //add to myobjects
                myquestions.add(readRecord);


            }
        }
        catch (IOException e) {e.printStackTrace();}
        //gets current channel
        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

return myquestions;

    }

}
