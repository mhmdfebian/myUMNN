package id.ac.umn.myumn.Event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.HashMap;
import java.util.Map;

import id.ac.umn.myumn.Course.Course;
import id.ac.umn.myumn.Menu;
import id.ac.umn.myumn.Notification;
import id.ac.umn.myumn.R;

public class Event extends AppCompatActivity implements EventAdapter.OnListItemClick {

    Button btnMenu, btnNotif, btnAdd;
    TextView event;
    Spinner spinnerEvent;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID, eventID, eventIDD, eventName, eventDate, eventTime, eventLocation, eventDesc;
    RecyclerView lvEvent;
    private EventAdapter adapter;
    private EventAddedAdapter adapteradded;
    Query query;
    EventModel eventModel1;
    boolean upadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);
        btnAdd = findViewById(R.id.btnAdd);

        lvEvent = findViewById(R.id.listviewEvent);
        event = findViewById(R.id.Event);

        userID = mAuth.getCurrentUser().getUid();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Event.this, Menu.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Event.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        upadd = false;

        spinnerEvent = findViewById(R.id.spinnerEvent);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Event.this, R.layout.spinner, getResources().getStringArray(R.array.event));
        myAdapter.setDropDownViewResource(R.layout.spinner_drodown);
        spinnerEvent.setAdapter(myAdapter);

        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    upadd = false;
                    upcomming();
                    lvEvent.setAdapter(adapter);

                }
                if (position == 1) {
                    upadd = true;
                    added();
                    lvEvent.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





    }

    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke activity Login
    }


    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Intent pindah = new Intent(Event.this, EventDetail.class);
        startActivity(pindah
                .putExtra("eventID", snapshot.getId())
                .putExtra("eventName", snapshot.getString("eventname"))
                .putExtra("eventDate", snapshot.getString("eventdate"))
                .putExtra("eventTime", snapshot.getString("eventtime"))
                .putExtra("eventLocation", snapshot.getString("eventlocation"))
                .putExtra("eventDesc", snapshot.getString("eventdesc"))
                .putExtra("upadd", upadd));
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void upcomming() {
        query = fStore.collection("event");
        FirestoreRecyclerOptions<EventModel> options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query, new SnapshotParser<EventModel>() {
                    @NonNull
                    @Override
                    public EventModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        EventModel eventModel = snapshot.toObject(EventModel.class);
                        eventID = snapshot.getId();
                        eventModel.setEventid(eventID);
                        return eventModel;
                    }
                })
                .build();
        adapter = new EventAdapter(options, this);
        lvEvent.setHasFixedSize(true);
        lvEvent.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();

    }

    public void added() {
        query = fStore.collection("user").document(userID).collection("events");
        FirestoreRecyclerOptions<EventModel> options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query, new SnapshotParser<EventModel>() {
                    @NonNull
                    @Override
                    public EventModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        EventModel eventModel = snapshot.toObject(EventModel.class);
                        String eventID = snapshot.getId();
                        eventModel.setEventid(eventID);
                        return eventModel;
                    }
                })
                .build();
        adapter = new EventAdapter(options, this);
        lvEvent.setHasFixedSize(true);
        lvEvent.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }


}
