package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.UserData.RulesRecyclerAdapter;
import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.R;

public class RulesActivity extends AppCompatActivity {
    private static final String TAG = "RulesActivity";
    private User currentUser;
    private List<String> ruleList;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private ConstraintLayout background;
    private ImageView taskButton;
    private ImageView storeButton;
    private ImageView bankButton;
    private ImageView rulesButton;
    private ImageView optionsButton;

    private RecyclerView rulesRecycler;
    private RulesRecyclerAdapter rulesRecyclerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        SetupDatabase();
        SetupViews();

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SetupTheme();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SetupDatabase();
        ruleList = new ArrayList<String>();
        rulesRecycler.hasFixedSize();
        rulesRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Rules");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ruleList.add(dataSnapshot.child("text").getValue(String.class));
                rulesRecyclerAdapter = new RulesRecyclerAdapter(ruleList, RulesActivity.this);
                rulesRecycler.setAdapter(rulesRecyclerAdapter);
                rulesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int position = Integer.parseInt(dataSnapshot.getKey())-1;
                rulesRecyclerAdapter.rulesList.set(position, dataSnapshot.child("text").getValue(String.class));
                rulesRecyclerAdapter.notifyItemChanged(position);
                rulesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int position = Integer.parseInt(dataSnapshot.getKey())-1;
                rulesRecyclerAdapter.rulesList.remove(position);
                rulesRecyclerAdapter.notifyItemRemoved(position);
                rulesRecyclerAdapter.notifyItemRangeChanged(position, rulesRecyclerAdapter.rulesList.size());
                rulesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SetupDatabase();
        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SetupTheme();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        background = (ConstraintLayout) findViewById(R.id.RulesLayout);
        taskButton = (ImageView) findViewById(R.id.TaskButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        bankButton = (ImageView) findViewById(R.id.BankButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);

        rulesRecycler = (RecyclerView) findViewById(R.id.RulesRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(RulesActivity.this, RulesActivity.class);
                switch (view.getId()) {
                    case R.id.TaskButton:
                        newIntent = new Intent(RulesActivity.this, TasksActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.StoreButton:
                        newIntent = new Intent(RulesActivity.this, StoreActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.BankButton:
                        newIntent = new Intent(RulesActivity.this, BankActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.OptionsButton:
                        newIntent = new Intent(RulesActivity.this, OptionsActivity.class);
                        newIntent.putExtra("Role", false);
                        startActivity(newIntent);
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);

    }

    private void SetupTheme() {
        switch (currentUser.getTheme()) {
            case "Mermaids":
                background.setBackgroundResource(R.drawable.mermaids_background_rules_english);
                taskButton.setImageResource(R.drawable.mermaids_button_task_english);
                bankButton.setImageResource(R.drawable.mermaids_button_bank_english);
                storeButton.setImageResource(R.drawable.mermaids_button_store_english);
                rulesButton.setImageResource(R.drawable.mermaids_button_rules_english);
                optionsButton.setImageResource(R.drawable.mermaids_button_options);
                break;
            case "Western":
                background.setBackgroundResource(R.drawable.western_background_rules_english);
                taskButton.setImageResource(R.drawable.western_button_task_english);
                bankButton.setImageResource(R.drawable.western_button_bank_english);
                storeButton.setImageResource(R.drawable.western_button_store_english);
                rulesButton.setImageResource(R.drawable.western_button_rules_english);
                optionsButton.setImageResource(R.drawable.western_button_options);
                break;
            case "Space":
                background.setBackgroundResource(R.drawable.space_background_rules_english);
                taskButton.setImageResource(R.drawable.space_button_task_english);
                bankButton.setImageResource(R.drawable.space_button_bank_english);
                storeButton.setImageResource(R.drawable.space_button_store_english);
                rulesButton.setImageResource(R.drawable.space_button_rules_english);
                optionsButton.setImageResource(R.drawable.space_button_options);
                break;
            case "Spring":
                background.setBackgroundResource(R.drawable.spring_background_rules);
                taskButton.setImageResource(R.drawable.spring_button_task);
                bankButton.setImageResource(R.drawable.spring_button_bank);
                storeButton.setImageResource(R.drawable.spring_button_store);
                rulesButton.setImageResource(R.drawable.spring_button_rules);
                optionsButton.setImageResource(R.drawable.spring_button_options);
                break;
            case "Summer":
                background.setBackgroundResource(R.drawable.summer_background_rules);
                taskButton.setImageResource(R.drawable.summer_button_task);
                bankButton.setImageResource(R.drawable.summer_button_bank);
                storeButton.setImageResource(R.drawable.summer_button_store);
                rulesButton.setImageResource(R.drawable.summer_button_rules);
                optionsButton.setImageResource(R.drawable.summer_button_options);
                break;
            case "Fall":
                background.setBackgroundResource(R.drawable.fall_background_rules);
                taskButton.setImageResource(R.drawable.fall_button_task);
                bankButton.setImageResource(R.drawable.fall_button_bank);
                storeButton.setImageResource(R.drawable.fall_button_store);
                rulesButton.setImageResource(R.drawable.fall_button_rules);
                optionsButton.setImageResource(R.drawable.fall_button_options);
                break;
            case "Winter":
                background.setBackgroundResource(R.drawable.winter_background_rules);
                taskButton.setImageResource(R.drawable.winter_button_task);
                bankButton.setImageResource(R.drawable.winter_button_bank);
                storeButton.setImageResource(R.drawable.winter_button_store);
                rulesButton.setImageResource(R.drawable.winter_button_rules);
                optionsButton.setImageResource(R.drawable.winter_button_options);
                break;
            case "Standard":
                background.setBackgroundResource(R.drawable.pirate_background_rules_english);
                taskButton.setImageResource(R.drawable.pirate_button_task_english);
                bankButton.setImageResource(R.drawable.pirate_button_bank_english);
                storeButton.setImageResource(R.drawable.pirate_button_store_english);
                rulesButton.setImageResource(R.drawable.pirate_button_rules_english);
                optionsButton.setImageResource(R.drawable.pirate_button_options);
                break;
        }
    }
}
