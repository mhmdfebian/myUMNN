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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Nullable;

import id.ac.umn.myumn.Menu;
import id.ac.umn.myumn.Notification;
import id.ac.umn.myumn.R;

public class Grade extends AppCompatActivity implements GradeAdapter.OnListItemClick {

    TextView ips;
    Button btnMenu, btnNotif;
    Spinner spinnerGrade;
    RecyclerView lvGrade;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;
    private static final String KEY_NILAIUTS = "nilaiuts";
    private static final String KEY_NILAIUAS = "nilaiuas";
    private static final String KEY_NILAITUGAS = "nilaitugas";
    private static final String KEY_SKS = "sks";
    double total, number,ips1, totalips = 0, totaltotalips, totalsks = 0;
    DecimalFormat REAL_FORMATTER = new DecimalFormat("0.00");

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
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Grade.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Grade.this, R.layout.spinner, getResources().getStringArray(R.array.semester));
        myAdapter.setDropDownViewResource(R.layout.spinner_drodown);
        spinnerGrade.setAdapter(myAdapter);

        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = spinnerGrade.getItemAtPosition(position).toString();

                if (selectedItem.equals("1st Semester")) {
                    totaltotalips = 0;
                   grade(selectedItem);


                } else if (selectedItem.equals("2nd Semester")) {
                    totaltotalips = 0;
                   grade(selectedItem);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void grade(final String selectedItem) {

        final String semester = selectedItem;
        Query query = fStore.collection("user").document(userID).collection("grade").document("semester").collection(selectedItem);
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
        adapter = new GradeAdapter(options,this);

        lvGrade.setHasFixedSize(true);
        lvGrade.setLayoutManager(new LinearLayoutManager(this));
        lvGrade.setAdapter(adapter);
        adapter.startListening();

        fStore.collection("user").document(userID).collection("grade").document("semester").collection(semester)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                fStore.collection("user").document(userID).collection("grade").document("semester").collection(semester).document(d.getId())
                                        .addSnapshotListener(Grade.this, new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot snapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                                if(snapshot.exists()){
                                                    int nilaiuts = snapshot.getLong(KEY_NILAIUTS).intValue();
                                                    int nilaiuas = snapshot.getLong(KEY_NILAIUAS).intValue();
                                                    int nilaitugas = snapshot.getLong(KEY_NILAITUGAS).intValue();
                                                    int sks = snapshot.getLong(KEY_SKS).intValue();

                                                    total = (nilaiuts * 0.3) + (nilaitugas * 0.3) + (nilaiuas * 0.4);
                                                    number = 0;

                                                    if (total >= 85.00){
                                                        number = 4.00;
                                                    }
                                                    else if (total >= 80.00){
                                                        number = 4.00;
                                                    }
                                                    else if (total >= 75.00){
                                                        number = 4.00;
                                                    }
                                                    else if (total >= 70.00){
                                                        number = 4.00;
                                                    }
                                                    else if (total >= 65.00){
                                                        number = 4.00;
                                                    }
                                                    else if (total >= 60.00){
                                                        number = 4.00;
                                                    }
                                                    else if (total >= 55.00){
                                                        number = 4.00;
                                                    }
                                                    else if (total >= 45.00){
                                                        number = 4.00;
                                                    }
                                                    else if (total >= 0){
                                                        number = 4.00;
                                                    }

                                                    ips1 = number * sks;

                                                    totalips = ips1 + totalips;
                                                    totalsks = sks + totalsks;
                                                    totaltotalips =  totalips / totalsks;

                                                    ips.setText(REAL_FORMATTER.format(totaltotalips));
                                                }

                                            }
                                });
                            }
                        }
                        else ips.setText(REAL_FORMATTER.format(totaltotalips));
                    }
                });
    }


    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke activity Login
    }



    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
//        Intent pindah = new Intent(Grade.this,GradeDetail.class);
//        startActivity(pindah.putExtra("courseID", snapshot.getId()));
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }
}