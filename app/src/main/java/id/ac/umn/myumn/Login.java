package id.ac.umn.myumn;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import id.ac.umn.myumn.Dashboard.Dashboard;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etEmail, etPass;
    Button btnLogin, btnShowHide;
    boolean status;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        status = true;

        etEmail = findViewById(R.id.emailHint);
        etPass = findViewById(R.id.passHint);
        btnLogin = findViewById(R.id.btnSign);
        btnShowHide = findViewById(R.id.btnShow);

        final Executor executor = Executors.newSingleThreadExecutor();

        final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Fingerpint")
                .setSubtitle("Subtitle")
                .setDescription("Description")
                .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).build();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = etEmail.getText().toString();
                String password = etPass.getText().toString();

                if (uname.equals("")) {
                    Toast.makeText(Login.this, "Silahkan Input Email", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(Login.this, "Silahkan Input Password", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(uname, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                                        @Override
                                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                                            startActivity(new Intent(getApplicationContext(), Dashboard.class));

                                        }
                                    });
                                } else {
                                    Toast.makeText(Login.this, "Login Gagal.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
                    etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    status = false;
                    etPass.setSelection(etPass.length());
                    btnShowHide.setBackgroundResource(R.drawable.invisible);
                } else {
                    etPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    status = true;
                    etPass.setSelection(etPass.length());
                    btnShowHide.setBackgroundResource(R.drawable.visible);
                }
            }
        });
    }
}
