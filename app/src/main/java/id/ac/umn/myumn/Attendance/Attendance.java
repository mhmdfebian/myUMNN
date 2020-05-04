package id.ac.umn.myumn.Attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import id.ac.umn.myumn.Course.CourseModel;
import id.ac.umn.myumn.CourseActivity;
import id.ac.umn.myumn.Menu;
import id.ac.umn.myumn.Notification;
import id.ac.umn.myumn.R;

public class Attendance extends AppCompatActivity implements AttendanceAdapter.OnListItemClick {

    Button btnMenu, btnNotif;
    Spinner spinnerAttendance;
    RecyclerView lvAttendance;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;

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
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Attendance.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        spinnerAttendance = findViewById(R.id.spinnerAttendance);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Attendance.this, R.layout.spinner, getResources().getStringArray(R.array.semester));
        myAdapter.setDropDownViewResource(R.layout.spinner_drodown);
        spinnerAttendance.setAdapter(myAdapter);

        spinnerAttendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = spinnerAttendance.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Query query = fStore.collection("user").document(userID).collection("attendance");
        FirestoreRecyclerOptions<AttendanceModel>options = new FirestoreRecyclerOptions.Builder<AttendanceModel>()
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
        adapter = new AttendanceAdapter(options,this);

        lvAttendance.setHasFixedSize(true);
        lvAttendance.setLayoutManager(new LinearLayoutManager(this));
        lvAttendance.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke activity Login
    }


    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Intent pindah = new Intent(Attendance.this, AttendanceDetail.class);
        startActivity(pindah.putExtra("attendanceID", snapshot.getId()));
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
