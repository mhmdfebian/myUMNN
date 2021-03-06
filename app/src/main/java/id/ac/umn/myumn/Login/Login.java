package id.ac.umn.myumn.Login;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import id.ac.umn.myumn.Dashboard.Dashboard;
import id.ac.umn.myumn.R;

public class Login extends AppCompatActivity {

    Button btnLogin, btnShowHide;
    EditText etEmail, etPass;
    FirebaseAuth mAuth;
    TextView alertLogin;

    boolean status;

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
        alertLogin = findViewById(R.id.alertLogin);

        final Executor executor = Executors.newSingleThreadExecutor();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = etEmail.getText().toString();
                String password = etPass.getText().toString();

                if (uname.equals("") && password.equals("")) {
                    alertLogin.setText("Could'nt Sign In, Please retype your Student Email and Password");
                } else if (password.equals("")) {
                    alertLogin.setText("Could'nt Sign In, Please retype your Password");
                } else if (uname.equals("")) {
                    alertLogin.setText("Could'nt Sign In, Please retype your Student Email");
                } else {

                    // Membandingkan username dan password yang diinput dan yang ada di database
                    mAuth.signInWithEmailAndPassword(uname, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Cek version API
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            // Declare Fingerprint
                                            final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(Login.this)
                                                    .setTitle("Hello Ultimafriend,")
                                                    .setSubtitle("Please authenticate your account")
                                                    .setDescription("using fingerprint to sign in to myumn account")
                                                    .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    }).build();
                                            // Manggil Biometric
                                            biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                                                @Override
                                                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                                }
                                            });
                                        } else {
                                            // Kalo API tidak mencukupi akan login biasa
                                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                        }
                                    } else {
                                        alertLogin.setText("Could'nt Sign In, Please retype your Student Email and Password");
                                    }
                                }
                            });
                }
            }
        });

        btnShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //button untuk show atau hide password
                if (status) {
                    etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    status = false;
                    etPass.setSelection(etPass.length());
                    btnShowHide.setBackgroundResource(R.drawable.invisible);
                    //hide password ketika button invisible ditekan
                } else {
                    etPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    status = true;
                    etPass.setSelection(etPass.length());
                    btnShowHide.setBackgroundResource(R.drawable.visible);
                    //show password ketika button visible ditekan
                }
            }
        });
    }
}
