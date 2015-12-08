package com.example.hz.mybankapp;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.hz.mybankapp.R.id.editText;
import static com.example.hz.mybankapp.R.id.editText2;
import static com.example.hz.mybankapp.R.id.editText3;
import static com.example.hz.mybankapp.R.id.editText4;
import static com.example.hz.mybankapp.R.id.editText5;
import static com.example.hz.mybankapp.R.id.register;


public class Register extends ActionBarActivity {

    String email;
    String firstName;
    String lastName;
    String ssn;
    String bankId;
    EditText emailField;
    EditText fNameField;
    EditText lNameField;
    EditText ssnField;
    EditText bankIdField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = (EditText)findViewById(R.id.editText);
        fNameField = (EditText)findViewById(R.id.editText2);
        lNameField = (EditText)findViewById(R.id.editText3);
        ssnField = (EditText)findViewById(R.id.editText5);
        bankIdField = (EditText)findViewById(R.id.editText4);
    }

    public void registrationClick(View view){
        if(view.getId() == register){
            //Log.d("here1", "i got here");
            // String urlLogin = "http://159.203.136.85/BankingSystem/Login?email=" +str1 + "&password=" + str2;
            email = emailField.getText().toString();
            firstName = fNameField.getText().toString();
            lastName = lNameField.getText().toString();
            ssn = ssnField.getText().toString();
            bankId = bankIdField.getText().toString();

            Log.d("test", "email is " + email);
            Log.d("test", "fName is " + firstName);
            Log.d("test", "lName is " + lastName);
            Log.d("test", "ssn is " + ssn);
            Log.d("test", "bankId is " + bankId);
            if(!isValidEmail(email)){
                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            }
            else if(!isValidSSN(ssn)){
                Toast.makeText(getApplicationContext(), "Invalid SSN", Toast.LENGTH_SHORT).show();
            }
            else {
                String urlRegister = "http://159.203.136.85/BankingSystem/account/create/" + bankId + "?ssn=" + ssn + "&name=" + firstName + "+" + lastName + "&email=" + email;
                Log.d("test", "url is " + urlRegister);
                boolean postCheck = postData(urlRegister);
                if (!postCheck) {
                    Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), PostLogin.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public boolean isValidSSN(String SSN){
        int count = 0;
        for (char c : SSN.toCharArray()){
            if (Character.isDigit(c)){
                count++;
            }
            else
                return false;
        }
        if (count != 9)
            return false;
        else
            return true;
    }
    public final static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public boolean postData(String url) {
        HttpClient client = new DefaultHttpClient();
        boolean httpost = false;
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse reponse = client.execute(get);
            // = reponse.getStatusLine().getStatusCode();
            int bcode = reponse.getStatusLine().getStatusCode();
            if (bcode != 200) {
                Log.d("page not reached" ,"bcode =" +bcode);

            }
            String result = EntityUtils.toString(reponse.getEntity());
            Log.d("JSON result",  result);

            JSONObject jsonMain = new JSONObject(result);
            JSONObject jsonInner = jsonMain.getJSONObject("meta");
            String metaCode = jsonInner.getString("code");
            if (metaCode.equals("400")) {
                String cause = jsonInner.getString("cause");
                Log.d("ERROR cause" ,  cause);
                Toast.makeText(getApplicationContext(), cause, Toast.LENGTH_SHORT).show();

                httpost = false;
            } else if (metaCode.equals("200")) {
                String casue = jsonInner.getString("cause");
                Log.d("ERROR cause" , casue);
                httpost = true;
            }

        } catch (ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return httpost;
    }


}