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
import android.widget.LinearLayout;
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

public class EditRotationControlActivity extends AppCompatActivity {

    LinearLayout lay_phone_rotation_pointer ;
    ImageButton btn_rotation_min ;
    ImageButton btn_rotation_plus;
    TextView txtv_current_degree ;
    ImageView ic_done_edit_min_20 ;
    ImageView ic_done_edit_min_40 ;
    ImageView ic_done_edit_min_60 ;
    ImageView ic_done_edit_min_80 ;
    ImageView ic_done_edit_min_100 ;
    ImageView ic_done_edit_min_120 ;
    ImageView ic_done_edit_min_140 ;
    ImageView ic_done_edit_min_160 ;
    ImageView ic_done_edit_min_180 ;
    ImageView ic_done_edit_plus_20 ;
    ImageView ic_done_edit_plus_40 ;
    ImageView ic_done_edit_plus_60 ;
    ImageView ic_done_edit_plus_80 ;
    ImageView ic_done_edit_plus_100 ;
    ImageView ic_done_edit_plus_120 ;
    ImageView ic_done_edit_plus_140 ;
    ImageView ic_done_edit_plus_160 ;
    ImageView ic_done_edit_plus_180 ;
    TextView txtv_project_name ;
    TextView txtv_project_description ;
    EditText edtxt_rotation_degree_command ;
    ImageButton btn_save_Project ;




    int LAY_ROTATION_POSS = 10 ;
    int FIRST_VAL;
    int LAST_VAL;
    DbConnction db ;
    Cursor c = null;
    String PROJECT_CONTROL_TYPE ;
    String PROJECT_NAME ;
    String PROJECT_CONTROL_DESCRIPTION ;
    String PROJECT_STATE ;
    String AD_STATE ;

