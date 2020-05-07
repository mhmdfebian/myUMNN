package id.ac.umn.myumn.Event;

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


public class EventAdapter extends FirestoreRecyclerAdapter<EventModel, EventAdapter.EventViewHolder>{

    private OnListItemClick onListItemClick;
    int position;
    String eventname, eventdesc, eventdate, eventlocation, eventtime;

    public EventAdapter(@NonNull FirestoreRecyclerOptions<EventModel> options, OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull EventModel model) {

        holder.tvName.setText(model.getEventname());
        holder.tvDesc.setText(model.getEventdesc());
        holder.tvDate.setText(model.getEventdate());
        holder.tvLocation.setText(model.getEventlocation());

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent,false);
        return new EventViewHolder(view);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName,tvDate,tvDesc,tvLocation;
        private Button btnAdd;


        public EventViewHolder(@NonNull View itemView){
            super(itemView);

            tvName = itemView.findViewById(R.id.nameEvent);
            tvDesc = itemView.findViewById(R.id.desc);
            tvDate = itemView.findViewById(R.id.date);
            tvLocation = itemView.findViewById(R.id.location);
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
