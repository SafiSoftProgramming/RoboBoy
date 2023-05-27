package safisoft.roboboy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class RotationControlActivity extends AppCompatActivity {


    SensorManager sensorManager;
    Sensor sensor;
    String BLUETOOTH_NAME;
    String MODULE_MAC ;
    public final static int REQUEST_ENABLE_BT = 1;
    DbConnction db ;
    Cursor c = null;
    String PROJECT_NAME ;
    TextView txtv_bluetooth_name ;
    TextView txtv_bluetooth_mac ;
    TextView txtv_connecting_lable ;
    ImageButton btn_turn_off ;
    TextView txtv_command_history ;

    int min_20_send_counter = 1 ;
    int min_40_send_counter = 1 ;
    int min_60_send_counter = 1 ;
    int min_80_send_counter = 1 ;
    int min_100_send_counter = 1 ;
    int min_120_send_counter = 1 ;
    int min_140_send_counter = 1 ;
    int min_160_send_counter = 1 ;
    int min_180_send_counter = 1 ;

    int plus_20_send_counter = 1 ;
    int plus_40_send_counter = 1 ;
    int plus_60_send_counter = 1 ;
    int plus_80_send_counter = 1 ;
    int plus_100_send_counter = 1 ;
    int plus_120_send_counter = 1 ;
    int plus_140_send_counter = 1 ;
    int plus_160_send_counter = 1 ;
    int plus_180_send_counter = 1 ;


    ImageView imgv_ic_rotation ;

    ImageView imgv_connect_led ;
    SensorEventListener rvListener ;

    ImageButton btn_clean_screen ;
    ImageButton btn_scroll_screen ;

    public Handler mHandler;
    private final int STATUS_CHECK_INTERVAL = 500;
    private final Handler handlerStatusCheck = new Handler();

    boolean State_Zero = true ;
    boolean State_One = true ;
    boolean State_Tow = true ;
    boolean First_lunch_Zero = true ;
    boolean First_lunch_One = true ;
    boolean First_lunch_Tow = true ;

    boolean first_lunch_chick = true ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_control);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        PROJECT_NAME = getIntent().getStringExtra("PROJECT_NAME");
        BLUETOOTH_NAME = getIntent().getStringExtra("bluetooth_name");
        MODULE_MAC = getIntent().getStringExtra("bluetooth_mac");



        db = new DbConnction(RotationControlActivity.this);
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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        
        if(sensor == null){

            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this,"your device not support rotation vector sensor!");

        }

        imgv_ic_rotation = findViewById(R.id.imgv_ic_rotation);
        txtv_bluetooth_name =findViewById(R.id.txtv_bluetooth_name);
        txtv_bluetooth_mac =findViewById(R.id.txtv_bluetooth_mac);
        txtv_connecting_lable = findViewById(R.id.txtv_connecting_lable);
        btn_turn_off = findViewById(R.id.btn_turn_off);
        txtv_command_history = findViewById(R.id.txtv_command_history);

        imgv_connect_led =findViewById(R.id.imgv_connect_led);


        txtv_bluetooth_name.setText(BLUETOOTH_NAME);
        txtv_bluetooth_mac.setText(MODULE_MAC);

        btn_clean_screen =findViewById(R.id.btn_clean_screen);
        btn_scroll_screen =findViewById(R.id.btn_scroll_screen);


        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();


        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndConnection();
                Intent intent = new Intent(RotationControlActivity.this, NewProjectChooseActivity.class);
                intent.putExtra("AD_STATE","true" );
                startActivity(intent);
                finish();
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


        rvListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {


                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, sensorEvent.values);

                // Remap coordinate system
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                // Convert to orientations
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                for(int i = 0; i < 3; i++) {
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }

                imgv_ic_rotation.setRotation(orientations[2]);



                if(orientations[2] > 0 && orientations[2] <= 20 ) {
                    if(min_20_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_20");}
                    min_20_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();

                } else if(orientations[2] > 20 && orientations[2] <= 40) {
                    if(min_40_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_40") ;}
                    min_40_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();

                } else if(orientations[2] > 40 && orientations[2] <= 60) {
                    if(min_60_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_60") ;}
                    min_60_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();

                }else if(orientations[2] > 60 && orientations[2] <= 80) {
                    if(min_80_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_80") ;}
                    min_80_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();

                }else if(orientations[2] > 80 && orientations[2] <= 100) {
                    if(min_100_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_100") ;}
                    min_100_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();

                }else if(orientations[2] > 100 && orientations[2] <= 120) {
                    if(min_120_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_120") ;}
                    min_120_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();

                }else if(orientations[2] > 120 && orientations[2] <= 140) {
                    if(min_140_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_140") ;}
                    min_140_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();

                }else if(orientations[2] > 140 && orientations[2] <= 160) {
                    if(min_160_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_160") ;}
                    min_160_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();

                }else if(orientations[2] > 160 && orientations[2] <= 180) {
                    if(min_180_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("min_180") ;}
                    min_180_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER();




                }else if(orientations[2] < 0 && orientations[2] >= -20) {
                    if(plus_20_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_20") ;}
                    plus_20_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;

                }else if(orientations[2] < -20 && orientations[2] >= -40) {
                    if(plus_40_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_40") ;}
                    plus_40_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;

                }else if(orientations[2] < -40 && orientations[2] >= -60) {
                    if(plus_60_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_60") ;}
                    plus_60_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;

                }else if(orientations[2] < -60 && orientations[2] >= -80) {
                    if(plus_80_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_80") ;}
                    plus_80_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;

                }else if(orientations[2] < -80 && orientations[2] >= -100) {
                    if(plus_100_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_100") ;}
                    plus_100_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;

                }else if(orientations[2] < -100 && orientations[2] >= -120) {
                    if(plus_120_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_120") ;}
                    plus_120_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;

                }else if(orientations[2] < -120 && orientations[2] >= -140) {
                    if(plus_140_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_140") ;}
                    plus_140_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;

                }else if(orientations[2] < -140 && orientations[2] >= -160) {
                    if(plus_160_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_160") ;}
                    plus_160_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;

                }else if(orientations[2] < -160 && orientations[2] >= -180) {
                    if(plus_180_send_counter > 0 ){
                    GET_COLUMN_FOR_COMMAND("plus_180") ;}
                    plus_180_send_counter = RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER() ;
                }

            }


            public void  GET_COLUMN_FOR_COMMAND(String COLUMN_FOR_COMMAND){
                c = db.Row_Query("Project_Rotation_Control", "project_name", PROJECT_NAME);
                c.moveToFirst();
                String COMMAND = c.getString(c.getColumnIndexOrThrow(COLUMN_FOR_COMMAND));
                if(!COMMAND.equals("Empty")){
                    SEND_COMMAND(COMMAND);
                }
            }

            private int RESET_SEND_COUNTERS_EXCEPT_THIS_COUNTER(){
                min_20_send_counter = 1 ;
                min_40_send_counter = 1 ;
                min_60_send_counter = 1 ;
                min_80_send_counter = 1 ;
                min_100_send_counter = 1 ;
                min_120_send_counter = 1 ;
                min_140_send_counter = 1 ;
                min_160_send_counter = 1 ;
                min_180_send_counter = 1 ;

                plus_20_send_counter = 1 ;
                plus_40_send_counter = 1 ;
                plus_60_send_counter = 1 ;
                plus_80_send_counter = 1 ;
                plus_100_send_counter = 1 ;
                plus_120_send_counter = 1 ;
                plus_140_send_counter = 1 ;
                plus_160_send_counter = 1 ;
                plus_180_send_counter = 1 ;

                return 0 ;
            }



            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

         // Register it
        sensorManager.registerListener(rvListener,
                sensor, SensorManager.SENSOR_DELAY_NORMAL);




    };

    @Override
    protected void onPause() {
        try {
            stopListening() ;
        } catch (Exception e) {
            System.out.println(e);
        }
        super.onPause();
    }

    public void stopListening()
    {

        sensorManager.unregisterListener(rvListener, sensor);
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
                if(txtv_command_history.getLineCount() > 200){
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this, "Connection Lost");
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this, "Trying to Connect");
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this, "Connected");
                        }
                        State_Zero = true ;
                        State_One = true ;
                        State_Tow = false ;
                        First_lunch_Tow = false ;
                        first_lunch_chick = false ;
                    }
                }
                handlerStatusCheck.postDelayed(this, STATUS_CHECK_INTERVAL);
            }
        }, STATUS_CHECK_INTERVAL);

    }


    private void SEND_COMMAND(String Commend){

        ((ApplicationEx)getApplication()).Start_Stop_Manual_control(0);
        if(((ApplicationEx)getApplication()).writeBt(Commend.getBytes(StandardCharsets.UTF_8))){
            txtv_command_history.append( "> "+Commend+"\n");
        }
        else {
            if(!first_lunch_chick) {
                SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this, "Something Went Wrong");
            }
        }

    }


    private void EndConnection() {
        ((ApplicationEx)getApplication()).Start_Stop_Manual_control(1);
        handlerStatusCheck.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        EndConnection();
        Intent intent = new Intent(RotationControlActivity.this, NewProjectChooseActivity.class);
        intent.putExtra("AD_STATE","true" );
        startActivity(intent);
        finish();
    }




}























//GYROSCOPE
/*
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        textX = findViewById(R.id.textX);
        textY = findViewById(R.id.textY);
        textZ = findViewById(R.id.textZ);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            textX.setText("X : " + (int) x + " rad/s");
            textY.setText("Y : " + (int) y + " rad/s");
            textZ.setText("Z : " + (int) z + " rad/s");
        }


 */
