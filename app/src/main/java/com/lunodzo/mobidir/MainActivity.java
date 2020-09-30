package com.lunodzo.mobidir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void buttonClicked(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                Intent addIntent = new Intent(getApplicationContext(), CompanyActivity.class);
                startActivity(addIntent);
                break;

            case R.id.btn_contact:
                Intent contactIntent = new Intent(getApplicationContext(), ContactsActivity.class);
                startActivity(contactIntent);
                break;

            case R.id.btn_register:
                Intent registeredIntent = new Intent(getApplicationContext(), RegisteredCompaniesActivity.class);
                startActivity(registeredIntent);
                break;
        }
    }
}