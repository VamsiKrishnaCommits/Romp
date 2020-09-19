package com.app.romp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.romp.ui.home.bill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import static com.app.romp.ViewCart.PAYMENT_REQUEST;

public class Main7Activity extends AppCompatActivity {
    String email;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        editText = findViewById(R.id.email);
        try {
            findViewById(R.id.buttonnn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = editText.getText().toString();
findViewById(R.id.reset).setVisibility(View.INVISIBLE);
findViewById(R.id.pro).setVisibility(View.VISIBLE);
                    if (email != null) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            findViewById(R.id.reset).setVisibility(View.VISIBLE);
                                            findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "A link is sent to the given Email ID to reset password", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            findViewById(R.id.reset).setVisibility(View.VISIBLE);
                                            findViewById(R.id.pro).setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "Something is wrong:(", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });

                    }
                }
            });

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Enter a valid E-mail ID", Toast.LENGTH_LONG).show();

        }
    }

}
