package utn.frba.mobile.experienciaapp.login;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import utn.frba.mobile.experienciaapp.R;
import utn.frba.mobile.experienciaapp.experiencia.BuscarExperienciaActivity;
import utn.frba.mobile.experienciaapp.service.LoginService;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "LoginActivity";
    private static final int SIGN_ING_GOOGLE_CODE = 1;
    private Button btnCreateAccount;
    private Button btnSignIn;
    private EditText edtEmail;
    private EditText edtPassword;
    private SignInButton btnSignInGoogle;
    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindElements();
        loginService=LoginService.builder(this);
        defineBehaviour();
    }

    private void defineBehaviour(){
        this.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService.createAccount(edtEmail.getText().toString(),edtPassword.getText().toString());
            }
        });

        this.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService.singIn(edtEmail.getText().toString(),edtPassword.getText().toString());

            }
        });

        this.btnSignInGoogle.setOnClickListener(loginService.onClickListerSignInGoogle());
    }

    private void bindElements() {
        btnCreateAccount=this.findViewById(R.id.btnCreateAccount);
        btnSignIn=this.findViewById(R.id.btnSignIn);
        edtEmail=this.findViewById(R.id.edtEmail);
        edtPassword=this.findViewById(R.id.edtPassword);
        btnSignInGoogle=this.findViewById(R.id.btnSignInGoogle);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginService.onActivityResult(requestCode,resultCode,data);
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //
    }
}
