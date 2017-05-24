package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

public class SPChooseActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView orderList,folder,invoiceList;
    private TextView projectNameS;

    private Project project;
    private String companyID;
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spchoose);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarS);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        orderList=(ImageView)findViewById(R.id.orderList);
        folder=(ImageView)findViewById(R.id.folder);
        invoiceList=(ImageView)findViewById(R.id.invoiceList);
        invoiceList.setOnClickListener(this);
        projectNameS=(TextView)findViewById(R.id.projectNameS);
        orderList.setOnClickListener(this);
        folder.setOnClickListener(this);

        if (getIntent() != null) {
            project = (Project) getIntent().getSerializableExtra("PROJECT");
            companyID = getIntent().getStringExtra("COMPANY_ID");
            user = (Person) getIntent().getSerializableExtra("USER");
            projectNameS.setText(project.getName());
        }
    }

    @Override
    public void onClick(View v) {
        if (v==orderList){
            Intent intent = new Intent(SPChooseActivity.this, SingleProjectHomeActivity.class).putExtra("PROJECT", project);
            intent.putExtra("COMPANY_ID", companyID);
            intent.putExtra("USER", user);
            finish();
            startActivity(intent);
        }
        if (v==folder){
            Intent intent=new Intent(SPChooseActivity.this,FileActivity.class);
            intent.putExtra("PROJECT",project);
            intent.putExtra("COMPANY_ID", companyID);
            intent.putExtra("USER", user);
            finish();
            startActivity(intent);
        }
        if (v==invoiceList){
            Intent intent=new Intent(SPChooseActivity.this,InvoiceActivity.class);
            intent.putExtra("PROJECT",project);
            intent.putExtra("COMPANY_ID", companyID);
            intent.putExtra("USER", user);
            finish();
            startActivity(intent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SPChooseActivity.this, ProjectActivity.class).putExtra("PROJECT", project);
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
        intent.setClass(SPChooseActivity.this,ProjectActivity.class);
        startActivity(intent);
        finish();
    }
}

