package safisoft.roboboy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.nio.charset.StandardCharsets;

public class SerialMonitorControlActivity extends AppCompatActivity {
    String BLUETOOTH_NAME;
    String MODULE_MAC;
    String PROJECT_NAME;
    TextView txtv_bluetooth_name;
    TextView txtv_bluetooth_mac;
    TextView txtv_connecting_lable ;
    EditText edittxt_commend_to_send ;
    ImageButton btn_turn_off;
    ImageButton btn_send_text_commend ;
    TextView txtv_command_history;
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

    InterstitialAd_Class aClass ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_monitor_control);

        aClass = new InterstitialAd_Class();
        aClass.Initialize_ads(SerialMonitorControlActivity.this);

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

        initiateBluetoothProcess();

        txtv_bluetooth_name = findViewById(R.id.txtv_bluetooth_name);
        txtv_bluetooth_mac = findViewById(R.id.txtv_bluetooth_mac);
        txtv_connecting_lable = findViewById(R.id.txtv_connecting_lable);
        btn_turn_off = findViewById(R.id.btn_turn_off);
        btn_send_text_commend = findViewById(R.id.btn_send_text_commend);
        txtv_command_history = findViewById(R.id.txtv_command_history);
        edittxt_commend_to_send = findViewById(R.id.edittxt_commend_to_send);
        imgv_connect_led =findViewById(R.id.imgv_connect_led);
        btn_clean_screen =findViewById(R.id.btn_clean_screen);
        btn_scroll_screen =findViewById(R.id.btn_scroll_screen);


        txtv_bluetooth_name.setText(BLUETOOTH_NAME);
        txtv_bluetooth_mac.setText(MODULE_MAC);


        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndConnection();
                aClass.Initialize_ads(SerialMonitorControlActivity.this);
                aClass.Start_Interstial_AD(SerialMonitorControlActivity.this);
            }
        });

        btn_send_text_commend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edittxt_commend_to_send.getText().toString().equals("")){
                    SEND_COMMAND(edittxt_commend_to_send.getText().toString());
                  //  edittxt_commend_to_send.setText("");
                }
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), SerialMonitorControlActivity.this, "Connection Lost");
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), SerialMonitorControlActivity.this, "Trying to Connect");
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), SerialMonitorControlActivity.this, "Connected");
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
            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), SerialMonitorControlActivity.this,"Something Went Wrong");
        }

    }

    @Override
    public void onBackPressed() {
        EndConnection();
        Intent intent = new Intent(SerialMonitorControlActivity.this, NewProjectChooseActivity.class);
        intent.putExtra("AD_STATE","true" );
        startActivity(intent);
        finish();
    }




}