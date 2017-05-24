package kr.ac.kaist.launduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class BaseSelectPlaceActivity extends AppCompatActivity {
    protected Toolbar mToolbar;
    protected RecyclerView mPlaces;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_select_place);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPlaces = (RecyclerView) findViewById(R.id.places);

        setSupportActionBar(mToolbar);

        mPlaces.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mPlaces.setLayoutManager(mLayoutManager);

        mAdapter = new PlaceAdapter();
        mPlaces.setAdapter(mAdapter);
    }

}
