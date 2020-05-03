package id.ac.umn.myumn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Skkm extends AppCompatActivity {
    Button btnMenu, btnNotif;
    TextView textViewPM, textViewBM , textViewOPK, textViewIP;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    ProgressBar pbSKKM;

    private static final String KEY_BM = "minatbakat";
    private static final String KEY_IP = "ilmupenalaran";
    private static final String KEY_OPK= "organisasi";
    private static final String KEY_PM = "pengabdianmasyarakat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skkm);

        btnMenu = findViewById(R.id.btnMenu);
        btnNotif = findViewById(R.id.btnNotif);

        pbSKKM = findViewById(R.id.pbSKKM);

        textViewBM = findViewById(R.id.tvBM);
        textViewIP = findViewById(R.id.tvIP);
        textViewOPK = findViewById(R.id.tvOPK);
        textViewPM = findViewById(R.id.tvPM);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Skkm.this, Menu.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finishAffinity();
            }
        });

        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Skkm.this, Notification.class);
                startActivity(i);
                //Transisi (buka sidebar) dari kiri ke kanan
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        DocumentReference documentReference = fStore.collection("user").document(userID).collection("skkm").document("skkm");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    int BM =  documentSnapshot.getLong(KEY_BM).intValue();
                    int IP =  documentSnapshot.getLong(KEY_IP).intValue();
                    int OPK =  documentSnapshot.getLong(KEY_OPK).intValue();
                    int PM =  documentSnapshot.getLong(KEY_PM).intValue();

                    if (BM > 4)
                    {
                        BM = 4;
                    }
                    else if (IP > 6)
                    {
                        IP = 6;
                    }
                    else if (OPK > 6)
                    {
                        OPK = 6;
                    }
                    else if (PM > 4)
                    {
                        PM = 4;
                    }

                    int totalSKKM = BM + IP + OPK + PM;

                    pbSKKM.setProgress(totalSKKM);

                    textViewBM.setText(documentSnapshot.getLong(KEY_BM).toString());
                    textViewIP.setText(documentSnapshot.getLong(KEY_IP).toString());
                    textViewOPK.setText(documentSnapshot.getLong(KEY_OPK).toString());
                    textViewPM.setText(documentSnapshot.getLong(KEY_PM).toString());
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

    }
}
