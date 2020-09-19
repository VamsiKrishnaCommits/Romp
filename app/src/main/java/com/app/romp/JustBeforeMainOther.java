package com.app.romp;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.app.romp.Auth;
import com.app.romp.MainActivity;
import com.app.romp.R;
import com.app.romp.ui.Myorders;
import com.app.romp.ui.Required;
import com.app.romp.ui.aboutcreator;
import com.app.romp.ui.measure;
import com.app.romp.ui.whatothersordered;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import static com.app.romp.Auth.handle;

public class JustBeforeMainOther extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_before_main_other);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        final View view=findViewById(R.id.pop);
        view.post(new Runnable() {
            @Override
            public void run() {
                measure.height=view.getHeight();
            }
        });

FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
DatabaseReference databaseReference=firebaseDatabase.getReference();
        final ArrayList<String> ar=new ArrayList<>();
databaseReference.child(Required.college).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
            ar.add(snapshot.getValue().toString());
        }
        findViewById(R.id.load).setVisibility(View.INVISIBLE);
        RecyclerView recyclerView=findViewById(R.id.recyclerfordisp);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        custom custom=new custom(ar,getApplicationContext());
        recyclerView.setAdapter(custom);
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

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
            Intent intent=new Intent(this, whatothersordered.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.Logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(JustBeforeMainOther.this);
            builder.setMessage("Are you sure you want to log out?");
            builder.setTitle("Log out");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreference=JustBeforeMainOther.this.getSharedPreferences("vamsi",0);
                    SharedPreferences.Editor editor=sharedPreference.edit();
                    editor.clear();
                    editor.apply();
                    Intent  intent=new Intent(JustBeforeMainOther.this, Auth.class);
                    startActivity(intent);
                    finish();
                }
            }).show();
        }
        else if(item.getItemId()==R.id.myorders){
            Intent intent=new Intent(this, Myorders.class);
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(this, aboutcreator.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(JustBeforeMainOther.this);
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
class custom extends RecyclerView.Adapter{
Context context;
ArrayList<String> arr;
    public custom(ArrayList<String> ar, Context applicationContext) {
        this.arr=ar;
        this.context=applicationContext;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowforoutlets, viewGroup,false);
        myy my = new myy(view);
        return my;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
final myy m=(myy)viewHolder;
m.Outlet.setText(arr.get(i));
m.Outlet.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context,Main9Activity.class);
        intent.putExtra("1",arr.get(i));
        context.startActivity(intent);
    }
});
FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
DatabaseReference databaseReference=firebaseDatabase.getReference(arr.get(i)+"availability");
databaseReference.setValue("0");
databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue().toString().equals("0")) {
m.status.setText("Status: Unavailable");
        }
        else{
            m.status.setText("Status: Available");
        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
}
class myy extends RecyclerView.ViewHolder{
TextView Outlet;
TextView status;
    public myy(@NonNull View itemView) {
        super(itemView);
        this.Outlet=itemView.findViewById(R.id.hotelid);
        this.status=itemView.findViewById(R.id.availability);
    }
}
