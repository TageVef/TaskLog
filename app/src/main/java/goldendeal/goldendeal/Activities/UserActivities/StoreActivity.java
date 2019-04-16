package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.UserData.CounterRecyclerAdapter;
import goldendeal.goldendeal.Data.UserData.ImageEconomyRecyclerAdapter;
import goldendeal.goldendeal.Data.UserData.StoreRecyclerAdapter;
import goldendeal.goldendeal.Model.StoreItem;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class StoreActivity extends AppCompatActivity {
    private static final String TAG = "StoreActivity";

    private ImageView taskButton;
    private ImageView storeButton;
    private ImageView bankButton;
    private ImageView rulesButton;
    private ImageView optionsButton;
    private ImageView faceButton;
    private TextView titleText;

    private List<StoreItem> itemList;
    private RecyclerView itemRecycler;
    private StoreRecyclerAdapter storeRecyclerAdapter;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        SetupDatabase();
        SetupViews();

        itemList = new ArrayList<StoreItem>();
        itemRecycler.hasFixedSize();
        itemRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Store");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                itemList.add(dataSnapshot.getValue(StoreItem.class));
                storeRecyclerAdapter = new StoreRecyclerAdapter(itemList, StoreActivity.this);
                itemRecycler.setAdapter(storeRecyclerAdapter);
                storeRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int position = Integer.parseInt(dataSnapshot.getKey());
                storeRecyclerAdapter.itemList.set(position, dataSnapshot.getValue(StoreItem.class));
                storeRecyclerAdapter.notifyItemChanged(position);
                storeRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int position = Integer.parseInt(dataSnapshot.getKey());
                storeRecyclerAdapter.itemList.remove(position);
                storeRecyclerAdapter.notifyItemRemoved(position);
                storeRecyclerAdapter.notifyItemRangeChanged(position, storeRecyclerAdapter.itemList.size());
                storeRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SetupLanguage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SetupLanguage();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        taskButton = (ImageView) findViewById(R.id.TaskButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        bankButton = (ImageView) findViewById(R.id.BankButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);
        faceButton = (ImageView) findViewById(R.id.FaceButton);
        itemRecycler = (RecyclerView) findViewById(R.id.StoreRecycler);
        titleText = (TextView) findViewById(R.id.TitleText);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(StoreActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(StoreActivity.this, StoreActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(StoreActivity.this, BankActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(StoreActivity.this, RulesActivity.class));
                        finish();
                        break;
                    case R.id.FaceButton:
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(StoreActivity.this, OptionsActivity.class));
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
        //faceButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
    }

    private void SetupLanguage(){
        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String language = dataSnapshot.getValue(String.class);

                if(TextUtils.equals(language, "Norsk")){
                    titleText.setText("Butikk");
                } else if(TextUtils.equals(language, "English")){
                    titleText.setText("Store");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
