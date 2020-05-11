package id.ac.umn.myumn.Grade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Nullable;

import id.ac.umn.myumn.Menu.Menu;
import id.ac.umn.myumn.Notification.Notification;
import id.ac.umn.myumn.R;

public class Grade extends AppCompatActivity implements GradeAdapter.OnListItemClick {

    Button btnMenu, btnNotif;
    DecimalFormat REAL_FORMATTER = new DecimalFormat("0.00");
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    RecyclerView lvGrade;
    Spinner spinnerGrade;
    String userID, selectedItem, first;
    TextView ips;

    double total, number, ips1, totalips = 0, totaltotalips, totalsks = 0;

    private static final String KEY_NILAIUTS = "nilaiuts";
    private static final String KEY_NILAIUAS = "nilaiuas";
    private static final String KEY_NILAITUGAS = "nilaitugas";
    private static final String KEY_SKS = "sks";

    private GradeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);

        lvGrade = findViewById(R.id.listviewGrade);

        ips = findViewById(R.id.ips);

        userID = mAuth.getCurrentUser().getUid();

        spinnerGrade = findViewById(R.id.spinnerGrade);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Grade.this, Menu.class);
                startActivity(i);
                //Transisi (buka Menu sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Grade.this, Notification.class);
                startActivity(i);
                //Transisi (buka Notification sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Grade.this, R.layout.spinner, getResources().getStringArray(R.array.semester));
        myAdapter.setDropDownViewResource(R.layout.spinner_drodown);
        spinnerGrade.setAdapter(myAdapter);

        DocumentReference documentReference = fStore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    first = documentSnapshot.getString("semester");
                    spinnerGrade.setSelection(myAdapter.getPosition(first));
                }
            }
        });

        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedItem = spinnerGrade.getItemAtPosition(position).toString();

                if (selectedItem.equals("1st Semester")) {
                    totaltotalips = 0;
                    grade(selectedItem);
                    lvGrade.setAdapter(adapter);

                } else if (selectedItem.equals("2nd Semester")) {
                    totaltotalips = 0;
                    grade(selectedItem);
                    lvGrade.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //Disable back button
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        //pilih item yang akan masuk ke GradeDetail
        Intent pindah = new Intent(Grade.this, GradeDetail.class);
        startActivity(pindah
                .putExtra("gradeID", snapshot.getId())
                .putExtra("semester", selectedItem));
    }

    private void grade(final String selectedItem) {

        final String semester = selectedItem;

        fStore.collection("user").document(userID).collection("course").document("semester").collection(semester)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e == null) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    fStore.collection("user").document(userID).collection("course").document("semester").collection(semester).document(d.getId())
                                            .addSnapshotListener(Grade.this, new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                                    if (e == null) {
                                                        if (snapshot.exists()) {
                                                            int nilaiuts = snapshot.getLong(KEY_NILAIUTS).intValue();
                                                            int nilaiuas = snapshot.getLong(KEY_NILAIUAS).intValue();
                                                            int nilaitugas = snapshot.getLong(KEY_NILAITUGAS).intValue();
                                                            int sks = snapshot.getLong(KEY_SKS).intValue();

                                                            total = (nilaiuts * 0.3) + (nilaitugas * 0.3) + (nilaiuas * 0.4);
                                                            number = 0;

                                                            if (total >= 85.00) {
                                                                number = 4.00;
                                                            } else if (total >= 80.00) {
                                                                number = 3.70;
                                                            } else if (total >= 75.00) {
                                                                number = 3.30;
                                                            } else if (total >= 70.00) {
                                                                number = 3.00;
                                                            } else if (total >= 65.00) {
                                                                number = 2.70;
                                                            } else if (total >= 60.00) {
                                                                number = 2.30;
                                                            } else if (total >= 55.00) {
                                                                number = 2.00;
                                                            } else if (total >= 45.00) {
                                                                number = 1.00;
                                                            } else if (total >= 0) {
                                                                number = 0.00;
                                                            }

                                                            ips1 = number * sks;
                                                            totalips = ips1 + totalips;
                                                            totalsks = sks + totalsks;
                                                            totaltotalips = totalips / totalsks;

                                                            ips.setText(REAL_FORMATTER.format(totaltotalips));
                                                        }
                                                    }
                                                }
                                            });
                                    totalips = 0;
                                    ips1 = 0;
                                    totalsks = 0;
                                    totaltotalips = 0;
                                }
                            } else ips.setText(REAL_FORMATTER.format(totaltotalips));
                        }
                    }
                });

        Query query = fStore.collection("user").document(userID).collection("course").document("semester").collection(selectedItem);
        FirestoreRecyclerOptions<GradeModel> options = new FirestoreRecyclerOptions.Builder<GradeModel>()
                .setQuery(query, new SnapshotParser<GradeModel>() {
                    @NonNull
                    @Override
                    public GradeModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        GradeModel gradeModel = snapshot.toObject(GradeModel.class);
                        String gradeID = snapshot.getId();
                        gradeModel.setGradeid(gradeID);
                        return gradeModel;
                    }
                })
                .build();

        adapter = new GradeAdapter(options, this);
        lvGrade.setHasFixedSize(true);
        lvGrade.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }
}


