package id.ac.umn.myumn.Enrollment;

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


import id.ac.umn.myumn.R;


public class EnrollmentAdapter extends FirestoreRecyclerAdapter<EnrollmentModel, EnrollmentAdapter.EnrollmentViewHolder> {

    private OnListItemClick onListItemClick;
    int position;


    public EnrollmentAdapter(@NonNull FirestoreRecyclerOptions<EnrollmentModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull EnrollmentViewHolder holder, int position, @NonNull EnrollmentModel model) {

        holder.tvSubject.setText(model.getSubject());
        holder.tvDay.setText(model.getDay());
        holder.tvTime.setText(model.getTime());
        holder.tvTimeEnd.setText(model.getTimeend());
    }

    @NonNull
    @Override
    public EnrollmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_enrollment, parent, false);
        return new EnrollmentViewHolder(view);
    }

    public class EnrollmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvSubject, tvDay, tvTime, tvTimeEnd;

        public EnrollmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubject = itemView.findViewById(R.id.nameSubject);
            tvDay = itemView.findViewById(R.id.day);
            tvTime = itemView.findViewById(R.id.time);
            tvTimeEnd = itemView.findViewById(R.id.timeend);
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
