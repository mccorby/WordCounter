package com.mccorby.wordcounter.app.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mccorby.wordcounter.R;
import com.mccorby.wordcounter.app.presentation.MainView;
import com.mccorby.wordcounter.app.views.adapter.WordOccurrenceListAdapter;

/**
 * Use the {@link WordOccurrenceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordOccurrenceListFragment extends Fragment implements MainView {


    private static final String TAG = WordOccurrenceListFragment.class.getSimpleName();
    private WordOccurrenceListAdapter mAdapter;

    static MainPresenter mPresenter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment WordOccurrenceListFragment.
     */
    public static WordOccurrenceListFragment newInstance(String param1, String param2) {
        WordOccurrenceListFragment fragment = new WordOccurrenceListFragment();
        return fragment;
    }

    public WordOccurrenceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO This to be replaced by injection!
        if (mPresenter == null) {
            mPresenter = new MainPresenter(this);
            mPresenter.onCreate();
        }
        mPresenter.setMainView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_word_occurrence_list, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_word_occurrence_list_rv);
        // Use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new WordOccurrenceListAdapter(mPresenter);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void notifyNewDataIsAvailable() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void processDone() {
        Log.d(TAG, "PROCESS DONE!!!");
    }
}