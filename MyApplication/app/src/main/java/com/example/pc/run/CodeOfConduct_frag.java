package com.example.pc.run;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Joss on 14/03/2016.
 */
public class CodeOfConduct_frag extends Fragment {

    TextView CodeOfConduct;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.code_of_conduct, container, false);
        setHasOptionsMenu(true);

        CodeOfConduct = (TextView)v.findViewById(R.id.codeOfConduct);

        CodeOfConduct.setText(R.string.code_conduct);


        return v;
    }
}
