package com.fr.marcoucou.placereminder.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.fr.marcoucou.placereminder.DBLite.PlacesDataSource;
import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.model.PlaceCategory;

public class PlaceInformation extends AppCompatActivity{

    private EditText title;
    private EditText address;
    private ImageView placeImageView;
    private Bitmap thumbnail;
    private NumberPicker categoryPicker;
    private Context myContext;
    int CAMERA_PIC_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeImageView = (ImageView) findViewById(R.id.imageView);
        title = (EditText) findViewById(R.id.editTextTitle);
        address = (EditText) findViewById(R.id.editTextAddress);
        categoryPicker = (NumberPicker) findViewById(R.id.categoryPicker);
        categoryPicker.setMinValue(0);
        categoryPicker.setMaxValue(3);
        categoryPicker.setDisplayedValues(new String[]{"Restaurant", "Scenery", "Shopping", "Dont know where to put this"});
        this.myContext = this;
        placeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissionInManifest(myContext, "android.permission.CAMERA")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == CAMERA_PIC_REQUEST)
        {
            thumbnail = (Bitmap) data.getExtras().get("data");
            placeImageView.setImageBitmap(thumbnail);
        }
        else
        {
            Toast.makeText(PlaceInformation.this, "Picture NOT taken", Toast.LENGTH_LONG).show();
        }

    }

    // for android M and above use to check this permission otherwise exeption will arise
    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }


    public void SubmitPlace(View v){
        if(title.getText().toString() == "" || address.getText().toString() == ""){
            Toast.makeText(PlaceInformation.this, "Please enter title and adress", Toast.LENGTH_LONG).show();
        }
        else {
            PlacesDataSource placesDataSource = new PlacesDataSource(this);
            placesDataSource.open();
            Log.d("place", "category chosen" + categoryPicker.getValue());
            PlaceCategory cat = new PlaceCategory(categoryPicker.getDisplayedValues()[categoryPicker.getValue()], categoryPicker.getValue());
            placesDataSource.createPlaces(title.getText().toString(), address.getText().toString(), cat);
            placesDataSource.close();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}