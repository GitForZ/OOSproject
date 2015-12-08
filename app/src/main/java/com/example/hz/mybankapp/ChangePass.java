package com.example.hz.mybankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ChangePass extends AppCompatActivity {
    String oldPassStr;
    String newPassStr;
    String confirmPassStr;
    EditText oldPass;
    EditText newPass;
    EditText confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        oldPass = (EditText)findViewById(R.id.editText8);
        newPass = (EditText)findViewById(R.id.editText9);
        confirmPass = (EditText)findViewById(R.id.editText10);
    }

    public void changePassClick(View view){
        if(view.getId() == R.id.changePasswordButton){
            oldPassStr = oldPass.getText().toString();
            newPassStr = newPass.getText().toString();
            confirmPassStr = confirmPass.getText().toString();

            //test case: two different passwords (new and confirm)
            if(!(newPassStr.equals(confirmPassStr))){
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
            else{

            }
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
                Log.d("page not reached", "bcode =" + bcode);

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
