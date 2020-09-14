package com.lunodzo.mobidir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View view) {
        if(view.getId() == R.id.btn_add){
            Intent addIntent = new Intent(getApplicationContext(), CompanyActivity.class);
            startActivity(addIntent);
        }
        else if(view.getId() == R.id.btn_contact){
            Intent contactIntent = new Intent(getApplicationContext(), ContactsActivity.class);
            startActivity(contactIntent);
        }
        else if(view.getId() == R.id.btn_register){
            Intent registerIntent = new Intent(getApplicationContext(), CompanyActivity.class);
        }
    }
}