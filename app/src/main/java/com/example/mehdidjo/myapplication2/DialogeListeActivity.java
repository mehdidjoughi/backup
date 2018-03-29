package com.example.mehdidjo.myapplication2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class DialogeListeActivity extends AppCompatActivity {

    String IMAGE_URL="https://hsto.org/getpro/habr/post_images/e4b/067/b17/e4b067b17a3e414083f7420351db272b.jpg";
    private CoordinatorLayout coordinatorLayout;
    private DialogsListAdapter<Dialog> adapter;
    ImageLoader  imageLoader;

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialoge_liste);



        DialogsList dialogsList =(DialogsList) findViewById(R.id.dialogsList);

        ArrayList<Author> users = new ArrayList<>();
        ArrayList<Author> users2 = new ArrayList<>();
        users.add(new Author("sdf","mehdi"));
        Author author = new Author("sqdqd" , "mehdi");
        Message message = new Message("hello" , "abcd" , author);


        ArrayList<Dialog> chats = new ArrayList<>();
        chats.add(new Dialog("jsqdnf", "mehdi",IMAGE_URL,users,message,2));
        chats.add(new Dialog("jsqwdnf", "reda",IMAGE_URL,users,message,0));

        adapter = new DialogsListAdapter<Dialog>(imageLoader);
        adapter.setItems(chats);
        dialogsList.setAdapter(adapter);

          imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(DialogeListeActivity.this).load(url).into(imageView);
            }
        };

        adapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<Dialog>() {
            @Override
            public void onDialogClick(Dialog dialog) {
                adapter.addItem( dialog);
                toDialog();
            }
        });

        adapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<Dialog>() {
            @Override
            public void onDialogLongClick(final Dialog dialog) {
                coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
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


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();


    }


    private void toDialog(){
        Intent intent = new Intent(DialogeListeActivity.this ,ChatActivity.class );
        startActivity(intent);
    }
}
