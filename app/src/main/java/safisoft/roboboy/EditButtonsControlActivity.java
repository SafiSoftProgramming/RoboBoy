package safisoft.roboboy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import safisoft.roboboy.R;

import java.io.IOException;

public class EditButtonsControlActivity extends AppCompatActivity {

    String PROJECT_CONTROL_TYPE ;
    String PROJECT_NAME ;
    String PROJECT_CONTROL_DESCRIPTION ;
    String PROJECT_STATE ;
    TextView txtv_project_name ;
    TextView txtv_project_description ;
    ImageButton btn_direction_up ;
    ImageButton btn_direction_left ;
    ImageButton btn_direction_center ;
    ImageButton btn_direction_right ;
    ImageButton btn_direction_down ;
    ImageView ic_done_ed_btn_direction_up ;
    ImageView ic_done_ed_btn_direction_left ;
    ImageView ic_done_ed_btn_direction_center ;
    ImageView ic_done_ed_btn_direction_right ;
    ImageView ic_done_ed_btn_direction_down ;
    ImageView ic_done_ed_btn_com1 ;
    ImageView ic_done_ed_btn_com2 ;
    ImageView ic_done_ed_btn_com3 ;
    ImageView ic_done_ed_btn_com4 ;
    ImageView ic_done_ed_btn_com5 ;
    ImageView ic_done_ed_btn_com6 ;
    EditText edtxt_btn_direction_name ;
    EditText edtxt_btn_direction_command_down ;
    EditText edtxt_btn_direction_command_up ;
    ImageButton btn_commend_1 ;
    ImageButton btn_commend_2 ;
    ImageButton btn_commend_3 ;
    ImageButton btn_commend_4 ;
    ImageButton btn_commend_5 ;
    ImageButton btn_commend_6 ;
    TextView txtv_btn_command_1_name ;
    TextView txtv_btn_command_2_name ;
    TextView txtv_btn_command_3_name ;
    TextView txtv_btn_command_4_name ;
    TextView txtv_btn_command_5_name ;
    TextView txtv_btn_command_6_name ;
    TextView txtv_command_1_down ;
    TextView txtv_command_2_down ;
    TextView txtv_command_3_down ;
    TextView txtv_command_4_down ;
    TextView txtv_command_5_down ;
    TextView txtv_command_6_down ;
    TextView txtv_command_1_up ;
    TextView txtv_command_2_up ;
    TextView txtv_command_3_up ;
    TextView txtv_command_4_up ;
    TextView txtv_command_5_up ;
    TextView txtv_command_6_up ;
    EditText edtxt_btn_commands_name ;
    EditText edtxt_btn_commands_command_down ;
    EditText edtxt_btn_commands_command_up ;
    ImageButton btn_save_Project ;

    DbConnction db ;
    Cursor c = null;
    boolean IS_EDITING ;



