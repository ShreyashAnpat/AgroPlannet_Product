package com.e.agroplannetproduct.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.agroplannetproduct.Adapter.ProductListAdapter;
import com.e.agroplannetproduct.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView productList ;
    ProductListAdapter  productListAdapter ;
    List<String> Product_Type ,Product_details,Product_MRP,Product_selling_price, ProductImage1,ProductImage2,ProductImage3  ,Available ,ProductMeasurement;
    ProgressDialog pd ;
    FirebaseFirestore db ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Product_details = new ArrayList<>();
        Product_Type = new ArrayList<>();
        Product_MRP = new ArrayList<>();
        Product_selling_price = new ArrayList<>();
        ProductImage1 = new ArrayList<>();
        ProductImage2 = new ArrayList<>();
        ProductImage3 = new ArrayList<>();
        ProductMeasurement = new ArrayList<>();
        Available = new ArrayList<>();
        pd  = new ProgressDialog(root.getContext());
        productList = root.findViewById(R.id.Product_List);
        db = FirebaseFirestore.getInstance();

        pd.setMessage("Loading Product");
        db.collection("Product").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    Product_details.add(documentChange.getDocument().get("Product_Details").toString());
                    Product_MRP.add(documentChange.getDocument().get("MRP").toString());
                    Product_selling_price.add(documentChange.getDocument().get("Product_Selling_Price").toString());
                    Product_Type.add(documentChange.getDocument().get("Product_Type").toString());
                    ProductImage1.add(documentChange.getDocument().get("Image1").toString());
                    ProductImage2.add(documentChange.getDocument().get("Image2").toString());
                    ProductImage3.add(documentChange.getDocument().get("Image3").toString());
                    Available.add(documentChange.getDocument().get("Available").toString());
                    ProductMeasurement.add(documentChange.getDocument().get("Measurement").toString());

                }

                productList.setLayoutManager(new LinearLayoutManager(root.getContext()));
                productListAdapter = new ProductListAdapter(root.getContext(),Product_details, Product_MRP,Product_selling_price,Product_Type ,ProductImage1,ProductImage2,ProductImage3 , Available,ProductMeasurement);
                productList.setAdapter(productListAdapter);
                pd.cancel();
            }
        });

        return root;
    }
}