package com.example.android.nusevents;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        String email1 = "ronak.lakhotia999@gmail.com";
        String contact1 = "+6593911558";
        String name1 = "Ronak Lakhotia";

        String email2 = "tshradheya@gmail.com";
        String contact2 = "+6583417302";
        String name2 = "Shradheya Thakre";
        String message = "Name"+" : "+name1+"\n"+"Contact Number"+" : "+contact1+"\n"+"Email Id"+" : "+email1+"\n"+"\n";
        String message2 = "Name"+" : "+name2+"\n"+"Contact Number"+" : "+contact2+"\n"+"Email Id"+" : "+email2+"\n"+"\n";
        displayAdminInfo(message);
        displayAdminInfo2(message2);



    }

    public void displayAdminInfo(String message)
    {
        TextView view = (TextView)findViewById(R.id.ad);
        view.setText(message);
    }
    public void displayAdminInfo2(String message)
    {
        TextView view = (TextView)findViewById(R.id.ad1);
        view.setText(message);
    }


    public void sendEmail(View view)
    {

        String emailAddress="nusevent16@gmail.com";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL,emailAddress);
        emailIntent.putExtra(Intent.EXTRA_CC,"tshrdheya@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_CC,"ronaklakhotia999@gmail.com");

        if(emailIntent.resolveActivity(getPackageManager())!=null) {
            startActivity(emailIntent);
        }
    }
}