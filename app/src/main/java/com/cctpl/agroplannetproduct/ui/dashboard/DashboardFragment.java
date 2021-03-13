package com.cctpl.agroplannetproduct.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.agroplannetproduct.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

    private static final int RESULT_OK = -1;
    String[] Product_Type_Array ={"SPRAYER PUMPS","FILMS" ,"BAGS" , "PLASTIC PRODUCTS" ,"ACCESSORIES"},measurement_Array ={"Piece", "Meter","Other"};
    Spinner Product_type , measurement;
    EditText  Product_Detail ,MRP , Selling_Price , total_product_number ,OffPercentages;
    String Product_Type , Measurement , Uri1,Uri,Uri2,Uri3 ;
    ImageView Image1,Image2,Image3 ;
    Button Add_Product ;
    FirebaseFirestore db ;
    StorageReference Folder ,Imagename;
    Uri imageUri ;
    Button Calculate ;
    ProgressDialog pd ;
    int imageNo ;
    FirebaseAuth auth ;
    RadioGroup radioGroup ;
    RadioButton button ;
    LinearLayout off ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        measurement = root.findViewById(R.id.measurement);
        Product_type = root.findViewById(R.id.Spinner);
        Product_type.setOnItemSelectedListener(this);
        measurement.setOnItemSelectedListener(this);
        Image1 = root.findViewById(R.id.image1);
        Image2 = root.findViewById(R.id.image2);
        Image3 = root.findViewById(R.id.image3);
        Product_Detail = root.findViewById(R.id.Details);
        MRP = root.findViewById(R.id.M_R_P);
        Selling_Price = root.findViewById(R.id.Selling_Prise);
        total_product_number = root.findViewById(R.id.product_number);
        Add_Product = root.findViewById(R.id.addProduct);
        pd = new ProgressDialog(root.getContext());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance() ;
        OffPercentages = root.findViewById(R.id.offPercentage);
        Folder = FirebaseStorage.getInstance().getReference().child("Image");
        Calculate = root.findViewById(R.id.Calculate);
        radioGroup = root.findViewById(R.id.RadioGroup);
        off = root.findViewById(R.id.off);
        button = root.findViewById(R.id.radioButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                off.setVisibility(View.VISIBLE);
                Calculate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MRP.getText().toString().isEmpty()){
                            MRP.setError("Enter MRP");
                        }else {
                            Double no = Double.parseDouble(MRP.getText().toString()) -(Double.parseDouble(MRP.getText().toString())* Double.parseDouble(OffPercentages.getText().toString()) /100);
                            Selling_Price.setText(no.toString());
                            off.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });








        ArrayAdapter category_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,Product_Type_Array);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Product_type.setAdapter(category_adapter);
        ArrayAdapter  measurement_adapter = new ArrayAdapter(getContext() ,android.R.layout.simple_spinner_item ,measurement_Array);
        measurement_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurement.setAdapter(measurement_adapter);

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

        Add_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getContext(), Uri, Toast.LENGTH_SHORT).show();
                pd.setMessage("Adding Project");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                Map<String , Object> addProduct = new HashMap<>();
                addProduct.put("Product_Type",Product_Type);
                addProduct.put("Product_Details",Product_Detail.getText().toString());
                addProduct.put("MRP" , MRP.getText().toString());
                addProduct.put("Product_Selling_Price" , Selling_Price.getText().toString());
                addProduct.put("Measurement",Measurement);
                addProduct.put("Image1", Uri);
                addProduct.put("Image2" , Uri2);
                addProduct.put("Image3",Uri3);
                addProduct.put("Available",total_product_number.getText().toString());

                db.collection("Product").document().set(addProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.cancel();
                        Fragment fragment = new DashboardFragment();
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();
                    }
                });

            }
        });

        return root;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spin = (Spinner)parent;
        Spinner spin2 = (Spinner)parent;

        if(spin.getId() == Product_type.getId()){
            Product_Type    = Product_Type_Array[position];

        }
        else
        if (spin2.getId() == measurement.getId()) {
            Measurement   = measurement_Array[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void selectImage() {

        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(getActivity(),this);


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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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


