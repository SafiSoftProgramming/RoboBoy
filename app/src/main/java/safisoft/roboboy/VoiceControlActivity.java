package safisoft.roboboy;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class VoiceControlActivity extends AppCompatActivity {
    String BLUETOOTH_NAME;
    String MODULE_MAC;
    public final static int REQUEST_ENABLE_BT = 1;
    UUID MY_UUID;
    DbConnction db;
    Cursor c = null;
    String PROJECT_NAME;
    TextView txtv_bluetooth_name;
    TextView txtv_bluetooth_mac;
    TextView txtv_connecting_lable ;
    ImageButton btn_turn_off;
    TextView txtv_command_history;
    TextView txtv_voice_command;
    ImageButton btn_save_voice_1;
    ImageButton btn_save_voice_2;
    ImageButton btn_save_voice_3;
    ImageButton btn_save_voice_4;
    ImageButton btn_save_voice_5;
    ImageButton btn_save_voice_6;
    ImageButton btn_save_voice_7;
    ImageButton btn_save_voice_8;
    ImageButton btn_save_voice_9;
    ImageButton IMG_BUTTON_ANIMATE;
    public Handler mHandler;
    private final int STATUS_CHECK_INTERVAL = 500;
    private final Handler handlerStatusCheck = new Handler();

    ImageView imgv_connect_led ;

    ImageButton btn_clean_screen ;
    ImageButton btn_scroll_screen ;


    boolean State_Zero = true;
    boolean State_One = true;
    boolean State_Tow = true;
    boolean First_lunch_Zero = true;
    boolean First_lunch_One = true;
    boolean First_lunch_Tow = true;
    private AdView mAdView;

    InterstitialAd_Class aClass ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);

        aClass = new InterstitialAd_Class();
        aClass.Initialize_ads(VoiceControlActivity.this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        PROJECT_NAME = getIntent().getStringExtra("PROJECT_NAME");

        BLUETOOTH_NAME = getIntent().getStringExtra("bluetooth_name");
        MODULE_MAC = getIntent().getStringExtra("bluetooth_mac");



        db = new DbConnction(VoiceControlActivity.this);
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


        initiateBluetoothProcess();


        ImageButton btnv = findViewById(R.id.btn_v);

        txtv_bluetooth_name = findViewById(R.id.txtv_bluetooth_name);
        txtv_bluetooth_mac = findViewById(R.id.txtv_bluetooth_mac);
        txtv_connecting_lable = findViewById(R.id.txtv_connecting_lable);
        btn_turn_off = findViewById(R.id.btn_turn_off);
        txtv_command_history = findViewById(R.id.txtv_command_history);
        txtv_voice_command = findViewById(R.id.txtv_voice_command);
        btn_save_voice_1 = findViewById(R.id.btn_save_voice_1);
        btn_save_voice_2 = findViewById(R.id.btn_save_voice_2);
        btn_save_voice_3 = findViewById(R.id.btn_save_voice_3);
        btn_save_voice_4 = findViewById(R.id.btn_save_voice_4);
        btn_save_voice_5 = findViewById(R.id.btn_save_voice_5);
        btn_save_voice_6 = findViewById(R.id.btn_save_voice_6);
        btn_save_voice_7 = findViewById(R.id.btn_save_voice_7);
        btn_save_voice_8 = findViewById(R.id.btn_save_voice_8);
        btn_save_voice_9 = findViewById(R.id.btn_save_voice_9);
        imgv_connect_led =findViewById(R.id.imgv_connect_led);

        btn_clean_screen =findViewById(R.id.btn_clean_screen);
        btn_scroll_screen =findViewById(R.id.btn_scroll_screen);


        txtv_bluetooth_name.setText(BLUETOOTH_NAME);
        txtv_bluetooth_mac.setText(MODULE_MAC);


        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();


        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndConnection();

                aClass.Initialize_ads(VoiceControlActivity.this);
                aClass.Start_Interstial_AD(VoiceControlActivity.this);




            }
        });


        btn_clean_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtv_command_history.setText("");
            }
        });

        btn_scroll_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtv = txtv_command_history.getText().toString();
                txtv_command_history.setText("");
                txtv_command_history.append(txtv);
            }
        });



        btnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        });


        btn_save_voice_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_1", "com_1");
            }
        });

        btn_save_voice_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_2", "com_2");
            }
        });

        btn_save_voice_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_3", "com_3");
            }
        });

        btn_save_voice_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_4", "com_4");
            }
        });

        btn_save_voice_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_5", "com_5");
            }
        });

        btn_save_voice_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_6", "com_6");
            }
        });

        btn_save_voice_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_7", "com_7");
            }
        });

        btn_save_voice_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_8", "com_8");
            }
        });

        btn_save_voice_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_9", "com_9");
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

            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this, "Your Device Don't Support Speech Input");

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String COMMAND_VOICE = "";
                    String COMMAND_SEND = "";


                    //  Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();


                    c = db.Row_Query("Project_Voice_Control", "project_name", PROJECT_NAME);
                    c.moveToFirst();


                    if (c.getString(c.getColumnIndexOrThrow("voice_1")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_1"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_1"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_1;
                    }

                    if (c.getString(c.getColumnIndexOrThrow("voice_2")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_2"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_2"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_2;
                    }

                    if (c.getString(c.getColumnIndexOrThrow("voice_3")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_3"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_3"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_3;
                    }

                    if (c.getString(c.getColumnIndexOrThrow("voice_4")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_4"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_4"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_4;
                    }

                    if (c.getString(c.getColumnIndexOrThrow("voice_5")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_5"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_5"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_5;
                    }

                    if (c.getString(c.getColumnIndexOrThrow("voice_6")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_6"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_6"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_6;
                    }

                    if (c.getString(c.getColumnIndexOrThrow("voice_7")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_7"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_7"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_7;
                    }

                    if (c.getString(c.getColumnIndexOrThrow("voice_8")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_8"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_8"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_8;
                    }

                    if (c.getString(c.getColumnIndexOrThrow("voice_9")).equals(result.get(0))) {
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_9"));
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_9"));
                        IMG_BUTTON_ANIMATE = btn_save_voice_9;
                    }


                    if (!COMMAND_SEND.equals("Empty") && COMMAND_VOICE.equals(result.get(0))) {
                        SEND_COMMAND(COMMAND_SEND);
                        ANIMATE_BUTTONS();
                    } else if (COMMAND_SEND.equals("Empty")) {

                        SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                        snackBarInfoControl.SnackBarInfoControlView(VoiceControlActivity.this, findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this, "Empty Command");

                    } else if (!COMMAND_VOICE.equals(result.get(0))) {

                        SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                        snackBarInfoControl.SnackBarInfoControlView(VoiceControlActivity.this, findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this, "No Voice Command Match");

                    }


                    txtv_voice_command.setText(result.get(0));


                }
                break;
        }
    }

    public void initiateBluetoothProcess(){
        ((ApplicationEx)getApplication()).mBtEngine.SET_MAC(MODULE_MAC); //back angel
        ((ApplicationEx)getApplication()).Start_Stop_Manual_control(0);

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String txt = (String)msg.obj;
                StringBuilder sb = new StringBuilder();
                sb.append(txt);                                      // append string
                String sbprint = sb.substring(0, sb.length());            // extract string
                sb.delete(0, sb.length());
                final String finalSbprint = sb.append(sbprint).toString();
                System.out.println(finalSbprint);
                txtv_command_history.append(finalSbprint);

            }
        };
        ((ApplicationEx)getApplication()).mBtEngine.SET_HANDLER(mHandler);


        handlerStatusCheck.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(txtv_command_history.getLineCount() > 500){
                    txtv_command_history.setText("");
                    txtv_command_history.append("> "+"Auto Screen Cleaner"+"\n");
                    System.out.println("Auto Screen Cleaner");
                }
                if(((ApplicationEx)getApplication()).mBtEngine.getState() == 0 ){
                    if(State_Zero) {
                        txtv_connecting_lable.setText("Connection Lost");
                        txtv_command_history.append( "> "+"Connection Lost"+"\n");
                        imgv_connect_led.setBackgroundResource(R.drawable.ic_red_dot_not_connected);
                        if(!First_lunch_Zero) {
                            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this, "Connection Lost");
                        }
                        State_Zero = false ;
                        State_One = true ;
                        State_Tow = true ;
                        First_lunch_Zero = false ;
                    }
                }
                if(((ApplicationEx)getApplication()).mBtEngine.getState() == 1 ){
                    if(State_One) {
                        txtv_connecting_lable.setText("Trying to Connect");
                        txtv_command_history.append( "> "+"Trying to Connect"+"\n");
                        imgv_connect_led.setBackgroundResource(R.drawable.ic_red_dot_not_connected);
                        if(!First_lunch_One) {
                            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this, "Trying to Connect");
                        }
                        State_Zero = true ;
                        State_One = false ;
                        State_Tow = true ;
                        First_lunch_One = false ;
                    }
                }
                if(((ApplicationEx)getApplication()).mBtEngine.getState() == 2 ){
                    if(State_Tow) {
                        txtv_connecting_lable.setText("Connected");
                        txtv_command_history.append( "> "+"Connected"+"\n");
                        imgv_connect_led.setBackgroundResource(R.drawable.ic_green_dot_connected);
                        if(!First_lunch_Tow) {
                            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this, "Connected");
                        }
                        State_Zero = true ;
                        State_One = true ;
                        State_Tow = false ;
                        First_lunch_Tow = false ;
                    }
                }
                handlerStatusCheck.postDelayed(this, STATUS_CHECK_INTERVAL);
            }
        }, STATUS_CHECK_INTERVAL);

    }


    private void EndConnection() {
        ((ApplicationEx)getApplication()).Start_Stop_Manual_control(1);
        handlerStatusCheck.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
    }

    private void SEND_COMMAND(String Commend){

        ((ApplicationEx)getApplication()).Start_Stop_Manual_control(0);
        if(((ApplicationEx)getApplication()).writeBt(Commend.getBytes(StandardCharsets.UTF_8))){
            txtv_command_history.append( "> "+Commend+"\n");
        }
        else {
            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this,"Something Went Wrong");
        }

    }


    private void HANDLE_COMMAND(String COMMAND_VOICE_COLUMN , String COMMAND_SEND_COLUNM ){

        c = db.Row_Query("Project_Voice_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();


        String COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow(COMMAND_VOICE_COLUMN)) ;
        String COMMAND_SEND = c.getString(c.getColumnIndexOrThrow(COMMAND_SEND_COLUNM)) ;

        if(!COMMAND_SEND.equals("Empty") && !COMMAND_VOICE.equals("Empty")){
            SEND_COMMAND(COMMAND_SEND);
            txtv_voice_command.setText(COMMAND_VOICE);
        }
        else if (COMMAND_SEND.equals("Empty")){

            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(VoiceControlActivity.this, findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this,"Empty Command");
            txtv_voice_command.setText("Empty");
        }
        else if (COMMAND_VOICE.equals("Empty")){

            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(VoiceControlActivity.this, findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this,"No Voice Command Match");
            txtv_voice_command.setText("Empty");
        }



    }



    private void ANIMATE_BUTTONS(){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(2);
        IMG_BUTTON_ANIMATE.startAnimation(anim);
    }

    @Override
    public void onBackPressed() {
        EndConnection();
        Intent intent = new Intent(VoiceControlActivity.this, NewProjectChooseActivity.class);
        intent.putExtra("AD_STATE","true" );
        startActivity(intent);
        finish();
    }





}