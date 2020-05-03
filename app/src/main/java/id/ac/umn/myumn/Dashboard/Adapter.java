package id.ac.umn.myumn.Dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import id.ac.umn.myumn.R;

public class Adapter extends PagerAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity, container, false);

        TextView subject, title, date, time;

        subject = view.findViewById(R.id.subject);
        title = view.findViewById(R.id.title);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);

        subject.setText(models.get(position).getSubject());
        title.setText(models.get(position).getTitle());
        date.setText(models.get(position).getDate());
        time.setText(models.get(position).getTime());

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
