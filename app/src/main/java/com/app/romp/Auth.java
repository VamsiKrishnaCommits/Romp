package com.app.romp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.app.romp.ui.JustBeforeMain;
import com.app.romp.ui.Main8Activity;
import com.app.romp.ui.Required;
import com.app.romp.ui.doubleclick;
import com.app.romp.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class Auth extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    public static  String user;
    EditText editText, editText1;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    public static ArrayList<doubleclick> handle=new ArrayList<>();
   SharedPreferences preferences;
  public static  SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                for(int i=0;i<14;i++)
                    handle.add(new doubleclick(0));
        preferences = getApplicationContext().getSharedPreferences("vamsi", 0);
        if (preferences.getBoolean("loggedin", false)){
            user=preferences.getString("UID","");
         String  college=preferences.getString("college","");
            Required.college=college;
            Log.i("hey",Required.college);
            Required.userid=user;
            if(college.equals("") || college.equals("IIITG")){
                Intent intent = new Intent(getBaseContext(), JustBeforeMain.class);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(getBaseContext(), JustBeforeMainOther.class);
                startActivity(intent);
                finish();
            }
        } else {
            setContentView(R.layout.activity_auth);
            findViewById(R.id.forgot).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Auth.this,Main7Activity.class);
                    startActivity(intent);
                }
            });
            findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - handle.get(0).getTime() < 1000){
                        return;
                    }
                    handle.set(0,new doubleclick( SystemClock.elapsedRealtime()));

                    Intent intent = new Intent(getBaseContext(), signup.class);
                    startActivity(intent);
                }
            });
            findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - handle.get(1).getTime() < 1000){
                        return;
                    }
                    handle.set(1,new doubleclick( SystemClock.elapsedRealtime()));
                    firebaseAuth = FirebaseAuth.getInstance();
                    editText = findViewById(R.id.edit0);
                    final String email = editText.getText().toString();
                    editText1 = findViewById(R.id.editText);
                    final String pass = editText1.getText().toString();
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                        editText.setError("Please enter a valid E-mail ID");
                    else if (TextUtils.isEmpty(email))
                        editText.setError("This field cannot be empty");
                    else if (pass.length() < 6)
                        editText1.setError("Password cannot be less than 6 characters");
                    else {
                        findViewById(R.id.proauth).setVisibility(View.VISIBLE);
                        findViewById(R.id.auth).setVisibility(View.INVISIBLE);
                        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful() && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                    preferences = getApplicationContext().getSharedPreferences("vamsi", 0);
                                   final String UID=FirebaseAuth.getInstance().getUid();
                                   Required.userid=UID;
                                    editor = preferences.edit();
                                    editor.putString("email", email);
                                    editor.putString("UID",UID);
                                    editor.putString("pass", pass);
                                    editor.putBoolean("loggedin", true);
                                    databaseReference.child(UID+"college").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                          try {
                                              String college = dataSnapshot.getValue().toString();
                                              editor.putString("college", college);
                                              Required.college = college;
                                          }
                                          catch (Exception e){
                                              Required.college=null;
                                              FirebaseMessaging.getInstance().subscribeToTopic("IIITG");
                                          }
                                            editor.apply();
                                              FirebaseMessaging.getInstance().subscribeToTopic("All").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                      FirebaseMessaging.getInstance().subscribeToTopic(UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<Void> task) {
                                                              if(task.isSuccessful()){
                                                                  if(Required.college==null || Required.college.equals("IIITG")) {
                                                                      Toast.makeText(getBaseContext(), "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                                                      Intent intent = new Intent(getBaseContext(), JustBeforeMain.class);
                                                                      startActivity(intent);
                                                                      finish();
                                                                  }
                                                                  else{
                                                                      Toast.makeText(getBaseContext(), "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                                                      Intent intent = new Intent(getBaseContext(), JustBeforeMainOther.class);
                                                                      startActivity(intent);
                                                                      finish();
                                                                  }
                                                              }
                                                              else {
                                                                  Toast.makeText(getApplicationContext(),"Something is wrong,please try again",Toast.LENGTH_SHORT).show();
                                                              }
                                                          }
                                                      });
                                                  }
                                              });


                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });



                                }
                                        else if (!task.isSuccessful()) {
                                    findViewById(R.id.proauth).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.auth).setVisibility(View.VISIBLE);
                                    try {
                                        throw task.getException();
                                    }
                                     catch (FirebaseAuthInvalidUserException e) {
                                        Toast.makeText(getApplicationContext(),"You don't seem to have an account in rompe,create one to enjoy the rompe services",Toast.LENGTH_LONG).show();
                                    }
                                    catch (FirebaseAuthInvalidCredentialsException e){
                                        Toast.makeText(getApplicationContext(),"Your romp credentials are incorrect,please try again",Toast.LENGTH_LONG).show();
                                    }
                                    catch (Exception e) {
                                        System.out.println(e.toString());
Toast.makeText(getApplicationContext(),"Please Check with your internet connection",Toast.LENGTH_LONG).show();                                    }
                                }
                                          else{
                                    Toast.makeText(getApplicationContext(),"Your account is not verified,please check the email rompe sent you",Toast.LENGTH_LONG).show();
                                    findViewById(R.id.proauth).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.auth).setVisibility(View.VISIBLE);
                                }
                            }


                        });
                    }
                }
            });
        }
    }
}
