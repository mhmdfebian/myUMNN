package id.ac.umn.myumn.Dashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.myumn.Course.Course;
import id.ac.umn.myumn.Event.Event;
import id.ac.umn.myumn.Menu;
import id.ac.umn.myumn.Notification;
import id.ac.umn.myumn.R;
import id.ac.umn.myumn.Schedule;
import id.ac.umn.myumn.Skkm;

public class Dashboard extends AppCompatActivity {

    TextView Name;
    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    Button btnMenu, btnNotif, btnEvent, btnSchedule, btnCourse;
    private static final String KEY_TITLE = "title";


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);

        btnSchedule = findViewById(R.id.btnSchedule);
        btnEvent = findViewById(R.id.btnEvent);
        btnCourse = findViewById(R.id.btnCourse);

        Name = findViewById(R.id.Name);

        models = new ArrayList<>();

        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(0, 0, 120, 0);

        userID = mAuth.getCurrentUser().getUid();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Menu.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Schedule.class);
                startActivity(i);
            }
        });

        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Course.class);
                startActivity(i);
            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Event.class);
                startActivity(i);
            }
        });


        DocumentReference documentReference = fStore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    Name.setText("Hello, " + documentSnapshot.getString("nickname"));
                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

        fStore.collection("user").document(userID).collection("course")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                fStore.collection("user").document(userID).collection("course").document(d.getId()).collection("topics")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@javax.annotation.Nullable QuerySnapshot querySnap, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                                if (!querySnap.isEmpty()) {
                                                    List<DocumentSnapshot> list = querySnap.getDocuments();
                                                    for (DocumentSnapshot x : list) {
                                                        if (!x.getString(KEY_TITLE).equals("Materi")) {
                                                            Model y = x.toObject(Model.class);
                                                            models.add(y);
                                                        }
//                                                        if (!x.getString(KEY_TITLE).equals("Materi")) {
//
//                                                        }
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke activity Login
    }
}
