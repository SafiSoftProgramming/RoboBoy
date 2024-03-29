package safisoft.roboboy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED;
import static com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdInspectorError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnAdInspectorClosedListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewProjectChooseActivity extends AppCompatActivity {

    Cursor c = null;
    DbConnction db ;
    List<Recent_Projects> Recent_Projects_List;
    RecyclerView recyclerView;
    Recent_Projects_Adapter recentProjectsAdapter;
    ImageButton btn_new_project ;

    ImageButton btn_Serial_Monitor ;
    ImageButton btn_more_new ;
    LinearLayout lay_no_data ;


    ReviewManager reviewManager ;
    ReviewInfo reviewInfo = null;
    private InterstitialAd mInterstitialAd;


    private AdView mAdView;


    //in app update
    private AppUpdateManager appUpdateManager;
    private static final int MY_REQUEST_CODE = 17326;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_choose);

          Initialize_ads();   

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn_new_project = findViewById(R.id.btn_new_project);
        btn_Serial_Monitor = findViewById(R.id.btn_Serial_Monitor);
        btn_more_new = findViewById(R.id.btn_more_new);
        lay_no_data = findViewById(R.id.lay_no_data);

        db = new DbConnction(NewProjectChooseActivity.this);
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


        btn_new_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NewProjectChooseActivity.this, StartNewProjectActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_Serial_Monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewProjectChooseActivity.this, FindBluetoothActivity.class);
                intent.putExtra("PROJECT_NAME","Serial Monitor");
                intent.putExtra("PROJECT_CONTROL_TYPE","Serial Monitor");
                startActivity(intent);
            }
        });

        btn_more_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MoreNewDialog moreNewDialog = new MoreNewDialog(NewProjectChooseActivity.this);
                moreNewDialog.show();
                moreNewDialog.setCanceledOnTouchOutside(false);
                moreNewDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                moreNewDialog.btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moreNewDialog.dismiss();
                    }
                });



            }
        });

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                checkUpdate();
            }
        }.start();


        Recent_Projects_List = new ArrayList<>();
        c = db.Query_Data("Projects_List", null, null, null, null, null, null);
        c =  db.sortRecord_id();
        c.moveToFirst();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Recent_Projects_List.add(new Recent_Projects(c.getInt(0),c.getString(1),c.getString(2),c.getString(3)));
            }while (c.moveToNext());
        }


        recentProjectsAdapter = new Recent_Projects_Adapter(NewProjectChooseActivity.this,Recent_Projects_List);
        recyclerView.setAdapter(recentProjectsAdapter);

        if(Recent_Projects_List.isEmpty()){
            lay_no_data.setVisibility(View.VISIBLE);
        }
        else {
            lay_no_data.setVisibility(View.GONE);
        }


      // new CountDownTimer(1000, 1000) {
      //     public void onTick(long millisUntilFinished) { }
      //     public void onFinish() {
      //         Initialize_ads();
      //     }
      // }.start();


        if(PROJECT_COUNT() > 0){
            int min = 3*1000;
            new CountDownTimer(min, 1000) {
                public void onTick(long millisUntilFinished) { }
                public void onFinish() {
                    StartInAppReview();

                }
            }.start();
        }



    }



    private int PROJECT_COUNT(){
        c = db.Query_Data("Projects_List", null, null, null, null, null, null);
        c.moveToLast();
        return c.getCount();
    }





    public void StartInAppReview(){

            ReviewManager manager = ReviewManagerFactory.create(NewProjectChooseActivity.this);

            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                try {
                    if (task.isSuccessful()) {
                        // We can get the ReviewInfo object
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(NewProjectChooseActivity.this, reviewInfo);
                        flow.addOnCompleteListener(task2 -> {
                            // The flow has finished. The API does not indicate whether the user
                            // reviewed or not, or even whether the review dialog was shown. Thus, no
                            // matter the result, we continue our app flow.
                         //   Toast.makeText(getApplicationContext(), "In-app review returned.", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // There was some problem, continue regardless of the result.
                        // goToAppPage(activity);
                    }
                } catch (Exception ex) {
                 //   Toast.makeText(getApplicationContext(), "Exception from openReview():", Toast.LENGTH_SHORT).show();
                }
            });

    }


    private void Initialize_ads(){


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
        snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(),findViewById(android.R.id.content).getRootView(),NewProjectChooseActivity.this,"Press Twice to Exit");

    }




    //in app update
    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if  (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, NewProjectChooseActivity.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }



}