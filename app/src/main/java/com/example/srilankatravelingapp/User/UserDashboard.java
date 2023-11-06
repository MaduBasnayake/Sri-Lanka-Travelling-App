package com.example.srilankatravelingapp.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.srilankatravelingapp.Common.LoginSignup.Login;
import com.example.srilankatravelingapp.Common.LoginSignup.UserProfile;
import com.example.srilankatravelingapp.HelperClasses.HomeAdapter.CategoriesAdapter;
import com.example.srilankatravelingapp.HelperClasses.HomeAdapter.CategoriesHelperClass;
import com.example.srilankatravelingapp.Maps.MapsActivity;
import com.example.srilankatravelingapp.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Variables
    RecyclerView categoriesRecycler;
    RecyclerView.Adapter adapter;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    static final float END_SCALE = 0.7f;

    ImageView menuIcon;
    LinearLayout contentView;

    private String name, username, email, phoneNo, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_deshboard);

        // Retrieve user data from intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        phoneNo = intent.getStringExtra("phoneNo");
        password = intent.getStringExtra("password");

        //Hooks
        categoriesRecycler = findViewById(R.id.categories_recycler);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);

        naviagtionDrawer();


        //Functions will be executed automatically when this activity will be created
        categoriesRecycler();

        // Initialize views and sensors
        TextView textView = findViewById(R.id.actionEvent);
        ImageView imageView = findViewById(R.id.temperatureIcon);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor tempSensor = null;
        boolean isTemperatureSensorAvailable = false;

        // Check if temperature sensor is available
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTemperatureSensorAvailable = true;
        } else {
            textView.setText("Temperature sensor is not available");
            isTemperatureSensorAvailable = false;
        }

        // Register sensor listener
        if (isTemperatureSensorAvailable) {
            SensorEventListener temperatureListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float temperatureCelsius = event.values[0];
                    textView.setText(String.format("TODAY %.1f °C", temperatureCelsius));

                    if (temperatureCelsius > 32) {
                        imageView.setImageResource(R.drawable.weather_1);
                    } else if (temperatureCelsius > 21) {
                        imageView.setImageResource(R.drawable.weather_2);
                    } else if (temperatureCelsius > 10) {
                        imageView.setImageResource(R.drawable.weather_3);
                    }else if (temperatureCelsius >-1) {
                        imageView.setImageResource(R.drawable.weather_4);
                    }else {
                        imageView.setImageResource(R.drawable.weather_5);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // Do nothing
                }
            };
            sensorManager.registerListener(temperatureListener, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //Navigation Drawer Functions
    private void naviagtionDrawer() {
        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case  R.id.nav_profile:
                openUserProfile(item.getActionView());
                break;
            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
        }
        return false;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void categoriesRecycler() {

        ArrayList<CategoriesHelperClass> categoriesHelperClasses = new ArrayList<>();
        categoriesHelperClasses.add(new CategoriesHelperClass( R.drawable.category_1, "Destinations"));
        categoriesHelperClasses.add(new CategoriesHelperClass( R.drawable.category_2, "FOOD"));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.category_3, "HOSPITAL"));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.category_4, "Transport"));

        categoriesRecycler.setHasFixedSize(true);
        adapter = new CategoriesAdapter(categoriesHelperClasses);

        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(adapter);
    }

    public void past_All(View view) {
        startActivity(new Intent(this, AllCategories.class));

    }

    public void map(View view) {
        startActivity(new Intent(this, MapsActivity.class));

    }

    public void  LoginSignup(View view) {
        startActivity(new Intent(this, Login.class));

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void openUserProfile(View view) {
        Intent intent = new Intent(UserDashboard.this, UserProfile.class);
        intent.putExtra("name", name);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("password", password);
        startActivity(intent);
    }


}