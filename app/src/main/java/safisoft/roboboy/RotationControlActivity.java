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
import android.util.Log;
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
import safisoft.roboboy.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

public class RotationControlActivity extends AppCompatActivity {


    SensorManager sensorManager;
    Sensor sensor;

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
    SensorEventListener rvListener ;




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



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        
        if(sensor == null){

            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this,"your device not support rotation vector sensor!");

        }

        imgv_ic_rotation = findViewById(R.id.imgv_ic_rotation);
        txtv_bluetooth_name =findViewById(R.id.txtv_bluetooth_name);
        txtv_bluetooth_mac =findViewById(R.id.txtv_bluetooth_mac);
        btn_turn_off = findViewById(R.id.btn_turn_off);
        txtv_command_history = findViewById(R.id.txtv_command_history);


        txtv_bluetooth_name.setText(bluetooth_name);
        txtv_bluetooth_mac.setText(bluetooth_mac);


        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();


        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetConnection();
                Intent intent = new Intent(RotationControlActivity.this, NewProjectChooseActivity.class);
                intent.putExtra("AD_STATE","true" );
                startActivity(intent);
                finish();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT){
            initiateBluetoothProcess();
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
                snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this,"Bluetooth Connection Lost!");

            }

            if (mmSocket.isConnected() && btt != null) {
                btt.write(Commend.getBytes());

                txtv_command_history.append( "> "+Commend+"\n");
            } else {
                SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
                snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this,"Something Went Wrong");
            }

        }
        catch (Exception E){

            SnackBarInfoControl snackBarInfoControl = new SnackBarInfoControl();
            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), RotationControlActivity.this,"Something Went Wrong");

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



    @Override
    public void onBackPressed() {
        resetConnection();
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
