package id.ac.umn.myumn.Event;

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

public class EventAddedAdapter extends FirestoreRecyclerAdapter<EventModel, EventAddedAdapter.EventViewHolder>{


    int position;



    public EventAddedAdapter(FirestoreRecyclerOptions<EventModel> options) {
        super(options);
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

    public class EventViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName,tvDate,tvDesc,tvLocation;


        public EventViewHolder(@NonNull View itemView){
            super(itemView);

            tvName = itemView.findViewById(R.id.nameEvent);
            tvDesc = itemView.findViewById(R.id.desc);
            tvDate = itemView.findViewById(R.id.date);
            tvLocation = itemView.findViewById(R.id.location);
        }

    }


}
