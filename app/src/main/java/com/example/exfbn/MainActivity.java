package com.example.exfbn;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modelo.Cliente;

public class MainActivity extends AppCompatActivity {
    private Button btAdicionar, btConsultar;
    private EditText edNome, edId, edEndereco, edBairro;
    private ListView lst;
    private List<Cliente> clienteList = new ArrayList<Cliente>();
    //Objeto que recebera a lista de clientes
    private ArrayAdapter<Cliente> clienteArrayAdapter;
    //Objeto responsavel por mapear os clientes recebidos do Firebase
    final private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //Objeto responsavel por pegar a instancia do Firebase
    private DatabaseReference databaseReference = firebaseDatabase.getReference("server");
   //Objeto que ira fazer a referencia ao da base de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carregaWidgets();
        inicializaFirebase();

    }
    private void carregaWidgets()
    {
        edId=(EditText)findViewById(R.id.edtId);
        edNome=(EditText)findViewById(R.id.edtNome);
        lst=(ListView)findViewById(R.id.lstView);
        btAdicionar=(Button)findViewById(R.id.btnInserir);
        btConsultar=(Button)findViewById(R.id.btnConsultar);
        btAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente cliente = new Cliente();
                cliente.setId(edId.getText().toString());
                cliente.setNome(edNome.getText().toString());
                DatabaseReference clienteRef = databaseReference.child("cliente").child(cliente.getNome().toString());
                //Map<String,Cliente> mcliente = new HashMap<>();
                clienteRef.setValue(cliente);
            }
        });
        btConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisa("");
            }
        });

    }
    private void inicializaFirebase()
    {
        FirebaseApp.initializeApp(MainActivity.this);
    }

    private void pesquisa(String nome)
    {
        Query query;//Objeto que recebera a consulta
        clienteList.clear();//limpa o listview
        query = databaseReference.child("/cliente").orderByChild("nome");
        //Pesquisa atraves da chave cliente ordenando pela nome do cliente
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot objdataSnapshot:dataSnapshot.getChildren())
                {
                    Cliente cliente = objdataSnapshot.getValue(Cliente.class);
                    clienteList.add(cliente);;
                }
                clienteArrayAdapter = new ArrayAdapter<Cliente>(MainActivity.this,
                        android.R.layout.simple_list_item_1, clienteList);
                lst.setAdapter(clienteArrayAdapter);
                if (clienteArrayAdapter.getCount() >0) {
                    Toast.makeText(getApplicationContext(), clienteList.get(1).getNome().toString(), Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Sem dados", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    void pesquisa2(String nome)
    {
        Query query;
        clienteList.clear();
        if (nome.equals(""))
        {
            query = databaseReference.child("cliente").orderByChild("nome");

        }
        else
        {
            query = databaseReference.child("server/cliente").orderByChild("nome").startAt(nome).endAt(nome+"uf8ff");

        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot objdataSnapshot:dataSnapshot.getChildren())
                {
                    Cliente cliente = objdataSnapshot.getValue(Cliente.class);
                    clienteList.add(cliente);;
                }
                clienteArrayAdapter = new ArrayAdapter<Cliente>(MainActivity.this,
                        android.R.layout.simple_list_item_1, clienteList);
                lst.setAdapter(clienteArrayAdapter);
                if (clienteArrayAdapter.getCount() >0) {
                    Toast.makeText(getApplicationContext(), clienteList.get(1).getNome().toString(), Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Sem dados", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
