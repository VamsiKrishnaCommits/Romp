package com.app.romp;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.romp.ui.doubleclick;
import com.app.romp.ui.notifications.cont;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseAuth firebaseAuth;
    EditText editText1,editText;
    String[] country;
    String college;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final Spinner spin=(Spinner) findViewById(R.id.spin);
        spin.setOnItemSelectedListener(this);
        final FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("colleges");
        final DatabaseReference databaseReference12=firebaseDatabase.getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 country=new String[(int)dataSnapshot.getChildrenCount()];
                int i=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        country[i]=snapshot.getValue().toString();
                        i++;
                }
                ArrayAdapter aa=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,country);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        country = new String[]{"Loading colleges..."};
        ArrayAdapter aa=new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - Auth.handle.get(2).getTime() < 1000){
                    return;
                }
                Auth.handle.set(2,new doubleclick( SystemClock.elapsedRealtime()));
                firebaseAuth=FirebaseAuth.getInstance();
                editText=findViewById(R.id.edit11);
                String email=editText.getText().toString();
                editText1=findViewById(R.id.editText2);
                String pass=editText1.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    editText.setError("Please enter a valid E-mail ID");
                else if(TextUtils.isEmpty(email))
                    editText.setError("This field cannot be empty");
                else  if(pass.length() <6)
                    editText1.setError("Password cannot be less than 6 characters");
                else {
                    setContentView(R.layout.load);
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            databaseReference12.child(firebaseAuth.getUid()+"college").setValue(college).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(), "verification mail sent.Please check the spam section of your e-mail service if you did not receive notification", Toast.LENGTH_LONG).show();
                                                    setContentView(R.layout.complete);
                                                }
                                            });
                                        }
                                        else {
                                            Intent intent=new Intent(getBaseContext(),signup.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(getApplicationContext(), "Could not send verification mail,try with another credentials", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else{
                                try {
                                    Intent intent=new Intent(getBaseContext(),signup.class);
                                    startActivity(intent);
                                    finish();
                                    throw task.getException();
                                }
                                catch (NullPointerException e) {
                                    Toast.makeText(getApplicationContext(),"some error occured",Toast.LENGTH_SHORT).show();
                                }
                                catch (FirebaseAuthUserCollisionException e){
                                    Toast.makeText(getApplicationContext(),"User already exists!",Toast.LENGTH_LONG).show();
                                }
                                catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Please Check with your internet connection",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
college=country[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
