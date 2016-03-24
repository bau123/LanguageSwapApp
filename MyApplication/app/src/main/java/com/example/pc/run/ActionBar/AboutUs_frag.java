package com.example.pc.run.ActionBar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.run.R;

/**
 * Activity used to display information about the app
 */
public class AboutUs_frag extends Fragment {
    TextView AboutUs;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_us, container, false);
        setHasOptionsMenu(true);

        AboutUs = (TextView)v.findViewById(R.id.aboutUs);

        AboutUs.setText(R.string.about_us);

        return v;
    }
}
