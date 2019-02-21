package br.com.crud01.telas;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.crud01.R;
import br.com.crud01.dados.Cliente;

public class b1CadastroClientes extends AppCompatActivity {
    //Classe - Firebase
    private FirebaseAuth     fireAuth  = FirebaseAuth.getInstance();      //Autenticação Instanciado
    private FirebaseDatabase fireDados = FirebaseDatabase.getInstance();  //Banco Dados Instanciado

    //Classe - Dados e Objetos da Tela
    private Cliente  cliente;
    private EditText nome, email, senha, dataNiver, celular;

    //Método - Inicial
    @Override protected void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState); setContentView(R.layout.cadastro_clientes);
        //Identificar Objetos
        nome        =  (EditText)  findViewById( R.id.nomeXml );
        email       =  (EditText)  findViewById( R.id.emailXml );
        senha       =  (EditText)  findViewById( R.id.senhaXml );
        dataNiver   =  (EditText)  findViewById( R.id.dataNiverXml );
        celular     =  (EditText)  findViewById( R.id.celularXml );
    }

    //Método - Cadastrar Novo Cliente - Parte 1
    public void btnCadastrar (View view){
        //Pegar Dados e Converter Para String
        String nomeS        = nome.getText().toString();
        String emailS       = email.getText().toString();
        String senhaS       = senha.getText().toString();
        String dataNiverS   = dataNiver.getText().toString();
        String celularS     = celular.getText().toString();

        //Instanciar Classe e Passar Valores
        cliente = new Cliente();
        cliente.setClienteNome( nomeS );
        cliente.setClienteEmail( emailS );
        cliente.setClienteSenha( senhaS );
        cliente.setClienteNiver( dataNiverS );
        cliente.setClienteCel(celularS );

        //Cadastrar no Firebase (Módulo Autenticação) - Senha Mínima de 6 Dígitos
        fireAuth.createUserWithEmailAndPassword( cliente.getClienteEmail(), cliente.getClienteSenha())
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()) {
                Toast.makeText( b1CadastroClientes.this, "Cadastrado", Toast.LENGTH_LONG ).show();
                cadastrarUsuarioNoBanco();
            } else {
                Toast.makeText( b1CadastroClientes.this, "Erro", Toast.LENGTH_LONG ).show();
            }}});
    }

    //Método - Cadastrar Novo Cliente - Parte 2
    public void cadastrarUsuarioNoBanco (){
        //Criar Usuário na Tabela Clientes (Será o Id do Usuário Criado Pelo Firebase)
        DatabaseReference refCliente = fireDados.getReference("Clientes").child(fireAuth.getCurrentUser().getUid());

        //Cadastrar Informações do Usuário
        refCliente.child( "clienteId" ).setValue( fireAuth.getCurrentUser().getUid() ); //Id Usuário
        refCliente.child( "clienteNome" ).setValue( cliente.getClienteNome() );
        refCliente.child( "clienteEmail" ).setValue( cliente.getClienteEmail() );
        refCliente.child( "clienteSenha" ).setValue( cliente.getClienteSenha() );
        refCliente.child( "clienteNiver" ).setValue( cliente.getClienteNiver() );
        refCliente.child( "clienteCel" ).setValue( cliente.getClienteCel() );

        //Fechar Tela Atual (Voltar Para Tela Anterior = Lista)
        finish();
    }
}

//Como Saber Se Cadastrou no Firebase? Tem algum  Task isSuccessful?
//Qual o Problema das Regras de Segurança Estar em Modo Teste no Firebase?

