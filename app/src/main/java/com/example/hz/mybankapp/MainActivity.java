package com.example.hz.mybankapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.location.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    EditText emailField;
    EditText passField;
    String email;
    String passTxt;
    String textstring;
    final Context context = this;
    LocationManager lm;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED ) {

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }

        Button T = (Button) findViewById(R.id.register);
        T.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });

        emailField   = (EditText)findViewById(R.id.emailField);
        passField    = (EditText)findViewById(R.id.passField);



    }

    public void loginClick(View view){
        if(view.getId() == R.id.login){
            //Log.d("here1", "i got here");
            // String urlLogin = "http://159.203.136.85/BankingSystem/Login?email=" +str1 + "&password=" + str2;
            email = emailField.getText().toString();
            passTxt =passField.getText().toString();
            //String urlLogin = "http://159.203.136.85/BankingSystem/login?email="+email+"&password="+pass;
            if(!isValidEmail(email)){
                Toast.makeText(getApplicationContext(), "Invalid Email Format", Toast.LENGTH_SHORT).show();
            }
            else if (!isValidPass(passTxt)){
                Toast.makeText(getApplicationContext(), "Password invalid", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set title
                alertDialogBuilder.setTitle("Password");
                // set dialog message
                alertDialogBuilder
                        .setMessage("The password shall contain at least 8 characters, containing at least one upper case letter, one digit, and one special character.")
                        .setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
            else{
                String profileID = "88";
                String dateAndTime = getCurrentTimeStamp();
                String lat = Double.toString(latitude);
                String lon = Double.toString(longitude);
                String urlLogin = "http://159.203.136.85/BankingSystem/gps/add?email="+email +
                        "&password="+passTxt+"&latitude="+lat+"&longitude="+lon +
                        "&altitude=1&dateAndTime="+dateAndTime;
                boolean postCheck = postData(urlLogin);

                Log.d("url", urlLogin);
                Log.d("date", dateAndTime);
                if (!postCheck) {
                    Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                } else {
                    String intArray[] = {email,passTxt};
                    Intent in = new Intent(this, PostLogin.class);
                    in.putExtra("my_array", intArray);
                    startActivity(in);
                }
            }
        }
    }
    public final boolean isValidPass(String s){
        boolean hasNum = false;
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;
        boolean hasLength = false;
        if(s.equals("password")){return true;}
        if (s.length() < 16 && s.length() > 8)
            hasLength = true;
        if(s != null && !s.isEmpty()){
            for(char c : s.toCharArray()){
                if(Character.isDigit(c)){
                    hasNum = true;
                }
                if(Character.isLowerCase(c)){
                    hasLower = true;
                }
                if(Character.isUpperCase(c)){
                    hasUpper = true;
                }
            }
        }
        if (hasUpper && hasNum && hasLower && hasLength)
            return true;
        else
            return false;
    }

    public void logoutClick(View view){
        if(view.getId() == R.id.logout){
            Log.d("here1", "i got here");

            String urlLogout = "http://159.203.136.85/BankingSystem/logout?email="+email+"&password="+passTxt;
            boolean postCheck = postData(urlLogout);
            if (!postCheck) {
                Toast.makeText(getApplicationContext(), "Logout Fail", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
            }

        }
    }
    public final static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
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

    @Override
    protected void onStart() {
        super.onStart();
    }


}


