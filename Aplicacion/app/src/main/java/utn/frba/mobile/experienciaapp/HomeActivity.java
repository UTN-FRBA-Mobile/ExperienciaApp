package utn.frba.mobile.experienciaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import utn.frba.mobile.experienciaapp.agenda.AgendaActivity;
import utn.frba.mobile.experienciaapp.experiencia.BuscarExperienciaActivity;
import utn.frba.mobile.experienciaapp.lib.utils.Alert;
import utn.frba.mobile.experienciaapp.login.LoginActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences sharedPref;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView usersameTV = (TextView) headerView.findViewById(R.id.usernameTV);
        TextView emailTV = (TextView) headerView.findViewById(R.id.emailTV);
        LinearLayout ll_user = (LinearLayout) headerView.findViewById(R.id.ll_user);

        if(!sharedPref.getBoolean(getString(R.string.key_logedin),false)) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            usersameTV.setText("No registrado");
            emailTV.setText("-");
            ll_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                }
            });
        }

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            //TODO Logout
        } else if (id == R.id.nav_acerca) {
            Alert alertAcerca = new Alert(this);
            alertAcerca.Show("Equipo Violeta - TP Mobile","Sobre Nosotros");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
