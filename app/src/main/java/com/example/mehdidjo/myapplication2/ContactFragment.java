package com.example.mehdidjo.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mehdidjo.myapplication2.model.Author;
import com.example.mehdidjo.myapplication2.model.Dialog;
import com.example.mehdidjo.myapplication2.model.Message;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mehdi Djo on 10/03/2018.
 */

public class ContactFragment extends Fragment {

    public ContactFragment (){

    }

    String IMAGE_URL="https://hsto.org/getpro/habr/post_images/e4b/067/b17/e4b067b17a3e414083f7420351db272b.jpg";
    private CoordinatorLayout coordinatorLayout;
    DialogsListAdapter<Dialog> adapter;
    ImageLoader imageLoader;
    private ArrayList<Dialog> chats;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dialoge_liste, container, false);



        DialogsList dialogsList =(DialogsList) rootView.findViewById(R.id.dialogsList);

        ArrayList<Author> users = new ArrayList<>();
        ArrayList<Author> users2 = new ArrayList<>();
        users.add(new Author("sdf","mehdi"));
        Author author = new Author("sqdqd" , "mehdi");
        Message message = new Message("hello" , "abcd" , author);


        chats = new ArrayList<>();
        chats.add(new Dialog("jsqdnf", "mehdi",IMAGE_URL,users,message,2));
        chats.add(new Dialog("jsqwdnf", "reda",IMAGE_URL,users,message,0));

        adapter = new DialogsListAdapter<Dialog>(imageLoader);
        adapter.setItems(chats);
        dialogsList.setAdapter(adapter);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(getContext()).load(url).into(imageView);
            }
        };

        adapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<Dialog>() {
            @Override
            public void onDialogClick(Dialog dialog) {
                adapter.addItem( dialog);
                toDialog();
            }
        });
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        adapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<Dialog>() {
            @Override
            public void onDialogLongClick(final Dialog dialog) {

                Snackbar snackbar = Snackbar.make(coordinatorLayout , "Delete this dialog", Snackbar.LENGTH_LONG)
                        .setAction("Delete", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adapter.deleteById( dialog.getId());
                            }
                        });
                snackbar.show();
            }
        });



        return  rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.setItems(chats);
    }
    @Override
    public void onPause() {
        super.onPause();
        adapter.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter.clear();
    }

    private void toDialog(){
        Intent intent = new Intent(getContext() ,ChatActivity.class );
        startActivity(intent);
    }
}
