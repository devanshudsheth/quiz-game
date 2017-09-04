/*****************************************************
 This is a Quiz Game for android.

 Quiz Game
 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001

 This activity shows the high scores in a ListView.
 The top ten scores are only taken.

 *******************************************************/

package com.example.devan.quizgame;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CheckHighScore extends AppCompatActivity
{
    //read MyObject
    MyObject readRecord;

    //make separate folder on the internal storage (project)
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/project";

    //this is the File path - it is in the project folder
    File file = new File(path + "/QuizGameHighScores.txt");

    //Declare List of MyObjects
    List<MyObject> myObjects = new ArrayList<>();

    //Declare the list view
    ListView contactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_high_score);


        //Directory Path
        File dir = new File(path);

        //if directory does not exist, create
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        //else read from the file
        else
        {
            readfromfile(file);
        }

        //instantiate the ListView
        contactListView = (ListView) findViewById(R.id.listHighScores);

        //initialize new ArrayAdapter
        ArrayAdapter<MyObject> adapter = new ContactListAdapter();

        //set the adapter sorting
        //override the compare method from Comparator
        adapter.sort(new Comparator<MyObject>()
        {
            @Override
            public int compare(MyObject lhs, MyObject rhs)
            {
                //return MyObject lhs comes first if absolute value of rank variable is less than rhs
                //return MyObject rhs comes first if absolute value of rank variable is less than lhs
                //return MyObject lhs is equal in rank if absolute value of rank variable is equal to rhs
                return lhs.Rank < rhs.Rank ? -1
                        : lhs.Rank > rhs.Rank ? 1
                        : 0;
            }
        });

        //sets adapter to ListView
        contactListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<MyObject>
    {
        //constructor for the adapter
        ContactListAdapter()
        {
            super(CheckHighScore.this,R.layout.list_view_item_2, myObjects);
        }

        //getItem method is overridden
        public MyObject getItem(int position){
            return myObjects.get(position);
        }
        //Override the getView method.
        //needs to be done to create custom adapter

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            //if the view == null
            if(view == null)
            {
                view = getLayoutInflater().inflate(R.layout.list_view_item_2, parent, false);
            }

            //define currentObject as MyObject with index position
            MyObject currentObject = myObjects.get(position);

            //put first TextView for the rank
            //gets rank and sets the text
            TextView rank = (TextView)view.findViewById(R.id.txtRank);
            rank.setText(String.valueOf(currentObject.getRank()));

            //put second TextView for the name
            //gets Name and sets the text
            TextView name = (TextView)view.findViewById(R.id.txtName);
            name.setText(currentObject.getName());

            //put third TextView for the points
            //gets points and sets the text
            TextView points = (TextView)view.findViewById(R.id.txtPoints);
            points.setText(String.valueOf(currentObject.getPoints()));

            return view;
        }
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
        catch (IOException e) {e.printStackTrace();}
    }


    //override the onBackPressed method
    //instead of going back to previous activity exit to main screen.
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(CheckHighScore.this, QuizGame.class));
        finish();

    }
}
