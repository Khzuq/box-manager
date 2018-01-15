package com.example.joaolopes.gestbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    Button btn_login;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.email_sign_in_button);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().length() > 0 && password.getText().length() > 0){
                    Requests login = new Requests(API.URL_LOGIN +"&username="+username.getText().toString()+"&password="+password.getText().toString());
                    login.execute();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Verifique todos os campos..", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class Requests extends AsyncTask<Void, Void, String> {
        String url;
        Requests(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
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

                if(s!=null) {
                    JSONObject object = new JSONObject(s);

                    if(object.getBoolean("login")){
                        Login.setId(object.getJSONArray("dados").getJSONObject(0).getInt("id"));
                        Login.setId_type(object.getJSONArray("dados").getJSONObject(0).getInt("usertype"));
                        Login.setNome(object.getJSONArray("dados").getJSONObject(0).getString("nome"));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Autenticação realizada com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Autenticação sem sucesso..", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}
