package com.example.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextInputEditText id,name,course,contact;
    Button registerBtn;
    ImageView imageView;
    Uri filepath;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        registerId = findViewById(R.id.registerId);
//        registerName = findViewById(R.id.registerName);
//        registerCourse = findViewById(R.id.registerCourse);
//        registerContact = findViewById(R.id.registerContact);

        registerBtn = findViewById(R.id.registerBtn);
        imageView = findViewById(R.id.imageView);

                imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Dexter.withActivity(MainActivity.this)
                                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");
                                        startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                        permissionToken.continuePermissionRequest();
                                    }
                                }).check();
                    }
                });

                registerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload();
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK){
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            }catch (Exception ex){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void upload() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();



        name = findViewById(R.id.name);
        course = findViewById(R.id.course);
        contact = findViewById(R.id.contact);
        id = findViewById(R.id.id);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference("Image1"+new Random().nextInt(50));


        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                dialog.dismiss();

                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference root = db.getReference("users");

                                dataHolder obj = new dataHolder(name.getText().toString(),course.getText().toString(),contact.getText().toString(),uri.toString());
                                root.child(id.getText().toString()).setValue(obj);

                                name.setText("");
                                course.setText("");
                                contact.setText("");
                                id.setText("");
                                imageView.setImageResource(R.drawable.ic_launcher_background);
                                Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setMessage("Uploaded : "+(int)percent+" %" );
            }
        });




    }





}