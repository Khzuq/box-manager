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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClassesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout dl;
    private ActionBarDrawerToggle toogle;
    //
    ProgressDialog pd;
    String TAG = MainActivity.class.getSimpleName();
    ListView lv;
    List<Classes> classesList;
    static List<Teachers> teachersList;
    ArrayAdapter<String> spinnerAdapter = null;
    FloatingActionButton fab_cal, fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        //MENU
        dl = (DrawerLayout) findViewById(R.id.drawerMenu);
        dl.setClickable(true);
        toogle = new ActionBarDrawerToggle(this, dl, R.string.openMenu, R.string.closeMenu);
        dl.addDrawerListener(toogle);
        toogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv);
        navigationView.bringToFront(); //para o click funcionar
        navigationView.setNavigationItemSelectedListener(this);

        //LISTAS / LISTVIEWS
        lv = (ListView) findViewById(R.id.lvClasses);
        classesList = new ArrayList<>();
        teachersList = new ArrayList<>();

        //FLOATING BUTTONS
        fab_cal = findViewById(R.id.fab_cal);
        fab_add = findViewById(R.id.fab_add);

        //ABRE POPUP ADICIONAR
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBoxADD();
            }
        });

        //ABRE POPUP CALENDARIO
        fab_cal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ClassesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if(monthOfYear+1<10) {
                                    GetClassesByDate(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                }
                                else{
                                    GetClassesByDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //
        GetClasses();
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
        CLASSES
     */

    private void GetClasses() {
        Requests req = new Requests(API.URL_READ_CLASSES, null);
        req.execute();
    }

    private void DeleteClasses(int id){
        Requests req = new Requests(API.URL_DELETE_CLASSES + id, null);
        req.execute();
    }

    private  void UpdateClasses(int id_aula, String nome, int spinner_id, String max_students, String data, String timer){
        Requests req = new Requests(API.URL_EDIT_CLASSES + "&id="+id_aula+"&nome="+nome+"&teacher="+spinner_id+"&max_students="+max_students+"" +
                "&data="+data+"&timer="+timer, null);
        req.execute();
    }

    private  void InsertClasses(String nome, int spinner_id, String max_students, String data, String timer){
        Requests req = new Requests(API.URL_CREATE_CLASSES+"&name="+nome+"&teacher="+spinner_id+"&max_students="+max_students+"" +
                "&data="+data+"&timer="+timer+"", null);
        req.execute();
    }

    private void GetClassesByDate(String data) {
        lv.setAdapter(null);
        Requests req = new Requests(API.URL_READbyDAY_CLASSES + data, null);
        req.execute();
    }
    /*
        TEACHERS -> para preencher o spinner no popup de editar
     */

    private void GetTeachers(Spinner spinner) {
        Requests req = new Requests(API.URL_READ_TEACHERS, spinner);
        req.execute();
    }

    private void RefreshListClasses(JSONArray classes) throws JSONException {
        classesList.clear();

        for (int i = 0; i < classes.getJSONArray(0).length(); i++) {
            JSONObject obj = classes.getJSONArray(0).getJSONObject(i);
            classesList.add(new Classes(
                    obj.getInt("id"),
                    obj.getString("classe_name"),
                    obj.getString("teacher"),
                    //obj.getInt("students"),
                    obj.getInt("max_students"),
                    obj.getString("data"),
                    obj.getString("timer")
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
                    int id = new Integer((Integer) apagar_button.getTag());
                    DeleteClasses(id);
                }
            });
            /*

             */
            editar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog(classes.getData(), classes.getClasse_name(), classes.getTeacher(), classes.getMax_students(), classes.getId(), classes.getTimer(), spinnerAdapter);
                }
            });

            tvData.setText(classes.getData());
            tvNome.setText(classes.getClasse_name());
            tvProfessor.setText(classes.getTeacher());
            //tvInscrito.setText(String.valueOf(classes.getStudents()));
            tvMax.setText(String.valueOf(classes.getMax_students()));

            return listItem;
        }
    }

    private class Requests extends AsyncTask<Void, Void, String> {
        String url;
        Spinner spinnerProf;

        Requests(String url, Spinner spin) {
            this.url = url;
            this.spinnerProf = spin;
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
                JSONObject obj = new JSONObject(s);
                if(s!=null) {
                    if (obj.getString("type").equals("classes")) {
                        if(obj.getString("classes").equals("empty")){
                            lv.setAdapter(null);
                        }
                        else{
                            RefreshListClasses(obj.getJSONArray("classes"));
                        }
                    } else if (obj.getString("type").equals("teachers")) {
                        CompleteSpinner(obj.getJSONArray("teachers"), spinnerProf);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSONException: " + e.getMessage());
            }
        }
    }

    private void CompleteSpinner(JSONArray teachers, Spinner spin) throws JSONException {
        teachersList.clear();
        for (int i = 0; i < teachers.getJSONArray(0).length(); i++) {
            JSONObject c = teachers.getJSONArray(0).getJSONObject(i);
            teachersList.add(new Teachers(
                    c.getInt("id"),
                    c.getString("nome")
            ));
        }
        List<String> aux = new ArrayList<String>();

        for (int i = 0; i < teachersList.size(); i++) {
            aux.add(teachersList.get(i).getNome());
        }

        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                aux
        );
        spin.setAdapter(spinnerAdapter);
    }

    private void CustomDialog(final String data, final String name, final String teacher, int max_alunos, final int id_aula, String trainingsTimer, final ArrayAdapter spinnerAdapter) {
        //Váriaveis iniciais, do layout, do alert, da caixa de texto, spinner e do que vai ser carregado da db
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        final EditText aula = (EditText)mView.findViewById(R.id.nome_aula);
        final EditText num_aulunos = (EditText)mView.findViewById(R.id.max_alunos_edit);
        final TextView hora = (TextView)mView.findViewById(R.id.timer_text);
        final TextView data_view = (TextView)mView.findViewById(R.id.aula_text);
        final TextView data_text = (TextView)mView.findViewById(R.id.data_text_aula);
        final TextView timer_text = (TextView)mView.findViewById(R.id.timer_tempo_view);
        Button calendar = (Button)mView.findViewById(R.id.btn_calen);
        final Button timer = (Button)mView.findViewById(R.id.btn_timer);
        Button save = (Button)mView.findViewById(R.id.save_changes);
        final Spinner spinner = mView.findViewById(R.id.spinner_professor);
        GetTeachers(spinner);

        aula.setText(name);
        data_text.setText(data);
        timer_text.setText(trainingsTimer);
        //click no calendario
        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                OpenCalendar(data_text);
            }
        });
        //click no relogio
        timer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                OpenClock(timer_text);
            }
        });
        //gravar alterações
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateClasses(id_aula, aula.getText().toString(), spinner.getSelectedItemPosition(), num_aulunos.getText().toString(), data_text.getText().toString(), timer_text.getText().toString());
            }
        });

        num_aulunos.setText(Integer.toString(max_alunos));


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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void OpenClock(final TextView hora_view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                hora_view.setText(selectedHour + ":" + selectedMinute +":"+ 000);
            }
        }, hour, minute, true);//Yes 24 hour time

        mTimePicker.show();
    }

    private void DialogBoxADD(){
        //Váriaveis iniciais, do layout, do alert, da caixa de texto, spinner e do que vai ser carregado da db
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.custom_add_dialog, null);

        final EditText aula = (EditText)mView.findViewById(R.id.nome_aula);
        final EditText num_aulunos = (EditText)mView.findViewById(R.id.max_alunos_edit);
        final TextView hora = (TextView)mView.findViewById(R.id.timer_text);
        final TextView data_view = (TextView)mView.findViewById(R.id.aula_text);
        final TextView data_text = (TextView)mView.findViewById(R.id.data_text_aula);
        final TextView timer_text = (TextView)mView.findViewById(R.id.timer_tempo_view);
        Button calendar = (Button)mView.findViewById(R.id.btn_calen);
        final Button timer = (Button)mView.findViewById(R.id.btn_timer);
        Button save = (Button)mView.findViewById(R.id.save_changes);
        final Spinner spinner = mView.findViewById(R.id.spinner_professor);
        GetTeachers(spinner);

        calendar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                OpenCalendar(data_text);
            }
        });
        timer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                OpenClock(timer_text);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertClasses(aula.getText().toString(), spinner.getSelectedItemPosition(), num_aulunos.getText().toString(), data_text.getText().toString(), timer_text.getText().toString());
            }
        });

        //TITULO
        TextView title = new TextView(this);
        title.setText("Adicionar Aula");
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
