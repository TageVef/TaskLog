package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.R;

public class BankActivity extends AppCompatActivity {

    private Button taskButton;
    private Button storeButton;
    private Button bankButton;
    private Button rulesButton;
    private Button optionsButton;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        setupDatabase();
        SetupViews();
    }

    private void setupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        taskButton = (Button) findViewById(R.id.TaskButton);
        storeButton = (Button) findViewById(R.id.StoreButton);
        bankButton = (Button) findViewById(R.id.BankButton);
        rulesButton = (Button) findViewById(R.id.RulesButton);
        optionsButton = (Button) findViewById(R.id.OptionsButton);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(BankActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(BankActivity.this, StoreActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(BankActivity.this, BankActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(BankActivity.this, RulesActivity.class));
                        finish();
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(BankActivity.this, OptionsActivity.class));
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        //storeButton.setOnClickListener(switchPage);
        //rulesButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
    }
}
