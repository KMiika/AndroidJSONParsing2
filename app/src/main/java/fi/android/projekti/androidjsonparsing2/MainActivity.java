package fi.android.projekti.androidjsonparsing2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    // url to make request
    // private static String url = "http://api.androidhive.info/contacts/";

    // JSON Node names
    private static final String TAG_PORTS = "ports";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_IP = "ip";
    private static final String TAG_PORT = "port";


    // contacts JSONArray
    JSONArray ports = null;
    ListView lv;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // selecting single ListView item
        lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                String cost = ((TextView) view.findViewById(R.id.address)).getText().toString();
                String description = ((TextView) view.findViewById(R.id.ip)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
                in.putExtra(TAG_NAME, name);
                in.putExtra(TAG_ADDRESS, cost);
                in.putExtra(TAG_IP, description);
                startActivity(in);
            }

        });

        String url = "HTTP://172.20.240.11:7003";
        new ParseTask().execute(url);
    }


    private class ParseTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

        @Override
        protected void onPreExecute() {
            //ProgressBar bar = (ProgressBar)findViewById(R.id.progressbar);
            //bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            String url = params[0];

            fi.android.projekti.androidjsonparsing2.JSONParser jParser = new fi.android.projekti.androidjsonparsing2.JSONParser();

            JSONObject json = jParser.getJSONFromUrl(url);

            ArrayList<HashMap<String, String>> contactList = new ArrayList<>();
            try {
                ports = json.getJSONArray(TAG_PORTS);

                for (int i = 0; i < ports.length(); i++) {
                    JSONObject c = ports.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String address = c.getString(TAG_ADDRESS);
                    String ip = c.getString(TAG_IP);
                    //String portss = c.getString(TAG_PORTS);


                    HashMap<String, String> map = new HashMap<>();

                    map.put(TAG_ID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_ADDRESS, address);
                    map.put(TAG_IP, ip);
                   // map.put(TAG_PORTS, portss);

                    contactList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return contactList;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> contactList) {

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[]{TAG_NAME, TAG_ADDRESS, TAG_IP}, new int[]{R.id.name, R.id.address, R.id.ip});
            lv.setAdapter(adapter);

            //ProgressBar bar = (ProgressBar)findViewById(R.id.progressbar);
            //bar.setVisibility(View.INVISIBLE);

        }
    }
}
