package id.ac.umn.myumn.Attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import id.ac.umn.myumn.R;

public class AttendanceAdapter extends FirestoreRecyclerAdapter<AttendanceModel, AttendanceAdapter.AttendanceViewHolder> {

    private OnListItemClick onListItemClick;

    public AttendanceAdapter(@NonNull FirestoreRecyclerOptions<AttendanceModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull AttendanceAdapter.AttendanceViewHolder holder, int position, @NonNull AttendanceModel model) {
        holder.lAttendance.setText(model.getSubject());
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }


    public class AttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView lAttendance;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);

            lAttendance = itemView.findViewById(R.id.listAttendance);

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
