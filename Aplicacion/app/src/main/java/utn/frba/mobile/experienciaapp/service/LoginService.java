package utn.frba.mobile.experienciaapp.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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
import utn.frba.mobile.experienciaapp.login.LoginActivity;

public class LoginService  {
    private static final String TAG = "LoginService";
    private SharedPreferences sharedPref;

    private static final int SIGN_ING_GOOGLE_CODE = 1;
    private FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    public GoogleApiClient googleApiClient;
    private LoginActivity loginActivity;



    public static LoginService builder(LoginActivity activiy){//
        return new LoginService(activiy);
    }

    private LoginService(LoginActivity activiy){
        this.loginActivity=activiy;
        this.sharedPref = this.loginActivity.getPreferences(Context.MODE_PRIVATE);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    Log.w(TAG,"onAuthStateChanged - singed in" );
                    Log.w(TAG,"onAuthStateChanged - UUID:"+firebaseUser.getUid());
                    Log.w(TAG,"onAuthStateChanged - email: "+firebaseUser.getEmail());
                    sharedPref.edit().putBoolean("key_logedin",true);
                    sharedPref.edit().putString("key_email",firebaseUser.getEmail());
                    sharedPref.edit().putString("key_image",firebaseUser.getPhotoUrl().toString());
                }else{
                    Log.w(TAG,"onAuthStateChanged - singed out" );
                    sharedPref.edit().putBoolean("key_logedin",false);
                }
            }
        };
        //Inicializacion de google account
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(loginActivity.getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this.loginActivity).enableAutoManage(this.loginActivity,this.loginActivity).addApi(Auth.GOOGLE_SIGN_IN_API,gso ).build();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    private void signInGoogleFirebase(GoogleSignInResult googleSignInResult){
        if(googleSignInResult.isSuccess()){
            final AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInResult.getSignInAccount().getIdToken(),null);

            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(loginActivity,"GOOGLE Login exitoso", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(loginActivity,BuscarExperienciaActivity.class);
                        loginActivity.startActivity(i);
                        loginActivity.finish();
                    }else{
                        Toast.makeText(loginActivity,"GOOGLE Login invalido", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }else{
            Toast.makeText(loginActivity,"GOOGLE SIGN IN INVALIDO", Toast.LENGTH_SHORT).show();
            sharedPref.edit().putBoolean("key_logedin",false);

        }
    }

    public boolean isLogged(){
        return firebaseAuth.getCurrentUser()!=null;
    }

    public void singOut(){
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    public View.OnClickListener onClickListerSignInGoogle(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                loginActivity.startActivityForResult(intent,SIGN_ING_GOOGLE_CODE);
            }
       };
    }

    public void createAccount(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(loginActivity,"Creacion exitosa", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(loginActivity,"UPS! ocurrio un error inesperado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void singIn(String email,String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(loginActivity,"Login exitoso", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(loginActivity,BuscarExperienciaActivity.class);
                    loginActivity.startActivity(i);
                    loginActivity.finish();
                }else{
                    Toast.makeText(loginActivity,"Login invalido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(SIGN_ING_GOOGLE_CODE == requestCode){
            GoogleSignInResult googleSignInResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            signInGoogleFirebase(googleSignInResult);
        }
    }
}


