/*****************************************************
 This is a Quiz Game for android.

 Quiz Game
 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001

 This activity is used to enter custom questions to the game!
 Be careful not to enter the same questions though.

 ******************************************************/

package com.example.devan.quizgame;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AddQuestion extends AppCompatActivity

{

    //list of custom MyQuestions
    List<MyQuestions> myquestions = new ArrayList<>();

    //checkbox count
    //this is done to programmatically check that more than one checkbox is not checked
    int checkboxcount = 0;

    //the Save button
    Button btnAdd;

    //used to read the current questions already available
    //this is done to check that no duplicate questions are allowed
    MyQuestions readRecord;

    //make separate folder on the internal storage (project)
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/project";


    //this is the File path - it is in the project folder
    File file = new File(path + "/QuizGameQuestions.txt");

    //Declare the EditText
    EditText questiontxt;

    //Declare the checkboxes
    CheckBox checkBoxfalse;
    CheckBox checkBoxtrue;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        //Directory Path
        File dir = new File(path);

        //if directory does not exist, create
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        //otherwise read from the database text file
        else
        {
             readfromfile(file);
          }

        //set the EditText for the text field txtAddQuestion
        questiontxt = (EditText) findViewById(R.id.txtAddQuestion);

        //set the checkboxes chkFalse, chkTrue
        checkBoxfalse = (CheckBox) findViewById(R.id.chkFalse);
        checkBoxtrue = (CheckBox) findViewById(R.id.chkTrue);

        //set the button, btnAddQuestion
        btnAdd = (Button) findViewById(R.id.btnAddQuestion);

       //when initially on opening, length of first name is 0, disable the btnSave
        btnAdd.setEnabled(false);


        //add a textChangedListener for when the text changed in questiontxt
        questiontxt.addTextChangedListener(new TextWatcher() {

            //when the text is changed, do the following
            public void onTextChanged(CharSequence s, int start, int before,int count)
            {
                //if text length goes back down to 0, disable the btnAdd again
                if(questiontxt.length() == 0)
                {
                   btnAdd.setEnabled(false);
                }
                //if only one checkbox is ticked and text length is > 0, enable the button
                else
                {
                    if(checkboxcount == 1)
                    btnAdd.setEnabled(true);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        //add a setonCheckedChangedListener for when the text changed in checkBoxfalse
        checkBoxfalse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if ( isChecked )
                { //on check

                   //increment checkbox count to indicate more than one checkbox is ticked
                    checkboxcount++;

                    //if current checkbox count is one and length of text field > 0, enable the button
                    if(checkboxcount == 1 && questiontxt.length() > 0)
                    {
                        btnAdd.setEnabled(true);
                    }
                    //if more than one checkboxes is ticked, disable the button
                    if(checkboxcount == 2)
                    {
                        btnAdd.setEnabled(false);
                    }
                }
                else{
                    //if checkbox count was 1 and we uncheck, disable the btnAdd again
                    if (checkboxcount == 1) {
                        btnAdd.setEnabled(false);
                        checkboxcount = 0;
                    }
                    //if checkbox count was more than one and one of them is unchecked again, enable the button
                    if(checkboxcount == 2 && questiontxt.length() > 0 )
                    {
                        btnAdd.setEnabled(true);
                        checkboxcount = 1;
                    }
                }
            }
        });

        //add a setonCheckedChangedListener for when the text changed in checkBoxtrue
        checkBoxtrue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    //increment checkbox count to indicate more than one checkbox is ticked
                    checkboxcount++;

                    //if current checkbox count is one and length of text field > 0, enable the button
                    if(checkboxcount == 1 && questiontxt.length() > 0)
                    {
                        btnAdd.setEnabled(true);
                    }

                    //if more than one checkboxes is ticked, disable the button
                    if(checkboxcount == 2)
                    {
                        btnAdd.setEnabled(false);
                    }
                }
                else{//on uncheck
                    //if checkbox count was 1 and we uncheck, disable the btnAdd again
                    if (checkboxcount == 1)
                    {
                        btnAdd.setEnabled(false);
                        checkboxcount = 0;
                    }
                    //if checkbox count was more than one and one of them is unchecked again, enable the button
                    if(checkboxcount == 2 && questiontxt.length() > 0)
                    {
                       btnAdd.setEnabled(true);
                        checkboxcount = 1;
                    }
                }
            }
        });
    }

    //readfromfile method which reads the file line by line
    //takes input file
    //ultimately used to check if the question is already in our list!
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



    }

    //override the onBackPressed method
    //instead of going back to previous activity exit to main screen.
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(AddQuestion.this, QuizGame.class));
        finish();

    }

    //method to check if a duplicate question has been entered
    //param List<MyQuestions> myquestion
    //return boolean true if it is a duplicate
    //else return false
    public boolean isDuplicate(List<MyQuestions> myquestion)
    {
        //set a value thaat will be returned initially to false
        boolean dupcheck = false;
        for (int i = 0; i < myquestion.size(); i++) {
            //ignore case while checking for questions which are potentially same
            if (questiontxt.getText().toString().equalsIgnoreCase(myquestion.get(i).Question)) {
                //if duplicate, set dupcheck to true
                dupcheck = true;
            }

        }
        return dupcheck;
    }

    //onClick btnAdd
    //method addQuestion
    //throws IOException
    public void addQuestion(View view) throws IOException
    {
    boolean dupcheck = isDuplicate(myquestions);
        //if not duplicate, add the question
        if(!dupcheck)
        {
            MyQuestions addRecord = new MyQuestions();
            addRecord.Question = questiontxt.getText().toString();

            if (checkBoxfalse.isChecked()) {
                addRecord.Answer = "false";
            } else if (checkBoxtrue.isChecked()) {
                addRecord.Answer = "true";
            }
            myquestions.add(addRecord);
            saveTofile(myquestions);
        }

        //finish the activity
        //reload same activity, in case user wants to add more questions
        finish();
        startActivity(getIntent());
    }

    //saveTofile method
    //throws IO Exception
    //param List<MyQuestions> myquestions
    //used to write to the text file
    public void saveTofile(List<MyQuestions> myquestions) throws IOException
    {
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
                for (int i = 0; i < myquestions.size(); i++)
                {
                    fw.write(myquestions.get(i).toString() );

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
