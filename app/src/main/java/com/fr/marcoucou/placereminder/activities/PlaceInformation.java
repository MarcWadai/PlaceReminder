package com.fr.marcoucou.placereminder.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.fr.marcoucou.placereminder.DBLite.PlacesDataSource;
import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.model.PlaceCategory;
import com.fr.marcoucou.placereminder.utils.Constants;
import com.fr.marcoucou.placereminder.utils.GetPostionUtils;
import com.fr.marcoucou.placereminder.utils.ResultLastLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlaceInformation extends AppCompatActivity implements ResultLastLocation{
    private ProgressDialog progressBar;
    private EditText title;
    private EditText address;
    private ImageView placeImageView;
    private Bitmap thumbnail;
    private Bitmap scaled;
    private NumberPicker categoryPicker;
    private Context myContext;
    private Button submitButton;
    private Boolean pictureTaken = false;
    int CAMERA_PIC_REQUEST = 2;
    private GetPostionUtils postionUtils;
    private GoogleApiClient googleApiClient;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        placeImageView = (ImageView) findViewById(R.id.imageView);
        title = (EditText) findViewById(R.id.editTextTitle);
        address = (EditText) findViewById(R.id.editTextAddress);

        initEditText(title);
        initEditText(address);
        submitButton = (Button) findViewById(R.id.buttonSubmitPlace);
        categoryPicker = (NumberPicker) findViewById(R.id.categoryPicker);
        categoryPicker.setMinValue(1);
        categoryPicker.setMaxValue(getResources().getStringArray(R.array.nav_drawer_items).length -1);
        String[] tmpNavItems = Arrays.copyOfRange(getResources().getStringArray(R.array.nav_drawer_items), 1, getResources().getStringArray(R.array.nav_drawer_items).length);
        categoryPicker.setDisplayedValues(tmpNavItems);
        setNumberPickerTextColor(categoryPicker, Color.WHITE);
        this.myContext = this;
        placeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (checkPermissions()) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUri = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    }
                }catch (SecurityException e){
                    Toast.makeText(getApplicationContext(), "Please enable permision to use camera",Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void initEditText(final EditText editText){
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
     /*   editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                v.setOnKeyListener(new View.OnKeyListener(){
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            return true;
                        }

                        return false;
                    }
                });
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart(){
        super.onStart();
        postionUtils = new GetPostionUtils(getApplicationContext(), this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(postionUtils)
                .addOnConnectionFailedListener(postionUtils)
                .addApi(LocationServices.API)
                .build();
        postionUtils.initializingPosition(googleApiClient);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == CAMERA_PIC_REQUEST)
                    if (data!= null) {
                        try {
                            pictureTaken = true;
                            thumbnail = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            int nh = (int) ( thumbnail.getHeight() * (1024 / thumbnail.getWidth()) );
                            scaled = Bitmap.createScaledBitmap(thumbnail, 1024, nh, true);
                            placeImageView.setImageBitmap(scaled);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        pictureTaken = false;
                        Toast.makeText(this, "Picture NOT taken", Toast.LENGTH_LONG).show();
                    }
        /*if( requestCode == CAMERA_PIC_REQUEST && data != null)
        {
            thumbnail = (Bitmap) data.getExtras().get("data");
            thumbnail = RoundImageUtils.getRoundedCornerBitmap(thumbnail,Constants.ROUND_LEVEL);
            placeImageView.setImageBitmap(thumbnail);
            pictureTaken = true;
        }
        else
        {
            pictureTaken = false;
            Toast.makeText(this, "Picture NOT taken, Please enable camera permission for this app", Toast.LENGTH_LONG).show();

        }*/

    }


    public void SubmitPlace(View v){
        if(title.getText().toString().matches("") || address.getText().toString().matches("")){
            Toast.makeText(this, "Please enter title and address", Toast.LENGTH_SHORT).show();
        }
        else {
            PlacesDataSource placesDataSource = new PlacesDataSource(this);
            placesDataSource.open();
            if (!pictureTaken) {
                PlaceCategory cat = new PlaceCategory(categoryPicker.getDisplayedValues()[categoryPicker.getValue()], categoryPicker.getValue());
                BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_empty_picture);
                placesDataSource.createPlaces(title.getText().toString(), address.getText().toString(), cat, drawable.getBitmap());

            }
            else{
                PlaceCategory cat = new PlaceCategory(categoryPicker.getDisplayedValues()[categoryPicker.getValue()], categoryPicker.getValue());
                placesDataSource.createPlaces(title.getText().toString(), address.getText().toString(), cat, scaled);
            }
            placesDataSource.close();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        Log.d("time",getCurrentTime());

        googleApiClient.disconnect();
    }


    public String getCurrentTime(){
        SimpleDateFormat serverFormat;
        Calendar c = Calendar.getInstance();
        serverFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss",Locale.getDefault());
        return serverFormat.format(c.getTime()).toString();
    }


    public void currentPosition(View v){
        if (!checkPermissions()){
                Toast.makeText(getApplicationContext(), "Please enable the permission for the geolocalisation in your preference", Toast.LENGTH_LONG).show();
        } else {
            // permission has been granted, continue as usual
            progressBar = new ProgressDialog(PlaceInformation.this);
            progressBar.setCancelable(false);
            progressBar.setMessage("Getting your current position ");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.show();
            postionUtils.gettingLastPosition();
        }
    }

    @Override
    public void connectedLocation(Location location) {
        String adressString = getAddressFromLatLng(location);
        if(adressString != ""){
            address.setText(adressString);
        }
        else {
            Toast.makeText(getApplicationContext(),"Couldnt get yout location", Toast.LENGTH_LONG).show();
        }
        progressBar.dismiss();
    }

    @Override
    public void disconnectedLocation() {
        Toast.makeText(getApplicationContext(),"Couldnt get yout location disconnected", Toast.LENGTH_LONG).show();
        progressBar.dismiss();
    }

    private String getAddressFromLatLng(Location location)
    {
        String result ="";
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> list = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude() , 1);
            if (list != null && list.size() > 0) {
                Address address = list.get(0);
                // sending back first address line and locality
                result = address.getAddressLine(0) + ", " + address.getLocality();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    Typeface typeface = Typeface.createFromAsset( getAssets(), Constants.TYPEFACE_NAME);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setTypeface(typeface);
                    ((EditText)child).setTypeface(typeface);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberPickercolor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberPickerColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberPickerColor", e);
                }
            }
        }
        return false;
    }
    public boolean checkPermissions(){
        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(Constants.requiredPermissions)) {
            Toast.makeText(this, "Please grant all permissions", Toast.LENGTH_LONG).show();
            goToSettings();
            return false;
        }
        else{
         return true;
        }
    }

    public boolean hasPermissions( @NonNull String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getApplicationContext(),permission))
                return false;
        return true;
    }

    public void goToSettings() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        getApplicationContext().startActivity(i);
    }
}
