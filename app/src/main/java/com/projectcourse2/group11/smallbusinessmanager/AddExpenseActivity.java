package com.projectcourse2.group11.smallbusinessmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projectcourse2.group11.smallbusinessmanager.model.Accountant;
import com.projectcourse2.group11.smallbusinessmanager.model.Expenses;
import com.projectcourse2.group11.smallbusinessmanager.model.Person;
import com.projectcourse2.group11.smallbusinessmanager.model.Position;

import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    private String companyID;
    private Button button;
    private EditText editText1, editText2, editText3 , editText4;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Person user;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        companyID = getIntent().getStringExtra("COMPANY_ID");
        user = (Person) getIntent().getSerializableExtra("USER");


        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);
        if (user.getPosition().equals(Position.WORKER)){
            Intent intent=getIntent();
            intent.setClass(AddExpenseActivity.this,MainActivity.class);
            finish();
            finishAffinity();
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            final int amount = Integer.parseInt(editText1.getText().toString().trim());
            final String description = editText2.getText().toString();
            final String details = editText3.getText().toString();
            final String date = editText4.getText().toString();
            if (editText1 != null && editText2 != null && editText3 != null && editText4 != null ) {
                Expenses expense = new Expenses(amount,details, description,date);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("expenses").child(companyID).child(expense.getId()).setValue(expense);

                Toast.makeText(AddExpenseActivity.this, "Expense Added Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddExpenseActivity.this, AccountantActivity.class).putExtra("COMPANY_ID",companyID);
                AddExpenseActivity.this.startActivity(intent);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.setClass(AddExpenseActivity.this, AccountantActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
