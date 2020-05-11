package id.ac.umn.myumn.Dashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.ac.umn.myumn.Attendance.Attendance;
import id.ac.umn.myumn.Course.Course;
import id.ac.umn.myumn.Enrollment.Enrollment;
import id.ac.umn.myumn.Event.Event;
import id.ac.umn.myumn.Grade.Grade;
import id.ac.umn.myumn.Menu.Menu;
import id.ac.umn.myumn.Notification.Notification;
import id.ac.umn.myumn.R;
import id.ac.umn.myumn.Schedule.Schedule;

public class Dashboard extends AppCompatActivity {

    Button btnMenu, btnNotif, btnSchedule, btnCourse, btnEvent, btnGrade, btnAttendance, btnEnrollment;
    DashboardAdapter adapter;
    Date date;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    List<DashboardModel> models;
    String userID, semester;
    TextView Name;
    ViewPager viewPager;

    private static final String KEY_TITLE = "title";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_TIME = "time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);


        btnSchedule = findViewById(R.id.btnSchedule);
        btnCourse = findViewById(R.id.btnCourse);
        btnEvent = findViewById(R.id.btnEvent);
        btnGrade = findViewById(R.id.btnGrade);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnEnrollment = findViewById(R.id.btnEnrollment);

        Name = findViewById(R.id.Name);

        models = new ArrayList<>();

        adapter = new DashboardAdapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(0, 0, 120, 0);

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

        btnGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Grade.class);
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

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Attendance.class);
                startActivity(i);
            }
        });

        btnEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Enrollment.class);
                startActivity(i);
            }
        });

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
            //Mengambil isi profile di database
            DocumentReference documentReference = fStore.collection("user").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        Name.setText("Hello, " + documentSnapshot.getString("nickname"));
                        semester = documentSnapshot.getString("semester");
                        //Mengambil document course atau matkul
                        fStore.collection("user").document(userID).collection("course").document("semester").collection(semester)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        if (e == null) {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                for (DocumentSnapshot d : list) {
                                                    fStore.collection("user").document(userID).collection("course").document("semester").collection(semester).document(d.getId()).collection("topics")
                                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onEvent(@Nullable QuerySnapshot querySnap, @Nullable FirebaseFirestoreException e) {
                                                                    if (e == null) {
                                                                        if (!querySnap.isEmpty()) {
                                                                            //Mengambil isi topics
                                                                            List<DocumentSnapshot> list = querySnap.getDocuments();
                                                                            for (DocumentSnapshot x : list) {
                                                                                if (!x.getString(KEY_TITLE).equals("Materi")) {
                                                                                    DashboardModel y = x.toObject(DashboardModel.class);
                                                                                    models.add(y);
                                                                                    String time = x.getString(KEY_TIME);
                                                                                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                                                                                    try {
                                                                                        date = df.parse(time);
                                                                                    } catch (ParseException ex) {
                                                                                        ex.printStackTrace();
                                                                                    }
                                                                                    //Mengambil waktu di database untuk notif
                                                                                    Calendar calendar = Calendar.getInstance();
                                                                                    calendar.set(Calendar.HOUR_OF_DAY, date.getHours());
                                                                                    calendar.set(Calendar.MINUTE, date.getMinutes());
                                                                                    calendar.set(Calendar.SECOND, 0);

                                                                                    String title = x.getString(KEY_TITLE);
                                                                                    String subject = x.getString(KEY_SUBJECT);
                                                                                    startAlarm(calendar, title, subject);
                                                                                }
                                                                            }
                                                                            adapter.notifyDataSetChanged();
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    }
                                });

                    } else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }
            });
        }
    }

    private void startAlarm(Calendar calendar, String title, String subject) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title", title)
                .putExtra("subject", subject);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke activity Login
    }
}
