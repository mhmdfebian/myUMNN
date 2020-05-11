
package id.ac.umn.myumn.Grade;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import id.ac.umn.myumn.R;

public class GradeDetail extends AppCompatActivity {


    Button btnClose;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID, gradeId, semester;
    TextView tvSubject, tvNilaitugas, tvNilaiuas, tvNilaiuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_detail);

        btnClose = findViewById(R.id.btnClose);

        Intent pindah = getIntent();
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tvSubject = findViewById(R.id.Subject);
        tvNilaitugas = findViewById(R.id.nilaiTugas);
        tvNilaiuts = findViewById(R.id.nilaiUTS);
        tvNilaiuas = findViewById(R.id.nilaiUAS);

        userID = mAuth.getCurrentUser().getUid();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent();
                setResult(RESULT_OK, intentBack);
                finish();
                //Transisi (tutup GradeDetail) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        gradeId = pindah.getStringExtra("gradeID");
        semester = pindah.getStringExtra("semester");

        DocumentReference documentReference = fStore.collection("user").document(userID).collection("course").document("semester").collection(semester).document(gradeId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    tvSubject.setText(documentSnapshot.getString("subject"));
                    tvNilaitugas.setText("" + documentSnapshot.getLong("nilaitugas").intValue());
                    tvNilaiuts.setText("" + documentSnapshot.getLong("nilaiuts").intValue());
                    tvNilaiuas.setText("" + documentSnapshot.getLong("nilaiuas").intValue());
                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent();
        setResult(RESULT_OK, intentBack);
        finish();
        //Transisi (tutup GradeDetail) dari kiri ke kanan
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}