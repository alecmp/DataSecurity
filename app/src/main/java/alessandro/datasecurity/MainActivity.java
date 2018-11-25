package alessandro.datasecurity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    public static Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    public static android.support.v4.app.FragmentManager sFm;
    private SharedPreferences pref;
    private static final String SHARED_PREFERENCES_TYPE = "Account";
    FirebaseUser user;
    //dichiarazioni Firebase
    static FirebaseDatabase database;
    private String userId;
    private Uri photoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
       // View header = navigationView.getHeaderView(0);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {

/*

                photoUrl = Uri.parse("https://firebasestorage.googleapis.com/v0/b/dibapp-b8fda.appspot.com/o/profile_icon.png?alt=media&token=cf3742c4-acb6-4c2d-b2ae-a516569aa082");
                if (user != null && user.getPhotoUrl() != null) {
                    photoUrl = user.getPhotoUrl();
                }

                pref = getSharedPreferences(SHARED_PREFERENCES_TYPE, MODE_PRIVATE);
                String localStr = pref.getString(userId, null);
                if (localStr != null) {
                    Uri localUrl = Uri.parse(localStr);
                    if (localUrl != photoUrl) {
                        photoUrl = localUrl;


                    }

                }

                Glide.with(getApplicationContext())
                        .load(photoUrl)
                        //.skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(profile);
*/

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new InboxFragment()).commit();
        }
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

/*
        if (id == R.id.save_exam) {
            return true;
        }*/


        return super.onOptionsItemSelected(item);


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        sFm = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_main);


            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new InboxFragment()).commit();


        } else if (id == R.id.nav_slideshow) {

            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_main);


            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new InboxFragment()).commit();


        }
            setTitle(item.getTitle());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


   /* public void onListFragmentInteraction(DummyContent.DummyItem uri) {
        //you can leave it empty
    }*/
}
