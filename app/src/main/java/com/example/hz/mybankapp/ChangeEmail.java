package com.example.hz.mybankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class ChangeEmail extends AppCompatActivity {
    String oldEmail;
    String newEmail;

    EditText oldText;
    EditText newText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        oldText = (EditText)findViewById(R.id.editText6);
        newText = (EditText)findViewById(R.id.editText7);


    }

    public void changeEmailClick(View view){
        if(view.getId() == R.id.changeEmailButton){
            oldEmail = oldText.getText().toString();
            newEmail = newText.getText().toString();
            if(!isValidEmail(oldEmail)){
                Toast.makeText(getApplicationContext(), "Old email is invalid", Toast.LENGTH_SHORT).show();
            }
            else if(!isValidEmail(newEmail)){
                Toast.makeText(getApplicationContext(), "New email is invalid", Toast.LENGTH_SHORT).show();

            }
            else{

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
