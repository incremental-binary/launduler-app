package kr.ac.kaist.launduler;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kr.ac.kaist.launduler.models.Place;
import kr.ac.kaist.launduler.services.LaundulerServiceKt;

import java.util.List;

public abstract class BaseSelectPlaceActivity extends RxAppCompatActivity {
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
        LaundulerServiceKt.getLaundulerService().getPlaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object places) throws Exception {
                        mAdapter = new PlaceAdapter((List<Place>) places);
                        mAdapter.getPositionClicks().subscribe(new Consumer<Place>() {
                            @Override
                            public void accept(Place p) throws Exception {
                                lastSelected = p;
                            }
                        });
                        mPlaces.setAdapter(mAdapter);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                R.string.error_places,
                                Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    }

    protected abstract void setupToolbar(ActionBar ab);

}
