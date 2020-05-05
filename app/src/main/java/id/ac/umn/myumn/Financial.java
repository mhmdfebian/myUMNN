package id.ac.umn.myumn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Financial extends AppCompatActivity {
    Button btnClose;
    Spinner spinnerFinancial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial);

        btnClose = findViewById(R.id.btnClose);
        
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
