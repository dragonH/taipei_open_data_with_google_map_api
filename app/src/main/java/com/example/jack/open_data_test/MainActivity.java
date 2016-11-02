package com.example.jack.open_data_test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String url_test_detials = "http://data.taipei/opendata/datalist/apiAccess";
    String[]test_name;
    String[]test_address;
    String[]test_phone;
    Button bt1,btmap;
    Handler myHandler;
    private ProgressDialog pDialog;
    //JSONParser jsonParser = new JSONParser();
    GSONParser gsonParser=new GSONParser();
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test_name=new String[30];
        test_address=new String[30];
        test_phone=new String[30];
        bt1= (Button) findViewById(R.id.bt1);
        btmap= (Button) findViewById(R.id.btmap);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 0:

                                Intent in = new Intent(getApplicationContext(), show.class);
                                in.putExtra("name", test_name);
                                in.putExtra("address", test_address);
                                in.putExtra("phone",test_phone);
                                startActivity(in);
                                break;
                            default:
                                break;
                        }
                    }
                };

                // Getting data in background thread

                new Getdata("test").execute();
            }
        });

        btmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa=new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(aa);
            }
        });
    }
    public void onResume() {
        super.onResume();
        //registerReceiver(networkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void onPause() {
        //unregisterReceiver(networkStateReceiver);
        super.onPause();
    }


    class Getdata extends AsyncTask<String, String, String> {
        String describe;
        Getdata(String desc) {
            describe = desc;
        }


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

           // String id = session.getUserID();
            //Log.d(TAG, "SESSION ID : " + id);


            try {

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //params.add(new BasicNameValuePair("id", id));
                // params.clear();
                //Log.d(TAG, "DESCRIBE : " + describe);
                //Log.d(TAG, "★★★★★★★★★★ : " + params);
               if(describe.equals("test")) {
                    //params.clear();

                    //params.add(new BasicNameValuePair("scope", "resourceAquire&rid=ca9b88ff-a881-4ca3-a3a4-b26747f3e3e7"));
                    String ppap="scope=resourceAquire&rid=ca9b88ff-a881-4ca3-a3a4-b26747f3e3e7";
                    Log.d(TAG, "★★★★★★★★★★ : " + ppap);
                    JSONObject json = gsonParser.makeHttpRequest(url_test_detials, "GET", ppap);
                    Log.d(TAG, "RETURN JSON : " + json.toString());
                    JSONObject cc = json.getJSONObject("result");
                    JSONArray aa = cc.getJSONArray("results");
                    String str="";
                    int kk=cc.getJSONArray("results").length();
                    Log.d("★☆★☆★☆★☆★☆",""+kk);
                    str += "\n--------\n";

                    for(int i=0;i<30;i++){

                        test_name[i]=aa.getJSONObject(i).getString("name");
                        test_address[i]=aa.getJSONObject(i).getString("address_for_display");
                        test_phone[i]=aa.getJSONObject(i).getString("telephone");
                        str += "name: "+aa.getJSONObject(i).getString("name")+"\n";
                        str += "address: "+aa.getJSONObject(i).getString("address_for_display")+"\n";
                        str += "phone: "+aa.getJSONObject(i).getString("telephone")+"\n";
                        str += "\n--------\n";
                    }


                    Log.d("", str);
                    // check your log for json response

                    // json success tag
                   /* int success;
                    success = json.getInt("success");
                    if (success == 1) {
                        // successfully received json
                        JSONArray userArr = json.getJSONArray("bmi"); // JSON Array
                        Log.d(TAG, "The userArr is" + userArr.toString());

                        //each bmi is here
                        for(int i=0; i<userArr.length(); i++) {

                            bmi_buffer[i] = Double.parseDouble(userArr.getString(i));
                            Log.d(TAG, "EVERY BMI SHOW HERE : " + Double.toString(bmi_buffer[i]));
                        }

                    }else{
                        // user with id not found
                        Log.d(TAG, "QUERY BMI NOT FOUND");
                    }*/
                }

                else {
                    //wait to add...
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            myHandler.sendEmptyMessage(0);

        }
    }
    public void onBackPressed() {
        finish();
    }

}
