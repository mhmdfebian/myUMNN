package id.ac.umn.myumn.Event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import id.ac.umn.myumn.R;

public class EventDetail extends AppCompatActivity {

    Button btnClose, btnAdd;

    FirebaseFirestore fStore;
    FirebaseAuth mAuth;

    String userID, eventID, eventName, eventDate, eventTime, eventLocation, eventDesc;

    TextView tvName, tvDate, tvDesc, tvLocation;

    boolean upadd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        btnClose = findViewById(R.id.btnClose);
        btnAdd = findViewById(R.id.btnAdd);


        tvName = findViewById(R.id.eventName);
        tvDesc = findViewById(R.id.desc);
        tvDate = findViewById(R.id.date);
        tvLocation = findViewById(R.id.location);

        Intent pindah = getIntent();
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


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



        eventID = pindah.getStringExtra("eventID");
        eventName = pindah.getStringExtra("eventName");
        eventDate = pindah.getStringExtra("eventDate");
        eventTime = pindah.getStringExtra("eventTime");
        eventLocation = pindah.getStringExtra("eventLocation");
        eventDesc = pindah.getStringExtra("eventDesc");
        upadd = pindah.getBooleanExtra("upadd",upadd);

        tvName.setText(eventName);
        tvDesc.setText(eventDesc);
        tvDate.setText(eventDate);
        tvLocation.setText(eventLocation);

        if(upadd) {
            btnAdd.setBackgroundResource(R.drawable.close);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference docRef = fStore.collection("user").document(userID).collection("events").document(eventID);
                    docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EventDetail.this, eventName + " berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                Intent intentBack = new Intent();
                                setResult(RESULT_OK, intentBack);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        }
                    });

                }
            });

        }
        else{
            btnAdd.setBackgroundResource(R.drawable.plus);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference docRef = fStore.collection("user").document(userID).collection("events").document(eventID);
                    Map<String, Object> event = new HashMap<>();
                    event.put("eventname", eventName);
                    event.put("eventdesc", eventDesc);
                    event.put("eventdate", eventDate);
                    event.put("eventlocation", eventLocation);
                    event.put("eventtime", eventTime);
                    docRef.set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EventDetail.this, eventName + " berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent();
        setResult(RESULT_OK, intentBack);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
