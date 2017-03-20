package com.fr.marcoucou.placereminder.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fr.marcoucou.placereminder.DBLite.PlacesDataSource;
import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.adapter.ListPlacesAdapter;
import com.fr.marcoucou.placereminder.animation.BackgroundContainer;
import com.fr.marcoucou.placereminder.model.Places;
import com.fr.marcoucou.placereminder.utils.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaceListAll extends AppCompatActivity {

    private ListView listPlaces;
    private FloatingActionButton fabAddPlace;
    private int indexPlace;
    boolean mSwiping = false;
    boolean mItemPressed = false;
    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
    private ListPlacesAdapter adapter;
    private BackgroundContainer mBackgroundContainer;
    private HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();
    private  ArrayList<Places> itemPlaces;
    private ArrayList<Integer> itemPlacesDeleted;
    private ArrayList<String> itemPlacesAddresses;
    private FloatingActionButton fabMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list_all);
        indexPlace = getIntent().getIntExtra("indexPlace", 0);
        mBackgroundContainer = (BackgroundContainer) findViewById(R.id.listViewBackground);
        listPlaces = (ListView) findViewById(R.id.list_places);
        fabAddPlace = (FloatingActionButton) findViewById(R.id.fab);
        fabAddPlace.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PlaceInformation.class);
                startActivity(intent);
            }
        });
        initializationView();
    }

    public void setListViewOnClick(){
        listPlaces.setClickable(true);
        listPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Places place = (Places) listPlaces.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("address", place.getAdresse());
                startActivity(intent);
            }
        });
    }


    public void initializationView() {
        PlacesDataSource placesDataSource = new PlacesDataSource(getApplicationContext());
        placesDataSource.open();
        if (indexPlace != 0){
            itemPlaces = placesDataSource.getPlacesFromCategory(indexPlace);
        }else{
            itemPlaces = placesDataSource.getAllPlaces();
        }
        adapter = new ListPlacesAdapter(getApplicationContext(),
                itemPlaces,mTouchListener);
        listPlaces.setAdapter(adapter);
        getAllAddress();
        placesDataSource.close();
        TextView emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        Typeface typeface = Typeface.createFromAsset( getAssets(), Constants.TYPEFACE_NAME);
        emptyTextView.setTypeface(typeface);
        listPlaces.setEmptyView(findViewById(R.id.emptyTextView));
        itemPlacesDeleted = new ArrayList<Integer>();

        fabMap = (FloatingActionButton) findViewById(R.id.fabMap);
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemPlaces.size() > 0) {
                    String[] stockArr = new String[itemPlacesAddresses.size()];
                    stockArr = itemPlacesAddresses.toArray(stockArr);
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("listaddress",indexPlace);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_noplace_found), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void getAllAddress(){
        itemPlacesAddresses = new ArrayList<String>();
        for (int i = 0; i< itemPlaces.size();i++){
           itemPlacesAddresses.add(itemPlaces.get(i).getAdresse());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(PlaceListAll.this).
                        getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return false;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1);
                    v.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            listPlaces.requestDisallowInterceptTouchEvent(true);
                            mBackgroundContainer.showBackground(v.getTop(), v.getHeight());
                        }
                    }
                    if (mSwiping) {
                        v.setTranslationX((x - mDownX));
                        v.setAlpha(1 - deltaXAbs / v.getWidth());
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                {
                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + v.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > v.getWidth() / 4) {
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / v.getWidth();
                            endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / v.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        // Animate position and alpha of swiped item
                        // NOTE: This is a simplified version of swipe behavior, for the
                        // purposes of this demo about animation. A real version should use
                        // velocity (via the VelocityTracker class) to send the item off or
                        // back at an appropriate speed.
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        listPlaces.setEnabled(false);
                        v.animate().setDuration(duration).
                                alpha(endAlpha).translationX(endX).
                                withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Restore animated values
                                        v.setAlpha(1);
                                        v.setTranslationX(0);
                                        if (remove) {
                                            animateRemoval(listPlaces, v);
                                        } else {
                                            mBackgroundContainer.hideBackground();
                                            mSwiping = false;
                                            listPlaces.setEnabled(true);
                                        }
                                    }
                                });
                    }
                }
                mItemPressed = false;
                break;

                default:
                    return false;
            }
            return true;
        }
    };

    private void animateRemoval(final ListView listview, View viewToRemove) {
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = adapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        int position = listPlaces.getPositionForView(viewToRemove);
        Places placetoDelete = (Places) adapter.getItem(position);
        itemPlaces.remove(placetoDelete);
        itemPlacesDeleted.add(placetoDelete.getId());
        Log.d("id", "id : "+ itemPlacesDeleted);
        adapter.notifyDataSetChanged();
        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
                                        mBackgroundContainer.hideBackground();
                                        mSwiping = false;
                                        listPlaces.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
                                    mBackgroundContainer.hideBackground();
                                    mSwiping = false;
                                    listPlaces.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deletePlacesFromDB();
        itemPlacesDeleted.clear();
    }


    public void deletePlacesFromDB(){
        if (itemPlacesDeleted.size()>0){
            PlacesDataSource placesDataSource = new PlacesDataSource(this);
            placesDataSource.open();
            for (int i = 0; i<itemPlacesDeleted.size();i++){
                placesDataSource.deletePlace(itemPlacesDeleted.get(i));
            }
            placesDataSource.close();
        }
    }
}
