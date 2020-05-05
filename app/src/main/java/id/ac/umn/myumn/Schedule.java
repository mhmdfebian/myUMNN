package id.ac.umn.myumn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {

    Button btnMenu, btnNotif, btnSave;
    Spinner spinnerSchedule;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID, text;
    ImageView imgSchedule;
    private StorageReference mStorageRef;
    private static final String TEXT_KEY = "14.jpg";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);
        imgSchedule = findViewById(R.id.imgSchedule);
        btnSave = findViewById(R.id.btnSave);

        userID = mAuth.getCurrentUser().getUid();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Schedule.this, Menu.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Schedule.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        spinnerSchedule = findViewById(R.id.spinnerSchedule);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Schedule.this, R.layout.spinner, getResources().getStringArray(R.array.schedule));
        myAdapter.setDropDownViewResource(R.layout.spinner_drodown);
        spinnerSchedule.setAdapter(myAdapter);

        spinnerSchedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = spinnerSchedule.getItemAtPosition(position).toString();

                if (selectedItem.equals("Weekly Schedule")) {
                    schedule(selectedItem);

                } else if (selectedItem.equals("Exam Schedule")) {
                    schedule(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void schedule(String selectedItem) {
        DocumentReference documentReference = fStore.collection("user").document(userID).collection("schedule").document(selectedItem);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    text = documentSnapshot.getString("filename");
                    Log.d("tag", text);
                    mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://myumn-7ff78.appspot.com/schedule").child(text);
                    try {
                        final File file = File.createTempFile("image", "jpeg");
                        mStorageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                imgSchedule.setImageBitmap(bitmap);
                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ContextCompat.checkSelfPermission(Schedule.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                            String NamaFolder = Environment.getExternalStorageDirectory() + "/Images/";
                                            File directory = new File(NamaFolder);
                                            if (!directory.exists()) {
                                                directory.mkdirs();
                                            }
                                            String namafile = text+".jpg";
                                            String namaaa = NamaFolder + namafile;
                                            try {
                                                outputStream = new FileOutputStream(namaaa, true);
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                                Toast.makeText(Schedule.this, "Schedule Saved", Toast.LENGTH_SHORT).show();
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            // Request permission from the user
                                            ActivityCompat.requestPermissions(Schedule.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception f) {
                                Toast.makeText(Schedule.this, "Image gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException f) {
                        f.printStackTrace();
                    }

                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        //Biar ga bisa mencet back yang bakal ngarahin ke activity Login
    }
}
