package id.ac.umn.myumn.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import id.ac.umn.myumn.Attendance.Attendance;
import id.ac.umn.myumn.Course.Course;
import id.ac.umn.myumn.Dashboard.Dashboard;
import id.ac.umn.myumn.Enrollment.Enrollment;
import id.ac.umn.myumn.Event.Event;
import id.ac.umn.myumn.Grade.Grade;
import id.ac.umn.myumn.Login.Login;
import id.ac.umn.myumn.Profile.Profile;
import id.ac.umn.myumn.R;
import id.ac.umn.myumn.Schedule.Schedule;

public class Menu extends AppCompatActivity {

    Button btnClose, btnSchedule, btnDashboard, btnProfile, btnCourse, btnEvent, btnGrade, btnAttendance, btnEnrollment, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnClose = findViewById(R.id.btnClose);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnProfile = findViewById(R.id.btnProfile);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnCourse = findViewById(R.id.btnCourse);
        btnEvent = findViewById(R.id.btnEvent);
        btnGrade = findViewById(R.id.btnGrade);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnEnrollment = findViewById(R.id.btnEnrollment);
        btnLogout = findViewById(R.id.btnLogout);

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Dashboard.class);
                startActivity(i);
                finish();
                //Transisi (masuk Dashboard) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Profile.class);
                startActivity(i);
                finish();
                //Transisi (masuk Profile) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                ;
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Schedule.class);
                startActivity(i);
                finish();
                //Transisi (masuk Schedule) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Course.class);
                startActivity(i);
                finish();
                //Transisi (masuk Course) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Event.class);
                startActivity(i);
                finish();
                //Transisi (masuk Event) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Grade.class);
                startActivity(i);
                finish();
                //Transisi (masuk Grade) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Attendance.class);
                startActivity(i);
                finish();
                //Transisi (masuk Attendance) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Enrollment.class);
                startActivity(i);
                finish();
                //Transisi (masuk Enrollment) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent();
                setResult(RESULT_OK, intentBack);
                finish();
                //Transisi (close menu) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finishAffinity();
                //Transisi (logout) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent();
        setResult(RESULT_OK, intentBack);
        finish();
        //Transisi (close menu) dari kanan ke kiri
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
