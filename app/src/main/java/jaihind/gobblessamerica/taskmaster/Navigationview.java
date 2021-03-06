package jaihind.gobblessamerica.taskmaster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import jaihind.gobblessamerica.taskmaster.authentication.GoogleSignin;
import jaihind.gobblessamerica.taskmaster.di.ApplicationComponent;

public class Navigationview extends AppCompatActivity
        implements android.support.design.widget.NavigationView.OnNavigationItemSelectedListener {
    @Inject
    FirebaseAuth mAuth;


  //  @BindView(R.id.user_iv)
    ImageView mUser_iv;
    //@BindView(R.id.username_tv)
    TextView mUsername_tv;

    HomeFragment homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);
        ButterKnife.bind(this);
        ApplicationComponent.getDi().inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        android.support.design.widget.NavigationView navigationView = (android.support.design.widget.NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_navigation_view);
      //  ButterKnife.bind(this,headerView);
        mUser_iv=(ImageView)headerView.findViewById(R.id.user_iv);
        mUsername_tv=(TextView)headerView.findViewById(R.id.username_tv);

        if(savedInstanceState==null){

            homeFragment=new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.homefragment_container,homeFragment,"homefragment")
                    .addToBackStack(null).commit();

        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            Picasso.with(Navigationview.this).load(photoUrl).into(mUser_iv);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            //update ui for the respective user
            String username= mUsername_tv.getText().toString();
            mUsername_tv.setText(username + name);



        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } /*else if (true) {
            homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.homefragment_container, homeFragment)
                    .addToBackStack(null).commit();
        } */else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home) {

            if(getFragmentManager().findFragmentByTag("homefragment")==null)
        getSupportFragmentManager().beginTransaction().replace(R.id.homefragment_container,homeFragment,"homeFragment")
                .addToBackStack(null).commit();

        } else if (id == R.id.projects) {

            Toast.makeText(this, "Projects", Toast.LENGTH_LONG).show();

        } else if (id == R.id.food) {

        } else if (id == R.id.fitness) {

        } else if (id == R.id.groomother) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if(id == R.id.logout_menu_item){
            mAuth.signOut();
            Intent signout_intent=new Intent(this, GoogleSignin.class);
            startActivity(signout_intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
