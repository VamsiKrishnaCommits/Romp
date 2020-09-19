package com.app.romp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.romp.ui.JustBeforeMain;
import com.app.romp.ui.dashboard.DashboardFragment;
import com.app.romp.ui.dashboard.upi;
import com.app.romp.ui.home.HomeFragment;
import com.app.romp.ui.home.bill;
import com.app.romp.ui.home.item;
import com.app.romp.ui.notifications.NotificationsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,HomeFragment.ini,ViewCart.inio,summary.iniot{
    FragmentManager fragmentManager=getSupportFragmentManager();
    public static ArrayList<item> items;
    String current="home";
    public  static upi upi;
    BottomNavigationView navigationView;
    public  static boolean issummary=false;
    String rep=null;
    public  static String hotelname;
    public static String ordernode=null;
    public static ArrayList<item> send;
    HomeFragment homeFragment;
    static String menu=null;
    public static boolean allow=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference avail=firebaseDatabase.getReference(getIntent().getStringExtra("3"));
        avail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().equals("0") && false){
                    Toast.makeText(getApplicationContext(),"This outlet is not available at this moment,please try later",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        issummary=false;
        super.onCreate(savedInstanceState);
        menu=getIntent().getStringExtra("1");
        firebaseDatabase.getReference(menu+"upi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                upi=dataSnapshot.getValue(upi.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ordernode=getIntent().getStringExtra("2");
        items=new ArrayList<>();
        send=new ArrayList<>();
        bill.number=0;
        bill.totalcost=0;
        hotelname=getIntent().getStringExtra("4");
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(hotelname+" -  Romp");
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(this);
        homeFragment = new HomeFragment();
       FragmentTransaction ft= fragmentManager.beginTransaction();
       ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
       ft.replace(R.id.nav_host_fragment,homeFragment).commit();
         firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseDatabase.getReference(getIntent().getStringExtra("1")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                GenericTypeIndicator<ArrayList<item>> t=new GenericTypeIndicator<ArrayList<item>>() {
                };
                try {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        items.add(snapshot.getValue(item.class));
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"some error occurred try again!",Toast.LENGTH_SHORT).show();
                }
                for(int i=0;i<items.size();i++){
                    try{
                        String a=items.get(i).name;
                    }
                    catch (Exception e){
                        items.remove(i);
                        i--;
                    }
                }
                send=new ArrayList<>();
                bill.number=0;
                bill.totalcost=0;
                homeFragment.trigger();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private boolean load(Fragment fragment){
        if(fragment!=null && !rep.equals(current)) {
            current=rep;

            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
            ft.replace(R.id.nav_host_fragment, fragment).commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment=null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment=new HomeFragment();
                homeFragment=(HomeFragment)fragment;
                rep="home";
                break;
            case R.id.navigation_dashboard:
                fragment=new DashboardFragment();
                rep="history";
                break;
            case R.id.navigation_notifications:
                fragment=new NotificationsFragment();
                rep="logout";
                break;
        }
        return load(fragment);
    }

    @Override
    public void bro() {

        FragmentTransaction ft=fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
        ft.addToBackStack(null).replace(R.id.nav_host_fragment,new ViewCart()).commit();
    }
    @Override
    public void onBackPressed() {
        if(!allow) {
            if (!allow && navigationView.getSelectedItemId() == R.id.navigation_home) {

                if(!issummary)
                    super.onBackPressed();
                else{
                    Intent intent=new Intent(this,JustBeforeMain.class);
                    finish();
                }


            }
            else {
                navigationView.setSelectedItemId(R.id.navigation_home);
               FragmentTransaction ft= fragmentManager.beginTransaction();
               ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
                       ft.replace(R.id.nav_host_fragment, new HomeFragment()).commit();
            }
        }
    }
    @Override
    public void broo() {
        try {
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
            ft.addToBackStack(null).replace(R.id.nav_host_fragment, new summary()).commit();
            findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Ordered",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    @Override
    public void brook() {
        Intent intent=new Intent(this, JustBeforeMain.class);
        finish();
    }
}
