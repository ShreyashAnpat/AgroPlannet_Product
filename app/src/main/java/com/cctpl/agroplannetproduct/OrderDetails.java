package com.cctpl.agroplannetproduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.agroplannetproduct.Adapter.Order_details_adapter;
import com.cctpl.agroplannetproduct.tabApp.DeliveredOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    RecyclerView recyclerView ;
    Order_details_adapter adapter ;
    List<String> Image , ProductDetails , ProductPrice , ProductCount , ProductMeasuriment , ProductSellingPrice;
    FirebaseFirestore db ;
    TextView subtotal , OrderDate ,OrderTime  ,ItemCount ,DeliveredOrder;
    CardView OrderInfo ;
    String Total , User ,Time , Date , PhoneNo;
    CardView Delete_order , cardView ;
    ImageView back ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        db = FirebaseFirestore.getInstance() ;
        User = getIntent().getStringExtra("UserId");
        Total = getIntent().getStringExtra("total");
        Time = getIntent().getStringExtra("Time");
        Date = getIntent().getStringExtra("date");
        PhoneNo = getIntent().getStringExtra("PhoneNo");

        recyclerView = findViewById(R.id.card_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Image = new ArrayList<>();
        ProductDetails = new ArrayList<>();
        ProductPrice = new ArrayList<>();
        ProductCount = new ArrayList<>();
        ProductMeasuriment = new ArrayList<>();
        ProductSellingPrice =new ArrayList<>();
        subtotal = findViewById(R.id.textView12);
        OrderDate = findViewById(R.id.textView14);
        OrderTime = findViewById(R.id.textView16);
        OrderInfo = findViewById(R.id.OrderInfo);
        back = findViewById(R.id.back);
        Delete_order = findViewById(R.id.Cancel_Order);
        DeliveredOrder = findViewById(R.id.place_order);
        cardView = findViewById(R.id.cardView);

        ProgressDialog pd = new ProgressDialog(OrderDetails.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Loading");
        pd.show();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              OrderDetails.super.onBackPressed();
            }
        });

        db.collection("Pending Order").whereEqualTo("UserId", User).whereEqualTo("Total",Total)
                .whereEqualTo("Order_Date",Date).whereEqualTo("Order_Time",Time)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentChange doc : task.getResult().getDocumentChanges()){

                    if (doc.getType() == DocumentChange.Type.ADDED){
                        Delete_order.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.collection("Pending Order").document(doc.getDocument().getId()).delete();
                                db.collection("Pending Order").document(doc.getDocument().getId()).collection("Order Product").document().delete() ;
                                startActivity(new Intent(OrderDetails.this , MainActivity.class));
                                finish();
                            }
                        });


                        db.collection("Pending Order").document(doc.getDocument().getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                OrderDate.setText(task.getResult().get("Order_Date").toString());
                                OrderTime.setText(task.getResult().get("Order_Time").toString());
                            }
                        });

                        if (getIntent().getStringExtra("flag").equals("2")){
                            cardView.setVisibility(View.GONE);
                            DeliveredOrder.setText("Remove From Order History");
                        }

                        DeliveredOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String , Object> update = new HashMap<>();
                                update.put("Flag" , "2");
                                db.collection("Pending Order").document(doc.getDocument().getId()).update(update);
                                startActivity(new Intent(OrderDetails.this ,MainActivity.class));
                                finish();
                            }
                        });


                        db.collection("Pending Order").document(doc.getDocument().getId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                db.collection("Pending Order").document(value.getId()).collection("Order Product")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        Image.clear();
                                        ProductDetails.clear();
                                        ProductCount.clear();
                                        ProductPrice.clear();
                                        ProductMeasuriment.clear();
                                        ProductSellingPrice.clear();
                                        Double total = 0.0;
                                        for (DocumentChange documentChange : value.getDocumentChanges()){
                                            if (documentChange.getType() == DocumentChange.Type.ADDED){

                                                Image.add(documentChange.getDocument().get("Image1").toString());
                                                ProductCount.add(documentChange.getDocument().get("ProductCount").toString());
                                                ProductDetails.add(documentChange.getDocument().get("ProductDetails").toString());
                                                ProductPrice.add(documentChange.getDocument().get("TotalPrice").toString());
                                                ProductMeasuriment.add(documentChange.getDocument().get("ProductMeasurement").toString());
                                                ProductSellingPrice.add(documentChange.getDocument().get("ProductOriginalPrice").toString());
                                                total = total + Double.valueOf(documentChange.getDocument().get("TotalPrice").toString());
                                            }



                                            recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetails.this));
                                            adapter = new Order_details_adapter(OrderDetails.this, Image , ProductCount , ProductDetails ,ProductPrice ,ProductMeasuriment,ProductSellingPrice );
                                            recyclerView.setAdapter(adapter);

                                            pd.cancel();
                                        }
                                        subtotal.setText("₹ " + total.toString());


                                    }
                                });
                            }
                        });
                    }
                }
            }
        });


        OrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetails.this, Order_Information.class);
                intent.putExtra("UserID" , User);
                intent.putExtra("Total", Total);
                intent.putExtra("Time" , Time);
                intent.putExtra("Date", Date);
                intent.putExtra("PhoneNo",PhoneNo);
                startActivity(intent);
            }
        });
//

    }
}