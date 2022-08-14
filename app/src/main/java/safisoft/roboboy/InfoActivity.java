package safisoft.roboboy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import safisoft.roboboy.R;

public class InfoActivity extends AppCompatActivity {

    ImageButton btn_info_rate , btn_info_youtube , btn_info_facebook , btn_info_playstore ,btn_info_gmail, btn_info_permissions  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);





        btn_info_rate = findViewById(R.id.btn_info_rate);
        btn_info_youtube = findViewById(R.id.btn_info_youtube);
        btn_info_facebook = findViewById(R.id.btn_info_facebook);
        btn_info_playstore = findViewById(R.id.btn_info_playstore);
        btn_info_gmail = findViewById(R.id.btn_info_gmail);
        btn_info_permissions = findViewById(R.id.btn_info_permissions);




        btn_info_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

        btn_info_playstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=SafiSoft")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=SafiSoft")));
                }

            }
        });

        btn_info_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/301343817018905"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/SafiSoft.programming")));
                }

            }
        });

        btn_info_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:SafiSoft.programmer@gmail.com")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "QR Scanner Duck User Feedback");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        btn_info_permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppPermissionsDialog appPermissionsDialog = new AppPermissionsDialog(InfoActivity.this);
                appPermissionsDialog.show();
                appPermissionsDialog.setCanceledOnTouchOutside(false);
                appPermissionsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                appPermissionsDialog.btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appPermissionsDialog.dismiss();
                    }
                });


            }
        });

        btn_info_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCtC8eqUUZmktsUoFznAL91w")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCtC8eqUUZmktsUoFznAL91w")));
                }

            }
        });





    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(InfoActivity.this, ChooseToDoActivity.class);
        startActivity(intent);
        finish();
    }

}