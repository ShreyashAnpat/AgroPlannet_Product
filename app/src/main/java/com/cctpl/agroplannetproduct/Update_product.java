package com.cctpl.agroplannetproduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.agroplannetproduct.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Update_product extends AppCompatActivity  {

    TextView Product_type , measurement;
    EditText Product_Detail ,MRP , Selling_Price , total_product_number ;
    String  Uri,Uri2,Uri3 ;
    ImageView Image1,Image2,Image3 ;
    Button Update_product ;
    FirebaseFirestore db ;
    StorageReference Folder ,Imagename;
    android.net.Uri imageUri ;
    ProgressDialog pd ;
    int imageNo ;
    FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        measurement = findViewById(R.id.measurement);
        Product_type =findViewById(R.id.Spinner);
        Image1 = findViewById(R.id.image1);
        Image2 = findViewById(R.id.image2);
        Image3 = findViewById(R.id.image3);
        Product_Detail = findViewById(R.id.Details);
        MRP = findViewById(R.id.M_R_P);
        Selling_Price = findViewById(R.id.Selling_Prise);
        total_product_number = findViewById(R.id.product_number);
        Update_product = findViewById(R.id.addProduct);
        pd = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance() ;

        Folder = FirebaseStorage.getInstance().getReference().child("Image");

        Image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = 1 ;
                selectImage();
            }
        });

        Image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = 2 ;
                selectImage();
            }
        });

        Image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo = 3 ;
                selectImage();
            }
        });

        Uri =getIntent().getStringExtra("Image1");
        Uri2 =getIntent().getStringExtra("Image2") ;
        Uri3 = getIntent().getStringExtra("Image3");
        Picasso.get().load(Uri).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(Image1);
        Picasso.get().load(Uri2).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(Image2);
        Picasso.get().load(Uri3).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(Image3);
        Product_Detail.setText(getIntent().getStringExtra("product_details"));
        MRP.setText(getIntent().getStringExtra("product_mrp"));
        Selling_Price.setText(getIntent().getStringExtra("product_selling_price"));
        total_product_number.setText(getIntent().getStringExtra("Available"));
        Product_type.setText(getIntent().getStringExtra("product_type"));
        measurement.setText(getIntent().getStringExtra("ProductMeasurement"));
        Update_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Adding Project");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                Map<String , Object> Update = new HashMap<>();
                Update.put("Product_Type",Product_type.getText());
                Update.put("Product_Details",Product_Detail.getText().toString());
                Update.put("MRP" , MRP.getText().toString());
                Update.put("Product_Selling_Price" , Selling_Price.getText().toString());
                Update.put("Measurement",measurement.getText());
                Update.put("Image1", Uri);
                Update.put("Image2" , Uri2);
                Update.put("Image3",Uri3);
                Update.put("Available",total_product_number.getText().toString());

                db.collection("Product").whereEqualTo("Product_Details" , getIntent().getStringExtra("product_details")).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot doc : value.getDocuments()){

                            String child = doc.getId() ;
                            db.collection("Product").document(child).update(Update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    com.cctpl.agroplannetproduct.Update_product.super.onBackPressed();
                                    pd.cancel();
                                }
                            });
                        }
                    }
                });


            }
        });

    }

    private void selectImage() {

        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == AppCompatActivity.RESULT_OK) {
                imageUri= result.getUri();
                if (imageNo == 1){
                    Image1.setImageURI(imageUri);
                    UploadImage(imageUri);
                    imageUri = null;

                }
                else if (imageNo == 2){
                    Image2.setImageURI(imageUri);
                    UploadImage(imageUri);
                    imageUri = null;
                }
                else if (imageNo == 3){
                    Image3.setImageURI(imageUri);
                    UploadImage(imageUri);
                    imageUri = null;
                }

            }
            else
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void UploadImage(Uri imageUri) {
        pd.show();
        pd.setMessage("Selecting Image");
        pd.setCanceledOnTouchOutside(false);
        Imagename = Folder.child(auth.getCurrentUser().getUid() + imageUri.getLastPathSegment());
        Imagename.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        if (imageNo == 1){
                            Uri = uri.toString() ;
                        }
                        else if (imageNo == 2){
                            Uri2 = uri.toString();
                        }
                        else  if (imageNo == 3){
                            Uri3 = uri.toString() ;
                        }
                    }
                });
                pd.cancel();
            }
        });
    }

}