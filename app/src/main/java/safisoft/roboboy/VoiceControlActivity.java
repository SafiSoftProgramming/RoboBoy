package safisoft.roboboy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import safisoft.roboboy.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class VoiceControlActivity extends AppCompatActivity {



    String bluetooth_name ;
    String bluetooth_mac ;
    String MODULE_MAC ;
    public final static int REQUEST_ENABLE_BT = 1;
    UUID MY_UUID ;
    BluetoothAdapter bta;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    ConnectedThread btt = null;
    public Handler mHandler;
    DbConnction db ;
    Cursor c = null;
    String PROJECT_NAME ;
    TextView txtv_bluetooth_name ;
    TextView txtv_bluetooth_mac ;
    ImageButton btn_turn_off ;
    TextView txtv_command_history ;
    TextView txtv_voice_command ;
    ImageButton btn_save_voice_1 ;
    ImageButton btn_save_voice_2 ;
    ImageButton btn_save_voice_3 ;
    ImageButton btn_save_voice_4 ;
    ImageButton btn_save_voice_5 ;
    ImageButton btn_save_voice_6 ;
    ImageButton btn_save_voice_7 ;
    ImageButton btn_save_voice_8 ;
    ImageButton btn_save_voice_9 ;
    ImageButton IMG_BUTTON_ANIMATE ;


    String message = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        PROJECT_NAME = getIntent().getStringExtra("PROJECT_NAME");

        bluetooth_name = getIntent().getStringExtra("bluetooth_name");
        bluetooth_mac = getIntent().getStringExtra("bluetooth_mac");

        MODULE_MAC = bluetooth_mac ;
        MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        bta = BluetoothAdapter.getDefaultAdapter();

        //if bluetooth is not enabled then create Intent for user to turn it on
        if(!bta.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }else{
            initiateBluetoothProcess();
        }

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






        ImageButton btnv = findViewById(R.id.btn_v);

        txtv_bluetooth_name =findViewById(R.id.txtv_bluetooth_name);
        txtv_bluetooth_mac =findViewById(R.id.txtv_bluetooth_mac);
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


        txtv_bluetooth_name.setText(bluetooth_name);
        txtv_bluetooth_mac.setText(bluetooth_mac);


        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();


        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetConnection();
                Intent intent = new Intent(VoiceControlActivity.this, NewProjectChooseActivity.class);
                intent.putExtra("AD_STATE","true" );
                startActivity(intent);
                finish();
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
                HANDLE_COMMAND("voice_1" , "com_1");
            }
        });

        btn_save_voice_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_2" ,"com_2");
            }
        });

        btn_save_voice_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_3" ,"com_3");
            }
        });

        btn_save_voice_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_4" , "com_4");
            }
        });

        btn_save_voice_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_5" , "com_5");
            }
        });

        btn_save_voice_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_6" , "com_6");
            }
        });

        btn_save_voice_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_7" , "com_7");
            }
        });

        btn_save_voice_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_8" ,"com_8");
            }
        });

        btn_save_voice_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HANDLE_COMMAND("voice_9" , "com_9");
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
            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this,"Your Device Don't Support Speech Input");

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            initiateBluetoothProcess();
        }

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String COMMAND_VOICE = "";
                    String COMMAND_SEND = "" ;


                  //  Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();


                    c = db.Row_Query("Project_Voice_Control", "project_name", PROJECT_NAME);
                    c.moveToFirst();


                    if(c.getString(c.getColumnIndexOrThrow("voice_1")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_1")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_1")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_1 ;
                    }

                    if(c.getString(c.getColumnIndexOrThrow("voice_2")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_2")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_2")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_2 ;
                    }

                    if(c.getString(c.getColumnIndexOrThrow("voice_3")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_3")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_3")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_3 ;
                    }

                    if(c.getString(c.getColumnIndexOrThrow("voice_4")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_4")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_4")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_4 ;
                    }

                    if(c.getString(c.getColumnIndexOrThrow("voice_5")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_5")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_5")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_5 ;
                    }

                    if(c.getString(c.getColumnIndexOrThrow("voice_6")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_6")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_6")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_6 ;
                    }

                    if(c.getString(c.getColumnIndexOrThrow("voice_7")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_7")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_7")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_7 ;
                    }

                    if(c.getString(c.getColumnIndexOrThrow("voice_8")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_8")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_8")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_8 ;
                    }

                    if(c.getString(c.getColumnIndexOrThrow("voice_9")).equals(result.get(0))){
                        COMMAND_VOICE = c.getString(c.getColumnIndexOrThrow("voice_9")) ;
                        COMMAND_SEND = c.getString(c.getColumnIndexOrThrow("com_9")) ;
                        IMG_BUTTON_ANIMATE = btn_save_voice_9 ;
                    }


                    if(!COMMAND_SEND.equals("Empty") && COMMAND_VOICE.equals(result.get(0))){
                        SEND_COMMAND(COMMAND_SEND);
                        ANIMATE_BUTTONS();
                    }
                    else if (COMMAND_SEND.equals("Empty")){

                        SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                        snackBarInfoControl.SnackBarInfoControlView(VoiceControlActivity.this, findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this,"Empty Command");

                    }
                    else if (!COMMAND_VOICE.equals(result.get(0))){

                        SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                        snackBarInfoControl.SnackBarInfoControlView(VoiceControlActivity.this, findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this,"No Voice Command Match");

                    }


                    txtv_voice_command.setText(result.get(0));




                }
                break;
        }
    }



    public void initiateBluetoothProcess(){



        if(bta.isEnabled()){
            //attempt to connect to bluetooth module
            BluetoothSocket tmp = null;
            mmDevice = bta.getRemoteDevice(MODULE_MAC);

            //create socket
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
                Log.i("[BLUETOOTH]","Connected to: "+mmDevice.getName());
            }catch(IOException e){
                try{mmSocket.close();}catch(IOException c){return;}
            }

            Log.i("[BLUETOOTH]", "Creating handler");
            mHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {

                  super.handleMessage(msg);
                  if(msg.what == ConnectedThread.RESPONSE_MESSAGE){
                      String txt = (String)msg.obj;

                      StringBuilder sb = new StringBuilder();
                      sb.append(txt);                                      // append string
                      String sbprint = sb.substring(0, sb.length());            // extract string
                      sb.delete(0, sb.length());
                      final String finalSbprint = sb.append(sbprint).toString();
                      txtv_command_history.append(finalSbprint);
                  }
                }
            };


            Log.i("[BLUETOOTH]", "Creating and running Thread");
            btt = new ConnectedThread(mmSocket,mHandler);
            btt.start();

        }
    }



    private void resetConnection() {
        if (mmSocket != null) {
            try {mmSocket.close();} catch (Exception e) {}
            mmSocket = null;


        }
    }

    private void SEND_COMMAND(String Commend){

        try {

            if(!isConnected(mmDevice)){

                SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this,"Bluetooth Connection Lost!");

            }

            if (mmSocket.isConnected() && btt != null) {
                btt.write(Commend.getBytes());

                txtv_command_history.append( "> "+Commend+"\n");
            } else {
                SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), VoiceControlActivity.this,"Something Went Wrong");
            }

        }
        catch (Exception E){

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

    public static boolean isConnected(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("isConnected", (Class[]) null);
            boolean connected = (boolean) m.invoke(device, (Object[]) null);
            return connected;
        } catch (Exception e) {
            throw new IllegalStateException(e);
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
        resetConnection();
        Intent intent = new Intent(VoiceControlActivity.this, NewProjectChooseActivity.class);
        intent.putExtra("AD_STATE","true" );
        startActivity(intent);
        finish();
    }





}