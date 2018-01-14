package com.example.joaolopes.gestbox;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PRsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dl;
    private ActionBarDrawerToggle toogle;
    //
    ProgressDialog pd;
    String TAG = PRsActivity.class.getSimpleName();
    ListView lv;
    List<Records> recordsList;
    static List<Modalities> modalitiesList;
    ArrayAdapter<String> spinnerAdapter = null;
    FloatingActionButton fab_add;


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
        modalitiesList = new ArrayList<>();
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
        GetRecords();
    }

    /*
        MENU
     */

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

    /*
        RECORDS
     */

    private void GetRecords() {
        Requests req = new Requests(API.URL_READ_RECORDS, null);
        req.execute();
    }

    private void DeleteRecords(int id) {
        Requests req = new Requests(API.URL_DELETE_RECORDS + id, null);
        req.execute();
    }

    private void UpdateRecords(int id, int spinner_id, String data, int peso) {
        Requests req = new Requests(API.URL_EDIT_RECORDS + "&id=" + id + "&data=" + data + "&peso=" + peso + "&modalidade=" + spinner_id, null);
        req.execute();
    }

    private void InsertRecords(int spinner_id, int peso, String data, int id_user) {
        Requests req = new Requests(API.URL_CREATE_RECORDS + "&id_user=" + id_user + "&data=" + data + "&peso=" + peso + "&modalidade=" + spinner_id, null);
        req.execute();
    }

    /*
        MODALITIES -> para preencher o spinner no popup de editar
     */

    private void GetModalities(Spinner spinner) {
        Requests req = new Requests(API.URL_READ_MODALITIES, spinner);
        req.execute();
    }

    private void RefreshListRecords(JSONArray records) throws JSONException {
        recordsList.clear();

        for (int i = 0; i < records.getJSONArray(0).length(); i++) {
            JSONObject obj = records.getJSONArray(0).getJSONObject(i);
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
            final ImageButton editar_button = listItem.findViewById(R.id.editar_button);
            final ImageButton apagar_button = listItem.findViewById(R.id.delete_button);

            final Records records = recordsList.get(position);

            editar_button.setTag(records.getId());
            apagar_button.setTag(records.getId());
            /*

             */
            apagar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = new Integer((Integer) apagar_button.getTag());
                    DeleteRecords(id);
                }
            });
            /*

             */
            editar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog(records.getDate(), records.getWeight(), records.getId(), spinnerAdapter);
                }
            });

            tvData.setText(records.getDate());
            tvModalidade.setText(records.getModality());
            tvPeso.setText(String.valueOf(records.getWeight()));

            return listItem;
        }
    }

    private class Requests extends AsyncTask<Void, Void, String> {
        String url;
        Spinner spinnerModality;

        Requests(String url, Spinner spin) {
            this.url = url;
            this.spinnerModality = spin;
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
                JSONObject obj = new JSONObject(s);
                if(s!=null) {
                    if (obj.getString("type").equals("records")) {
                        if(obj.getString("records").equals("empty")){
                            lv.setAdapter(null);
                        }
                        else{
                            RefreshListRecords(obj.getJSONArray("records"));
                        }
                    } else if (obj.getString("type").equals("modalities")) {
                        CompleteSpinner(obj.getJSONArray("modalities"), spinnerModality);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
    }

    private void CompleteSpinner(JSONArray teachers, Spinner spin) throws JSONException {
        modalitiesList.clear();
        for (int i = 0; i < teachers.getJSONArray(0).length(); i++) {
            JSONObject c = teachers.getJSONArray(0).getJSONObject(i);
            modalitiesList.add(new Modalities(
                    c.getInt("id"),
                    c.getString("modality")
            ));
        }
        List<String> aux = new ArrayList<String>();

        for (int i = 0; i < modalitiesList.size(); i++) {
            aux.add(modalitiesList.get(i).getModality());
        }

        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                aux
        );
        spin.setAdapter(spinnerAdapter);
    }

    private void CustomDialog(final String data, final int peso, final int id, final ArrayAdapter spinnerAdapter) {
        //Váriaveis iniciais, do layout, do alert, da caixa de texto, spinner e do que vai ser carregado da db
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.cd_edit_record, null);

        final EditText peso_pr = (EditText)mView.findViewById(R.id.peso);
        final TextView data_pr = (TextView)mView.findViewById(R.id.data_pr);
        Button calendar = (Button)mView.findViewById(R.id.btn_calen);
        Button save = (Button)mView.findViewById(R.id.save_changes);
        final Spinner spinner = mView.findViewById(R.id.spinner_modalidade);
        GetModalities(spinner);

        peso_pr.setText(Integer.toString(peso));
        data_pr.setText(data);
        //click no calendario
        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                OpenCalendar(data_pr);
            }
        });
        //gravar alterações
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateRecords(id, spinner.getSelectedItemPosition(), data_pr.getText().toString(), Integer.parseInt(peso_pr.getText().toString()));
            }
        });

        dlgAlert.setView(mView);
        dlgAlert.create().show();
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
        View mView = getLayoutInflater().inflate(R.layout.cd_add_record, null);

        final EditText peso_pr = (EditText)mView.findViewById(R.id.peso);
        final TextView data_pr = (TextView)mView.findViewById(R.id.data_pr);
        Button calendar = (Button)mView.findViewById(R.id.btn_calen);
        Button save = (Button)mView.findViewById(R.id.save_changes);
        final Spinner spinner = mView.findViewById(R.id.spinner_modalidade);
        GetModalities(spinner);

        //click no calendario
        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                OpenCalendar(data_pr);
            }
        });
        //gravar alterações
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertRecords(spinner.getSelectedItemPosition(), Integer.parseInt(peso_pr.getText().toString()), data_pr.getText().toString(), 1);
            }
        });

        //TITULO
        TextView title = new TextView(this);
        title.setText("Adicionar PR");
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        dlgAlert.setCustomTitle(title);
        dlgAlert.setView(mView);
        dlgAlert.create().show();
    }
}
