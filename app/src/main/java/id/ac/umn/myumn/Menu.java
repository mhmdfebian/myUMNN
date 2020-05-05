package id.ac.umn.myumn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import id.ac.umn.myumn.Attendance.Attendance;
import id.ac.umn.myumn.Course.Course;
import id.ac.umn.myumn.Dashboard.Dashboard;
import id.ac.umn.myumn.Event.Event;

public class Menu extends AppCompatActivity {

    Button btnClose, btnSchedule, btnDashboard, btnProfile, btnCourse, btnEvent, btnGrade, btnAttendance, btnLogout;

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
        btnLogout = findViewById(R.id.btnLogout);

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Dashboard.class);
                startActivity(i);
                finish();
                //Transisi (masuk menu) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Profile.class);
                startActivity(i);
                finish();
                //Transisi (masuk menu) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);;
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Schedule.class);
                startActivity(i);
                finish();
                //Transisi (masuk menu) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Course.class);
                startActivity(i);
                finish();
                //Transisi (masuk menu) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Event.class);
                startActivity(i);
                finish();
                //Transisi (masuk menu) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Grade.class);
                startActivity(i);
                finish();
                //Transisi (masuk menu) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Attendance.class);
                startActivity(i);
                finish();
                //Transisi (masuk menu) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent();
                setResult(RESULT_OK, intentBack);
                finish();
                //Transisi (keluar) dari kanan ke kiri
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
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
        //Transisi (keluar) dari kanan ke kiri
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
