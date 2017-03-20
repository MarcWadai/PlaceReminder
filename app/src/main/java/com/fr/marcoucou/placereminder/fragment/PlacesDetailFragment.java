package com.fr.marcoucou.placereminder.fragment;


import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fr.marcoucou.placereminder.R;
import com.fr.marcoucou.placereminder.model.Places;
import com.fr.marcoucou.placereminder.utils.Constants;
import com.fr.marcoucou.placereminder.utils.RoundImageUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesDetailFragment extends Fragment {



    private boolean isImageFitToScreen;
    private Places place;
    public PlacesDetailFragment(){
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null){
            Bundle bundle = this.getArguments();
            place = bundle.getParcelable("place");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_detail, container, false);
        TextView title = (TextView) view.findViewById(R.id.detailTitle);
        TextView adress = (TextView) view.findViewById(R.id.detailAdress);
        TextView date = (TextView) view.findViewById(R.id.textViewDate);
        ImageView image = (ImageView) view.findViewById(R.id.imageView2);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), Constants.TYPEFACE_NAME);
        title.setTypeface(typeface);
        adress.setTypeface(typeface);

        if(place != null) {
            title.setText(place.getTitle());
            adress.setText(place.getAdresse());
            date.setText(place.getDate());
            Bitmap bitmap = RoundImageUtils.getRoundedCornerBitmap(place.getPlaceImage(), Constants.ROUND_LEVEL);
            image.setImageBitmap(bitmap);
        }
        else{

            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.empy_view_pager2);
            image.setImageBitmap(drawable.getBitmap());
        }
        onImageClick(image, title, adress, date);
        // Inflate the layout for this fragment
        return view ;
    }


    public void onImageClick(final ImageView imageView, final TextView title, final TextView address,final TextView date){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    title.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.detailAdress);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.setMargins(0,10,0,0);
                    imageView.setLayoutParams(params);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    //imageView.setAdjustViewBounds(true);
                }else{
                    isImageFitToScreen=true;
                    title.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
    }
}
