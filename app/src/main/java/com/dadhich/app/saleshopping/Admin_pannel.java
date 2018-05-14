package com.dadhich.app.saleshopping;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.resource.file.FileResource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Admin_pannel extends AppCompatActivity {

    private Button mSelectImage, mUploadButton, mShowButton;

    private ProgressDialog progressDialog;
    private EditText ProductName , ManufracName, PriceName;
    public static  final int GALLAY_INTENT = 2;
    public static int c=0;
    private StorageReference mStorage;
    private  DatabaseReference mdatabse;
    private Uri mImageUri;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pannel);



        mSelectImage = (Button)findViewById(R.id.gallaryButton);
        ManufracName = (EditText)findViewById(R.id.mnameId);
        ProductName = (EditText)findViewById(R.id.pnameId);
        PriceName = (EditText)findViewById(R.id.priceId);
        mUploadButton = (Button)findViewById(R.id.uploadID);
        mShowButton = (Button)findViewById(R.id.showUploadId);


        mStorage = FirebaseStorage.getInstance().getReference("user");
        mdatabse = FirebaseDatabase.getInstance().getReference("user");


        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();

            }
        });

        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesAcitivity();
            }
        });





        progressDialog = new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLAY_INTENT);
            }
        });

    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver() ;
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLAY_INTENT && resultCode == RESULT_OK && data!=null && data.getData() !=null)
        {



           // Upload user = new Upload(productname1 , manufracname1 , pricename1);

            //String userId = mdatabse.push().getKey();
            //mdatabse.child(userId).setValue(user);


               mImageUri = data.getData();
               if(mImageUri!=null){
                   Toast.makeText(getApplicationContext(), "Image selected sucessfully" , Toast.LENGTH_LONG).show();
               }
               else {
                   Toast.makeText(getApplicationContext(), "Selection unsuccessfull", Toast.LENGTH_LONG).show();
               }




        }

    }


    public void uploadFile()
    {

        progressDialog.setMessage("Uploading picture...");
        progressDialog.show();

        if(mImageUri!=null){
            StorageReference filepath = mStorage.child(mImageUri.getLastPathSegment()+ "product " +c + "." +getFileExtension(mImageUri));
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    progressDialog.dismiss();
                    Toast.makeText(Admin_pannel.this, "upload done..", Toast.LENGTH_LONG).show();
                    c=c+1;

                    Upload upload = new Upload(ProductName.getText().toString(), ManufracName.getText().toString(), PriceName.getText()
                    .toString(), taskSnapshot.getDownloadUrl().toString());
                    String uploadId = mdatabse.push().getKey();
                    mdatabse.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Admin_pannel.this, "upload fail..", Toast.LENGTH_LONG).show();

                }
            });
        }else {
            Toast.makeText(getApplicationContext(), "No file selected" , Toast.LENGTH_SHORT).show();
        }

    }


    public void openImagesAcitivity()
    {
        Intent intent = new Intent(getApplicationContext(), Cart.class);
        startActivity(intent);


    }
    //protected void onActivityResult(int )
}
