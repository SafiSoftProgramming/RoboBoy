package safisoft.roboboy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ButtonsControlActivity extends AppCompatActivity {
    String BLUETOOTH_NAME;
    String MODULE_MAC ;
    DbConnction db ;
    Cursor c = null;
    ImageButton btn_send_text_commend ;
    EditText edittxt_commend_to_send ;
    TextView txtv_command_history ;
    TextView txtv_connecting_lable ;
    TextView txtv_command_name1 ;
    TextView txtv_command_name2 ;
    TextView txtv_command_name3 ;
    TextView txtv_command_name4 ;
    TextView txtv_command_name5 ;
    TextView txtv_command_name6 ;
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
    ImageButton btn_commend1 ;
    ImageButton btn_commend2 ;
    ImageButton btn_commend3 ;
    ImageButton btn_commend4 ;
    ImageButton btn_commend5 ;
    ImageButton btn_commend6 ;
    ImageButton btn_commend_up ;
    ImageButton btn_commend_down ;
    ImageButton btn_commend_left ;
    ImageButton btn_commend_right ;
    ImageButton btn_commend_center ;
    ImageButton btn_turn_off ;
    TextView txtv_bluetooth_name ;
    TextView txtv_bluetooth_mac ;
    String COLUMN_FOR_COMMAND_DOWN ;
    String COLUMN_FOR_COMMAND_UP ;
    String PROJECT_NAME ;
    ImageView imgv_connect_led ;
    ImageButton btn_clean_screen ;
    ImageButton btn_scroll_screen ;

    public final static int REQUEST_ENABLE_BT = 1;
    public Handler mHandler;
    private final int STATUS_CHECK_INTERVAL = 500;
    private final Handler handlerStatusCheck = new Handler();




    boolean State_Zero = true ;
    boolean State_One = true ;
    boolean State_Tow = true ;
    boolean First_lunch_Zero = true ;
    boolean First_lunch_One = true ;
    boolean First_lunch_Tow = true ;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons_control);

        PROJECT_NAME = getIntent().getStringExtra("PROJECT_NAME");
        BLUETOOTH_NAME = getIntent().getStringExtra("bluetooth_name");
        MODULE_MAC = getIntent().getStringExtra("bluetooth_mac");

        initiateBluetoothProcess();

        db = new DbConnction(ButtonsControlActivity.this);
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


        btn_send_text_commend = findViewById(R.id.btn_send_text_commend);
        edittxt_commend_to_send = findViewById(R.id.edittxt_commend_to_send);
        imgv_connect_led =findViewById(R.id.imgv_connect_led);
        txtv_command_history = findViewById(R.id.txtv_command_history);
        txtv_connecting_lable = findViewById(R.id.txtv_connecting_lable);
        txtv_command_name1 = findViewById(R.id.txtv_command_name1);
        txtv_command_name2 = findViewById(R.id.txtv_command_name2);
        txtv_command_name3 = findViewById(R.id.txtv_command_name3);
        txtv_command_name4 = findViewById(R.id.txtv_command_name4);
        txtv_command_name5 = findViewById(R.id.txtv_command_name5);
        txtv_command_name6 = findViewById(R.id.txtv_command_name6);

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

        btn_commend1 = findViewById(R.id.btn_commend1);
        btn_commend2 = findViewById(R.id.btn_commend2);
        btn_commend3 = findViewById(R.id.btn_commend3);
        btn_commend4 = findViewById(R.id.btn_commend4);
        btn_commend5 = findViewById(R.id.btn_commend5);
        btn_commend6 = findViewById(R.id.btn_commend6);

        btn_commend_up = findViewById(R.id.btn_commend_up);
        btn_commend_down = findViewById(R.id.btn_commend_down);
        btn_commend_left = findViewById(R.id.btn_commend_left);
        btn_commend_right = findViewById(R.id.btn_commend_right);
        btn_commend_center = findViewById(R.id.btn_commend_center);


        btn_turn_off = findViewById(R.id.btn_turn_off);

        btn_clean_screen =findViewById(R.id.btn_clean_screen);
        btn_scroll_screen =findViewById(R.id.btn_scroll_screen);

        txtv_bluetooth_name =findViewById(R.id.txtv_bluetooth_name);
        txtv_bluetooth_mac =findViewById(R.id.txtv_bluetooth_mac);


        txtv_bluetooth_name.setText(BLUETOOTH_NAME);
        txtv_bluetooth_mac.setText(MODULE_MAC);

        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();

        txtv_command_name1.setText(c.getString(c.getColumnIndexOrThrow("btn_command_1_name")));
        txtv_command_name2.setText(c.getString(c.getColumnIndexOrThrow("btn_command_2_name")));
        txtv_command_name3.setText(c.getString(c.getColumnIndexOrThrow("btn_command_3_name")));
        txtv_command_name4.setText(c.getString(c.getColumnIndexOrThrow("btn_command_4_name")));
        txtv_command_name5.setText(c.getString(c.getColumnIndexOrThrow("btn_command_5_name")));
        txtv_command_name6.setText(c.getString(c.getColumnIndexOrThrow("btn_command_6_name")));

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


        btn_send_text_commend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edittxt_commend_to_send.getText().toString().equals("")){
                    SEND_COMMAND(edittxt_commend_to_send.getText().toString());
                    edittxt_commend_to_send.setText("");
                }
            }
        });



        btn_commend1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_com1_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com1_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });


        btn_commend2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_com2_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com2_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_com3_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com3_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_com4_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com4_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_com5_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com5_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_com6_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_com6_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_up_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_up_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_down_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_down_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_left_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_left_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_right_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_right_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });

        btn_commend_center.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                COLUMN_FOR_COMMAND_DOWN = "btn_center_key_down" ;
                COLUMN_FOR_COMMAND_UP = "btn_center_key_up" ;
                HANDLE_BUTTONS(event);

                return false;
            }
        });




        btn_turn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                   EndConnection();
                   Intent intent = new Intent(ButtonsControlActivity.this, NewProjectChooseActivity.class);
                   intent.putExtra("AD_STATE","true" );
                   startActivity(intent);
                   finish();
            }
        });


    }






    private void HANDLE_BUTTONS(MotionEvent event){

        c = db.Row_Query("Project_Buttons_Control", "project_name", PROJECT_NAME);
        c.moveToFirst();

        String COMMAND_DOWN = c.getString(c.getColumnIndexOrThrow(COLUMN_FOR_COMMAND_DOWN));
        String COMMAND_UP = c.getString(c.getColumnIndexOrThrow(COLUMN_FOR_COMMAND_UP));

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!COMMAND_DOWN.equals("Empty")){
                SEND_COMMAND(COMMAND_DOWN);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if(!COMMAND_UP.equals("Empty")) {
                SEND_COMMAND(COMMAND_UP);
            }
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
              //  System.out.println(finalSbprint);
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), ButtonsControlActivity.this, "Connection Lost");
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), ButtonsControlActivity.this, "Trying to Connect");
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
                            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), ButtonsControlActivity.this, "Connected");
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
            snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(), findViewById(android.R.id.content).getRootView(), ButtonsControlActivity.this,"Something Went Wrong");
        }

    }



    @Override
    public void onBackPressed() {
        EndConnection();
        Intent intent = new Intent(ButtonsControlActivity.this, NewProjectChooseActivity.class);
        intent.putExtra("AD_STATE","true" );
        startActivity(intent);
        finish();
    }





}
