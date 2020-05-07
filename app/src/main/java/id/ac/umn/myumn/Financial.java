package id.ac.umn.myumn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.ac.umn.myumn.Dashboard.Model;

public class Financial extends AppCompatActivity {
    Button btnClose;
    Spinner spinnerFinancial;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    TextView tvSemester, tvBiayatetap, tvSks, tvSkspeminatan, tvBiayasks, tvJumlahsks, tvJumlahpeminatan, tvTotal, tvSudahbayar, tvBelumbayar, tvBiayapeminatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btnClose = findViewById(R.id.btnClose);

        tvSemester = findViewById(R.id.semester);
        tvBiayatetap = findViewById(R.id.jumlahTetap);
        tvBiayasks = findViewById(R.id.biayaSKS);
        tvBiayapeminatan = findViewById(R.id.biayaMinat);
        tvSks = findViewById(R.id.sksSKS);
        tvSkspeminatan = findViewById(R.id.sksMinat);
        tvJumlahsks = findViewById(R.id.jumlahSKS);
        tvJumlahpeminatan = findViewById(R.id.jumlahMinat);
        tvTotal = findViewById(R.id.jumlahTotal);
        tvBelumbayar = findViewById(R.id.Belum);
        tvSudahbayar = findViewById(R.id.Sudah);


        userID = mAuth.getCurrentUser().getUid();

        spinnerFinancial = findViewById(R.id.spinnerFinancial);

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

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                Financial.this, R.layout.spinner_light, getResources().getStringArray(R.array.semester));
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_light);
        spinnerFinancial.setAdapter(myAdapter);

        spinnerFinancial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = spinnerFinancial.getItemAtPosition(position).toString();
                if (selectedItem.equals("1st Semester")) {
                    semesterFinancial(selectedItem);

                } else if (selectedItem.equals("2nd Semester")) {
                    semesterFinancial(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void semesterFinancial(String selectedItem) {
        DocumentReference documentReference = fStore.collection("user").document(userID).collection("financial").document(selectedItem);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");
                    int Biayatetap = documentSnapshot.getLong("biayatetap").intValue();
                    int Biayasks = documentSnapshot.getLong("biayasks").intValue();
                    int Sks = documentSnapshot.getLong("sks").intValue();
                    int Skspeminatan = documentSnapshot.getLong("skspeminatan").intValue();
                    int Jumlahsks;
                    int Jumlahpeminatan;
                    int Total;
                    int Sudahbayar = documentSnapshot.getLong("sudahbayar").intValue();
                    int Belumbayar;

                    Jumlahsks = Biayasks * Sks;
                    Jumlahpeminatan = Biayasks * Skspeminatan;
                    Total = Biayatetap + Jumlahsks + Jumlahpeminatan;
                    Belumbayar = Total - Sudahbayar;

                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);


                    tvSemester.setText(documentSnapshot.getString("semester"));
                    tvBiayatetap.setText(formatRupiah.format((double) Biayatetap));
                    tvBiayasks.setText(formatRupiah.format((double) Biayasks));

                    if (Skspeminatan == 0) {
                        tvBiayapeminatan.setText(formatRupiah.format((double) 0));
                    } else {
                        tvBiayapeminatan.setText(formatRupiah.format((double) Biayasks));
                    }

                    tvSks.setText("" + Sks);
                    tvSkspeminatan.setText("" + Skspeminatan);
                    tvJumlahsks.setText(formatRupiah.format((double) Jumlahsks));
                    tvJumlahpeminatan.setText(formatRupiah.format((double) Jumlahpeminatan));
                    tvTotal.setText(formatRupiah.format((double) Total));
                    tvSudahbayar.setText(formatRupiah.format((double) Sudahbayar));
                    tvBelumbayar.setText(formatRupiah.format((double) Belumbayar));

                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
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
