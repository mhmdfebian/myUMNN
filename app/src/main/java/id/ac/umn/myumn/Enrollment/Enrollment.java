package id.ac.umn.myumn.Enrollment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import id.ac.umn.myumn.Menu.Menu;
import id.ac.umn.myumn.Notification.Notification;
import id.ac.umn.myumn.R;

public class Enrollment extends AppCompatActivity implements EnrollmentAdapter.OnListItemClick {

    Button btnMenu, btnNotif;
    TextView event;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID, courseID;
    RecyclerView lvEnrollment;
    private EnrollmentAdapter adapter;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);


        lvEnrollment = findViewById(R.id.listviewEnrollment);
        event = findViewById(R.id.Event);

        userID = mAuth.getCurrentUser().getUid();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Enrollment.this, Menu.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Enrollment.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        query = fStore.collection("enrollment");
        FirestoreRecyclerOptions<EnrollmentModel> options = new FirestoreRecyclerOptions.Builder<EnrollmentModel>()
                .setQuery(query, new SnapshotParser<EnrollmentModel>() {
                    @NonNull
                    @Override
                    public EnrollmentModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        EnrollmentModel EnrollmentModel = snapshot.toObject(EnrollmentModel.class);
                        courseID = snapshot.getId();
                        EnrollmentModel.setCourseid(courseID);
                        return EnrollmentModel;
                    }
                })
                .build();
        adapter = new EnrollmentAdapter(options, this);
        lvEnrollment.setAdapter(adapter);
        lvEnrollment.setHasFixedSize(true);
        lvEnrollment.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke activity Login
    }


    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Intent pindah = new Intent(Enrollment.this, EnrollmentDetail.class);
        startActivity(pindah
                .putExtra("courseID", snapshot.getId())
                .putExtra("subject", snapshot.getString("subject"))
                .putExtra("day", snapshot.getString("day"))
                .putExtra("time", snapshot.getString("time"))
                .putExtra("timeend", snapshot.getString("timeend")));
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

}
