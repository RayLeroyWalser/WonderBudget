package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class AddUpgradeTransactionActivity extends Activity {
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //No title bar
        setContentView(R.layout.activity_add_upgrade_transaction);

        Intent intent = getIntent();
        double currentAmount = intent.getDoubleExtra("currentAmount", 0);
        input = (EditText) findViewById(R.id.editAmount);
        ImageView ok = (ImageView) findViewById(R.id.ok);
        TextView title = (TextView) findViewById(R.id.title);

        title.setText(getResources().getString(R.string.add_upgrade_transaction));

        ok.setTag(currentAmount);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double desiredValue = Double.parseDouble(input.getText().toString());
                double value = desiredValue - (Double) v.getTag();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("value", value);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}