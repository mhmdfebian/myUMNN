package id.ac.umn.myumn.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import id.ac.umn.myumn.Financial.Financial;
import id.ac.umn.myumn.Menu.Menu;
import id.ac.umn.myumn.Notification.Notification;
import id.ac.umn.myumn.R;
import id.ac.umn.myumn.Skkm.Skkm;

public class Profile extends AppCompatActivity {

    Button btnMenu, btnNotif, btnSKKM, btnFinancial;
    TextView Name, EmailStudent , Nim, faculty, program, year;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);
        btnSKKM = findViewById(R.id.btnSKKM);
        btnFinancial = findViewById(R.id.btnFinancial);

        Name = findViewById(R.id.Name);
        EmailStudent = findViewById(R.id.EmailStudent);
        Nim = findViewById(R.id.Nim);
        faculty = findViewById(R.id.faculty);
        program = findViewById(R.id.program);
        year = findViewById(R.id.year);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, Menu.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnSKKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, Skkm.class);
                startActivity(i);
            }
        });

        btnFinancial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, Financial.class);
                startActivity(i);
            }
        });


        DocumentReference documentReference = fStore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Name.setText(documentSnapshot.getString("fullname"));
                    EmailStudent.setText(documentSnapshot.getString("email"));
                    Nim.setText(documentSnapshot.getString("nim"));
                    faculty.setText(documentSnapshot.getString("faculty"));
                    program.setText(documentSnapshot.getString("program"));
                    year.setText(documentSnapshot.getString("year"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke Menu bar lagi
    }
}
