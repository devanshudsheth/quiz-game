
/*****************************************************

 This is a quiz game for android.

Quiz Game
 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001


 This is the MyQuestions class that is defined for the list of objects which hold the questions

 It can set/get values for Question and answer
 Also overrides toString method for the MyQuestions



 ******************************************************/
package com.example.devan.quizgame;

import java.io.Serializable;

/**
 * Created by devan on 4/21/2017.
 */

class MyQuestions implements Serializable
{

    //Empty Constructor
    MyQuestions()
    {
    }

    //Empty Constructor
    public MyQuestions(String Question,String Answer)
    {
    }

    //getter for Question
    public String getQuestion() {
        return Question;
    }

    //getter for Answer
    public String getAnswer() {
        return Answer;
    }

    // Override the ToString method for MyQuestions
    // Print all data as a single TAB spaced Line in data record
    // Each MyQuestions corresponds to one unique line
    @Override
    public String toString()
    {
        String newLine = System.getProperty("line.separator");
        return this.Question + "\t" + this.Answer + newLine ;
    }
    //Question
    String Question;

    //Answer
    String Answer;

}