    ImageView TARGET_DONE_EDITING ;
    String TARGET_COLUMN ;
    boolean IS_EDITING ;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rotation_control);



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
                        mInterstitialAd.show(EditRotationControlActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }

                }
            }.start();


        }



        lay_phone_rotation_pointer =findViewById(R.id.lay_phone_rotation_pointer);
        btn_rotation_min = findViewById(R.id.btn_rotation_min);
        btn_rotation_plus = findViewById(R.id.btn_rotation_plus);
        txtv_current_degree = findViewById(R.id.txtv_current_degree);
        txtv_project_name = findViewById(R.id.txtv_project_name);
        txtv_project_description = findViewById(R.id.txtv_project_description);
        edtxt_rotation_degree_command = findViewById(R.id.edtxt_rotation_degree_command);
        btn_save_Project = findViewById(R.id.btn_save_Project);

        ic_done_edit_min_20 = findViewById(R.id.ic_done_edit_min_20);
        ic_done_edit_min_40 = findViewById(R.id.ic_done_edit_min_40);
        ic_done_edit_min_60 = findViewById(R.id.ic_done_edit_min_60);
        ic_done_edit_min_80 = findViewById(R.id.ic_done_edit_min_80);
        ic_done_edit_min_100 = findViewById(R.id.ic_done_edit_min_100);
        ic_done_edit_min_120 = findViewById(R.id.ic_done_edit_min_120);
        ic_done_edit_min_140 = findViewById(R.id.ic_done_edit_min_140);
        ic_done_edit_min_160 = findViewById(R.id.ic_done_edit_min_160);
        ic_done_edit_min_180 = findViewById(R.id.ic_done_edit_min_180);
        ic_done_edit_plus_20 = findViewById(R.id.ic_done_edit_plus_20);
        ic_done_edit_plus_40 = findViewById(R.id.ic_done_edit_plus_40);
        ic_done_edit_plus_60 = findViewById(R.id.ic_done_edit_plus_60);
        ic_done_edit_plus_80 = findViewById(R.id.ic_done_edit_plus_80);
        ic_done_edit_plus_100 = findViewById(R.id.ic_done_edit_plus_100);
        ic_done_edit_plus_120 = findViewById(R.id.ic_done_edit_plus_120);
        ic_done_edit_plus_140 = findViewById(R.id.ic_done_edit_plus_140);
        ic_done_edit_plus_160 = findViewById(R.id.ic_done_edit_plus_160);
        ic_done_edit_plus_180 = findViewById(R.id.ic_done_edit_plus_180);




        db = new DbConnction(EditRotationControlActivity.this);
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


            db.InsertNewProjectRotationControl(PROJECT_NAME, PROJECT_CONTROL_TYPE, PROJECT_CONTROL_DESCRIPTION, "Empty", "Empty", "Empty", "Empty"
                    , "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"
                    , "Empty", "Empty", "Empty", "Empty", "Empty");

            db.InsertNewProjectDetails(PROJECT_NAME, PROJECT_CONTROL_TYPE, PROJECT_CONTROL_DESCRIPTION);

        }
        else if (PROJECT_STATE.equals("EDIT")){
            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(EditRotationControlActivity.this, findViewById(android.R.id.content).getRootView(), EditRotationControlActivity.this,"Edit Your Project");
        }



        c = db.Row_Query("Project_Rotation_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();



        if(!c.getString(c.getColumnIndexOrThrow("min_20")).equals("Empty")){ic_done_edit_min_20.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("min_40")).equals("Empty")){ic_done_edit_min_40.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("min_60")).equals("Empty")){ic_done_edit_min_60.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("min_80")).equals("Empty")){ic_done_edit_min_80.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("min_100")).equals("Empty")){ic_done_edit_min_100.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("min_120")).equals("Empty")){ic_done_edit_min_120.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("min_140")).equals("Empty")){ic_done_edit_min_140.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("min_160")).equals("Empty")){ic_done_edit_min_160.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("min_180")).equals("Empty")){ic_done_edit_min_180.setVisibility(View.VISIBLE);}

        if(!c.getString(c.getColumnIndexOrThrow("plus_20")).equals("Empty")){ic_done_edit_plus_20.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("plus_40")).equals("Empty")){ic_done_edit_plus_40.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("plus_60")).equals("Empty")){ic_done_edit_plus_60.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("plus_80")).equals("Empty")){ic_done_edit_plus_80.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("plus_100")).equals("Empty")){ic_done_edit_plus_100.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("plus_120")).equals("Empty")){ic_done_edit_plus_120.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("plus_140")).equals("Empty")){ic_done_edit_plus_140.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("plus_160")).equals("Empty")){ic_done_edit_plus_160.setVisibility(View.VISIBLE);}
        if(!c.getString(c.getColumnIndexOrThrow("plus_180")).equals("Empty")){ic_done_edit_plus_180.setVisibility(View.VISIBLE);}

        edtxt_rotation_degree_command.setText(c.getString(c.getColumnIndexOrThrow("plus_20")));
        TARGET_COLUMN = "plus_20";
        TARGET_DONE_EDITING = ic_done_edit_plus_20;




        btn_rotation_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_EDITING = true ;

                LAY_ROTATION_POSS = LAY_ROTATION_POSS - 20 ;
                lay_phone_rotation_pointer.setRotation(LAY_ROTATION_POSS);
                FIRST_VAL = LAY_ROTATION_POSS-10 ;
                LAST_VAL = LAY_ROTATION_POSS+10 ;
                btn_rotation_plus.setEnabled(true);

                if (FIRST_VAL == -180){
                    btn_rotation_min.setEnabled(false);
                }
                txtv_current_degree.setText(Integer.toString(FIRST_VAL)+"°"+" to "+Integer.toString(LAST_VAL)+"°");
                HANDLE_ROTATION_DATA_POSS();

            }
        });

        btn_rotation_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_EDITING = true ;

                LAY_ROTATION_POSS = LAY_ROTATION_POSS + 20 ;
                lay_phone_rotation_pointer.setRotation(LAY_ROTATION_POSS);

                FIRST_VAL = LAY_ROTATION_POSS-10 ;
                LAST_VAL = LAY_ROTATION_POSS+10 ;
                btn_rotation_min.setEnabled(true);

                if (LAST_VAL == 180){
                    btn_rotation_plus.setEnabled(false);
                }
                txtv_current_degree.setText(Integer.toString(FIRST_VAL)+"°"+" to "+Integer.toString(LAST_VAL)+"°");
                HANDLE_ROTATION_DATA_POSS();

            }
        });

        edtxt_rotation_degree_command.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                HANDLE_ROTATION_COMMAND(TARGET_COLUMN,TARGET_DONE_EDITING);

            }
        });


        btn_save_Project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditRotationControlActivity.this, FindBluetoothActivity.class);
                intent.putExtra("PROJECT_NAME",PROJECT_NAME);
                intent.putExtra("PROJECT_CONTROL_TYPE",PROJECT_CONTROL_TYPE);
                startActivity(intent);
                finish();

            }
        });



    }




    private void HANDLE_ROTATION_COMMAND(String TARGET_COLUMN ,ImageView TARGET_DONE_EDITING_IMGV) {

        if(!IS_EDITING) {

            db.UpdateRotationControlRecord(TARGET_COLUMN,edtxt_rotation_degree_command.getText().toString(), PROJECT_NAME);

            if(edtxt_rotation_degree_command.getText().toString().equals("") || edtxt_rotation_degree_command.getText().toString().equals("Empty")) {

                TARGET_DONE_EDITING_IMGV.setVisibility(View.INVISIBLE);
                db.UpdateRotationControlRecord(TARGET_COLUMN, "Empty", PROJECT_NAME);
            }
            else {
                TARGET_DONE_EDITING_IMGV.setVisibility(View.VISIBLE);
            }

        }else {
            IS_EDITING = false;
        }



    }

    private void HANDLE_ROTATION_DATA_POSS(){

        if(LAST_VAL == 180){TARGET_COLUMN = "plus_180"; TARGET_DONE_EDITING = ic_done_edit_plus_180; }
        if(LAST_VAL == 160){TARGET_COLUMN = "plus_160"; TARGET_DONE_EDITING = ic_done_edit_plus_160; }
        if(LAST_VAL == 140){TARGET_COLUMN = "plus_140"; TARGET_DONE_EDITING = ic_done_edit_plus_140; }
        if(LAST_VAL == 120){TARGET_COLUMN = "plus_120"; TARGET_DONE_EDITING = ic_done_edit_plus_120; }
        if(LAST_VAL == 100){TARGET_COLUMN = "plus_100"; TARGET_DONE_EDITING = ic_done_edit_plus_100; }
        if(LAST_VAL == 80){TARGET_COLUMN = "plus_80"; TARGET_DONE_EDITING = ic_done_edit_plus_80;    }
        if(LAST_VAL == 60){TARGET_COLUMN = "plus_60"; TARGET_DONE_EDITING = ic_done_edit_plus_60;    }
        if(LAST_VAL == 40){TARGET_COLUMN = "plus_40"; TARGET_DONE_EDITING = ic_done_edit_plus_40;    }
        if(LAST_VAL == 20){TARGET_COLUMN = "plus_20"; TARGET_DONE_EDITING = ic_done_edit_plus_20;    }
        if(LAST_VAL == -0){TARGET_COLUMN = "min_20"; TARGET_DONE_EDITING = ic_done_edit_min_20;      }
        if(LAST_VAL == -20){TARGET_COLUMN = "min_40"; TARGET_DONE_EDITING = ic_done_edit_min_40;     }
        if(LAST_VAL == -40){TARGET_COLUMN = "min_60"; TARGET_DONE_EDITING = ic_done_edit_min_60;     }
        if(LAST_VAL == -60){TARGET_COLUMN = "min_80"; TARGET_DONE_EDITING = ic_done_edit_min_80;     }
        if(LAST_VAL == -80){TARGET_COLUMN = "min_100"; TARGET_DONE_EDITING = ic_done_edit_min_100;   }
        if(LAST_VAL == -100){TARGET_COLUMN = "min_120"; TARGET_DONE_EDITING = ic_done_edit_min_120;  }
        if(LAST_VAL == -120){TARGET_COLUMN = "min_140"; TARGET_DONE_EDITING = ic_done_edit_min_140;  }
        if(LAST_VAL == -140){TARGET_COLUMN = "min_160"; TARGET_DONE_EDITING = ic_done_edit_min_160;  }
        if(LAST_VAL == -160){TARGET_COLUMN = "min_180"; TARGET_DONE_EDITING = ic_done_edit_min_180;  }

        c = db.Row_Query("Project_Rotation_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();
        edtxt_rotation_degree_command.setText(c.getString(c.getColumnIndexOrThrow(TARGET_COLUMN)));

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditRotationControlActivity.this, NewProjectChooseActivity.class);
        intent.putExtra("AD_STATE","false" );
        startActivity(intent);
        finish();
    }




}