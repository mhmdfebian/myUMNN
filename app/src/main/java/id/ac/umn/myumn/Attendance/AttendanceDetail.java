package id.ac.umn.myumn.Attendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.myumn.Course.CourseAdapter;
import id.ac.umn.myumn.Course.CourseModel;
import id.ac.umn.myumn.R;

public class AttendanceDetail extends AppCompatActivity {

    Button btnClose;
    RecyclerView lvAttendance;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID, attendanceId;
    TextView tvAttendance;


    private FirestoreRecyclerAdapter adapter;

    private List<String> nameList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);

        btnClose = findViewById(R.id.btnClose);

        Intent pindah = getIntent();
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        tvAttendance = findViewById(R.id.Attendance);


        lvAttendance = findViewById(R.id.listviewAttendanceDetail);


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


        attendanceId = pindah.getStringExtra("attendanceID");
        tvAttendance.setText(attendanceId);


        DocumentReference documentReference = fStore.collection("user").document(userID).collection("attendance").document(attendanceId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    tvAttendance.setText(documentSnapshot.getString("subject"));
                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });


        Query query = fStore.collection("user").document(userID).collection("attendance").document(attendanceId).collection("attendances");
        FirestoreRecyclerOptions<AttendanceModel> options = new FirestoreRecyclerOptions.Builder<AttendanceModel>()
                .setQuery(query, AttendanceModel.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<AttendanceModel, AttendanceViewHolder>(options) {
            @NonNull
            @Override
            public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendance_detail, parent, false);
                return new AttendanceViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position, @NonNull AttendanceModel model) {
                holder.tvWeek.setText(model.getWeek());
                holder.tvPresent.setText(model.getPresent());
                holder.tvDate.setText(model.getDate());

            }
        };

        lvAttendance.setHasFixedSize(true);
        lvAttendance.setLayoutManager(new LinearLayoutManager(this));
        lvAttendance.setAdapter(adapter);
    }


    private class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvWeek;
        private TextView tvPresent;
        private TextView tvDate;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWeek = itemView.findViewById(R.id.week);
            tvPresent = itemView.findViewById(R.id.present);
            tvDate = itemView.findViewById(R.id.date);
        }
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

    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent();
        setResult(RESULT_OK, intentBack);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
