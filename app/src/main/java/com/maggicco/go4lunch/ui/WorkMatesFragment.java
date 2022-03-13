package com.maggicco.go4lunch.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.maggicco.go4lunch.viewmodel.WorkMateViewModel;

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
    WorkMatesRecyclerViewAdapter adapter;
    ProgressDialog progressDialog;
    WorkMateViewModel workMateViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkMatesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_mates, container, false);
        ButterKnife.bind(this, view);

        ((LoggedInActivity)getActivity()).setToolbarNavigation();
        ((LoggedInActivity) getActivity()).getSupportActionBar().setTitle("Available workmates");

        workMateViewModel = new ViewModelProvider(getActivity()).get(WorkMateViewModel.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        //get workMates through viewModel
        workMateViewModel.getWorkMateMutableLiveData().observe(getViewLifecycleOwner(), workMateList -> {

            progressDialog.dismiss();
            adapter = new WorkMatesRecyclerViewAdapter(workMateList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();
    }
}