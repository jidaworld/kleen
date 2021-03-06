package com.example.john.kleen.DB;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.john.kleen.Controller.StepCounterService;
import com.example.john.kleen.Model.ProgressObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler {

    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = db.getReference("kleen");
    private static ProgressObject po;
    private ArrayList<ProgressObject> list = new ArrayList<>();



    public DBHandler() {
    }

    public static Result doInBackground(CallBack callBack) {
        Result result = new Result();
        Log.i("DebugFirebase","doInBackground");

        myRef.child("test")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(result.dataList!=null) {
                    Log.i("DebugFirebase","clearing list");
                    result.dataList.clear();
                }
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    po = postSnapshot.getValue(ProgressObject.class);
                    Log.i("DebugFirebase",po.toString());
                    result.dataList.add(po);
                }
                callBack.callBack(result.dataList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return result;
    }

    public static class Result {
        public List<ProgressObject> dataList = new ArrayList<>();
        public Exception error = null;
    }

    public static void sendToDB(ProgressObject po) {
        myRef.child("test").child(po.getDate()).setValue(po)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("DebugFirebase","Sucess!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DebugFirebase","Failed!");
                    }
                });
    }

    public static void updateChild(ProgressObject po){
        Map<String, Object> postValues = new HashMap<String,Object>();
        if(po.getStep_goal()!=0) {
            myRef.child("test").child(po.getDate()).child("step_goal").setValue(po.getStep_goal());
        }
        if (po.getWeight() != 0) {
            myRef.child("test").child(po.getDate()).child("weight").setValue(po.getWeight());
        }

    }

    public static FirebaseDatabase getDb() {
        return db;
    }

    public static DatabaseReference getMyRef() {
        return myRef;
    }


}
