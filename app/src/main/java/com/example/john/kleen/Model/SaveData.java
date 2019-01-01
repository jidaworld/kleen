package com.example.john.kleen.Model;

import android.app.Activity;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SaveData {

    private Activity activity;

    public SaveData(Activity activity) {
        this.activity = activity;
    }

    public void write(ArrayList<ProgressObject> list) {
        FileOutputStream out = null;
        ObjectOutputStream o = null;

        try {
            out = activity.openFileOutput("stepsData.txt", Context.MODE_PRIVATE);
            o = new ObjectOutputStream(out);
            o.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(o != null) {
                    o.close();
                }
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<ProgressObject> read() {

        FileInputStream in = null;
        ObjectInputStream o = null;
        ArrayList<ProgressObject> list = null;

        try {
            in = activity.openFileInput("stepsData.txt");
            o = new ObjectInputStream(in);
            list = (ArrayList<ProgressObject>) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(o != null) {
                    o.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;

    }
}
