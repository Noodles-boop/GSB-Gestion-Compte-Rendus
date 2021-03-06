package com.example.gsb_doctors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class identification extends AppCompatActivity {

    android.widget.EditText pseudo, mdp;
    Button btn;
    ProgressBar progressBar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

          super.onCreate(savedInstanceState);
          setContentView(R.layout.login_main);

          pseudo = findViewById(R.id.connexionPseudo);
          mdp = findViewById(R.id.connexionPassword);
          btn = findViewById(R.id.btnLogin);
          progressBar = findViewById(R.id.progress);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String  Pseudo, password;
                    Pseudo = String.valueOf(pseudo.getText());
                    password = String.valueOf(mdp.getText());

                    // Verification of the type of the value entered in the EditText
                    if (!Pseudo.equals("") && !password.equals("")) {
                        //Start ProgressBar first (Set visibility VISIBLE)
                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "pseudo";
                                field[1] = "mdp";

                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = Pseudo;
                                data[1] = password;

                                PutData putData = new PutData("https://apicompterendu.fr/login.php", "POST", field, data);  // Mettre son adrr ip
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);

                                        String result = putData.getResult();

                                        JSONArray jsonArray = null;
                                        try {
                                            jsonArray = new JSONArray(result);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (jsonArray != null) {
                                            String[] get_login = new String[jsonArray.length()];
                                            String[] get_region = new String[jsonArray.length()];
                                            String[] get_role = new String[jsonArray.length()];
                                            String[] get_id = new String[jsonArray.length()];


                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject obj = null;
                                                try {
                                                    obj = jsonArray.getJSONObject(i);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    get_login[i] = obj.getString("login");
                                                    get_region[i] = obj.getString("region");
                                                    get_role[i] = obj.getString("role");
                                                    get_id[i] = obj.getString("id");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            if (get_login[0].equals("Login Success")) {
                                                SharedPreferences sharedpreferences = getSharedPreferences("region", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                sharedpreferences.edit().clear().apply();
                                                editor.putString("region1", get_region[0]);
                                                editor.putString("role1", get_role[0]);
                                                editor.putString("id1", get_id[0]);
                                                editor.commit();
                                                Toast.makeText(getApplicationContext(), "Bienvenue", Toast.LENGTH_SHORT).show();

                                                switch (get_role[0]) {
                                                    case "delegue":
                                                        Intent intent = new Intent(getApplicationContext(), delegue_compteRendu.class);
                                                        startActivity(intent);
                                                        break;
                                                    case "visiteur":
                                                        Intent intent2 = new Intent(getApplicationContext(), consulter.class);
                                                        startActivity(intent2);
                                                        break;
                                                }
                                            }
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Identifiant ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Tous les champs sont requis", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
