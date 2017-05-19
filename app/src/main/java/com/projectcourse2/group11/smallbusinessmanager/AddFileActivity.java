package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projectcourse2.group11.smallbusinessmanager.model.FileUpload;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

import java.io.File;
import java.io.IOException;

public class AddFileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button browse, upload;
    private ImageView imageView;
    private EditText fileName;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri fileUri;

    public static final String DATABASE_PATH = "file";
    public static final int REQUEST_CODE = 1234;

    private Project project;
    private Person user;
    private String companyID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarA);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            project = (Project) getIntent().getSerializableExtra("PROJECT");
            user = (Person) getIntent().getSerializableExtra("USER");
            companyID = getIntent().getStringExtra("COMPANY_ID");
            this.setTitle(project.getName());
        }
        browse = (Button) findViewById(R.id.browse);
        upload = (Button) findViewById(R.id.upload);
        imageView = (ImageView) findViewById(R.id.imageViewA);
        fileName = (EditText) findViewById(R.id.fileName);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("projectFiles").child(project.getId());

        browse.setOnClickListener(this);
        upload.setOnClickListener(this);

    }

    @Override
    @SuppressWarnings("VisibleForTests")
    public void onClick(View v) {
        if (v == browse) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "select file"), REQUEST_CODE);
        }
        if (v == upload) {
            if (fileUri != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading file");
                progressDialog.show();

                StorageReference ref = mStorageRef.child("projectFiles").child(project.getId()+System.currentTimeMillis() + "." + getFileExt(fileUri));
                ref.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(AddFileActivity.this, "File uploaded", Toast.LENGTH_SHORT).show();
                        FileUpload fileUpload = new FileUpload(fileName.getText().toString()+"."+getFileExt(fileUri), taskSnapshot.getDownloadUrl().toString());
                        String uploadID = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadID).setValue(fileUpload);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                         progressDialog.dismiss();
                        Toast.makeText(AddFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
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
                switch (getFileExt(fileUri)) {
                    case ".jpeg":
                    case ".png":
                    case ".exif":
                    case ".jpeg 2000":
                    case ".tiff":
                    case ".gif":
                    case ".bmp":
                    case ".ppm":
                    case ".pgm":
                    case ".pbm":
                    case ".pnm":
                        Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                        imageView.setImageBitmap(bm);
                        break;
                    case ".docx":
                        setImageView(R.drawable.ic_docx);
                        break;
                    case ".pdf":
                        setImageView(R.drawable.ic_pdf);
                        break;
                    case ".txt":
                        setImageView(R.drawable.ic_txt);
                        break;
                    case ".ppt":
                        setImageView(R.drawable.ic_ppt);
                        break;
                    default:
                        setImageView(R.drawable.ic_doc);
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setImageView(int i){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable(getResources().getDrawable(i, getApplicationContext().getTheme()));
        } else {
            imageView.setImageDrawable(getResources().getDrawable(i));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AddFileActivity.this, SPChooseActivity.class).putExtra("PROJECT", project);
                intent.putExtra("COMPANY_ID", companyID);
                intent.putExtra("USER", user);
                finish();
                startActivity(intent);
                break;
        }
        return true;
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




