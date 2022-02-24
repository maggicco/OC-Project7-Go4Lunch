package com.maggicco.go4lunch.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.model.WorkMate;
import com.maggicco.go4lunch.ui.placeholder.PlaceholderContent;

import java.sql.Struct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 */
public class WorkMatesFragment extends Fragment {


    DatabaseReference databaseReference;
    @BindView(R.id.workMates_recyclerView)
    RecyclerView recyclerView;
    //WorkMatesRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkMatesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_mates, container, false);
        ButterKnife.bind(this, view);

        ((LoggedInActivity)getActivity()).setToolbarNavigation();
        ((LoggedInActivity) getActivity()).getSupportActionBar().setTitle("Available workmates");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        CollectionReference collectionUsers = db.collection("users");

//        Query query = collectionUsers.orderBy("placeId", Query.Direction.DESCENDING);
//
//        // Set the adapter
//        FirebaseRecyclerOptions<WorkMate> options
//                = new FirebaseRecyclerOptions.Builder<WorkMate>()
//                .setQuery(query, WorkMate.class)
//                .build();
//        // Connecting object of required Adapter class to
//        // the Adapter class itself
//        this.adapter = new WorkMatesRecyclerViewAdapter(options);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        // Connecting Adapter class with the Recycler view*/
//        recyclerView.setAdapter(adapter);

        return view;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity


    @Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();
    }
}