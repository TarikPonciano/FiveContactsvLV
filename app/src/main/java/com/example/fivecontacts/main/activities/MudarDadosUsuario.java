package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MudarDadosUsuario extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    boolean primeiraVezUser=true;
    boolean primeiraVezSenha=true;
    boolean primeiraVezNome=true;
    boolean primeiraVezEmail=true;
    EditText edUser;
    EditText edPass;
    EditText edNome;
    EditText edEmail;
    Switch swLogado;

    Button btModificar;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_usuario);

        btModificar=findViewById(R.id.btCriar);
        bnv = findViewById(R.id.bnv);
        bnv.setSelectedItemId(R.id.anvPerfil);
        bnv.setOnNavigationItemSelectedListener(this);

        edUser=findViewById(R.id.edT_Login2);
        edPass=findViewById(R.id.edt_Pass2);
        edNome=findViewById(R.id.edtNome);
        edEmail=findViewById(R.id.edtEmail);
        swLogado=findViewById(R.id.swLogado);

        SharedPreferences temUser = getSharedPreferences("usuarioPadrao",Activity.MODE_PRIVATE);
        edUser.setText(temUser.getString("login",""));
        edNome.setText(temUser.getString("nome",""));
        edPass.setText(temUser.getString("senha",""));
        edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edEmail.setText(temUser.getString("email",""));


        //Evento de limpar Componente
        edUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (primeiraVezUser){
                    primeiraVezUser=false;
                    edUser.setText("");
                }

                return false;
            }
        });
        //Evento de limpar Componente

        edPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (primeiraVezSenha){
                    primeiraVezSenha=false;
                    edPass.setText("");
                    edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    );
                }
                return false;
            }
        });

        edNome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (primeiraVezNome){
                    primeiraVezNome=false;
                    edNome.setText("");

                }
                return false;
            }
        });

        edEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (primeiraVezEmail){
                    primeiraVezEmail=false;
                    edEmail.setText("");

                }
                return false;
            }
        });

        //Evento de limpar Componente - E-mail
        //TO-DO

        //Evento de limpar Componente - Nome
        //TO-DO



        btModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String nome, login, senha;
                nome = edNome.getText().toString();
                login = edUser.getText().toString();
                senha = edPass.getText().toString();

                //Novos componentes
                String email;
                email = edEmail.getText().toString();
                boolean manterLogado;
                manterLogado= swLogado.isChecked();


                SharedPreferences salvaUser= getSharedPreferences("usuarioPadrao", Activity.MODE_PRIVATE);
                SharedPreferences.Editor escritor= salvaUser.edit();

                escritor.putString("nome",nome);
                escritor.putString("senha",senha);
                escritor.putString("login",login);

                //Escrever no SharedPreferences
                escritor.putString("email",email);
                escritor.putBoolean("manterLogado",manterLogado);


                //Falta Salvar o E-mail

                escritor.commit(); //Salva em Disco

                Toast.makeText(MudarDadosUsuario.this,"Modificações Salvas",Toast.LENGTH_LONG).show() ;

                finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.anvPerfil){
            Intent intent = new Intent(this, MudarDadosUsuario.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.anvMudar){
            Intent intent = new Intent(this, Pick_Contacts.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.anvLigar){

            Intent intent = new Intent(this, ListaDeContatos_ListView.class);
            startActivity(intent);
        }
        return true;
    }
}