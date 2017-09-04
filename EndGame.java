package com.example.devan.quizgame;

/*****************************************************
 This is a Quiz Game for android.

 Quiz Game
 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001

 This activity is brought up when the user's game is over.
 It displays the final score.

 ******************************************************/
import android.content.Intent;
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

public class EndGame extends AppCompatActivity {

    //declare MyObject
    MyObject readRecord;

    //declare finalscore variable - this is passed on from QuizPlay
    int finalscore = 0;

    //make separate folder on the internal storage (project)
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/project";

    //this is the File path - it is in the project folder
    File file = new File(path + "/QuizGameHighScores.txt");

   //List of myObjects
    List<MyObject> myObjects = new ArrayList<>();

    //MyObject record
    MyObject record;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        TextView txtfinalscore = (TextView) findViewById(R.id.txtfinalpts);

        //Directory Path
        File dir = new File(path);

        //if directory does not exist, create
        if(!dir.exists())
        {
            dir.mkdirs();
        }

        else
        {
            //read from text file
            readfromfile(file);
        }
        //if something is bundled onto the intent, retrieve
        if(getIntent().getExtras() != null)
        {
            Bundle extras = getIntent().getExtras();
            //we retrieve the running score as final score at end of the game
            finalscore =  extras.getInt("runscore");

        }
        //show final score on screen
        txtfinalscore.setText(Integer.toString(finalscore));

    }

    //onClick Next button
    public void checkhighscore( View view)
    {
        //call checkhighscore on myObjects
        checkhighscore(myObjects);
    }

    //checkhighscoremethod
    //param List<MyObject> myObjects
    //this method finds out if the running score is one of the top score
    //First it makes sure that only ten top score are there at any single time
    //Second it will check if the score is higher than the lowest in top scores list
    public void checkhighscore(List<MyObject> myObjects)
    {
        //set b to size of myObjects
        int b = myObjects.size();

        //if b is less than 10 and finalscore > 0, we will add the current score to high score without checking last element
        //this is because if less than 10 scores are in list we allow till 10
        if(b < 10 && finalscore != 0)
            {
                //bundle the final score and serializable myObject to pass onto AddHighScore
                //bundled with intent "intent"
                Intent intent = new Intent(EndGame.this, AddHighScore.class);
                Bundle bundle = new Bundle();
                bundle.putInt("finalscore", finalscore);
                bundle.putSerializable("ARRAYLIST",(Serializable)myObjects);
                intent.putExtras(bundle);
                EndGame.this.finish();
                startActivity(intent);
            }
            //if there are already ten scores in the high scores list
            //check if the last element is <= the final score
            //if yes, then first remove the last element as that will be displaced from myObjects
            //Next, we will add the new high score
            //this is done to keep the list with only top 10 scores and not more
        else if (b == 10 && finalscore != 0)
            {
                record = myObjects.get(b - 1);
                if (record.Points <= finalscore )
                {
                myObjects.remove(b-1);
                //bundle the final score and serializable myObject to pass onto AddHighScore
                //bundled with intent "intent"
                Intent intent = new Intent(EndGame.this, AddHighScore.class);
                Bundle bundle = new Bundle();
                bundle.putInt("finalscore", finalscore);
                bundle.putSerializable("ARRAYLIST",(Serializable)myObjects);
                intent.putExtras(bundle);
                EndGame.this.finish();
                startActivity(intent);
                }
                else
                {
                    //final score lower than lowest in the list
                    Intent intent = new Intent(EndGame.this, QuizGame.class);
                    EndGame.this.finish();
                    startActivity(intent);
                }
            }
            //otherwise, just exit to main screen.
        else
            {
                Intent intent = new Intent(EndGame.this, QuizGame.class);
                EndGame.this.finish();
                startActivity(intent);
            }



    }

    //override the onBackPressed method
    //instead of going back to previous activity exit to main screen.
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(EndGame.this, QuizGame.class));
        finish();

    }

    //readfromfile method which reads the file line by line
    //takes input file
    public void readfromfile(File file)
    {
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
                readRecord = new MyObject();
                readRecord.Rank = Integer.valueOf(data[0]);
                readRecord.Name = data[1];
                readRecord.Points = Integer.valueOf(data[2]);

                //add to myobjects
                myObjects.add(readRecord);
            }
        }
        catch (IOException e) {e.printStackTrace();}
        //gets current channel
        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();
        }
    }

}
