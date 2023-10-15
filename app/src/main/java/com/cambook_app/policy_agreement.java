package com.cambook_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class policy_agreement extends AppCompatActivity {

    private Button button;
    private CheckBox policyCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_agreement);

        policyCheckbox = findViewById(R.id.policyCheckbox);
        TextView errorText = findViewById(R.id.errorText);
        button = (Button) findViewById(R.id.agreebtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the policy agreement checkbox is checked
                if (policyCheckbox.isChecked()) {
                    create_user();
                } else {
                    errorText.setText("Please check the policy agreement checkbox to proceed.");
                }
            }

            private void create_user() {
                Intent intent = new Intent(policy_agreement.this, homepage.class);
                startActivity(intent);
                finish();
            }
        });
        // Add a listener to enable/disable the button based on the checkbox state
        policyCheckbox.setOnClickListener(view -> {
            button.setEnabled(policyCheckbox.isChecked());

            // Update the button background based on the checkbox state
            int buttonBackground = policyCheckbox.isChecked() ?
                    R.drawable.color_enabled_button_background :
                    R.drawable.color_disabled_button_background;
            button.setBackgroundResource(buttonBackground);
        });
    }
}