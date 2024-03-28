package safisoft.roboboy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
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
import java.util.ArrayList;
import java.util.Locale;

public class EditVoiceControlActivity extends AppCompatActivity {


    ImageButton btn_record_voice_edit ;
    EditText edtxt_record_voice_edit ;
    EditText edtxt_send_command ;
    ImageButton btn_save_voice_1 ;
    ImageButton btn_save_voice_2 ;
    ImageButton btn_save_voice_3 ;
    ImageButton btn_save_voice_4 ;
    ImageButton btn_save_voice_5 ;
    ImageButton btn_save_voice_6 ;
    ImageButton btn_save_voice_7 ;
    ImageButton btn_save_voice_8 ;
    ImageButton btn_save_voice_9 ;
    TextView txtv_project_name ;
    TextView txtv_project_description ;
    ImageView ic_done_edit_voice_1 ;
    ImageView ic_done_edit_voice_2 ;
    ImageView ic_done_edit_voice_3 ;
    ImageView ic_done_edit_voice_4 ;
    ImageView ic_done_edit_voice_5 ;
    ImageView ic_done_edit_voice_6 ;
    ImageView ic_done_edit_voice_7 ;
    ImageView ic_done_edit_voice_8 ;
    ImageView ic_done_edit_voice_9 ;
    ImageButton btn_save_Project ;







    DbConnction db ;
    Cursor c = null;
    String PROJECT_CONTROL_TYPE ;
    String PROJECT_NAME ;
    String PROJECT_CONTROL_DESCRIPTION ;
    String PROJECT_STATE ;
    String AD_STATE ;

    boolean IS_EDITING ;
    String COLUMN_VOICE_COMMAND;
    String COLUMN_SEND_COMMAND;
    ImageView CURRENT_IC_DONE_ED_BTN ;

