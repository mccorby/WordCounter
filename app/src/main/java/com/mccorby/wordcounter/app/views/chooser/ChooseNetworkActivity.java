package com.mccorby.wordcounter.app.views.chooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mccorby.wordcounter.R;
import com.mccorby.wordcounter.app.Constants;
import com.mccorby.wordcounter.app.views.di.ChooseNetworkComponent;
import com.mccorby.wordcounter.app.views.di.ChooseNetworkModule;
import com.mccorby.wordcounter.app.views.di.DaggerChooseNetworkComponent;
import com.mccorby.wordcounter.app.views.error.ErrorHandler;
import com.mccorby.wordcounter.datasource.entities.BaseEvent;

import java.net.URL;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChooseNetworkActivity extends AppCompatActivity {

    private static final String TAG = ChooseNetworkActivity.class.getSimpleName();
    @Inject
    ErrorHandler mErrorHandler;

    @InjectView(R.id.activity_choose_network_url_et)
    EditText mUrlEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_network);

        ChooseNetworkComponent component = DaggerChooseNetworkComponent.builder()
                .chooseNetworkModule(new ChooseNetworkModule())
                .build();
        component.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ButterKnife.inject(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_network, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                returnSelectedValue();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnSelectedValue() {
        if (!TextUtils.isEmpty(mUrlEt.getText())) {
            // Check it's a valid url
            try {
                URL url = new URL(mUrlEt.getText().toString());
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.SELECTED_URL, url.toURI().getPath());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            } catch (Exception e) {
                BaseEvent error = new BaseEvent();
                error.setErrorMessage(getString(R.string.bad_url));
                mErrorHandler.showError(this, error);
            }
        }
    }
}
