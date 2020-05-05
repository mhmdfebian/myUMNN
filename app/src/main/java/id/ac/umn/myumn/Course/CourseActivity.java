package id.ac.umn.myumn.Course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.myumn.Course.CourseModel;
import id.ac.umn.myumn.R;

public class CourseActivity extends AppCompatActivity {

    Button btnClose;
    RecyclerView lvCourse;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userID, courseId;
    TextView tvCourse, tvTitle, tvTopic;


    private FirestoreRecyclerAdapter adapter;

    private List<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course2);

        btnClose = findViewById(R.id.btnClose);

        Intent pindah = getIntent();
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        lvCourse = findViewById(R.id.listviewCourse);
        tvCourse = findViewById(R.id.Course);

        userID = mAuth.getCurrentUser().getUid();

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


        courseId = pindah.getStringExtra("courseID");
        tvCourse.setText(courseId);


        DocumentReference documentReference = fStore.collection("user").document(userID).collection("course").document(courseId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    tvCourse.setText(documentSnapshot.getString("subject"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

        Query query = fStore.collection("user").document(userID).collection("course").document(courseId).collection("topics");
        FirestoreRecyclerOptions<CourseModel> options = new FirestoreRecyclerOptions.Builder<CourseModel>()
                .setQuery(query, CourseModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<CourseModel, CourseViewHolder>(options){
           @NonNull
          @Override
           public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_coursetopic, parent,false);
               return new CourseViewHolder(view);
           }

           @Override
            protected void onBindViewHolder(@NonNull CourseViewHolder holder, int position, @NonNull CourseModel model) {
                holder.tvTitle.setText(model.getTitle());
                holder.tvTopic.setText(model.getTopic());
            }
        };

        lvCourse.setHasFixedSize(true);
        lvCourse.setLayoutManager(new LinearLayoutManager(this));
        lvCourse.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent();
        setResult(RESULT_OK, intentBack);
        finish();
        //Transisi (keluar) dari kiri ke kanan
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class CourseViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private TextView tvTopic;

        public CourseViewHolder(@NonNull View itemView){
            super(itemView);

            tvTitle = itemView.findViewById(R.id.title);
            tvTopic = itemView.findViewById(R.id.topic);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
