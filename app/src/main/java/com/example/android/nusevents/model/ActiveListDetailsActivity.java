package com.example.android.nusevents.model;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nusevents.DatePickerFragment;
import com.example.android.nusevents.DatePickerUpdateFragment;
import com.example.android.nusevents.DisplayEventList;
import com.example.android.nusevents.MainActivity;
import com.example.android.nusevents.R;
import com.example.android.nusevents.TimePickerFragment;
import com.example.android.nusevents.TimePickerUpdateFragment;
import com.example.android.nusevents.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.id;
import static android.R.attr.key;
import static com.example.android.nusevents.Details.dateButton;
import static com.example.android.nusevents.Details.timeButton;
import static com.example.android.nusevents.R.id.event;

public class ActiveListDetailsActivity extends FragmentActivity {

    private ListView mListView;
    private TextView mTextViewListName, mTextViewListOwner;
    private TextView mTextViewInfo,getmTextViewTime,getmTextViewLocation;

    private CheckBox mBookmark;

    public static Button dateUpdate;
    public static Button timeUpdate;


    public static int year,month, day, hour,min;


    View dialogview;

    long time;
    String name="",dAndT="",loc="",owner="",info="",usercreate="",id="";


    private FirebaseDatabase bookmarkDatabase;
    private DatabaseReference bookmarkDatabaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        //dateUpdate=(Button)dialogview.findViewById(R.id.datepickerUpdate);
        //timeUpdate =(Button)dialogview.findViewById(R.id.timepickerUpdate);

        //mListView = (ListView) findViewById(R.id.main_list);
        //mTextViewListName = (TextView) findViewById(R.id.about);
        mTextViewListOwner = (TextView) findViewById(R.id.organize_by);
        mTextViewInfo = (TextView) findViewById(R.id.about_the_event);
        getmTextViewTime = (TextView) findViewById(R.id.about_time);
        getmTextViewLocation = (TextView) findViewById(R.id.about_loc_event);

        mBookmark=(CheckBox)findViewById(R.id.checkBox);

        bookmarkDatabase=FirebaseDatabase.getInstance();
        bookmarkDatabaseReference=bookmarkDatabase.getReference().child("Bookmark");

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currUser=mAuth.getCurrentUser();

        String currid=currUser.getUid();


        DatabaseReference bookmarkUserReference=bookmarkDatabaseReference.child(currid);

        if(bookmarkUserReference!=null) {


            bookmarkUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot eventsnap : dataSnapshot.getChildren()) {

                        EventInfo obj=eventsnap.getValue(EventInfo.class);

                        String temp=obj.getId();

                        if(temp.equals(id))
                        {
                            mBookmark.setChecked(true);
                        }

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else
        {
            mBookmark.setChecked(false);
        }






        dAndT="";
        Intent i =getIntent();
        id = i.getStringExtra(DisplayEventList.event_id);
        name = i.getStringExtra(DisplayEventList.event_name);
        time = i.getLongExtra(DisplayEventList.event_time,0);
        loc = i.getStringExtra(DisplayEventList.event_loc);
        owner = i.getStringExtra(DisplayEventList.event_own);
        info = i.getStringExtra(DisplayEventList.event_info);
        usercreate = i.getStringExtra(DisplayEventList.event_userid);

        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser curr= Auth.getCurrentUser();
        final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);

        final String currUid=curr.getUid();


        try{
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date netDate = (new Date(time));
            dAndT=sdf.format(netDate);
        }
        catch(Exception ex){

        }



       // mTextViewListName.setText(name);
        setTitle(name);
        mTextViewListOwner.setText(owner);
        mTextViewInfo.setText(info);
        getmTextViewLocation.setText(loc);
        getmTextViewTime.setText(dAndT);

        final Button button = (Button)findViewById(R.id.detaillist);

