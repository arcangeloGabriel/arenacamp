package com.arena.biel.arenacamp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.arena.biel.arenacamp.R;
import com.arena.biel.arenacamp.modelo.classe.Contato;
import com.arena.biel.arenacamp.modelo.conexao.ConfiguracaoFirebase;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;

public class FaleconoscoActivity extends AppCompatActivity {
     private BootstrapEditText nome,email,telefone,msg;

     private BootstrapButton btnEnviar;
     private Contato contato;
     private Toolbar toolbar;
     private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faleconosco);


        getSupportActionBar().setTitle("Fale Conosco");



        nome = findViewById(R.id.edNomeFaleconoscoId);
        email = findViewById(R.id.edEmailFaleconoscoId);
        telefone = findViewById(R.id.edFoneFaleconoscoId);
        msg = findViewById(R.id.edMsgFaleconoscoId);
        btnEnviar = findViewById(R.id.btEnviarFaleId);
        //fazendo a mascara do telefone

        SimpleMaskFormatter simpleMasTelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone,simpleMasTelefone);
        telefone.addTextChangedListener(maskTelefone);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contato = new Contato();
                contato.setNomeContato(nome.getText().toString());
                contato.setEmailContato(email.getText().toString());
                contato.setTelefoneContato(telefone.getText().toString());
                contato.setMsgContato(msg.getText().toString());
                salvarContato(contato);

            }
        });
    }

    private boolean salvarContato(Contato contato) {
        try {


            databaseReference = ConfiguracaoFirebase.getFirebase().child("contato");

            String key = databaseReference.push().getKey();
            contato.setUidContato(key);

            databaseReference.child(key).setValue(contato);//inseri os usuarios com a chave exclusive dele



            Toast.makeText(FaleconoscoActivity.this,"Mensagem enviada com sucesso",Toast.LENGTH_LONG).show();
            liparCampos();
            //abrirLoginUsuario();
            return true ;

        }catch (Exception e){
            Toast.makeText(FaleconoscoActivity.this,"Erro:mensagen não enviada",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private void liparCampos() {
        nome.setText("");
        email.setText("");
        telefone.setText("");
        msg.setText("");
    }


}
