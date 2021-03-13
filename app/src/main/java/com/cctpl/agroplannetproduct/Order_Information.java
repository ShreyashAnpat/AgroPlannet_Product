package com.cctpl.agroplannetproduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Order_Information extends AppCompatActivity {

    String Total, UserID , Date, Time ,PhoneNumber;
    FirebaseFirestore db ;
    CircleImageView profile ;
    TextView UserName , PhoneNo , Location , OrderTime , OrderDate ;
    Button Call ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__information);

        Total = getIntent().getStringExtra("Total");
        UserID = getIntent().getStringExtra("UserID");
        Date = getIntent().getStringExtra("Date");
        Time = getIntent().getStringExtra("Time");
        PhoneNumber = getIntent().getStringExtra("PhoneNo");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = FirebaseFirestore.getInstance();
        profile = findViewById(R.id.profile_image);
        UserName = findViewById(R.id.username);
        PhoneNo = findViewById(R.id.phone_number);
        Location = findViewById(R.id.location);
        OrderTime = findViewById(R.id.Time);
        OrderDate = findViewById(R.id.Date);
        Call = findViewById(R.id.callUser);

        if (ContextCompat.checkSelfPermission(Order_Information.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Order_Information.this,new String[]{
                    Manifest.permission.CALL_PHONE
            },100);
        }

        db.collection("user").document(UserID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                UserName.setText(value.get("name").toString());
                PhoneNo.setText(value.get("phone_no").toString());
                Location.setText(value.get("address ").toString());
                OrderDate.setText(Date);
                OrderTime.setText(Time);
                Picasso.get().load(value.get("imgUrl").toString()).into(profile);
            }
        });


        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" +PhoneNumber));
                startActivity(callIntent);
            }
        });



    }
}