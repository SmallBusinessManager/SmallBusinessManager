

package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.FileUpload;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

public class FileActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private FloatingActionButton fab;
    private CheckBox checkBox;
    private ProgressDialog dialog;

    private DatabaseReference mDatabaseRef;
    private ListAdapter adapter;

    private Project project;
    private Person user;
    private String companyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarF);
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

        listView = (ListView) findViewById(R.id.listViewP);
        fab = (FloatingActionButton) findViewById(R.id.fabF);
        fab.setOnClickListener(this);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("projectFiles").child(project.getId());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait loading file...");
        dialog.show();

        adapter = new FirebaseListAdapter<FileUpload>(
                FileActivity.this,
                FileUpload.class,
                R.layout.file_item,
                mDatabaseRef
        ) {
            @Override
            protected void populateView(View v, FileUpload model, int position) {
                ImageView imageView = (ImageView) v.findViewById(R.id.imageF);
                TextView textView = (TextView) v.findViewById(R.id.textF);
                checkBox = (CheckBox) v.findViewById(R.id.checkBox);

                textView.setText(model.getName());
                String fileExt = model.getName().substring(model.getName().indexOf("."));
                switch (fileExt) {
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
                        Glide.with(FileActivity.this).load(model.getUri()).into(imageView);
                        break;
                    case ".docx":
                        setImageView(R.drawable.ic_docx, imageView);
                        break;
                    case ".pdf":
                        setImageView(R.drawable.ic_pdf, imageView);
                        break;
                    case ".txt":
                        setImageView(R.drawable.ic_txt, imageView);
                        break;
                    case ".ppt":
                        setImageView(R.drawable.ic_ppt, imageView);
                        break;
                    default:
                        setImageView(R.drawable.ic_doc, imageView);
                        break;
                }
            }
        };

        listView.setAdapter(adapter);
        dialog.dismiss();


      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final FileUpload file = (FileUpload) parent.getItemAtPosition(position);
                StorageReference islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(file.getUri());

                //file stored at Local storage/Device storage/SBMFile
                File rootPath = new File(Environment.getExternalStorageDirectory(), "SBMFile");
                if (!rootPath.exists()) {
                    rootPath.mkdirs();
                } else {
                    final File localFile = new File(rootPath, file.getName());

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
        });*/
        checkBox.setOnClickListener(this);
    }

    private void setImageView(int i, ImageView imageView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable(getResources().getDrawable(i, getApplicationContext().getTheme()));
        } else {
            imageView.setImageDrawable(getResources().getDrawable(i));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == fab) {
            Intent intent = new Intent(FileActivity.this, AddFileActivity.class);
            intent.putExtra("PROJECT", project);
            intent.putExtra("COMPANY_ID", companyID);
            intent.putExtra("USER", user);
            finish();
            startActivity(intent);
        }
        if (v == checkBox) {
            if (checkBox.isChecked()) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_files, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(FileActivity.this, SPChooseActivity.class).putExtra("PROJECT", project);
                intent.putExtra("COMPANY_ID", companyID);
                intent.putExtra("USER", user);
                finish();
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setClass(FileActivity.this, SPChooseActivity.class);
        finish();
        startActivity(intent);
    }

}
