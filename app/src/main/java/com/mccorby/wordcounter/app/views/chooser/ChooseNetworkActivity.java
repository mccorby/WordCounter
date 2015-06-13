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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChooseNetworkActivity extends AppCompatActivity {

    @InjectView(R.id.activity_choose_network_url_et)
    EditText mUrlEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_network);
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
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.SELECTED_URL, mUrlEt.getText().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