    private InterstitialAd mInterstitialAd;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_voice_control);




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
                        mInterstitialAd.show(EditVoiceControlActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }

                }
            }.start();


        }



        btn_record_voice_edit = findViewById(R.id.btn_record_voice_edit);
        edtxt_record_voice_edit =findViewById(R.id.edtxt_record_voice_edit);
        edtxt_send_command = findViewById(R.id.edtxt_send_command);
        btn_save_voice_1 = findViewById(R.id.btn_save_voice_1);
        btn_save_voice_2 = findViewById(R.id.btn_save_voice_2);
        btn_save_voice_3 = findViewById(R.id.btn_save_voice_3);
        btn_save_voice_4 = findViewById(R.id.btn_save_voice_4);
        btn_save_voice_5 = findViewById(R.id.btn_save_voice_5);
        btn_save_voice_6 = findViewById(R.id.btn_save_voice_6);
        btn_save_voice_7 = findViewById(R.id.btn_save_voice_7);
        btn_save_voice_8 = findViewById(R.id.btn_save_voice_8);
        btn_save_voice_9 = findViewById(R.id.btn_save_voice_9);
        txtv_project_name = findViewById(R.id.txtv_project_name);
        txtv_project_description = findViewById(R.id.txtv_project_description);
        ic_done_edit_voice_1 = findViewById(R.id.ic_done_edit_voice_1);
        ic_done_edit_voice_2 = findViewById(R.id.ic_done_edit_voice_2);
        ic_done_edit_voice_3 = findViewById(R.id.ic_done_edit_voice_3);
        ic_done_edit_voice_4 = findViewById(R.id.ic_done_edit_voice_4);
        ic_done_edit_voice_5 = findViewById(R.id.ic_done_edit_voice_5);
        ic_done_edit_voice_6 = findViewById(R.id.ic_done_edit_voice_6);
        ic_done_edit_voice_7 = findViewById(R.id.ic_done_edit_voice_7);
        ic_done_edit_voice_8 = findViewById(R.id.ic_done_edit_voice_8);
        ic_done_edit_voice_9 = findViewById(R.id.ic_done_edit_voice_9);
        btn_save_Project = findViewById(R.id.btn_save_Project);




        db = new DbConnction(EditVoiceControlActivity.this);
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
        btn_record_voice_edit.setEnabled(false);


        if(PROJECT_STATE.equals("NEW")) {

            db.InsertNewProjectVoiceControl(PROJECT_NAME, PROJECT_CONTROL_TYPE, PROJECT_CONTROL_DESCRIPTION, "Empty", "Empty", "Empty", "Empty"
                    , "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"
                    , "Empty", "Empty", "Empty", "Empty", "Empty");

            db.InsertNewProjectDetails(PROJECT_NAME, PROJECT_CONTROL_TYPE, PROJECT_CONTROL_DESCRIPTION);

        }
        else if (PROJECT_STATE.equals("EDIT")){
            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(EditVoiceControlActivity.this, findViewById(android.R.id.content).getRootView(), EditVoiceControlActivity.this,"Edit Your Project");
        }

        c = db.Row_Query("Project_Voice_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();


     if(!c.getString(c.getColumnIndexOrThrow("voice_1")).equals("Empty")){ic_done_edit_voice_1.setVisibility(View.VISIBLE);}
     if(!c.getString(c.getColumnIndexOrThrow("voice_2")).equals("Empty")){ic_done_edit_voice_2.setVisibility(View.VISIBLE);}
     if(!c.getString(c.getColumnIndexOrThrow("voice_3")).equals("Empty")){ic_done_edit_voice_3.setVisibility(View.VISIBLE);}
     if(!c.getString(c.getColumnIndexOrThrow("voice_4")).equals("Empty")){ic_done_edit_voice_4.setVisibility(View.VISIBLE);}
     if(!c.getString(c.getColumnIndexOrThrow("voice_5")).equals("Empty")){ic_done_edit_voice_5.setVisibility(View.VISIBLE);}
     if(!c.getString(c.getColumnIndexOrThrow("voice_6")).equals("Empty")){ic_done_edit_voice_6.setVisibility(View.VISIBLE);}
     if(!c.getString(c.getColumnIndexOrThrow("voice_7")).equals("Empty")){ic_done_edit_voice_7.setVisibility(View.VISIBLE);}
     if(!c.getString(c.getColumnIndexOrThrow("voice_8")).equals("Empty")){ic_done_edit_voice_8.setVisibility(View.VISIBLE);}
     if(!c.getString(c.getColumnIndexOrThrow("voice_9")).equals("Empty")){ic_done_edit_voice_9.setVisibility(View.VISIBLE);}

        btn_save_voice_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_1" ;
                COLUMN_SEND_COMMAND = "com_1" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_1 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);
            }
        });

        btn_save_voice_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_2" ;
                COLUMN_SEND_COMMAND = "com_2" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_2 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);
            }
        });

        btn_save_voice_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_3" ;
                COLUMN_SEND_COMMAND = "com_3" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_3 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);
            }
        });

        btn_save_voice_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_4" ;
                COLUMN_SEND_COMMAND = "com_4" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_4 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);

            }
        });

        btn_save_voice_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_5" ;
                COLUMN_SEND_COMMAND = "com_5" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_5 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);

            }
        });

        btn_save_voice_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_6" ;
                COLUMN_SEND_COMMAND = "com_6" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_6 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);

            }
        });

        btn_save_voice_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_7" ;
                COLUMN_SEND_COMMAND = "com_7" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_7 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);

            }
        });

        btn_save_voice_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_8" ;
                COLUMN_SEND_COMMAND = "com_8" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_8 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);

            }
        });

        btn_save_voice_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IS_EDITING = true ;
                COLUMN_VOICE_COMMAND = "voice_9" ;
                COLUMN_SEND_COMMAND = "com_9" ;
                CURRENT_IC_DONE_ED_BTN = ic_done_edit_voice_9 ;

                HANDLE_SHOW_VOICE_BUTTON_DATA(COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);

            }
        });

        btn_save_Project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditVoiceControlActivity.this, FindBluetoothActivity.class);
                intent.putExtra("PROJECT_NAME",PROJECT_NAME);
                intent.putExtra("PROJECT_CONTROL_TYPE",PROJECT_CONTROL_TYPE);
                startActivity(intent);
                finish();

            }
        });



        btn_record_voice_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        });


        edtxt_record_voice_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_DIRECTION_KEYS(COLUMN_VOICE_COMMAND,edtxt_record_voice_edit,CURRENT_IC_DONE_ED_BTN,
                        COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);

            }
        });


        edtxt_send_command.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_DIRECTION_KEYS(COLUMN_SEND_COMMAND,edtxt_send_command,CURRENT_IC_DONE_ED_BTN,
                        COLUMN_VOICE_COMMAND,COLUMN_SEND_COMMAND);

            }
        });


    }



    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
          //  Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(EditVoiceControlActivity.this, findViewById(android.R.id.content).getRootView(), EditVoiceControlActivity.this,"Your Device Don't Support Speech Input");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                   // Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();
                    edtxt_record_voice_edit.setText(result.get(0));

                }
                break;
        }
    }

    private void HANDLE_SHOW_VOICE_BUTTON_DATA(String VOICE_COMMAND, String SEND_COMMAND){
        c = db.Row_Query("Project_Voice_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();

        edtxt_record_voice_edit.setText(c.getString(c.getColumnIndexOrThrow(VOICE_COMMAND)));
        edtxt_send_command.setText(c.getString(c.getColumnIndexOrThrow(SEND_COMMAND)));

        ENABLE_EDIT_TEXT();
    }

    private void HANDLE_DIRECTION_KEYS(String TARGET_COLUMN ,EditText TARGET_EDITTEXT,ImageView TARGET_DONE_EDITING_IMGV,String BUTTON_CAL_VOICE_COMM,String BUTTON_CAL_SEND_COMM) {

        if(!IS_EDITING) {

            db.UpdateVoiceControlRecord(TARGET_COLUMN,TARGET_EDITTEXT.getText().toString(), PROJECT_NAME);

            if(edtxt_record_voice_edit.getText().toString().equals("") && edtxt_send_command.getText().toString().equals("")
                    || edtxt_record_voice_edit.getText().toString().equals("Empty") && edtxt_send_command.getText().toString().equals("Empty")
                    || edtxt_record_voice_edit.getText().toString().equals("") && edtxt_send_command.getText().toString().equals("Empty")
                    || edtxt_record_voice_edit.getText().toString().equals("Empty") && edtxt_send_command.getText().toString().equals("")){

                TARGET_DONE_EDITING_IMGV.setVisibility(View.INVISIBLE);
            }
            else {
                TARGET_DONE_EDITING_IMGV.setVisibility(View.VISIBLE);
            }


        }else {
            IS_EDITING = false;
        }


        if(edtxt_record_voice_edit.getText().toString().equals("")){
            db.UpdateVoiceControlRecord(BUTTON_CAL_VOICE_COMM, "Empty", PROJECT_NAME);
        }
        if(edtxt_send_command.getText().toString().equals("")){
            db.UpdateVoiceControlRecord(BUTTON_CAL_SEND_COMM, "Empty", PROJECT_NAME);
        }

    }

    private void ENABLE_EDIT_TEXT(){
        edtxt_record_voice_edit.setEnabled(true);
        edtxt_send_command.setEnabled(true);
        btn_record_voice_edit.setEnabled(true);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditVoiceControlActivity.this, NewProjectChooseActivity.class);
        intent.putExtra("AD_STATE","false" );
        startActivity(intent);
        finish();
    }


}