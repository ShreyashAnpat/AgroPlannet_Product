package com.cctpl.agroplannetproduct.tabApp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.agroplannetproduct.Adapter.pendingOrderAdapter;
import com.cctpl.agroplannetproduct.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class PendingOrder extends Fragment {

    RecyclerView Pending_order ;
    pendingOrderAdapter Adapter ;
    FirebaseFirestore db ;
    FirebaseAuth auth ;
    String CurrentUser ;
    List<String> Phone_no ,Profile , address ,name ,UserId , Total ,ProductCount ,Time ,Date ,flag ;
    LottieAnimationView nodata ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pending_order, container, false);
        auth = FirebaseAuth.getInstance() ;
        db = FirebaseFirestore.getInstance();
        CurrentUser = auth.getCurrentUser().getUid() ;
        Phone_no = new ArrayList<>();
        Profile = new ArrayList<>();
        address = new ArrayList<>();
        name = new ArrayList<>();
        UserId = new ArrayList<>();
        Total = new ArrayList<>();
        Time = new ArrayList<>();
        Date = new ArrayList<>();
        nodata = view.findViewById(R.id.nodata);
        ProductCount = new ArrayList<>();
        flag = new ArrayList<>();
        ProgressDialog pd = new ProgressDialog(view.getContext());
        pd.setMessage("Loading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        db.collection("Pending Order").whereEqualTo("Flag","1").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (DocumentChange doc : value.getDocumentChanges()){
                        Total.add(doc.getDocument().get("Total").toString());
                        UserId.add(doc.getDocument().get("UserId").toString());
                        ProductCount.add(doc.getDocument().get("Total_Product").toString());
                        Time.add(doc.getDocument().get("Order_Time").toString());
                        Date.add(doc.getDocument().get("Order_Date").toString());
                        flag.add(doc.getDocument().get("Flag").toString());
                        if (Date.size() != 0){
                            nodata.setVisibility(View.GONE);
                            pd.cancel();

                        }



                        db.collection("user").document(doc.getDocument()
                                .get("UserId").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Phone_no.add(task.getResult().get("phone_no").toString());
                                Profile.add(task.getResult().get("imgUrl").toString());
                                address.add(task.getResult().get("address ").toString());
                                name.add(task.getResult().get("name").toString());

                                Pending_order = view.findViewById(R.id.Pending_order);
                                Pending_order.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                Adapter = new pendingOrderAdapter(view.getContext(), Phone_no,Profile , address , name, Total,UserId ,ProductCount,Time,Date,flag);
                                Pending_order.setAdapter(Adapter);
                            }
                        });
                    }
                pd.cancel();
            }
        });


        return  view ;
    }
}