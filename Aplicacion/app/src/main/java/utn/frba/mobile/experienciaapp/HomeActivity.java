package utn.frba.mobile.experienciaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import utn.frba.mobile.experienciaapp.agenda.AgendaActivity;
import utn.frba.mobile.experienciaapp.experiencia.BuscarExperienciaActivity;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.login.LoginActivity;
import utn.frba.mobile.experienciaapp.models.Turista;
import utn.frba.mobile.experienciaapp.service.LoginService;
import utn.frba.mobile.experienciaapp.service.SessionService;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    private SharedPreferences sharedPref;
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        loginBehaviorButton();

        Button buscarExperienciaIB = (Button) findViewById(R.id.buscarExperienciaIB);


        buscarExperienciaIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), BuscarExperienciaActivity.class);
                startActivity(i);
            }
        });

        Button miAgendaaIB = (Button) findViewById(R.id.miAgendaaIB);


        miAgendaaIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AgendaActivity.class);
                startActivity(i);
            }
        });
    }

    private void loginBehaviorButton() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        ImageView profileImg = (ImageView) headerView.findViewById(R.id.profileImg);

        TextView usersameTV = (TextView) headerView.findViewById(R.id.usernameTV);
        TextView emailTV = (TextView) headerView.findViewById(R.id.emailTV);
        LinearLayout ll_user = (LinearLayout) headerView.findViewById(R.id.ll_user);

        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);

        if(!SessionService.getInstance().isSessionActive(this)) {
            usersameTV.setText("No registrado");
            emailTV.setText("-");
            Picasso.get().load(R.mipmap.ic_launcher_round).into(profileImg);
            ll_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginActivity.activityDestino=BuscarExperienciaActivity.class;
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                }
            });
        }else{
            Turista turista=SessionService.getInstance().getTurista();
            Picasso.get().load(turista.getImageUrl()).into(profileImg);
            usersameTV.setText("Registrado");
            emailTV.setText(turista.getEmail());
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            ll_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this,"Session ya iniciada.", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginBehaviorButton();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_experiencias) {
            Intent i = new Intent(getBaseContext(), BuscarExperienciaActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_mi_agenda) {
            Intent i = new Intent(getBaseContext(), AgendaActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            signOut();
            loginBehaviorButton();
            Toast.makeText(HomeActivity.this,"Se ha deslogueado con exito.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_acerca) {
            Alert alertAcerca = new Alert(this);
            alertAcerca.Show("Equipo Violeta - TP Mobile","Sobre Nosotros");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(LoginService.authStateListener!=null){
            FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.removeAuthStateListener(LoginService.authStateListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            //Inicializacion de google account
            GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso ).build();
        }catch(Exception e){
            //
        }

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            //Inicializacion de google account
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        }catch (Exception e){
        }
    }

    private void signOut(){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if(status.isSuccess()){
                    Intent i =new Intent(HomeActivity.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(HomeActivity.this,"Error en google signOut", Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
