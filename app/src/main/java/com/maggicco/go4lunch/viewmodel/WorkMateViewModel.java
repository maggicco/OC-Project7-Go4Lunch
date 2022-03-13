package com.maggicco.go4lunch.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.maggicco.go4lunch.model.WorkMate;
import com.maggicco.go4lunch.repository.WorkMateRepository;

import java.util.List;

public class WorkMateViewModel extends ViewModel {

    MutableLiveData<List<WorkMate>> workMateListMutableLiveData;
    FirebaseFirestore firebaseFirestore;
    WorkMateRepository workMateRepository;

    public WorkMateViewModel() {

        workMateRepository = new WorkMateRepository();
        workMateListMutableLiveData = workMateRepository.getWorkMateListMutableLiveData();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public MutableLiveData<List<WorkMate>> getWorkMateMutableLiveData() {
        return workMateListMutableLiveData;
    }

}