    String COLUMN_FOR_NAME  ;
    String COLUMN_FOR_COMMAND_DOWN ;
    String COLUMN_FOR_COMMAND_UP ;
    String DEFAULT_BUTTON_NAME ;
    String AD_STATE ;
    ImageView CURRENT_IC_DONE_ED_BTN ;
    TextView BTN_NAME ;
    TextView COM_DOWN ;
    TextView COM_UP ;


    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buttons_control);



        PROJECT_CONTROL_TYPE = getIntent().getStringExtra("PROJECT_CONTROL_TYPE");
        PROJECT_NAME = getIntent().getStringExtra("PROJECT_NAME");
        PROJECT_CONTROL_DESCRIPTION = getIntent().getStringExtra("PROJECT_CONTROL_DESCRIPTION");
        PROJECT_STATE = getIntent().getStringExtra("PROJECT_STATE");
        AD_STATE = getIntent().getStringExtra("AD_STATE");


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        AdRequest adRequest_interstitial = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-5637187199850424/9384235892", adRequest_interstitial,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

        if(AD_STATE.equals("true")) {


            int min = 2*1000;
            new CountDownTimer(min, 1000) {
                public void onTick(long millisUntilFinished) { }
                public void onFinish() {

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(EditButtonsControlActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }

                }
            }.start();


        }


        txtv_project_name = findViewById(R.id.txtv_project_name);
        txtv_project_description = findViewById(R.id.txtv_project_description);
        btn_direction_up = findViewById(R.id.btn_direction_up);
        btn_direction_left = findViewById(R.id.btn_direction_left);
        btn_direction_center = findViewById(R.id.btn_direction_center);
        btn_direction_right = findViewById(R.id.btn_direction_right);
        btn_direction_down = findViewById(R.id.btn_direction_down);
        ic_done_ed_btn_direction_up = findViewById(R.id.ic_done_ed_btn_direction_up);
        ic_done_ed_btn_direction_left = findViewById(R.id.ic_done_ed_btn_direction_left);
        ic_done_ed_btn_direction_center = findViewById(R.id.ic_done_ed_btn_direction_center);
        ic_done_ed_btn_direction_right = findViewById(R.id.ic_done_ed_btn_direction_right);
        ic_done_ed_btn_direction_down = findViewById(R.id.ic_done_ed_btn_direction_down);
        ic_done_ed_btn_com1 = findViewById(R.id.ic_done_ed_btn_com1);
        ic_done_ed_btn_com2 = findViewById(R.id.ic_done_ed_btn_com2);
        ic_done_ed_btn_com3 = findViewById(R.id.ic_done_ed_btn_com3);
        ic_done_ed_btn_com4 = findViewById(R.id.ic_done_ed_btn_com4);
        ic_done_ed_btn_com5 = findViewById(R.id.ic_done_ed_btn_com5);
        ic_done_ed_btn_com6 = findViewById(R.id.ic_done_ed_btn_com6);
        edtxt_btn_direction_name = findViewById(R.id.edtxt_btn_direction_name);
        edtxt_btn_direction_command_down = findViewById(R.id.edtxt_btn_direction_command_down);
        edtxt_btn_direction_command_up = findViewById(R.id.edtxt_btn_direction_command_up);

        btn_commend_1 = findViewById(R.id.btn_commend_1);
        btn_commend_2 = findViewById(R.id.btn_commend_2);
        btn_commend_3 = findViewById(R.id.btn_commend_3);
        btn_commend_4 = findViewById(R.id.btn_commend_4);
        btn_commend_5 = findViewById(R.id.btn_commend_5);
        btn_commend_6 = findViewById(R.id.btn_commend_6);
        txtv_btn_command_1_name = findViewById(R.id.txtv_btn_command_1_name);
        txtv_btn_command_2_name = findViewById(R.id.txtv_btn_command_2_name);
        txtv_btn_command_3_name = findViewById(R.id.txtv_btn_command_3_name);
        txtv_btn_command_4_name = findViewById(R.id.txtv_btn_command_4_name);
        txtv_btn_command_5_name = findViewById(R.id.txtv_btn_command_5_name);
        txtv_btn_command_6_name = findViewById(R.id.txtv_btn_command_6_name);
        txtv_command_1_down = findViewById(R.id.txtv_command_1_down);
        txtv_command_2_down = findViewById(R.id.txtv_command_2_down);
        txtv_command_3_down = findViewById(R.id.txtv_command_3_down);
        txtv_command_4_down = findViewById(R.id.txtv_command_4_down);
        txtv_command_5_down = findViewById(R.id.txtv_command_5_down);
        txtv_command_6_down = findViewById(R.id.txtv_command_6_down);
        txtv_command_1_up = findViewById(R.id.txtv_command_1_up);
        txtv_command_2_up = findViewById(R.id.txtv_command_2_up);
        txtv_command_3_up = findViewById(R.id.txtv_command_3_up);
        txtv_command_4_up = findViewById(R.id.txtv_command_4_up);
        txtv_command_5_up = findViewById(R.id.txtv_command_5_up);
        txtv_command_6_up = findViewById(R.id.txtv_command_6_up);
        edtxt_btn_commands_name = findViewById(R.id.edtxt_btn_commands_name);
        edtxt_btn_commands_command_down = findViewById(R.id.edtxt_btn_commands_command_down);
        edtxt_btn_commands_command_up = findViewById(R.id.edtxt_btn_commands_command_up);
        btn_save_Project = findViewById(R.id.btn_save_Project);



        db = new DbConnction(EditButtonsControlActivity.this);
        try {
            db.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            db.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }


        txtv_project_name.setText(PROJECT_NAME);
        txtv_project_description.setText(PROJECT_CONTROL_DESCRIPTION);


        if(PROJECT_STATE.equals("NEW")) {


            db.InsertNewProjectButtonsControl(PROJECT_NAME, PROJECT_CONTROL_TYPE, PROJECT_CONTROL_DESCRIPTION, "Command1", "Command2", "Command3"
                    , "Command4", "Command5", "Command6", "UP", "Down", "Left", "Right", "Center", "Empty", "Empty", "Empty", "Empty"
                    , "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"
                    , "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty");

            db.InsertNewProjectDetails(PROJECT_NAME, PROJECT_CONTROL_TYPE, PROJECT_CONTROL_DESCRIPTION);

        }
        else if (PROJECT_STATE.equals("EDIT")){
            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(EditButtonsControlActivity.this, findViewById(android.R.id.content).getRootView(), EditButtonsControlActivity.this,"Edit Your Project");
        }



        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();

        txtv_btn_command_1_name.setText(c.getString(c.getColumnIndexOrThrow("btn_command_1_name")));
        txtv_btn_command_2_name.setText(c.getString(c.getColumnIndexOrThrow("btn_command_2_name")));
        txtv_btn_command_3_name.setText(c.getString(c.getColumnIndexOrThrow("btn_command_3_name")));
        txtv_btn_command_4_name.setText(c.getString(c.getColumnIndexOrThrow("btn_command_4_name")));
        txtv_btn_command_5_name.setText(c.getString(c.getColumnIndexOrThrow("btn_command_5_name")));
        txtv_btn_command_6_name.setText(c.getString(c.getColumnIndexOrThrow("btn_command_6_name")));

        txtv_command_1_down.setText(c.getString(c.getColumnIndexOrThrow("btn_com1_key_down")));
        txtv_command_2_down.setText(c.getString(c.getColumnIndexOrThrow("btn_com2_key_down")));
        txtv_command_3_down.setText(c.getString(c.getColumnIndexOrThrow("btn_com3_key_down")));
        txtv_command_4_down.setText(c.getString(c.getColumnIndexOrThrow("btn_com4_key_down")));
        txtv_command_5_down.setText(c.getString(c.getColumnIndexOrThrow("btn_com5_key_down")));
        txtv_command_6_down.setText(c.getString(c.getColumnIndexOrThrow("btn_com6_key_down")));

        txtv_command_1_up.setText(c.getString(c.getColumnIndexOrThrow("btn_com1_key_up")));
        txtv_command_2_up.setText(c.getString(c.getColumnIndexOrThrow("btn_com2_key_up")));
        txtv_command_3_up.setText(c.getString(c.getColumnIndexOrThrow("btn_com3_key_up")));
        txtv_command_4_up.setText(c.getString(c.getColumnIndexOrThrow("btn_com4_key_up")));
        txtv_command_5_up.setText(c.getString(c.getColumnIndexOrThrow("btn_com5_key_up")));
        txtv_command_6_up.setText(c.getString(c.getColumnIndexOrThrow("btn_com6_key_up")));






        btn_direction_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    IS_EDITING = true ;

                    COLUMN_FOR_NAME = "btn_up_key_name" ;
                    COLUMN_FOR_COMMAND_DOWN = "btn_up_key_down" ;
                    COLUMN_FOR_COMMAND_UP = "btn_up_key_up" ;
                    DEFAULT_BUTTON_NAME = "UP" ;
                    CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_direction_up ;

                HANDLE_SHOW_DIRECTION_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);
            }
        });

        btn_direction_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_left_key_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_left_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_left_key_up" ;
                DEFAULT_BUTTON_NAME = "LEFT" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_direction_left ;

                HANDLE_SHOW_DIRECTION_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);



            }
        });

        btn_direction_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_center_key_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_center_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_center_key_up" ;
                DEFAULT_BUTTON_NAME = "CENTER" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_direction_center ;

                HANDLE_SHOW_DIRECTION_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        btn_direction_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_right_key_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_right_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_right_key_up" ;
                DEFAULT_BUTTON_NAME = "RIGHT" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_direction_right ;

                HANDLE_SHOW_DIRECTION_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        btn_direction_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_down_key_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_down_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_down_key_up" ;
                DEFAULT_BUTTON_NAME = "DOWN" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_direction_down ;

                HANDLE_SHOW_DIRECTION_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });


        btn_commend_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_command_1_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_com1_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com1_key_up" ;
                DEFAULT_BUTTON_NAME = "Command1" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_com1 ;
                BTN_NAME = txtv_btn_command_1_name ;
                COM_DOWN = txtv_command_1_down ;
                COM_UP = txtv_command_1_up ;

                HANDLE_SHOW_COMMAND_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        btn_commend_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_command_2_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_com2_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com2_key_up" ;
                DEFAULT_BUTTON_NAME = "Command2" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_com2 ;
                BTN_NAME = txtv_btn_command_2_name ;
                COM_DOWN = txtv_command_2_down ;
                COM_UP = txtv_command_2_up ;

                HANDLE_SHOW_COMMAND_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        btn_commend_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_command_3_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_com3_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com3_key_up" ;
                DEFAULT_BUTTON_NAME = "Command3" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_com3 ;
                BTN_NAME = txtv_btn_command_3_name ;
                COM_DOWN = txtv_command_3_down ;
                COM_UP = txtv_command_3_up ;

                HANDLE_SHOW_COMMAND_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        btn_commend_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_command_4_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_com4_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com4_key_up" ;
                DEFAULT_BUTTON_NAME = "Command4" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_com4 ;
                BTN_NAME = txtv_btn_command_4_name ;
                COM_DOWN = txtv_command_4_down ;
                COM_UP = txtv_command_4_up ;

                HANDLE_SHOW_COMMAND_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        btn_commend_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_command_5_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_com5_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com5_key_up" ;
                DEFAULT_BUTTON_NAME = "Command5" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_com5 ;
                BTN_NAME = txtv_btn_command_5_name ;
                COM_DOWN = txtv_command_5_down ;
                COM_UP = txtv_command_5_up ;

                HANDLE_SHOW_COMMAND_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        btn_commend_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;

                COLUMN_FOR_NAME = "btn_command_6_name" ;
                COLUMN_FOR_COMMAND_DOWN = "btn_com6_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com6_key_up" ;
                DEFAULT_BUTTON_NAME = "Command6" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_ed_btn_com6 ;
                BTN_NAME = txtv_btn_command_6_name ;
                COM_DOWN = txtv_command_6_down ;
                COM_UP = txtv_command_6_up ;

                HANDLE_SHOW_COMMAND_BUTTON_DATA(COLUMN_FOR_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });


        edtxt_btn_direction_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_DIRECTION_KEYS(COLUMN_FOR_NAME,edtxt_btn_direction_name,CURRENT_IC_DONE_ED_BTN,
                        COLUMN_FOR_NAME,DEFAULT_BUTTON_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });


        edtxt_btn_direction_command_down.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_DIRECTION_KEYS(COLUMN_FOR_COMMAND_DOWN,edtxt_btn_direction_command_down,CURRENT_IC_DONE_ED_BTN,
                        COLUMN_FOR_NAME,DEFAULT_BUTTON_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        edtxt_btn_direction_command_up.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_DIRECTION_KEYS(COLUMN_FOR_COMMAND_UP,edtxt_btn_direction_command_up,CURRENT_IC_DONE_ED_BTN,
                        COLUMN_FOR_NAME,DEFAULT_BUTTON_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP);

            }
        });

        edtxt_btn_commands_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_COMMANDS_KEYS(COLUMN_FOR_NAME,edtxt_btn_commands_name,CURRENT_IC_DONE_ED_BTN,
                        COLUMN_FOR_NAME,DEFAULT_BUTTON_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP,BTN_NAME,COM_DOWN,COM_UP);

            }
        });

        edtxt_btn_commands_command_down.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_COMMANDS_KEYS(COLUMN_FOR_COMMAND_DOWN,edtxt_btn_commands_command_down,CURRENT_IC_DONE_ED_BTN,
                        COLUMN_FOR_NAME,DEFAULT_BUTTON_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP,BTN_NAME,COM_DOWN,COM_UP);

            }
        });


        edtxt_btn_commands_command_up.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_COMMANDS_KEYS(COLUMN_FOR_COMMAND_UP,edtxt_btn_commands_command_up,CURRENT_IC_DONE_ED_BTN,
                        COLUMN_FOR_NAME,DEFAULT_BUTTON_NAME,COLUMN_FOR_COMMAND_DOWN,COLUMN_FOR_COMMAND_UP,BTN_NAME,COM_DOWN,COM_UP);

            }
        });

        btn_save_Project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(EditButtonsControlActivity.this, FindBluetoothActivity.class);
                intent.putExtra("PROJECT_NAME",PROJECT_NAME);
                intent.putExtra("PROJECT_CONTROL_TYPE",PROJECT_CONTROL_TYPE);
                startActivity(intent);
                finish();

            }
        });



    }

    private void HANDLE_DIRECTION_KEYS(String TARGET_COLUMN ,EditText TARGET_EDITTEXT,ImageView TARGET_DONE_EDITING_IMGV,String BUTTON_CAL_NAME,String DEFAULT_BUTTON_NAME,String BUTTON_CAL_DOWN_COMM,String BUTTON_CAL_UP_COMM) {

        if(!IS_EDITING) {

            db.UpdateButtonControlRecord(TARGET_COLUMN,TARGET_EDITTEXT.getText().toString(), PROJECT_NAME);

            if(edtxt_btn_direction_command_down.getText().toString().equals("") && edtxt_btn_direction_command_up.getText().toString().equals("")
            || edtxt_btn_direction_command_down.getText().toString().equals("Empty") && edtxt_btn_direction_command_up.getText().toString().equals("Empty")
            || edtxt_btn_direction_command_down.getText().toString().equals("") && edtxt_btn_direction_command_up.getText().toString().equals("Empty")
            || edtxt_btn_direction_command_down.getText().toString().equals("Empty") && edtxt_btn_direction_command_up.getText().toString().equals("")){

                TARGET_DONE_EDITING_IMGV.setVisibility(View.INVISIBLE);
            }
            else {
                TARGET_DONE_EDITING_IMGV.setVisibility(View.VISIBLE);
            }


        }else {
            IS_EDITING = false;
        }

        if(edtxt_btn_direction_name.getText().toString().equals("")){
            db.UpdateButtonControlRecord(BUTTON_CAL_NAME, DEFAULT_BUTTON_NAME, PROJECT_NAME);
        }
        if(edtxt_btn_direction_command_down.getText().toString().equals("")){
            db.UpdateButtonControlRecord(BUTTON_CAL_DOWN_COMM, "Empty", PROJECT_NAME);
        }
        if(edtxt_btn_direction_command_up.getText().toString().equals("")){
            db.UpdateButtonControlRecord(BUTTON_CAL_UP_COMM, "Empty", PROJECT_NAME);
        }

    }

    private void HANDLE_SHOW_DIRECTION_BUTTON_DATA(String BUTTON_NAME, String COMMAND_DOWN, String COMMAND_UP){
        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();

        edtxt_btn_direction_name.setText(c.getString(c.getColumnIndexOrThrow(BUTTON_NAME)));
        edtxt_btn_direction_command_down.setText(c.getString(c.getColumnIndexOrThrow(COMMAND_DOWN)));
        edtxt_btn_direction_command_up.setText(c.getString(c.getColumnIndexOrThrow(COMMAND_UP)));

        ENABLE_DIR_EDIT_TEXT();

    }


    private void HANDLE_COMMANDS_KEYS(String TARGET_COLUMN ,EditText TARGET_EDITTEXT,ImageView TARGET_DONE_EDITING_IMGV,String BUTTON_CAL_NAME,String DEFAULT_BUTTON_NAME,String BUTTON_CAL_DOWN_COMM,String BUTTON_CAL_UP_COMM,TextView BTN_NAME,TextView COM_DOWN,TextView COM_UP) {

        if(!IS_EDITING) {

            db.UpdateButtonControlRecord(TARGET_COLUMN,TARGET_EDITTEXT.getText().toString(), PROJECT_NAME);

            if(edtxt_btn_commands_command_down.getText().toString().equals("") && edtxt_btn_commands_command_up.getText().toString().equals("")
                    || edtxt_btn_commands_command_down.getText().toString().equals("Empty") && edtxt_btn_commands_command_up.getText().toString().equals("Empty")
                    || edtxt_btn_commands_command_down.getText().toString().equals("") && edtxt_btn_commands_command_up.getText().toString().equals("Empty")
                    || edtxt_btn_commands_command_down.getText().toString().equals("Empty") && edtxt_btn_commands_command_up.getText().toString().equals("")){

                TARGET_DONE_EDITING_IMGV.setVisibility(View.INVISIBLE);
            }
            else {
                TARGET_DONE_EDITING_IMGV.setVisibility(View.VISIBLE);
            }


        }else {
            IS_EDITING = false;
        }

       if(edtxt_btn_commands_name.getText().toString().equals("")){
           db.UpdateButtonControlRecord(BUTTON_CAL_NAME, DEFAULT_BUTTON_NAME, PROJECT_NAME);
       }
       if(edtxt_btn_commands_command_down.getText().toString().equals("")){
           db.UpdateButtonControlRecord(BUTTON_CAL_DOWN_COMM, "Empty", PROJECT_NAME);
       }
       if(edtxt_btn_commands_command_up.getText().toString().equals("")){
           db.UpdateButtonControlRecord(BUTTON_CAL_UP_COMM, "Empty", PROJECT_NAME);
       }

        BTN_NAME.setText(edtxt_btn_commands_name.getText().toString());
        COM_DOWN.setText(edtxt_btn_commands_command_down.getText().toString());
        COM_UP.setText(edtxt_btn_commands_command_up.getText().toString());
    }

    private void HANDLE_SHOW_COMMAND_BUTTON_DATA(String BUTTON_NAME, String COMMAND_DOWN, String COMMAND_UP){
        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();

        edtxt_btn_commands_name.setText(c.getString(c.getColumnIndexOrThrow(BUTTON_NAME)));
        edtxt_btn_commands_command_down.setText(c.getString(c.getColumnIndexOrThrow(COMMAND_DOWN)));
        edtxt_btn_commands_command_up.setText(c.getString(c.getColumnIndexOrThrow(COMMAND_UP)));

        ENABLE_COM_EDIT_TEXT();
    }

    private void ENABLE_DIR_EDIT_TEXT(){

        edtxt_btn_direction_name.setEnabled(true);
        edtxt_btn_direction_command_down.setEnabled(true);
        edtxt_btn_direction_command_up.setEnabled(true);

    }

    private void ENABLE_COM_EDIT_TEXT() {

        edtxt_btn_commands_name.setEnabled(true);
        edtxt_btn_commands_command_down.setEnabled(true);
        edtxt_btn_commands_command_up.setEnabled(true);

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditButtonsControlActivity.this, NewProjectChooseActivity.class);
        intent.putExtra("AD_STATE","false" );
        startActivity(intent);
        finish();
    }








}