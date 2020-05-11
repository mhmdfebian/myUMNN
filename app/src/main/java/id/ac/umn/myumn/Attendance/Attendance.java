package id.ac.umn.myumn.Attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import id.ac.umn.myumn.Menu.Menu;
import id.ac.umn.myumn.Notification.Notification;
import id.ac.umn.myumn.R;

public class Attendance extends AppCompatActivity implements AttendanceAdapter.OnListItemClick {

    Button btnMenu, btnNotif;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    RecyclerView lvAttendance;
    Spinner spinnerAttendance;
    String userID, first, selectedItem;

    private AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);
        lvAttendance = findViewById(R.id.listviewAttendance);

        userID = mAuth.getCurrentUser().getUid();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Attendance.this, Menu.class);
                startActivity(i);
                //Transisi (buka Menu sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Attendance.this, Notification.class);
                startActivity(i);
                //Transisi (buka Notification sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        spinnerAttendance = findViewById(R.id.spinnerAttendance);

        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Attendance.this, R.layout.spinner, getResources().getStringArray(R.array.semester));
        myAdapter.setDropDownViewResource(R.layout.spinner_drodown);
        spinnerAttendance.setAdapter(myAdapter);

        // Mengatur selected item sesuai dengan yang ada di database
        DocumentReference documentReference = fStore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    first = documentSnapshot.getString("semester");
                    spinnerAttendance.setSelection(myAdapter.getPosition(first));
                }
            }
        });

        spinnerAttendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedItem = spinnerAttendance.getItemAtPosition(position).toString();
                if (selectedItem.equals("1st Semester")) {
                    semesterAttendance(selectedItem);

                } else if (selectedItem.equals("2nd Semester")) {
                    semesterAttendance(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void semesterAttendance(String selectedItem) {
        Query query = fStore.collection("user").document(userID).collection("course").document("semester").collection(selectedItem);
        FirestoreRecyclerOptions<AttendanceModel> options = new FirestoreRecyclerOptions.Builder<AttendanceModel>()
                .setQuery(query, new SnapshotParser<AttendanceModel>() {
                    @NonNull
                    @Override
                    public AttendanceModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        AttendanceModel attendanceModel = snapshot.toObject(AttendanceModel.class);
                        String attendanceID = snapshot.getId();
                        attendanceModel.setAttendanceid(attendanceID);
                        return attendanceModel;
                    }
                })
                .build();
        adapter = new AttendanceAdapter(options, this);

        lvAttendance.setHasFixedSize(true);
        lvAttendance.setLayoutManager(new LinearLayoutManager(this));
        lvAttendance.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        //Disable back button
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        //pilih item yang akan masuk ke AttendanceDetail
        Intent pindah = new Intent(Attendance.this, AttendanceDetail.class);
        startActivity(pindah
                .putExtra("semester", selectedItem)
                .putExtra("attendanceID", snapshot.getId()));
    }
}
