package com.example.joaolopes.gestbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PRsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dl;
    private ActionBarDrawerToggle toogle;
    //
    ProgressDialog pd;
    String TAG = PRsActivity.class.getSimpleName();
    ListView lv;
    List<Records> recordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prs);
        //
        dl = (DrawerLayout) findViewById(R.id.drawerMenu);
        dl.setClickable(true);
        toogle = new ActionBarDrawerToggle(this, dl, R.string.openMenu, R.string.closeMenu);
        dl.addDrawerListener(toogle);
        toogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv);
        navigationView.bringToFront(); //para o click funcionar
        navigationView.setNavigationItemSelectedListener(this);
        //
        lv = (ListView) findViewById(R.id.lvPRs);
        recordsList = new ArrayList<>();
        //
        GetRecords();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.treinos:
                OpenAnotherActivity(MainActivity.class);
                break;
            case R.id.aulas:
                OpenAnotherActivity(ClassesActivity.class);
                break;
            case R.id.pr:
                OpenAnotherActivity(PRsActivity.class);
                break;
            case R.id.configs:
                OpenAnotherActivity(SettingsActivity.class);
                break;
        }

        dl.closeDrawers();

        return false;
    }

    private void OpenAnotherActivity(Class<?> subActivity) {
        Intent newActivity = new Intent(this, subActivity);
        //newActivity.putExtra("x", "value");
        startActivity(newActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetRecords() {
        Requests req = new Requests(API.URL_READ_RECORDS);
        req.execute();
    }

    private void RefreshListRecords(JSONArray records) throws JSONException {
        recordsList.clear();

        for (int i = 0; i < records.length(); i++) {
            JSONObject obj = records.getJSONObject(i);
            recordsList.add(new Records(
                    obj.getInt("id"),
                    obj.getInt("weight"),
                    obj.getString("modality"),
                    obj.getString("student"),
                    obj.getString("date")
            ));
        }

        RecordsAdapter adapter = new RecordsAdapter(recordsList);
        lv.setAdapter(adapter);
    }

    class RecordsAdapter extends ArrayAdapter<Records> {
        List<Records> recordsList;

        public RecordsAdapter(List<Records> recordsList) {
            super(PRsActivity.this, R.layout.list_records, recordsList);
            this.recordsList = recordsList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listItem = inflater.inflate(R.layout.list_records, null, true);

            TextView tvData = listItem.findViewById(R.id.data);
            TextView tvModalidade = listItem.findViewById(R.id.modality);
            TextView tvPeso = listItem.findViewById(R.id.weight);

            final Records records = recordsList.get(position);

            tvData.setText(records.getDate());
            tvModalidade.setText(records.getModality());
            tvPeso.setText(String.valueOf(records.getWeight()));

            return listItem;
        }
    }

    private class Requests extends AsyncTask<Void, Void, String> {
        String url;

        Requests(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PRsActivity.this);
            pd.setMessage("A carregar..");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String aux = handler.makeServiceCall(url);

            return aux;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing())
                pd.dismiss();

            try {
                JSONArray obj = new JSONArray(s);
                RefreshListRecords(obj);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
    }
}
