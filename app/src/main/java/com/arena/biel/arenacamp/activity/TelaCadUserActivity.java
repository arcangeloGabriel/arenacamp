package com.arena.biel.arenacamp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.arena.biel.arenacamp.R;
import com.arena.biel.arenacamp.modelo.classe.Pessoa;
import com.arena.biel.arenacamp.modelo.conexao.ConfiguracaoFirebase;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class TelaCadUserActivity extends AppCompatActivity {
    private BootstrapEditText nome;
    private BootstrapEditText telefone;
    private BootstrapEditText email;
    private BootstrapEditText senha;
    private BootstrapEditText endereco;
    private ImageView cadfoto;
    private BootstrapButton btncad;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private Pessoa pessoa;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final int heigth = 150;
        final int width = 150;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {
                Uri imagemSelecionada = data.getData();
                Picasso.with(TelaCadUserActivity.this).load(imagemSelecionada.toString()).resize(width, heigth).into(cadfoto);
            }
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cad_user);

        nome = findViewById(R.id.edNomeId);
        telefone = findViewById(R.id.edTelefoneId);
        email = findViewById(R.id.edEmailId);
        senha = findViewById(R.id.edSenhaId);
        endereco= findViewById(R.id.edEnderecoId);
        cadfoto = findViewById(R.id.cadfotoId);
        btncad = findViewById(R.id.btCadastrarId);
        //funcao buscar foto no dispositivo
        carregarImagemPadrao();
        cadfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 123);
            }
        });

        btncad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent btn = new Intent(TelaCadUserActivity.this,TelaLoginActivity.class);
                startActivity(btn);
            }
        });
        //metodo para cadastrar um usuario
        btncad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             pessoa = new Pessoa();

             pessoa.setNome(nome.getText().toString());
             pessoa.setTelefone(telefone.getText().toString());
             pessoa.setEmail(email.getText().toString());
             pessoa.setSenha(senha.getText().toString());
             pessoa.setEndereco(endereco.getText().toString());

                //chamada de metodo para cadastros de usuarios
                cadastrarUsuario();

            }
        });
    }

    private void cadastrarUsuario() {

      mFirebaseAuth = FirebaseAuth.getInstance();
      mFirebaseAuth.createUserWithEmailAndPassword(pessoa.getEmail(),pessoa.getSenha())
              .addOnCompleteListener(TelaCadUserActivity.this, new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){

                          
                          insereUsuario(pessoa);

                      }else {
                          String erroExcecao = "";
                          try {
                              throw task.getException();
                          } catch (FirebaseAuthWeakPasswordException e) {
                              erroExcecao = "Digite yma senha mais forte, contento no minimo 8 caracteres e que contenha letras e números";
                          } catch (FirebaseAuthInvalidCredentialsException e) {
                              erroExcecao = "O email e inválido, digite outro email";
                          } catch (FirebaseAuthUserCollisionException e) {
                              erroExcecao = "Esse email já esta cadastrado";
                          } catch (Exception e) {
                              erroExcecao = "erro ao efetuar o cadastro";
                              e.printStackTrace();
                          }
                          Toast.makeText(TelaCadUserActivity.this,"erro" + erroExcecao,Toast.LENGTH_LONG).show();
                      }
                  }
              });
    }

    private boolean insereUsuario(Pessoa pessoa) {

        try {


            databaseReference =ConfiguracaoFirebase.getFirebase().child("pessoa");

            String key = databaseReference.push().getKey();
            pessoa.setUid(key);

            databaseReference.child(key).setValue(pessoa);//inseri os usuarios com a chave exclusive dele



            Toast.makeText(TelaCadUserActivity.this,"Usuario gravado com sucesso",Toast.LENGTH_LONG).show();
            abrirLoginUsuario();
            return true ;

        }catch (Exception e){
            Toast.makeText(TelaCadUserActivity.this,"Erro ao gravar o usuario",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private void abrirLoginUsuario() {

        Intent i = new Intent(TelaCadUserActivity.this, TelaLoginActivity.class);
        startActivity(i);
        finish();
    }

    private void carregarImagemPadrao() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl("gs://arenacamp-54841.appspot.com/foto_perfil.png");
        final int heigth = 300;
        final int width = 300;
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(TelaCadUserActivity.this).load(uri.toString()).resize(width, heigth).centerCrop().into(cadfoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}
