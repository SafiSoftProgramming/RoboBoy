package safisoft.roboboy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import safisoft.roboboy.R;

public class ChooseToDoActivity extends AppCompatActivity {

    ImageButton btn_connect_control , btn_info ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_to_do);

        btn_connect_control = findViewById(R.id.btn_connect_control);
        btn_info = findViewById(R.id.btn_info);

        btn_connect_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseToDoActivity.this, NewProjectChooseActivity.class);
                intent.putExtra("AD_STATE","false" );
                startActivity(intent);
                finish();
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(ChooseToDoActivity.this, InfoActivity.class);
               startActivity(intent);
               finish();

            }
        });

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
        snackBarInfoControl.SnackBarInfoControlView(getApplicationContext(),findViewById(android.R.id.content).getRootView(),ChooseToDoActivity.this,"Press Twice to Exit");

    }








}