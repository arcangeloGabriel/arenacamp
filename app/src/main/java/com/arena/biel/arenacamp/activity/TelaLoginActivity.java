package com.arena.biel.arenacamp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arena.biel.arenacamp.Manifest;
import com.arena.biel.arenacamp.R;
import com.arena.biel.arenacamp.helper.Base64Custom;
import com.arena.biel.arenacamp.helper.Preferencias;
import com.arena.biel.arenacamp.modelo.classe.Pessoa;
import com.arena.biel.arenacamp.modelo.conexao.ConfiguracaoFirebase;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TelaLoginActivity extends AppCompatActivity {

    private BootstrapEditText email,senha;
    private TextView tvCrieAqui;
    private BootstrapButton btnEntrar;
    private Pessoa pessoa;
    private FirebaseAuth autenticacao;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private String identificadorUsuarioLogado;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Pessoa pessoaSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        verificarUsuarioLogado();

         email = findViewById(R.id.userEmail);
         senha = findViewById(R.id.userEmail);
        tvCrieAqui = findViewById(R.id.tvCrieAqui);
        btnEntrar = findViewById(R.id.btnEntrar);

        permission();

        tvCrieAqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crie = new Intent(TelaLoginActivity.this, TelaCadUserActivity.class);
                startActivity(crie);
            }
        });
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().equals("")  &&  !senha.getText().equals("")){

                    pessoa = new Pessoa();
                    pessoa.setEmail(email.getText().toString());
                    pessoa.setSenha(senha.getText().toString());

                    validarLogin();

               }else {
                   Toast.makeText(TelaLoginActivity.this, "preencha os campos de email e senha válidos", Toast.LENGTH_SHORT).show();
               }
            }
        });


    }

    private void validarLogin() {
        //firebaseAuth = FirebaseAuth.getInstance();
        //autenticacao = FirebaseAuth.getInstance();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(pessoa.getEmail().toString().toString(),pessoa.getSenha().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                        }
                        Toast.makeText(TelaLoginActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                        abrirTelaPrincipal();
                    }
                });

        /**autenticacao.signInWithEmailAndPassword(pessoa.getEmail().toString(),pessoa.getSenha().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()){

                            abrirTelaPrincipal();

                            Preferencias preferencias = new Preferencias(TelaLoginActivity.this);
                            preferencias.salvarDados(pessoa.getEmail(), pessoa.getSenha());

                            Toast.makeText(TelaLoginActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();


                        }else{
                            Toast.makeText(TelaLoginActivity.this, "Usuário ou senha inválidos! tente novamente", Toast.LENGTH_SHORT).show();
                        }

                    }
                });**/
    }

    private void abrirTelaPrincipal() {

        Intent i = new Intent(TelaLoginActivity.this,PrinciaplActivity.class);
        startActivity(i);
        finish();
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if( autenticacao.getCurrentUser() != null ){
            abrirTelaPrincipal();
        }
    }

    private void permission() {
        int PERMISSION_ALL = 1;

                //String[] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};String[]
               String[] PERMISSIONCAMERAS= {android.Manifest.permission.CAMERA};
               String[] PERMISSIONS ={android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

    }
}