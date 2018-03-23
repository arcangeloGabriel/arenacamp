package com.arena.biel.arenacamp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.arena.biel.arenacamp.R;
import com.arena.biel.arenacamp.modelo.classe.Pessoa;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
     EditText edNome,edEmail;
     ListView listv_dados;
     FirebaseDatabase firebaseDatabase;
     DatabaseReference databaseReference;
    Pessoa pessoaSelecionada;
     private List<Pessoa> listaPessoa= new ArrayList<Pessoa>();
     private ArrayAdapter<Pessoa> arrayAdapterPessoa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNome = findViewById(R.id.editnome);
        edEmail = findViewById(R.id.editeMAIL);
        listv_dados = findViewById(R.id.listv_dados);

        inicializarFirebase();
        eventoDatabase();

        //busca e atualiza um usuario
        listv_dados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pessoaSelecionada = (Pessoa)parent.getItemAtPosition(position);
                edNome.setText(pessoaSelecionada.getNome());
                edEmail.setText(pessoaSelecionada.getEmail());
            }
        });


    }
   // evento para atualizar uma pessoa ou usuario
    private void eventoDatabase() {
        databaseReference.child("pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
         //LIMPAR PARA NÃO FICAR SUBESCREVENDO
                listaPessoa.clear();
                for (DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                Pessoa p = objSnapshot.getValue(Pessoa.class);
                listaPessoa.add(p);
                }

                arrayAdapterPessoa = new ArrayAdapter<Pessoa>(MainActivity.this,android.R.layout.simple_list_item_1,listaPessoa);
                listv_dados.setAdapter(arrayAdapterPessoa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
  // metodo para inserir um usuario no banco
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_novo){
            Pessoa p = new Pessoa();
            p.setUid(UUID.randomUUID().toString());
            p.setNome(edNome.getText().toString());
            p.setEmail(edEmail.getText().toString());
            databaseReference.child("pessoa").child(p.getUid()).setValue(p);
            limparCampos();
        }else if(id == R.id.menu_atualizar){

            Pessoa p = new Pessoa();
            p.setUid(pessoaSelecionada.getUid());
            p.setNome(edNome.getText().toString().trim());
            p.setEmail(edEmail.getText().toString().trim());
            databaseReference.child("pessoa").child(p.getUid()).setValue(p);
            limparCampos();
        }else if(id == R.id.menu_deletar){

            Pessoa p = new Pessoa();
            p.setUid(pessoaSelecionada.getUid());
            databaseReference.child("pessoa").child(p.getUid()).removeValue();
            limparCampos();
        }
        return true;
    }

    private void limparCampos() {

        edEmail.setText("");
        edNome.setText("");
    }
}
