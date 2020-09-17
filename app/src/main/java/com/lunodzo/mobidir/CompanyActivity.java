package com.lunodzo.mobidir;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class CompanyActivity extends AppCompatActivity {
    private Object String;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);


        //Make reference of xml to Java
        Spinner spinnerRegion = findViewById(R.id.spinnerRegion);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);

        EditText companyName = findViewById(R.id.companyName);
        EditText companyNumber = findViewById(R.id.phoneNumber);
        EditText companyEmail = findViewById(R.id.companyEmail);
        EditText companyWebsite = findViewById(R.id.companyWebsite);


        //Define data source
        String[] regionNames = new String[]{"Arusha", "Dodoma", "Tanga", "Iringa", "Njombe", "Tabora", "Mwanza", "Morogoro", "Dar es Salaam"};
        String[] categoryNames = new String[]{"Education", "Agricilture", "Transport", "Mining", "Engineering"};

        //Configure an array adapter
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(
                CompanyActivity.this,R.layout.spinner_itemdesign,R.id.itemMenu, regionNames);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(
                CompanyActivity.this,R.layout.spinner_itemdesign,R.id.itemMenu, categoryNames
        );

        //Assign array adopter to spinner
        spinnerRegion.setAdapter(regionAdapter);
        spinnerCategory.setAdapter(categoryAdapter);


        Button btnSubmit = findViewById(R.id.btn_submit);

    }
}
