package com.example.fivecontacts.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fivecontacts.R;
import com.example.fivecontacts.main.model.Contato;
import com.example.fivecontacts.main.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ListaDeContatos_ListView extends AppCompatActivity implements UIEducacionalPermissao.NoticeDialogListener, BottomNavigationView.OnNavigationItemSelectedListener {

    ListView lv;
    String[] itens ={"Filha", "Filho", "Netinho"};
    String[] numeros ={"tel:000000003435","tel:2000348835","tel:1003435888" };
    String numeroLigar = "";

    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contatos);

        bnv = findViewById(R.id.bnv);
        bnv.setSelectedItemId(R.id.anvLigar);
        bnv.setOnNavigationItemSelectedListener(this);

        lv= findViewById(R.id.listView1);

        preencherListaDeContatos(); //Montagem do ListView

        //Dados da Intent Anterior
        Intent quemChamou=this.getIntent();
        if (quemChamou!=null) {
            Bundle params = quemChamou.getExtras();
            if (params!=null) {
                //Recuperando o Usuario
                User u1 = (User) params.getSerializable("user");
                if (u1 != null) {

                    Log.v("pdm", u1.getNome());
                }
            }
        }

    }

    protected void preencherListaDeContatos (){

        SharedPreferences recuperarContatos = getSharedPreferences("contatos2", Activity.MODE_PRIVATE);

        int num = recuperarContatos.getInt("numContatos", 0);

        final ArrayList<Contato> contatos = new ArrayList<Contato>();

        Contato contato;
        for (int i=1;i<=num;i++){
            String objSel = recuperarContatos.getString("contato"+i, "");
            if (objSel.compareTo("")!=0){
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(objSel.getBytes(StandardCharsets.ISO_8859_1.name()));

                    ObjectInputStream ois = new ObjectInputStream(bis);
                    contato = (Contato) ois.readObject();

                    if (contato != null){
                        contatos.add(contato);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        //Vamos montar o ListView
        if (contatos!=null) {
            final String[] nomesSP;
            nomesSP = new String[contatos.size()];
            Contato tempContato;
            for (int i=0;i<contatos.size();i++){
                tempContato = contatos.get(i);
                nomesSP[i] = tempContato.getNome();

            }

            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomesSP);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(ListaDeContatos_ListView.this, "Contato Selecionado: " + nomesSP[i], Toast.LENGTH_LONG).show();

                   // numeroLigar = contatos.get(i).getNumero();
                    Uri uri = Uri.parse (contatos.get(i).getNumero());


                   if (checarPermissaoPhone()){
                       Intent intent = new Intent(Intent.ACTION_CALL, uri);
                       startActivity(intent);
                   } else{
                       Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                       startActivity(intent);
                   }


                   //else{ intent = new Intent(Intent.ACTION_DIAL, uri);}


                    // intent = new Intent(Intent.ACTION_CALL, uri);



                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.v("PDM3",item.toString());
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

    protected boolean checarPermissaoPhone() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                String mensagem = "A aplicação requer acesso do telefone para discagem automática. A permissão será requisitada.";
                String titulo = "Permissão de acesso a chamadas";
                int codigo = 1;
                UIEducacionalPermissao mensagemPermissao = new UIEducacionalPermissao(mensagem,titulo, codigo);

                mensagemPermissao.onAttach((Context)this);
                mensagemPermissao.show(getSupportFragmentManager(), "primeiravez");

            }
            else{
               return false;
            }

        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch (requestCode) {
            case 1212:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão concedida", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permissão não concedida", Toast.LENGTH_LONG).show();

                    String mensagem = "O aplicativo só pode fazer ligações diretamente com sua permissão. Se você marcou não, o número será apenas discado.";
                    String titulo = "Precisamos de sua permissão";
                    UIEducacionalPermissao mensagemPermissao = new UIEducacionalPermissao(mensagem, titulo, 2);
                    mensagemPermissao.onAttach((Context) this);
                    mensagemPermissao.show(getSupportFragmentManager(), "segundavez");
                }
                break;
        }
    }


    @Override
    public void onDialogPositiveClick(int codigo) {
        if (codigo ==1) {
            String[] permissoes = {Manifest.permission.CALL_PHONE};
            requestPermissions(permissoes, 1212);
        }
    }
}