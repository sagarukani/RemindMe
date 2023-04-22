package com.example.remindme.home;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remindme.HomeViewModal;
import com.example.remindme.Notes;
import com.example.remindme.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Search fragment for searching notes
 */
public class SearchFragment extends Fragment implements ReminderAdapter.ItemClickListener {

    //variables
    RecyclerView rvNotes;
    ImageView ivSearch;
    TextView tvNoData;
    EditText etSearch;
    private HomeViewModal viewModal;
    ReminderAdapter adapter;
    List<Notes> noteList = new ArrayList<>();
    LiveData<List<Notes>> viewModelData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initializing variables and iews
        rvNotes = view.findViewById(R.id.rvNotes);
        ivSearch = view.findViewById(R.id.ivSearch);
        etSearch = view.findViewById(R.id.etSearch);
        tvNoData = view.findViewById(R.id.tvNoData);


        viewModal = ViewModelProviders.of(this).get(HomeViewModal.class);

        viewModelData = viewModal.search("");

        //observing view model
        viewModal.getAllCourses().observe(getViewLifecycleOwner(), notes -> {
            Log.d("TAG", new Gson().toJson(notes));
            noteList = notes;
            adapter.AddAllList(noteList);
            if(adapter.getAllList().size()==0){
                tvNoData.setVisibility(View.VISIBLE);
                rvNotes.setVisibility(View.GONE);
            }else {
                tvNoData.setVisibility(View.GONE);
                rvNotes.setVisibility(View.VISIBLE);
            }
        });

        //EditText change listener
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    searchList(etSearch.getText().toString());
                } else {
                    adapter.AddAllList(noteList);
                    if(adapter.getAllList().size()==0){
                        tvNoData.setVisibility(View.VISIBLE);
                        rvNotes.setVisibility(View.GONE);
                    }else {
                        tvNoData.setVisibility(View.GONE);
                        rvNotes.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //Setting up adapter
        adapter = new ReminderAdapter();
        rvNotes.setHasFixedSize(true);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotes.setAdapter(adapter);
        adapter.addItemClickListener(this);
    }


    //Search function
    private void searchList(String s) {
        List<Notes> notes = new ArrayList<>();
        for (int i = 0; i < noteList.size(); i++) {
            if (noteList.get(i).getTitle().contains(s)) {
                notes.add(noteList.get(i));
            }
        }
        adapter.AddAllList(notes);
        if(adapter.getAllList().size()==0){
            tvNoData.setVisibility(View.VISIBLE);
            rvNotes.setVisibility(View.GONE);
        }else {
            tvNoData.setVisibility(View.GONE);
            rvNotes.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClick(View v, int position, Notes notes) {

    }
}