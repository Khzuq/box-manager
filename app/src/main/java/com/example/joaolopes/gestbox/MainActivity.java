package com.example.joaolopes.gestbox;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dl;
    private ActionBarDrawerToggle toogle;
    //
    ProgressDialog pd;
    String TAG = MainActivity.class.getSimpleName();
    ListView lv;
    List<Trainings> trainingsList;
    FloatingActionButton fab_add;

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
        //FLOATING BUTTONS
        fab_add = findViewById(R.id.fab_add);

        //ABRE POPUP ADICIONAR
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBoxADD();
            }
        });
        //
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

    private void InsertTrainings(String warmup, String skill, String wod, String data) {
        Requests req = new Requests(API.URL_CREATE_TRAININGS + "&warmup=" + warmup + "&skill=" + skill + "&wod=" + wod + "&data=" + data);
        req.execute();
    }

    private void DeleteTrainings(int id) {
        Requests req = new Requests(API.URL_DELETE_TRAININGS + "&id=" + id);
        req.execute();
    }

    private void UpdateTrainings(int id, String warmup, String skill, String wod, String data) {
        Requests req = new Requests(API.URL_EDIT_TRAININGS + "&id=" + id + "&warmup=" + warmup + "&skill=" + skill + "&wod=" + wod + "&data=" + data);
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
            final ImageButton editar_button = listItem.findViewById(R.id.editar_button);
            final ImageButton apagar_button = listItem.findViewById(R.id.delete_button);

            final Trainings trainings = trainingsList.get(position);

            editar_button.setTag(trainings.getId());
            apagar_button.setTag(trainings.getId());
            /*

             */
            apagar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = new Integer((Integer) apagar_button.getTag());
                    DeleteTrainings(id);
                }
            });
            /*

             */
            editar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog(trainings.getId(), trainings.getData(), trainings.getWarmup(), trainings.getSkill(), trainings.getWod());
                }
            });

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void OpenCalendar(final TextView data_view) {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        data_view.setText(date_time.toString());
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void DialogBoxADD(){
        //Váriaveis iniciais, do layout, do alert, da caixa de texto, spinner e do que vai ser carregado da db
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.cd_add_training, null);

        final EditText warmup = (EditText)mView.findViewById(R.id.warmup);
        final EditText skill = (EditText)mView.findViewById(R.id.skill);
        final EditText wod = (EditText)mView.findViewById(R.id.wod);
        final TextView data_treino = (TextView)mView.findViewById(R.id.data_treino);
        Button calendar = (Button)mView.findViewById(R.id.btn_calen);
        Button save = (Button)mView.findViewById(R.id.save_changes);

        //click no calendario
        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                OpenCalendar(data_treino);
            }
        });
        //gravar alterações
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertTrainings(warmup.getText().toString(), skill.getText().toString(), wod.getText().toString(), data_treino.getText().toString());
            }
        });

        //TITULO
        TextView title = new TextView(this);
        title.setText("Adicionar Treino");
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        dlgAlert.setCustomTitle(title);
        dlgAlert.setView(mView);
        dlgAlert.create().show();
    }

    private void CustomDialog(final int id, final String data, final String warmup, final String skill, final String wod) {
        //Váriaveis iniciais, do layout, do alert, da caixa de texto, spinner e do que vai ser carregado da db
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.cd_edit_training, null);

        final EditText et_warmup = (EditText)mView.findViewById(R.id.warmup);
        final EditText et_skill = (EditText)mView.findViewById(R.id.skill);
        final EditText et_wod = (EditText)mView.findViewById(R.id.wod);
        final TextView data_treino = (TextView)mView.findViewById(R.id.data_treino);
        Button calendar = (Button)mView.findViewById(R.id.btn_calen);
        Button save = (Button)mView.findViewById(R.id.save_changes);


        et_warmup.setText(warmup.toString());
        et_skill.setText(skill.toString());
        et_wod.setText(wod.toString());
        data_treino.setText(data);
        //click no calendario
        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                OpenCalendar(data_treino);
            }
        });
        //gravar alterações
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateTrainings(id, et_warmup.getText().toString(), et_skill.getText().toString(), et_wod.getText().toString(), data_treino.getText().toString());
            }
        });

        dlgAlert.setView(mView);
        dlgAlert.create().show();
    }
}
