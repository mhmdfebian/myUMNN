package id.ac.umn.myumn.Enrollment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import id.ac.umn.myumn.R;

public class EnrollmentDetail extends AppCompatActivity {

    Button btnClose, btnAdd;

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    String userID, courseID, subject, time, timeend, day, semester;

    TextView tvSubject, tvDay, tvTime, tvTimeEnd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_detail);

        btnClose = findViewById(R.id.btnClose);
        btnAdd = findViewById(R.id.btnAdd);


        tvSubject = findViewById(R.id.subject);
        tvDay = findViewById(R.id.day);
        tvTime = findViewById(R.id.time);
        tvTimeEnd = findViewById(R.id.timeend);

        Intent pindah = getIntent();
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        userID = mAuth.getCurrentUser().getUid();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent();
                setResult(RESULT_OK, intentBack);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });



        courseID = pindah.getStringExtra("courseID");
        subject = pindah.getStringExtra("subject");
        day = pindah.getStringExtra("day");
        time = pindah.getStringExtra("time");
        timeend = pindah.getStringExtra("timeend");

        tvSubject.setText(subject);
        tvDay.setText(day);
        tvTime.setText(time);
        tvTimeEnd.setText(timeend);

        DocumentReference documentReference = fStore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    semester = documentSnapshot.getString("semester");
                }
            }
        });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DocumentReference docRef = fStore.collection("user").document(userID).collection("course").document("semester").collection(semester).document(courseID);
                    Map<String, Object> event = new HashMap<>();
                    event.put("subject", subject);
                    event.put("day", day);
                    event.put("time",time);
                    event.put("timeend", timeend);
                    docRef.set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EnrollmentDetail.this, subject + " berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });



    }

    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent();
        setResult(RESULT_OK, intentBack);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
