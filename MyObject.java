
/*****************************************************
This is a Quiz Game for android.

 Quiz Game
 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001


 This is the MyObject class that is defined for the list of objects which hold the high scores

 It can set/get values for rank, name, points
 Also overrides toString method for the MyObject



 ******************************************************/

package com.example.devan.quizgame;

import java.io.Serializable;
import java.lang.String;

//class implements Serializable because we can use it to pass our object array with putSerializable bundled onto an intent
class MyObject implements Serializable
{

    //Empty Constructor
    MyObject()
    {
    }

    //Empty Constructor
    public MyObject(String Question,String Answer)
    {
    }

    //getter for Rank
    int getRank() {
        return Rank;
    }

    //getter for Name
    public String getName() {
        return Name;
    }

    //getter for Points
    int getPoints() {
        return Points;
    }

    // Override the ToString method for MyObject
    // Print all data as a single TAB spaced Line in data record
    // Each MyObject corresponds to one unique line
    @Override
    public String toString()
    {
        String newLine = System.getProperty("line.separator");
        return this.Rank + "\t" + this.Name + "\t" + this.Points + newLine ;
    }

    //int Rank
    int Rank;

    //String Name
    String Name;

    //int Points
    int Points;



}
