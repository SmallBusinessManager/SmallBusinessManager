package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectcourse2.group11.smallbusinessmanager.model.FileUpload;

import java.io.File;
import java.io.IOException;

public class AddFileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button browse, upload;
    private ImageView imageView;
    private EditText fileName;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri fileUri;

    public static final String STORAGE_PATH = "file/";
    public static final String DATABASE_PATH = "file";
    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);

        browse = (Button) findViewById(R.id.browse);
        upload = (Button) findViewById(R.id.upload);
        imageView = (ImageView) findViewById(R.id.imageViewA);
        fileName = (EditText) findViewById(R.id.fileName);

        Log.d("hehe", "findviewbyid");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        browse.setOnClickListener(this);
        upload.setOnClickListener(this);
        Log.d("hehe", "set onclickListener");

    }

    @Override
    @SuppressWarnings("VisibleForTests")
    public void onClick(View v) {
        if (v == browse) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "select file"), REQUEST_CODE);
            Log.d("hehe", "browse clicked");
        }
        if (v == upload) {
            if (fileUri != null) {
                Log.d("hehe", fileUri.toString());
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading file");
                progressDialog.show();

                Log.d("hehe", "dialog shown");


                Log.d("hehe", String.valueOf(System.currentTimeMillis()));
                Log.d("hehe", getFileExt(fileUri));
                StorageReference ref = mStorageRef.child(STORAGE_PATH+System.currentTimeMillis() + "." + getFileExt(fileUri));
                Log.d("hehe", "after getImageExt");
                ref.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("hehe", "success");
                        //progressDialog.dismiss();
                        Toast.makeText(AddFileActivity.this, "File uploaded", Toast.LENGTH_SHORT).show();
                        FileUpload fileUpload = new FileUpload(fileName.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                        String uploadID = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadID).setValue(fileUpload);
                        Log.d("hehe", "success");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // progressDialog.dismiss();
                        Toast.makeText(AddFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("hehe", "onProgress");
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded" + (int) progress + "%");
                    }
                });
            } else {
                Toast.makeText(AddFileActivity.this, "Please select File", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                imageView.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onBackPressed() {

        finish();
//        startActivity(new Intent(AddFileActivity.this, FileActivity.class).
//                putExtra("USER", getIntent().getSerializableExtra("USER"))
//                .putExtra("COMPANY_ID", getIntent().getStringExtra("COMPANY_ID"))
//                .putExtra("PROJECT", getIntent().getSerializableExtra("PROJECT")));
        Intent intent = getIntent();
        intent.setClass(AddFileActivity.this,FileActivity.class);
        startActivity(intent);
    }

}



