package com.example.mehdidjo.myapplication2;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.mehdidjo.myapplication2.model.Author;
import com.example.mehdidjo.myapplication2.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements BottomSheetFragment.BottomSheetListner{

    android.support.v7.widget.Toolbar toolbar;
    public static final int RC_PHOTO_PICKER=2;
    public static final int RC_FILE_PICKER=3;
    private static final int CAMERA_REQUEST =4 ;
    MessagesListAdapter<Message> adapter;
    private ChildEventListener mchildEventListener;
    private Author author;

    private FirebaseDatabase database;
   private DatabaseReference myRef;
   private FirebaseStorage mFirebaseStorage;
   private StorageReference mChatPhotosStorageReference;



    String IMAGE_URL="https://hsto.org/getpro/habr/post_images/e4b/067/b17/e4b067b17a3e414083f7420351db272b.jpg";

    ImageLoader imageLoader = new ImageLoader() {
        @Override
        public void loadImage(ImageView imageView, String url) {
            Picasso.with(ChatActivity.this).load(url).into(imageView);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        database = FirebaseDatabase.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        author = new Author(user.getUid() ,user.getDisplayName() , user.getEmail() ,user.getPhotoUrl().toString());

        mFirebaseStorage = FirebaseStorage.getInstance();
        myRef = database.getReference().child("Message");
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");



        MessagesList messagesList = (MessagesList) findViewById(R.id.messagesList) ;
        MessageHolders messageHolders;
        messageHolders = new MessageHolders();



       // adapter = new MessagesListAdapter<>("dfsdf", imageLoader);
        adapter =new MessagesListAdapter<Message>(author.getId() , messageHolders, imageLoader);
        messagesList.setAdapter(adapter);


        attachDatabaseReadListener();


        MessageInput inputView = (MessageInput) findViewById(R.id.input);

        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message

                String id = myRef.push().getKey();
                Message message = new Message(input.toString(),id, author);
                //adapter.addToStart(message, true);
                myRef.child(id).setValue(message);
                return true;
            }
        });


        inputView.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {

                BottomSheetFragment bottomSheet = new BottomSheetFragment();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();


                            String id = myRef.push().getKey();
                            Message message = new Message(null, id, author);
                            message.setImage(new Message.Image(downloadUrl.toString()));
                            adapter.addToStart(message, true);
                            myRef.child(id).setValue(message);




                        }
                    });
        }else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){

            Uri selectedImageUri = data.getData();

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            String id = myRef.push().getKey();
            Message message = new Message(null, id, author);
            message.setImage(new Message.Image(tempUri.toString()));
            adapter.addToStart(message, true);
            myRef.child(id).setValue(message);

            Log.v("CAMERA_REQUEST","--> "+tempUri.toString());
        }
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void toDialog(){
        Intent intent = new Intent(ChatActivity.this ,DialogeListeActivity.class );
        startActivity(intent);
    }

    @Override
    public void onButtonClicked(int id) {

        if (id== R.id.imagePicker){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY ,true);
            startActivityForResult(Intent.createChooser(intent,"Complet action using " ),RC_PHOTO_PICKER);


        }else if (id == R.id.photoPicker){

            /*
            Intent intent = new Intent();
            intent.setType("pdf/*");
            intent.putExtra(Intent.ACTION_GET_CONTENT ,true);
            startActivityForResult(Intent.createChooser(intent,"Complet action using " ),RC_FILE_PICKER);

            Author author = new Author("sqdaqd" , "mehdi");
            Message message = new Message(null, "qkjsndj", author);*/
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        } else if (id == R.id.filePcker){

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_FILE_PICKER);
        }

    }

    private void attachDatabaseReadListener() {

        if (mchildEventListener == null){
            mchildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setDate();
                   adapter.addToStart(message,true);

                    //adapter.addToEnd(List<IMessage> messages, boolean reverse);

                   /* ArrayList<Message> messages = new ArrayList<>();
                    messages.add(dataSnapshot.getValue(Message.class));
                    adapter.addToEnd( messages, true);*/

                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            myRef.addChildEventListener(mchildEventListener);
        }
    }

}
