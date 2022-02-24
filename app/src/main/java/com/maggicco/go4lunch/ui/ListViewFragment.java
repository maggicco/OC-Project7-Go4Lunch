package com.maggicco.go4lunch.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.ui.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class ListViewFragment extends Fragment {

    private Toolbar toolbarFragment;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListViewFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
//    public static ListViewFragment newInstance(int columnCount) {
//        ListViewFragment fragment = new ListViewFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        ((LoggedInActivity)getActivity()).setToolbarNavigation();
        ((LoggedInActivity) getActivity()).getSupportActionBar().setTitle("I'm hungry");

//        AppCompatActivity actionBar = (AppCompatActivity) getActivity();
//        actionBar.setSupportActionBar(toolbar);
//        DrawerLayout drawer = (DrawerLayout) actionBar.findViewById(R.id.activity_logged_in);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                getActivity(), drawer, toolbar, R.string.Open, R.string.Close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        drawerLayout = (DrawerLayout)findViewById(R.id.activity_logged_in);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);


        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem searchViewItem = menu.findItem(R.id.actionSearch);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        super.onCreateOptionsMenu(menu, inflater);

    }

}