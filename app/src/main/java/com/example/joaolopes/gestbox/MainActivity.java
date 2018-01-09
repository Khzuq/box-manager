package com.example.joaolopes.gestbox;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dl;
    private ActionBarDrawerToggle toogle;
    //
    ProgressDialog pd;
    String TAG = MainActivity.class.getSimpleName();
    ListView lv;
    List<Trainings> trainingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        lv = (ListView) findViewById(R.id.lvTreinos);
        trainingsList = new ArrayList<>();
        //
        GetTrainings();
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
        newActivity.putExtra("x", "value");
        startActivity(newActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetTrainings() {
        Requests req = new Requests(API.URL_READ_TRAININGS);
        req.execute();
    }

    private void RefreshListTrainings(JSONArray trainings) throws JSONException {
        trainingsList.clear();

        for (int i = 0; i < trainings.length(); i++) {
            JSONObject obj = trainings.getJSONObject(i);
            trainingsList.add(new Trainings(
                    obj.getInt("id"),
                    obj.getString("warmup"),
                    obj.getString("skill"),
                    obj.getString("wod"),
                    obj.getString("data")
            ));
        }

        TrainingsAdapter adapter = new TrainingsAdapter(trainingsList);
        lv.setAdapter(adapter);
    }

    class TrainingsAdapter extends ArrayAdapter<Trainings> {
        List<Trainings> trainingsList;

        public TrainingsAdapter(List<Trainings> trainingsList) {
            super(MainActivity.this, R.layout.list_trainings, trainingsList);
            this.trainingsList = trainingsList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listItem = inflater.inflate(R.layout.list_trainings, null, true);

            TextView tvData = listItem.findViewById(R.id.data);
            TextView tvWarmup = listItem.findViewById(R.id.warm_up);
            TextView tvSkill = listItem.findViewById(R.id.skill);
            TextView tvWod = listItem.findViewById(R.id.wod);

            final Trainings trainings = trainingsList.get(position);

            tvData.setText(trainings.getData());
            tvWarmup.setText(trainings.getWarmup());
            tvSkill.setText(trainings.getSkill());
            tvWod.setText(trainings.getWod());

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
            pd = new ProgressDialog(MainActivity.this);
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
                RefreshListTrainings(obj);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
    }
}
