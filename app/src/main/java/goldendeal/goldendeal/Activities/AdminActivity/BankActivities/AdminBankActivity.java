package goldendeal.goldendeal.Activities.AdminActivity.BankActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

import goldendeal.goldendeal.Activities.AdminActivity.RulesActivity.AdminRulesActivity;
import goldendeal.goldendeal.Activities.AdminActivity.StoreActivity.AdminStoreActivity;
import goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys.AdminPlanActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Activities.UserActivities.RulesActivity;
import goldendeal.goldendeal.Data.AdminData.CurrencyRecyclerAdapter;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class AdminBankActivity extends AppCompatActivity {
    private static final String TAG = "AdminBankActivity";

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
    private Button adminButton;
    private TextView titleText;

    private TextView currencyText;
    private TextView imageEconomyText;


    private Button newCurrencyButton;

    private RecyclerView currencyRecycler;
    private List<VirtualCurrency> currencyList;
    private CurrencyRecyclerAdapter currencyRecyclerAdapter;
    private RecyclerView imageEconomyRecycler;
    private List<VirtualCurrency> imageEconomyList;
    private CurrencyRecyclerAdapter imageEconomyRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bank);
        SetupDatabase();
        SetupViews();
        SetupLanguage();
        SetupTheme();

        currencyList = new ArrayList<VirtualCurrency>();
        currencyRecycler.hasFixedSize();
        currencyRecycler.setLayoutManager(new LinearLayoutManager(this));
        imageEconomyList = new ArrayList<VirtualCurrency>();
        imageEconomyRecycler.hasFixedSize();
        imageEconomyRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentAccess = dataSnapshot.getValue(String.class);
                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Bank");
                mDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        VirtualCurrency currentCurrency = dataSnapshot.getValue(VirtualCurrency.class);
                        if(currentCurrency.isImageEconomy()){
                            imageEconomyList.add(currentCurrency);

                            imageEconomyRecyclerAdapter = new CurrencyRecyclerAdapter(AdminBankActivity.this, imageEconomyList);
                            imageEconomyRecycler.setAdapter(imageEconomyRecyclerAdapter);
                            imageEconomyRecyclerAdapter.notifyDataSetChanged();
                        }else{
                            currencyList.add(currentCurrency);

                            currencyRecyclerAdapter = new CurrencyRecyclerAdapter(AdminBankActivity.this, currencyList);
                            currencyRecycler.setAdapter(currencyRecyclerAdapter);
                            currencyRecyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String currencyTitle = dataSnapshot.getKey();
                        for (int i = 0; i < currencyList.size(); i++){
                            if(TextUtils.equals(currencyList.get(i).getTitle(), currencyTitle)){
                                currencyRecyclerAdapter.currencyList.set(i, dataSnapshot.getValue(VirtualCurrency.class));
                                currencyRecyclerAdapter.notifyItemChanged(i);
                                currencyRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        for(int i = 0; i < imageEconomyList.size(); i++){
                            if(TextUtils.equals(imageEconomyList.get(i).getTitle(), currencyTitle)){
                                imageEconomyRecyclerAdapter.currencyList.set(i, dataSnapshot.getValue(VirtualCurrency.class));
                                imageEconomyRecyclerAdapter.notifyItemChanged(i);
                                imageEconomyRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        String currencyTitle = dataSnapshot.getKey();
                        for (int i = 0; i < currencyList.size(); i++){
                            if(TextUtils.equals(currencyList.get(i).getTitle(), currencyTitle)){
                                currencyRecyclerAdapter.currencyList.remove(i);
                                currencyRecyclerAdapter.notifyItemRemoved(i);
                                currencyRecyclerAdapter.notifyItemRangeChanged(i, currencyRecyclerAdapter.currencyList.size());
                                currencyRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        for(int i = 0; i < imageEconomyList.size(); i++){
                            if(TextUtils.equals(imageEconomyList.get(i).getTitle(), currencyTitle)){
                                imageEconomyRecyclerAdapter.currencyList.remove(i);
                                imageEconomyRecyclerAdapter.notifyItemRemoved(i);
                                imageEconomyRecyclerAdapter.notifyItemRangeChanged(i, currencyRecyclerAdapter.currencyList.size());
                                imageEconomyRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SetupLanguage();
        SetupTheme();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        background = (ConstraintLayout) findViewById(R.id.AdminBankLayout);
        taskButton = (ImageView) findViewById(R.id.TaskButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        bankButton = (ImageView) findViewById(R.id.BankButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);
        adminButton = (Button) findViewById(R.id.AdminButton);
        newCurrencyButton = (Button) findViewById(R.id.NewCurrencyButton);
        titleText = (TextView) findViewById(R.id.TitleText);

        currencyText = (TextView) findViewById(R.id.CurrencyTitle);
        imageEconomyText = (TextView) findViewById(R.id.ImageEconomyTitle);

        currencyRecycler = (RecyclerView) findViewById(R.id.CurrencyRecycler);
        imageEconomyRecycler = (RecyclerView) findViewById(R.id.ImageEconomyRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(AdminBankActivity.this, AdminBankActivity.class);
                switch (view.getId()) {
                    case R.id.TaskButton:
                        newIntent = new Intent(AdminBankActivity.this, AdminTasksActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.StoreButton:
                        newIntent = new Intent(AdminBankActivity.this, AdminStoreActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.RulesButton:
                        newIntent = new Intent(AdminBankActivity.this, AdminRulesActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.OptionsButton:
                        newIntent = new Intent(AdminBankActivity.this, OptionsActivity.class);
                        newIntent.putExtra("Role", true);
                        startActivity(newIntent);
                        break;
                    case R.id.AdminButton:
                        newIntent = new Intent(AdminBankActivity.this, AdminPlanActivity.class);
                        startActivity(newIntent);
                        break;
                    case R.id.NewCurrencyButton:
                        newIntent = new Intent(AdminBankActivity.this, NewCurrencyActivity.class);
                        startActivity(newIntent);
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
        adminButton.setOnClickListener(switchPage);
        newCurrencyButton.setOnClickListener(switchPage);
    }

    private void SetupLanguage(){
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String language = dataSnapshot.getValue(String.class);

                if(TextUtils.equals(language, "Norsk")){
                    adminButton.setText("Velg Plan");
                    newCurrencyButton.setText("Ny Valuta");
                    currencyText.setText("Valuta");
                    imageEconomyText.setText("Bilde Økonomi");
                    titleText.setText("Bank");
                } else if(TextUtils.equals(language, "English")){
                    adminButton.setText("Choose Plan");
                    newCurrencyButton.setText("New currency");
                    currencyText.setText("Currency");
                    imageEconomyText.setText("Image Economy");
                    titleText.setText("Bank");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetupTheme(){
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("theme");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theme = dataSnapshot.getValue(String.class);

                switch(theme){
                    case "Mermaids":
                        background.setBackgroundResource(R.drawable.mermaids_bank_background);
                        break;
                    case "Western":
                        break;
                    case "Standard":
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
