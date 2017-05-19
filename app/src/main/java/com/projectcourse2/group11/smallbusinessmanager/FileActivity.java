package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.projectcourse2.group11.smallbusinessmanager.model.FileUpload;

import java.io.File;

public class FileActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private FloatingActionButton fab;
    private ProgressDialog dialog;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait loading file...");
        dialog.show();

        listView = (ListView) findViewById(R.id.listView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("file");

        fab.setOnClickListener(this);

        FirebaseListAdapter<FileUpload> adapter = new FirebaseListAdapter<FileUpload>(
                FileActivity.this,
                FileUpload.class,
                R.layout.file_item,
                mDatabaseRef
        ) {
            @Override
            protected void populateView(View v, FileUpload model, int position) {
                ImageView imageView = (ImageView) v.findViewById(R.id.image1);
                TextView textView = (TextView) v.findViewById(R.id.text1);

                Glide.with(FileActivity.this).load(model.getUri()).into(imageView);
                textView.setText(model.getName());
            }
        };

        listView.setAdapter(adapter);
        dialog.dismiss();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final FileUpload file = (FileUpload) parent.getItemAtPosition(position);
                StorageReference islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(file.getUri());

                //file stored at Local storage/Device storage/SBMFile
                File rootPath = new File(Environment.getExternalStorageDirectory(), "SBMFile");
                if (!rootPath.exists()) {
                    rootPath.mkdirs();
                } else {
                    String fileExtensiton = file.getUri().substring(file.getUri().lastIndexOf("."), file.getUri().lastIndexOf("?"));
                    final File localFile = new File(rootPath, file.getName() + fileExtensiton);

                    islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FileActivity.this);
                            builder.setMessage("File *" + file.getName() + "* downloaded at Device storage/SBMFile")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == fab) {
          //  startActivity(new Intent(FileActivity.this, AddFileActivity.class));
        }
    }

}
