package id.ac.umn.myumn.Course;

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

import id.ac.umn.myumn.CourseActivity;
import id.ac.umn.myumn.Menu;
import id.ac.umn.myumn.Notification;
import id.ac.umn.myumn.R;

public class Course extends AppCompatActivity implements CourseAdapter.OnListItemClick {

    Button btnMenu, btnNotif;
    Spinner spinnerCourse;
    RecyclerView lvCourse;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID;

    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);
        lvCourse = findViewById(R.id.listviewCourse);

        userID = mAuth.getCurrentUser().getUid();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Course.this, Menu.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Course.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        spinnerCourse = findViewById(R.id.spinnerCourse);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Course.this, R.layout.spinner, getResources().getStringArray(R.array.semester));
        myAdapter.setDropDownViewResource(R.layout.spinner_drodown);
        spinnerCourse.setAdapter(myAdapter);

        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = spinnerCourse.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Query query = fStore.collection("user").document(userID).collection("course");
        FirestoreRecyclerOptions<CourseModel>options = new FirestoreRecyclerOptions.Builder<CourseModel>()
                .setQuery(query, new SnapshotParser<CourseModel>() {
                    @NonNull
                    @Override
                    public CourseModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        CourseModel courseModel = snapshot.toObject(CourseModel.class);
                        String courseID = snapshot.getId();
                        courseModel.setCourseid(courseID);
                        return courseModel;
                    }
                })
                .build();
        adapter = new CourseAdapter(options,this);

        lvCourse.setHasFixedSize(true);
        lvCourse.setLayoutManager(new LinearLayoutManager(this));
        lvCourse.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke activity Login
    }


    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Intent pindah = new Intent(Course.this, CourseActivity.class);
        startActivity(pindah.putExtra("courseID", snapshot.getId()));
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