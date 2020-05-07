package id.ac.umn.myumn.Grade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


import java.text.DecimalFormat;

import id.ac.umn.myumn.R;


public class GradeAdapter extends FirestoreRecyclerAdapter<GradeModel, GradeAdapter.GradeViewHolder>{

    private OnListItemClick onListItemClick;


    public GradeAdapter(@NonNull FirestoreRecyclerOptions<GradeModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull GradeViewHolder holder, int position, @NonNull GradeModel model) {

        double total, number = 0;

        int nilaiuts;
        int nilaiuas;
        int nilaitugas;
        String letter = "F";

        DecimalFormat REAL_FORMATTER = new DecimalFormat("0.00");

        nilaiuts = model.getNilaiuts();
        nilaiuas = model.getNilaiuas();
        nilaitugas = model.getNilaitugas();


        total = (nilaiuts * 0.3) + (nilaitugas * 0.3) + (nilaiuas * 0.4);

        if (total >= 85.00){
            letter = "A";
            number = 4.00;
        }
        else if (total >= 80.00){
            letter = "A-";
            number = 4.00;
        }
        else if (total >= 75.00){
            letter = "B+";
            number = 4.00;
        }
        else if (total >= 70.00){
            letter = "B";
            number = 4.00;
        }
        else if (total >= 65.00){
            letter = "B-";
            number = 4.00;
        }
        else if (total >= 60.00){
            letter = "C+";
            number = 4.00;
        }
        else if (total >= 55.00){
            letter = "C";
            number = 4.00;
        }
        else if (total >= 45.00){
            letter = "D";
            number = 4.00;
        }
        else if (total >= 0){
            letter = "E";
            number = 4.00;
        }

        holder.tvSubject.setText(model.getSubject());
        holder.tvNumberGrade.setText(REAL_FORMATTER.format(number));
        holder.tvLetterGrade.setText(letter);
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_grade, parent,false);
        return new GradeViewHolder(view);
    }

    public class GradeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvSubject,tvNumberGrade,tvLetterGrade;


        public GradeViewHolder(@NonNull View itemView){
            super(itemView);

            tvSubject = itemView.findViewById(R.id.subject);
            tvNumberGrade = itemView.findViewById(R.id.numberGrade);
            tvLetterGrade = itemView.findViewById(R.id.letterGrade);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DocumentSnapshot snapshot = getSnapshots().getSnapshot(this.getAdapterPosition());
            onListItemClick.onItemClick(snapshot, getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(DocumentSnapshot snapshot, int position);
    }
}
