package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contato;
import com.example.fivecontacts.main.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

public class Pick_Contacts extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    TextView tv;
    EditText edContato;
    Button btSalvar;
    User user;
    BottomNavigationView bnv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contacts);
        tv = findViewById(R.id.MessageIntent);

        bnv = findViewById(R.id.bnv);
        bnv.setSelectedItemId(R.id.anvMudar);
        bnv.setOnNavigationItemSelectedListener(this);

        edContato = findViewById(R.id.edtContato);

        //Dados da Intent Anterior
        Intent quemChamou=this.getIntent();
        if (quemChamou!=null) {
            Bundle params = quemChamou.getExtras();
            if (params!=null) {
                //Recuperando o Usuario
                user = (User) params.getSerializable("usuario");
                if (user != null) {
                    tv.setText(user.getNome());

                }
            }
        }

    }
    public void onClickBuscar(View v){
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED){
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 3333);
        return;
    }
    else{
        ContentResolver cr =getContentResolver();

        String consulta = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
        String [] argumentosConsulta = {"%"+edContato.getText()+"%"};

        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()){
            int indiceNome = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
            String contatoNome = cursor.getString(indiceNome);

            int indiceContactID = cursor.getColumnIndexOrThrow((ContactsContract.Contacts._ID));
            String contactID = cursor.getString(indiceContactID);

            String consultaPhone = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactID;
            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);


            while (phones.moveToNext()){
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        }
    }
    }

    public void cliquedoSalvar (View v) throws IOException {
        Contato c, k, z, y, x;
        c = new Contato();
        c.setNumero("tel:+141414141");
        c.setNome("Peppa");
        k = new Contato();
        k.setNumero("tel:+2424241521");
        k.setNome("George");
        z= new Contato();
        z.setNumero("tel:+342414141");
        z.setNome("Richard");
        x= new Contato();
        x.setNumero("tel:+6666666666");
        x.setNome("xxxxxxxxx");
        y= new Contato();
        y.setNumero("tel:+4242424242");
        y.setNome("yyyyyyy");


        SharedPreferences salvaContatos = getSharedPreferences("contatos2", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = salvaContatos.edit();

        editor.putInt("numContatos", 5);


        try {

            ByteArrayOutputStream dt = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(dt);
            oos.writeObject(c);
            String contatoSerializado = dt.toString(StandardCharsets.ISO_8859_1.name());
            editor.putString("contato1", contatoSerializado);


            dt = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(dt);
            oos.writeObject(k);
            contatoSerializado = dt.toString(StandardCharsets.ISO_8859_1.name());
            editor.putString("contato2", contatoSerializado);


            dt = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(dt);
            oos.writeObject(z);
            contatoSerializado = dt.toString(StandardCharsets.ISO_8859_1.name());
            editor.putString("contato3", contatoSerializado);


            dt = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(dt);
            oos.writeObject(x);
            contatoSerializado = dt.toString(StandardCharsets.ISO_8859_1.name());
            editor.putString("contato4", contatoSerializado);


            dt = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(dt);
            oos.writeObject(y);
            contatoSerializado = dt.toString(StandardCharsets.ISO_8859_1.name());
            editor.putString("contato5", contatoSerializado);

            editor.commit();

        }catch(Exception e) {
        e.printStackTrace();}

        finish();
        }


    @Override
    protected void onStop() {
        super.onStop();
        Log.v("PDM","Matando a Activity Lista de Contatos");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("PDM","Matei a Activity Lista de Contatos");
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