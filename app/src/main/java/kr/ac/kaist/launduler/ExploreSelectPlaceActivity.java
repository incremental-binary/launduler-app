package kr.ac.kaist.launduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Activity for 'Select a Place' screen jumped from 'Explore' screen.
 *
 * This activity should be started with an intent which has an extra value.
 * The extra value, with key {@code EXTRA_PLACE_ID}, is the ID of selected place.
 * If there is no selected place, the value should not be set or less than zero.
 * In this case, the 'Close' button will not be available.
 *
 * When the user has selected a place and taps 'Select' button, this activity
 * will be finished with result code {@code Activity.RESULT_OK} and the ID of selected
 * place as the value of extra {@code EXTRA_PLACE_ID}. When the user taps
 * 'Close' button (if available,) this activity will be finished with no result
 * and the user is navigated to the parent activity.
 */
public class ExploreSelectPlaceActivity extends BaseSelectPlaceActivity {
    public static final String EXTRA_PLACE_ID = "PLACE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_place_explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select:
                if (lastSelected == null) {
                    return false;
                }
                updateDefaultPlace();
                Intent it = getIntent();
                it.putExtra(EXTRA_PLACE_ID, lastSelected.getId());
                setResult(Activity.RESULT_OK, it);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void setupToolbar(ActionBar ab) {
        if (isRequired()) {
            return;
        }
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    private boolean isRequired() {
        Intent it = getIntent();
        long placeId = it.getLongExtra(EXTRA_PLACE_ID, -1);
        return placeId < 0;
    }

    private void updateDefaultPlace() {
        // TODO: write some code which sets the default place to `lastSelected`.
    }
}
