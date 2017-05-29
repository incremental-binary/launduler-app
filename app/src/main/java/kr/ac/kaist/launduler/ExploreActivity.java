package kr.ac.kaist.launduler;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
    }

    @Override
    public void onStart(){
        super.onStart();
        String list_machines_json = "{\n" +
                "\"machines\": [\n" +
                "{\n" +
                "\"SerialNum\": \"012345678\",\n" +
                "\"location\": \"N1 525\",\n" +
                "\"isUse\": \"using\"\n" +
                "},\n" +
                "{\n" +
                "\"SerialNum\": \"012345678\",\n" +
                "\"location\": \"N1 525\",\n" +
                "\"isUse\": \"using\"\n" +
                "}\n" +
                "]\n" +
                "}";

        JSONArray list_machines;
        try {
            list_machines = new JSONObject(list_machines_json).getJSONArray("machines");
        } catch (JSONException e) {
            list_machines = null;
            Log.d("ExploreActivity", "Failed to load list");

        }

        if(list_machines != null){
            int i;
            List<JSONObject> arraylist_machines = new ArrayList<JSONObject>();
            for(i=0;i<list_machines.length();i++) {
                try {
                    arraylist_machines.add(list_machines.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ExploreActivity","Failed to get JSONObject");

                }
            }
            ListView list = (ListView)findViewById(R.id.list_machines);
            MachineListAdapter adapter = new MachineListAdapter(this,R.layout.item_machine,arraylist_machines);
            list.setAdapter(adapter);

        }
    }

    public interface MachineInterface{
        @Headers({"Accept: application/json"})
        @GET("/place/machine")
        Call<ExploreActivity> repo(@Query("appid") String appid, @Query("lat") double lat, @Query("lon") double lon);

    }
}
