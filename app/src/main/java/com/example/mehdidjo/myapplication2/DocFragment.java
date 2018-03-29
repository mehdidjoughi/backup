package com.example.mehdidjo.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Mehdi Djo on 10/03/2018.
 */

public class DocFragment extends Fragment {

public  DocFragment (){

}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.document_activity, container, false);

        TextView  textView = rootView.findViewById(R.id.doc);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , ChatActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
