package com.cctpl.agroplannetproduct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cctpl.agroplannetproduct.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class Login extends AppCompatActivity {
    CountryCodePicker ccp ;
    EditText phonenumber ;
    Button getotp ;
    TextView sign_up ;
    FirebaseFirestore db  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_up = findViewById(R.id.sign_up);
        ccp = findViewById(R.id.ccp);
        phonenumber = findViewById(R.id.number);
        ccp.registerCarrierNumberEditText(phonenumber);
        getotp = findViewById(R.id.getOtp);

        getSupportActionBar().hide();
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this , GetOTP.class));
                finish();
            }
        });



        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phonenumber.getText().toString().isEmpty()){
                    phonenumber.setError("Enter Phone No");
                }
                else {

                    Intent intent = new Intent(Login.this , GetOTP.class);
                    intent.putExtra("number",ccp.getFullNumberWithPlus().replace(" ",""));
                    startActivity(intent);
                    finish();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }
}