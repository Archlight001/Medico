package denokela.com.medico.activities;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import denokela.com.medico.R;
import denokela.com.medico.fragments.HealthCenterMaps;
import denokela.com.medico.fragments.HomeFragment;
import denokela.com.medico.fragments.PrescriptionFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;


//    SharedPreferences sharedPref= getSharedPreferences("userNo", Context.MODE_PRIVATE);
//    SharedPreferences.Editor editor = sharedPref.edit();



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        editor.putInt("CurrentID",1);
//        editor.apply();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        String intentvalue = getIntent().getStringExtra("fragment_to_load");
        String previousLocation = getIntent().getStringExtra("previousLocation");
        if(intentvalue != null){
            if(intentvalue.equals("Map"))
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HealthCenterMaps()).commit();
        }else if(previousLocation != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrescriptionFragment()).commit();
        }
        else if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==  android.R.id.home){
            drawer.openDrawer(GravityCompat.START);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_p_reminder:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrescriptionFragment()).commit();
                break;
            case R.id.nav_health_center_finder:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HealthCenterMaps()).commit();
                break;
            case R.id.nav_feedback:
                 startActivity(new Intent(this,Webview.class));
                 break;
            case R.id.exit:
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