        if(usercreate.equals(currUid))
        {
            button.setVisibility(View.VISIBLE);
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usercreate.equals(currUid))
                {

                    showUpdateDialog(id,usercreate);
                }
                else
                {

                    myAlert.setMessage("You are not authenticated to edit this event!")
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();



                                }
                            })
                            .create();
                    myAlert.show();
                }
            }
        });
    }

    private void showUpdateDialog(final String eventid,  final String userid)

    {

        LayoutInflater layoutInflater = getLayoutInflater();


        dialogview = layoutInflater.inflate(R.layout.update,null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setView(dialogview);



        final EditText editTextname = (EditText)dialogview.findViewById(R.id.event_nameUpdate);
        final EditText editTextinfo = (EditText)dialogview.findViewById(R.id.about_eventUpdate);
        final EditText editTextloc = (EditText)dialogview.findViewById(R.id.locationUpdate);
        final EditText editTextown = (EditText)dialogview.findViewById(R.id.organizeUpdate);


        editTextname.setText(name);
        editTextinfo.setText(info);
        editTextloc.setText(loc);
        editTextown.setText(owner);

        dateUpdate=(Button)dialogview.findViewById(R.id.datepickerUpdate);
        timeUpdate =(Button)dialogview.findViewById(R.id.timepickerUpdate);







        //time and date to be done

        final Button button = (Button)dialogview.findViewById(R.id.sendButtonUpdate);
        dialog.setTitle("Update Details of the Event");
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newname = editTextname.getText().toString().trim();
                String newloc = editTextloc.getText().toString().trim();
                String newown = editTextown.getText().toString().trim();
                String newinfo = editTextinfo.getText().toString().trim();

                String dateString=day+"/"+month+"/"+year+" "+hour+":"+min;

                long eventDateLong=0;

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                    Date eventDate = sdf.parse(dateString);
                    eventDateLong=eventDate.getTime();

                }

                catch (ParseException e){
                }

                updatedetails(eventid,newname,newloc,newown,newinfo,eventDateLong,userid);
                alertDialog.dismiss();

            }
        });

    }
    private boolean updatedetails(final String id,String name,String loc,String owner,String info,long time,String user)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Events").child(id);
       final DatabaseReference mdatabaseReference= FirebaseDatabase.getInstance().getReference("Bookmark").child(user);
        final EventInfo event = new EventInfo(name,time,loc,info,owner,user,id);

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot eventsnap: dataSnapshot.getChildren())
                {
                    EventInfo event1 = eventsnap.getValue(EventInfo.class);
                    if(event1.getId().equals(id)) {
                        mdatabaseReference.child(eventsnap.getKey()).setValue(event);
                        break;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.setValue(event);// Intent i = new Intent(this,MainActivity.class);
        //startActivity(i);
        finish();
        Toast.makeText(this,"Event Updated Successfully!",Toast.LENGTH_LONG).show();
        return true;
    }


    public void showDateDialog(View v) {
        DialogFragment newFragment = new DatePickerUpdateFragment();

        newFragment.show(getFragmentManager(),"datePicker");


    }

    public void showTimeDialog(View v) {
        DialogFragment newFragment = new TimePickerUpdateFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }




    public static void showUpdateDate(int y,int m,int d)
    {
        year=y;
        month=m+1;
        day=d;


        dateUpdate.setText(d+"-"+month+"-"+y);
    }


    public static void showUpdateTime(int h,int m)
    {

        hour=h;
        min=m;

        if(m/10==0) {


            timeUpdate.setText(h + ":0" + m);
        }

        else{
            timeUpdate.setText(h + ":" + m);

        }
    }


    public void bookmarkEvent(View view){

        boolean checked= ((CheckBox)view).isChecked();


        if(checked)
        {

            ((CheckBox)view).setChecked(true);

            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            FirebaseUser currUser=mAuth.getCurrentUser();

            String currUid=currUser.getUid();

            DatabaseReference bookmarkUserReference=bookmarkDatabaseReference.child(currUid);



            EventInfo obj=new EventInfo(name,time,loc,info,owner,usercreate,id);
          //  String temp2 = obj.getId();
           // DatabaseReference bookmarkUserReference=bookmarkDatabaseReference.child(currUid).child(temp2);

            bookmarkUserReference.push().setValue(obj);


        }

        else{

            ((CheckBox)view).setChecked(false);

            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            FirebaseUser currUser=mAuth.getCurrentUser();

            String currUid=currUser.getUid();

            final DatabaseReference bookmarkUserReference=bookmarkDatabaseReference.child(currUid);


            bookmarkUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot eventsnap: dataSnapshot.getChildren())  {

                        EventInfo obj=eventsnap.getValue(EventInfo.class);


                        if(obj.getId().equals(id)) {
                            bookmarkUserReference.child(eventsnap.getKey()).removeValue();
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
