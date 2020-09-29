package com.lunodzo.mobidir;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CompanyActivity extends AppCompatActivity {
    String cname, cemail, cphone, cwebsite, region, category;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        //Make reference to XML widgets
        final Spinner spinnerRegion = findViewById(R.id.spinnerRegion);
        final Spinner spinnerCategory = findViewById(R.id.spinnerCategory);

        final EditText companyName = findViewById(R.id.companyName);
        final EditText companyEmail = findViewById(R.id.companyEmail);
        final EditText companyPhone = findViewById(R.id.phoneNumber);
        final EditText companyWebsite = findViewById(R.id.companyWebsite);
        Button buttonSubmit = findViewById(R.id.btn_submit);

        //Define data source
        final String[] regionNames = new String[]{"Arusha", "Shinyanga", "Singida", "Mwanza", "Morogoro", "Njombe", "Iringa", "Mbeya", "Ruvuma"};
        final String[] categoryNames = new String[]{"Education", "Mining", "Transport", "Agriculture", "Automobile"};

        //configure array adapter
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(
                CompanyActivity.this,R.layout.spinner_itemdesign,R.id.itemMenu, regionNames);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(
                CompanyActivity.this,R.layout.spinner_itemdesign,R.id.itemMenu, categoryNames);

        //Assign array adapter to spinner
        spinnerRegion.setAdapter(regionAdapter);
        spinnerCategory.setAdapter(categoryAdapter);

//        //Add spinner select event listener
//        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                final String selectedRegion = regionNames[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Select a Region", Toast.LENGTH_SHORT).show();
//            }
//        });

//        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Object selCat = spinnerCategory.getSelectedItem().toString();
//                final String selectedCategory = categoryNames[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Select a Category", Toast.LENGTH_SHORT).show();
//            }
//        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cname = companyName.getText().toString();
                cemail = companyEmail.getText().toString();
                cphone = companyPhone.getText().toString();
                cwebsite = companyWebsite.getText().toString();
                category = spinnerCategory.getSelectedItem().toString();
                region = spinnerRegion.getSelectedItem().toString();


                if(cname.isEmpty()){
                    Toast.makeText(CompanyActivity.this, "Company Name is required", Toast.LENGTH_SHORT).show();
                }else if (cemail.isEmpty()){
                    Toast.makeText(CompanyActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                }else if (cphone.isEmpty()){
                    Toast.makeText(CompanyActivity.this, "Phone is required", Toast.LENGTH_SHORT).show();
                }else if (cwebsite.isEmpty()){
                    Toast.makeText(CompanyActivity.this, "Company Website is required", Toast.LENGTH_SHORT).show();
                }

                if (haveNetworkConnection()){
                    new RegisterNewCompany().execute();
                }else{
                    Toast.makeText(CompanyActivity.this, "No Internet, Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Create progress dialog
    ProgressDialog pDialog;
    String php_response;


    //Create an inner class AsyncTask to upload data
    class RegisterNewCompany extends AsyncTask<String,String,String>{

        //Add the three methods (REQUIRED)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CompanyActivity.this);
            pDialog.setMessage("Uploading Data");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                /* setting up the connection and send data with url */
                // create a http default client - initialize the HTTp client

                DefaultHttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost("http://twiga2.com/mobidir_lunodzo/register_company.php");

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("cname", cname));
                nameValuePairs.add(new BasicNameValuePair("cemail",cemail));
                nameValuePairs.add(new BasicNameValuePair("cphone",cphone));
                nameValuePairs.add(new BasicNameValuePair("cwebsite",cwebsite));
                nameValuePairs.add(new BasicNameValuePair("selectCategory", category));
                nameValuePairs.add(new BasicNameValuePair("selectRegion",region));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                InputStream inputStream = response.getEntity().getContent();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream), 4096);
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                php_response = sb.toString();
                inputStream.close();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Try Again", Toast.LENGTH_LONG).show();
            }

            return php_response;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    //method to check internet availability(WiFi and MobileData)
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
