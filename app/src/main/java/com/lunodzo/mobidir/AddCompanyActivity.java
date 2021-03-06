package com.lunodzo.mobidir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class AddCompanyActivity extends AppCompatActivity {
    String cname, cemail, cphone, cwebsite, description, selectedRegion, selectedCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        //Make reference to XML widgets
        final Spinner spinnerRegion = findViewById(R.id.spinnerRegion);
        final Spinner spinnerCategory = findViewById(R.id.spinnerCategory);

        final EditText companyName = findViewById(R.id.companyName);
        final EditText companyEmail = findViewById(R.id.companyEmail);
        final EditText companyPhone = findViewById(R.id.phoneNumber);
        final EditText companyWebsite = findViewById(R.id.companyWebsite);
        final EditText companyDescription = findViewById(R.id.companyDescription);
        Button buttonSubmit = findViewById(R.id.btn_submit);

        //Define data source
        final String[] regionNames = new String[]{"Arusha", "Shinyanga", "Singida", "Mwanza", "Morogoro", "Njombe", "Iringa", "Mbeya", "Ruvuma"};
        final String[] categoryNames = new String[]{"Education", "Mining", "Transport", "Agriculture", "Automobile"};

        //configure array adapter
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(
                AddCompanyActivity.this, R.layout.spinner_itemdesign, R.id.itemMenu, regionNames);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                AddCompanyActivity.this, R.layout.spinner_itemdesign, R.id.itemMenu, categoryNames);

        //Assign array adapter to spinner
        spinnerRegion.setAdapter(regionAdapter);
        spinnerCategory.setAdapter(categoryAdapter);

        //Add spinner select event listener
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRegion = regionNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRegion = "Not Specified";
            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoryNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "Not Specified";
            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cname = companyName.getText().toString();
                cemail = companyEmail.getText().toString();
                cphone = companyPhone.getText().toString();
                cwebsite = companyWebsite.getText().toString();
                description = companyDescription.getText().toString();


                if(cname.isEmpty()){
                    Toast.makeText(AddCompanyActivity.this, "Company Name is required", Toast.LENGTH_SHORT).show();
                }else if (cemail.isEmpty()){
                    Toast.makeText(AddCompanyActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                }else if (cphone.isEmpty()){
                    Toast.makeText(AddCompanyActivity.this, "Phone is required", Toast.LENGTH_SHORT).show();
                }else if (cwebsite.isEmpty()){
                    Toast.makeText(AddCompanyActivity.this, "Company Website is required", Toast.LENGTH_SHORT).show();
                }


                //Execute Async class
                if (haveNetworkConnection()){
                    new RegisterNewCompany().execute();
                }else{
                    Toast.makeText(AddCompanyActivity.this, "No Internet, Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    //Create an inner class AsyncTask to upload data
    class RegisterNewCompany extends AsyncTask<String,String,String>{


        //Create progress dialog
        ProgressDialog pDialog;
        String php_response;

        //Add the three methods (REQUIRED)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddCompanyActivity.this);
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

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(3);

                nameValuePairs.add(new BasicNameValuePair("cname", cname));
                nameValuePairs.add(new BasicNameValuePair("cemail", cemail));
                nameValuePairs.add(new BasicNameValuePair("cphone", cphone));
                nameValuePairs.add(new BasicNameValuePair("cwebsite", cwebsite));
                nameValuePairs.add(new BasicNameValuePair("selectCategory", selectedCategory));
                nameValuePairs.add(new BasicNameValuePair("selectRegion", selectedRegion));
                nameValuePairs.add(new BasicNameValuePair("description", description));

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
            pDialog.dismiss();

            if (php_response.equals("1")) {
                Toast.makeText(AddCompanyActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), RegisteredCompaniesActivity.class));
            } else {
                Toast.makeText(AddCompanyActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
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
