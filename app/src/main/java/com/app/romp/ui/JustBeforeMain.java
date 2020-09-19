package com.app.romp.ui;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.app.romp.Auth;
import com.app.romp.MainActivity;
import com.app.romp.R;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import static com.app.romp.Auth.handle;

public class JustBeforeMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseMessaging firebaseMessaging=FirebaseMessaging.getInstance();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference ranj=firebaseDatabase.getReference("ranjeethaavail");
        DatabaseReference foodtruck=firebaseDatabase.getReference("foodtruckavail");
        DatabaseReference canteen=firebaseDatabase.getReference("canteenavail");
       DatabaseReference acad=firebaseDatabase.getReference("canteenacad");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_before_main);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        final View view=findViewById(R.id.pop);
        view.post(new Runnable() {
            @Override
            public void run() {
                measure.height=view.getHeight();
            }
        });
        final TextView textView=findViewById(R.id.availability3);
        ranj.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("heyy","heyy");
                try {
                    if (dataSnapshot.getValue().equals("1")) {
                        textView.setText("Status : Available");
                    }
                    else{
                        textView.setText("Status : Unavailable");
                    }
                }
                catch (Exception e){
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        foodtruck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView=findViewById(R.id.availability2);
                Log.i("heyy","heyy");
                try {
                    if (dataSnapshot.getValue().equals("1")) {
                        textView.setText("Status : Available");
                    } else {
                        textView.setText("Status : Unavailable");

                    }
                }
                catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        acad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView=findViewById(R.id.availability4);
                Log.i("heyy","heyy");
                try {
                    if (dataSnapshot.getValue().equals("1")) {
                        textView.setText("Status : Available");
                    } else {
                        textView.setText("Status : Unavailable");

                    }
                }
                catch (Exception e){
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        canteen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView textView=findViewById(R.id.availability);
                try {
                    if (dataSnapshot.getValue().equals("1")) {
                        textView.setText("Status : Available");
                    } else {
                        textView.setText("Status : Unavailable");
                    }
                }
                catch (Exception e){

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        findViewById(R.id.cant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(3).getTime() < 1000){
                    return;
                }
                handle.set(3,new doubleclick( SystemClock.elapsedRealtime()));
                Intent in=new Intent(JustBeforeMain.this, MainActivity.class);
                in.putExtra("1","realiiitgmenu");
                in.putExtra("2","ordersforcanteen");
                in.putExtra("3","canteenavail");
                in.putExtra("4","IIITG Canteen");
                startActivity(in);

            }
        });
        findViewById(R.id.food).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(4).getTime() < 1000){
                    return;
                }
                handle.set(4,new doubleclick( SystemClock.elapsedRealtime()));
                Intent in=new Intent(JustBeforeMain.this, MainActivity.class);
                in.putExtra("1","foodtruckrealtime");
                in.putExtra("2","ordersforfoodtruck");
                in.putExtra("3","foodtruckavail");
                in.putExtra("4","Food Truck");
                startActivity(in);
            }
        });
        findViewById(R.id.ranj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(5).getTime() < 1000){
                    return;
                }
                handle.set(5,new doubleclick( SystemClock.elapsedRealtime()));
                Intent in=new Intent(JustBeforeMain.this, MainActivity.class);
                in.putExtra("1","ranjeetharealtime");
                in.putExtra("4","Ranjeetha");
                in.putExtra("2","ordersforranjeetha");
                in.putExtra("3","ranjeethaavail");
                startActivity(in);
            }
        });
        findViewById(R.id.cantacad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - handle.get(13).getTime() < 1000){
                    return;
                }
                handle.set(13,new doubleclick( SystemClock.elapsedRealtime()));
                Intent in=new Intent(JustBeforeMain.this, MainActivity.class);
                in.putExtra("1","canteenrealtime");
                in.putExtra("4","The Grocery Store");
                in.putExtra("2","ordersforacad");
                in.putExtra("3","canteenacad");
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.others){
            Intent intent=new Intent(this,whatothersordered.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.Logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(JustBeforeMain.this);
            builder.setMessage("Are you sure you want to log out?");
            builder.setTitle("Log out");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreference=JustBeforeMain.this.getSharedPreferences("vamsi",0);
                    SharedPreferences.Editor editor=sharedPreference.edit();
                    editor.clear();
                    editor.apply();
                    Intent  intent=new Intent(JustBeforeMain.this, Auth.class);
                    startActivity(intent);
                    finish();
                }
            }).show();
        }
        else if(item.getItemId()==R.id.myorders){
            Intent intent=new Intent(this,Myorders.class);
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(this,aboutcreator.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(JustBeforeMain.this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setTitle("Confirm");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();

    }
}
