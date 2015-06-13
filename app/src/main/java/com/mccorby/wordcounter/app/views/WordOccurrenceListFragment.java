package com.mccorby.wordcounter.app.views;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mccorby.wordcounter.R;
import com.mccorby.wordcounter.app.Constants;
import com.mccorby.wordcounter.app.presentation.MainView;
import com.mccorby.wordcounter.app.views.adapter.WordOccurrenceListAdapter;
import com.mccorby.wordcounter.app.views.chooser.ChooseNetworkActivity;
import com.mccorby.wordcounter.app.views.di.DaggerMainComponent;
import com.mccorby.wordcounter.app.views.di.MainComponent;
import com.mccorby.wordcounter.app.views.di.MainModule;
import com.mccorby.wordcounter.app.views.error.ErrorHandler;
import com.mccorby.wordcounter.datasource.entities.BaseEvent;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

/**
 * Use the {@link WordOccurrenceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * TODO Refactor source chooser
 */
public class WordOccurrenceListFragment extends Fragment implements MainView {


    private static final String TAG = WordOccurrenceListFragment.class.getSimpleName();
    private static final int FILE_SELECTION_REQUEST_CODE = 1;
    private static final int NETWORK_SELECTION_REQUEST_CODE = 2;

    private WordOccurrenceListAdapter mAdapter;

    @Inject
    MainPresenter mPresenter;

    @Inject
    ErrorHandler mErrorHandler;

    private boolean isProcessing;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WordOccurrenceListFragment.
     */
    public static WordOccurrenceListFragment newInstance() {
        WordOccurrenceListFragment fragment = new WordOccurrenceListFragment();
        return fragment;
    }

    public WordOccurrenceListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mPresenter == null) {
            MainComponent component = DaggerMainComponent.builder()
                    .mainModule(new MainModule(this))
                    .build();
            component.inject(this);
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isProcessing) {
            menu.clear();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSortAlpha:
                mPresenter.sortList(MainPresenter.SORTING.ALPHANUMERIC);
                break;
            case R.id.menuSortDefault:
                mPresenter.sortList(MainPresenter.SORTING.DEFAULT);
                break;
            case R.id.menuSortOccurrences:
                mPresenter.sortList(MainPresenter.SORTING.OCCURRENCES);
                break;
            case R.id.menuSourceFile:
                chooseFile();
                break;
            case R.id.menuSourceNetwork:
                chooseNetwork();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processStarted() {
        isProcessing = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void notifyNewDataIsAvailable() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void processDone() {
        isProcessing = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void displayError(BaseEvent errorMessage) {
        mErrorHandler.showError(getActivity(), errorMessage);
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, FILE_SELECTION_REQUEST_CODE);
    }

    private void chooseNetwork() {
        Intent intent = new Intent(getActivity(), ChooseNetworkActivity.class);
        startActivityForResult(intent, NETWORK_SELECTION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case FILE_SELECTION_REQUEST_CODE:
                    String filePath = data.getDataString();
                    Uri uri = Uri.parse(filePath);
                    File file = new File(uri.getPath());
                    mPresenter.processFile(file);
                    break;
                case NETWORK_SELECTION_REQUEST_CODE:
                    String urlData = data.getStringExtra(Constants.SELECTED_URL);
                    URL url = null;
                    try {
                        url = new URL(urlData);
                    } catch (MalformedURLException e) {
                        // We shouldn't be here
                    }
                    mPresenter.processUrl(url);
                    break;
            }
    }

}
