package com.maggicco.go4lunch.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maggicco.go4lunch.model.WorkMate;
import com.maggicco.go4lunch.ui.MapsViewFragment;

import java.util.ArrayList;
import java.util.List;

public class WorkMateRepository {

    private static final String TAG =  " FireStore workMates ";
    MutableLiveData<List<WorkMate>> workMateListMutableLiveData;
    FirebaseFirestore firebaseFirestore;
    MutableLiveData<WorkMate> workMateMutableLiveData;


    public WorkMateRepository() {
        this.workMateListMutableLiveData = new MutableLiveData<>();
        //fireStore
        firebaseFirestore = FirebaseFirestore.getInstance();
        //workMatesList
        workMateMutableLiveData = new MutableLiveData<>();
    }

    //get workMates from fireStore
    public MutableLiveData<List<WorkMate>> getWorkMateListMutableLiveData(){

        Log.i(TAG, "getWorkMateListMutableLiveData: ");

        firebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                List<WorkMate> workMateList = new ArrayList<>();
                for (QueryDocumentSnapshot mate : value){

                    if(mate != null){
                        workMateList.add(mate.toObject(WorkMate.class));
                        Log.i(TAG, "onEvent: !!!!!!!!!!!!!!!!!!!!!!!!!!" + mate);
                    }
                }
                workMateListMutableLiveData.postValue(workMateList);
            }
        });

        return workMateListMutableLiveData;

    }


}
