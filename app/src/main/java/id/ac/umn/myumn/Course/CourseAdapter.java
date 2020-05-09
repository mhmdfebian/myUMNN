package id.ac.umn.myumn.Course;

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

public class CourseAdapter extends FirestoreRecyclerAdapter<CourseModel, CourseAdapter.CourseViewHolder>{

    private OnListItemClick onListItemClick;
    int position;

    public CourseAdapter(@NonNull FirestoreRecyclerOptions<CourseModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull CourseViewHolder holder, int position, @NonNull CourseModel model) {
        holder.lCourse.setText(model.getSubject());
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_course, parent,false);
        return new CourseViewHolder(view);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView lCourse;


        public CourseViewHolder(@NonNull View itemView){
            super(itemView);

            lCourse = itemView.findViewById(R.id.listCourse);

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
