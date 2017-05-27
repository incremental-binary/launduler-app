package kr.ac.kaist.launduler;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import io.reactivex.functions.Consumer;
import kr.ac.kaist.launduler.model.Place;

public abstract class BaseSelectPlaceActivity extends AppCompatActivity {
    protected Toolbar mToolbar;
    protected RecyclerView mPlaces;
    protected PlaceAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected Place lastSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_select_place);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPlaces = (RecyclerView) findViewById(R.id.places);

        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            setupToolbar(ab);
        }

        mPlaces.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mPlaces.setLayoutManager(mLayoutManager);

        mAdapter = new PlaceAdapter();
        mPlaces.setAdapter(mAdapter);

        mAdapter.getPositionClicks().subscribe(new Consumer<Place>() {
            @Override
            public void accept(Place p) throws Exception {
                lastSelected = p;
            }
        });
    }

    protected abstract void setupToolbar(ActionBar ab);

}
