package id.ac.umn.myumn;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notification extends AppCompatActivity {

    Button btnClose;
    TextView Atas, Bawah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnClose = findViewById(R.id.btnClose);

        Atas = findViewById(R.id.Atas);
        Bawah = findViewById(R.id.Bawah);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent();
                setResult(RESULT_OK, intentBack);
                finish();
                //Transisi (keluar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        Intent pindah = getIntent();
        String title = pindah.getStringExtra("title");

        Atas.setText("Reminder");
        Bawah.setText(title);

    }

    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent();
        setResult(RESULT_OK, intentBack);
        finish();
        //Transisi (keluar) dari kiri ke kanan
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
