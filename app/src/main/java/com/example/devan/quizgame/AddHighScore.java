/*****************************************************
 This is a Quiz Game for android.

 Quiz Game
 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001

 This activity comes up when one has successfully registered a high score.

 It can be used to enter one's name.
 ******************************************************/

package com.example.devan.quizgame;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddHighScore extends AppCompatActivity {

    //initialize variable for final Score. This will be obtained from the bundle which was sent with intent from End Game.
    int finalscore = 0;

    //MyObjects to read a record from data file
    MyObject record;

    //EditText for the Enter Name field
    EditText nametxt;

    //make separate folder on the internal storage (project)
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/project";

    //this is the File path - it is in the project folder
    File file = new File(path + "/QuizGameHighScores.txt");

    //List of MyObjects - this is used to store the High Scores
    List<MyObject> myObjects ;

    //This is the duplicate list we will use to insert the entry in correct order by rank
    List<MyObject> mynewObjects = new ArrayList<>(11);

   //onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_high_score);

        //set the nametxt EditText and match to txtEnterName in our layour
        nametxt = (EditText) findViewById(R.id.txtEnterName);

        //Directory Path
        File dir = new File(path);

        //if directory does not exist, create
        if(!dir.exists())
        {
            dir.mkdirs();
        }

        //if anything is bundled with intent, get it.
        if(getIntent().getExtras() != null)
        {
            //obtain the final score from EndGame using the key "finalscore"
            Bundle extras = getIntent().getExtras();
            myObjects = (ArrayList<MyObject>) extras.getSerializable("ARRAYLIST");
            finalscore =  extras.getInt("finalscore");
        }
    }


    //override the onBackPressed method
    //instead of going back to previous activity exit to main screen.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddHighScore.this, QuizGame.class));
        finish();
    }

    //onSubmit method which is triggered when button Submit in layout is clicked
    //throws IO Exception
    public void onSubmit(View view) throws IOException
    {
        //if there are no high scores in current list, add a record
        if(myObjects.size() == 0)
        {
            //create a temporary list mysingleobject
            List<MyObject> mysingleobject = new ArrayList<>();

            //add new record with rank = 1 , name = as mentioned in EditText, points = finalscore
            record = new MyObject();
            record.Name = nametxt.getText().toString();
            record.Rank = 1;
            record.Points = finalscore;

            mysingleobject.add(record);

            //call save to file method which writes back to text file
            saveTofile(mysingleobject);

            //send intent back to main acitivity signalling succesful completion of task.
            Intent intent = new Intent(AddHighScore.this, QuizGame.class);
            startActivity(intent);
            finish();
        }
        else
        {
            //call method addToCorrectPosition on myObjects
            //returns a list of MyObjects
            mynewObjects = addToCorrectPosition(myObjects  );

            //after mynewObjects is in right order of rank and with new entry of high score, write to text file.
            saveTofile(mynewObjects);

            //send intent back to main activity signalling successful completion
            Intent intent = new Intent(AddHighScore.this, QuizGame.class);
            startActivity(intent);
            finish();
        }
    }

    //addToCorrectPosition method
    //returns List<MyObject> mynewObjects
    //param List<MyObject> myObjects
    public List<MyObject> addToCorrectPosition(List<MyObject> myObjects)
    {
        //create a temporary list of MyObjects for method
        List<MyObject> newmyobjects = new ArrayList<>(myObjects.size()+1);

        //create a temporary MyObject
        MyObject a = new MyObject();

        //the boolean flag that lets us know if new entry has already been added or not
        boolean found = false;

        //index to insert the entry
        int indexToInsert ;

        //loop through the myObjects
        for (int i = 0; i < myObjects.size(); i++)
        {
            //if found flag says entry has not been added previously and the finalscore > current entry's Points
            if (!found && finalscore >= myObjects.get(i).Points)
            {
                //change the flag to true saying we have found the place to add
                found = true;

                //insert at index i
                indexToInsert = i;

                //rank = i + 1
                a.Rank = indexToInsert + 1;

                //finalscore is the points
                a.Points = finalscore;
                a.Name = nametxt.getText().toString();

                //insert at index indextoInsert, MyObject a
                newmyobjects.add(indexToInsert, a);

                //decrease i, so we do not skip one entry from myObjects
                i--;
            }

            else {
                //if found is true, just add the rest of the entries of myObjects to newmyobjects with a rank + 1
                if (found) {
                    myObjects.get(i).Rank += 1;
                    newmyobjects.add(i+1, myObjects.get(i));
                } else {
                    //if found is not true, keep looking for correct place to add the entry.
                    //copy the current objects as we go to index i
                   newmyobjects.add(i, myObjects.get(i));
                }

            }
        }

        //if at end of the list we do not find the right place, insert at end
        if (!found) {
            indexToInsert = myObjects.size();
            a.Rank = indexToInsert + 1;
            a.Points = finalscore;
            a.Name = nametxt.getText().toString();
            newmyobjects.add(indexToInsert, a);
        }
        //return the newmyobjects
return newmyobjects;
    }

   //saveTofile method
    //write updated high scores list to text file
    //param mynewObjects - new list of MyObjects
    //throws IO Exception
    public void saveTofile(List<MyObject> mynewObjects) throws IOException {

        //initialize fos as our FileWriter
        FileWriter fw = null;

        //try with catch block for FileNotFoundException
        //pass the absolute path of our database file
        try {
            fw = new FileWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //prints line by line all MyObject in myobjects
        try {
            try {
                for (int i = 0; i < mynewObjects.size(); i++)
                {
                    //write each MyObject line by line
                    //calls the overridden toString method of MyObject
                    fw.write(mynewObjects.get(i).toString() );

                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        finally
        {   //close the FileWriter
            try
            {
                fw.close();
            }
            catch(IOException e )
            {
                e.printStackTrace();
            }

        }
    }
}
