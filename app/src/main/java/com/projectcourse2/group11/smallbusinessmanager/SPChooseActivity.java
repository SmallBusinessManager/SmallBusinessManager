package com.projectcourse2.group11.smallbusinessmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Project;

public class SPChooseActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView orderList,folder;

    private Project project;
    private String projectUID,projectName,companyID;
    private Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spchoose);

        orderList=(ImageView)findViewById(R.id.orderList);
        folder=(ImageView)findViewById(R.id.folder);
        orderList.setOnClickListener(this);
        folder.setOnClickListener(this);

        if (getIntent() != null) {
            project = (Project) getIntent().getSerializableExtra("PROJECT");
            projectUID = project.getId();
            projectName = project.getName();
            companyID = getIntent().getStringExtra("COMPANY_ID");
            user = (Person) getIntent().getSerializableExtra("USER");
            this.setTitle(projectName);
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
            intent.putExtra("COMPANY_ID", companyID);
            intent.putExtra("PROJECT",project);
            intent.putExtra("USER", user);
            finish();
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(SPChooseActivity.this, ProjectActivity.class).putExtra("USER", user).putExtra("COMPANY_ID", companyID));
    }
}
