package safisoft.roboboy;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;


public class MoreNewDialog extends Dialog implements
        View.OnClickListener {

    public ImageButton btn_ok;
    public Activity c;

    ImageButton btn_info_rate , btn_info_youtube , btn_info_facebook , btn_info_playstore ,btn_info_gmail, btn_watch_the_video , btn_dowmload_fc  ;
    public MoreNewDialog(@NonNull Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.more_new_dialog);

        btn_info_rate = findViewById(R.id.btn_info_rate);
        btn_info_youtube = findViewById(R.id.btn_info_youtube);
        btn_info_facebook = findViewById(R.id.btn_info_facebook);
        btn_info_playstore = findViewById(R.id.btn_info_playstore);
        btn_info_gmail = findViewById(R.id.btn_info_gmail);

        btn_dowmload_fc = findViewById(R.id.btn_dowmload_fc);
        btn_watch_the_video = findViewById(R.id.btn_watch_the_video);

        btn_ok = findViewById(R.id.btn_ok_info);
        btn_ok.setOnClickListener(this);


        btn_watch_the_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=guju-UC-9Jo")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=guju-UC-9Jo")));
                }

            }
        });

        btn_dowmload_fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=safisoft.facecommander")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=safisoft.facecommander")));
                }

            }
        });



        btn_info_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

        btn_info_playstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=SafiSoft")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=SafiSoft")));
                }

            }
        });

        btn_info_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/301343817018905"));
                    getContext().startActivity(intent);
                } catch(Exception e) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/SafiSoft.programming")));
                }

            }
        });

        btn_info_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:SafiSoft.programmer@gmail.com")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "QR Scanner Duck User Feedback");
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(intent);
                }

            }
        });



        btn_info_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCtC8eqUUZmktsUoFznAL91w")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCtC8eqUUZmktsUoFznAL91w")));
                }

            }
        });




    }

    @Override
    public void onClick(View v) {

    }
}
