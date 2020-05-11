package id.ac.umn.myumn.Course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Course extends AppCompatActivity implements CourseAdapter.OnListItemClick {

    Button btnMenu, btnNotif;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    Query query;
    RecyclerView lvCourse;
    Spinner spinnerCourse;
    String userID, selectedItem, first;
    TextView course;

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

        course = findViewById(R.id.Course);

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

        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Course.this, R.layout.spinner, getResources().getStringArray(R.array.semester));
        myAdapter.setDropDownViewResource(R.layout.spinner_drodown);
        spinnerCourse.setAdapter(myAdapter);

        DocumentReference documentReference = fStore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    first = documentSnapshot.getString("semester");
                    spinnerCourse.setSelection(myAdapter.getPosition(first));
                }
            }
        });

        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedItem = spinnerCourse.getItemAtPosition(position).toString();

                if (selectedItem.equals("1st Semester")) {
                    semesterCourse(selectedItem);

                } else if (selectedItem.equals("2nd Semester")) {
                    semesterCourse(selectedItem);
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

    public void semesterCourse(String selectedItem) {
        query = fStore.collection("user").document(userID).collection("course").document("semester").collection(selectedItem);
        FirestoreRecyclerOptions<CourseModel> options = new FirestoreRecyclerOptions.Builder<CourseModel>()
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
        adapter = new CourseAdapter(options, this);
        lvCourse.setHasFixedSize(true);
        lvCourse.setLayoutManager(new LinearLayoutManager(this));
        lvCourse.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        //pilih item yang akan masuk ke CourseDetail
        Intent pindah = new Intent(Course.this, CourseDetail.class);
        startActivity(pindah
                .putExtra("semester", selectedItem)
                .putExtra("courseID", snapshot.getId()));
    }
}
