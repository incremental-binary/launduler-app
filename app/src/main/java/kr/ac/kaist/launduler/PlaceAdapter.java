package kr.ac.kaist.launduler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import kr.ac.kaist.launduler.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyukyukyu on 24/05/2017.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private static final String TAG = "PlaceAdapter";

    private final List<Place> places;
    private final PublishSubject<Place> onClickSubject = PublishSubject.create();
    private Place lastSelected = null;

    public PlaceAdapter() {
        places = new ArrayList<>(10);
        for (int i = 0; i < 10; ++i) {
            places.add(new Place(i, "Room " + (i + 1)));
        }
        getPositionClicks().subscribe(new Consumer<Place>() {
            @Override
            public void accept(Place p) throws Exception {
                lastSelected = p;
                notifyItemRangeChanged(0, 10);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public ImageView mCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mCheck = (ImageView) itemView.findViewById(R.id.check);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Place p = places.get(position);
        holder.mName.setText(p.getName());
        holder.mCheck.setVisibility(p == lastSelected ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubject.onNext(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public Observable<Place> getPositionClicks() {
        return onClickSubject;
    }
}
