package com.example.discovery.Adapter;

import static com.example.discovery.R.layout.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.discovery.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private View view;
    private LayoutInflater layoutInflater;

    public CustomInfoWindow(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(custom_info_window, null);
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        TextView parkName = view.findViewById(R.id.info_titre_textView);
        TextView parkState = view.findViewById(R.id.info_state_textView);

        parkName.setText(marker.getTitle());
        parkState.setText(marker.getSnippet());

        return view;
    }
}
