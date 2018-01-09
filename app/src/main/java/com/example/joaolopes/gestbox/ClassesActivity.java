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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dl;
    private ActionBarDrawerToggle toogle;
    //
    ProgressDialog pd;
    String TAG = MainActivity.class.getSimpleName();
    ListView lv;
    List<Classes> classesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
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
        lv = (ListView) findViewById(R.id.lvClasses);
        classesList = new ArrayList<>();
        //
        GetClasses();
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
        startActivity(newActivity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetClasses() {
        Requests req = new Requests(API.URL_READ_CLASSES);
        req.execute();
    }

    private void RefreshListClasses(JSONArray classes) throws JSONException {
        classesList.clear();

        for (int i = 0; i < classes.length(); i++) {
            JSONObject obj = classes.getJSONObject(i);
            classesList.add(new Classes(
                    obj.getInt("id"),
                    obj.getString("classe_name"),
                    obj.getString("teacher"),
                    obj.getInt("students"),
                    obj.getInt("max_students"),
                    obj.getString("data")
            ));
        }

        ClassesAdapter adapter = new ClassesAdapter(classesList);
        lv.setAdapter(adapter);
    }

    class ClassesAdapter extends ArrayAdapter<Classes> {
        List<Classes> classesList;

        public ClassesAdapter(List<Classes> classesList) {
            super(ClassesActivity.this, R.layout.list_classes, classesList);
            this.classesList = classesList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listItem = inflater.inflate(R.layout.list_classes, null, true);

            TextView tvData = listItem.findViewById(R.id.data);
            TextView tvNome = listItem.findViewById(R.id.name_class);
            TextView tvProfessor = listItem.findViewById(R.id.teacher_name);
            TextView tvInscrito = listItem.findViewById(R.id.number_students);
            TextView tvMax = listItem.findViewById(R.id.total_students);
            final ImageButton editar_button = listItem.findViewById(R.id.editar_button);
            final ImageButton apagar_button = listItem.findViewById(R.id.delete_button);

            final Classes classes = classesList.get(position);


            editar_button.setTag(classes.getId());
            apagar_button.setTag(classes.getId());
            /*

             */
            apagar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //url_apagar = url_apagar + apagar_button.getTag();
                    classesList.clear();
                    //GetTrainings();
                }
            });
            /*

             */
            editar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Dialogbox_categoria("OLA");
                }
            });

            tvData.setText(classes.getData());
            tvNome.setText(classes.getClasse_name());
            tvProfessor.setText(classes.getTeacher());
            tvInscrito.setText(String.valueOf(classes.getStudents()));
            tvMax.setText(String.valueOf(classes.getMax_students()));

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
            pd = new ProgressDialog(ClassesActivity.this);
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
                RefreshListClasses(obj);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
    }
}
