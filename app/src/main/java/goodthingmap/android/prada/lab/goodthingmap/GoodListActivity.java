package goodthingmap.android.prada.lab.goodthingmap;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.prada.lab.goodthingmap.model.GoodThing;
import android.prada.lab.goodthingmap.model.GoodThingsData;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import goodthingmap.android.prada.lab.goodthingmap.component.BaseServiceFragment;
import goodthingmap.android.prada.lab.goodthingmap.component.GoodThingAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GoodListActivity extends BaseActivity {

    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_LOCATION = "extra_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_my_fravor:
                intent = new Intent(this, FavorActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends BaseServiceFragment
            implements AdapterView.OnItemClickListener, Callback<GoodThingsData> {

        private HomeActivity.PlaceholderFragment.GoodThingType mType;
        private GoodThingAdapter mAdapter;
        private Location mLocation;

        public PlaceholderFragment() {
            super();
        }

        @Override
        public void onCreate(Bundle savedStateInstance) {
            super.onCreate(savedStateInstance);
            int typeId = getArguments().getInt(EXTRA_TYPE, 0);
            mType = HomeActivity.PlaceholderFragment.GoodThingType.values()[typeId];
            mLocation = getArguments().getParcelable(EXTRA_LOCATION);
            getActivity().setTitle(mType.getName());

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_good_list, container, false);
            mAdapter = new GoodThingAdapter(getActivity());
            mAdapter.setLocation(mLocation);
            ListView lv = (ListView)rootView.findViewById(R.id.list_view);
            lv.setAdapter(mAdapter);
            lv.setOnItemClickListener(this);

            if (mLocation != null) {
                // FIXME refactor this later
                if (mType == HomeActivity.PlaceholderFragment.GoodThingType.NEAR)
                    mService.listStory(mLocation.getLatitude(), mLocation.getLongitude(), this);
                else
                    mService.listStory(mType.getTypeId(), mLocation.getLatitude(), mLocation.getLongitude(), this);
            } else {
                if (mType == HomeActivity.PlaceholderFragment.GoodThingType.NEAR)
                    mService.listStory(this);
                else
                    mService.listStory(mType.getTypeId(), this);
            }
            return rootView;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            GoodThing item = mAdapter.getItem(i);
            if (item != null) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(GoodThing.EXTRA_GOODTHING, item);
                intent.putExtra(EXTRA_LOCATION, mLocation);
                startActivity(intent);
            }
        }

        @Override
        public void success(GoodThingsData data, Response response) {
            for (GoodThing thing : data.getGoodThingList()) {
                mAdapter.add(thing);
            }
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
        }
    }
}
