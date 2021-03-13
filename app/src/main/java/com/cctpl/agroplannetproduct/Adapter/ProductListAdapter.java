package com.cctpl.agroplannetproduct.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.agroplannetproduct.R;
import com.cctpl.agroplannetproduct.Update_product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{
    Context context;
    LayoutInflater inflater ;
    List<String> product_details ,product_mrp , product_selling_price , product_type ,productImage1 ,productImage2,productImage3 ,Available , ProductMeasurement;

    public ProductListAdapter(Context context, List<String> product_details, List<String> product_mrp, List<String> product_selling_price, List<String> product_type, List<String> productImage1, List<String> productImage2, List<String> productImage3, List<String> available, List<String> productMeasurement) {
        this.context = context ;
        this.inflater = LayoutInflater.from(context);
        this.product_details = product_details ;
        this.product_mrp = product_mrp ;
        this.product_selling_price= product_selling_price ;
        this.product_type = product_type ;
        this.productImage1 = productImage1 ;
        this.productImage2 = productImage2 ;
        this.productImage3 = productImage3 ;
        this.ProductMeasurement = productMeasurement ;
        this.Available= available ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Picasso.get().load(productImage1.get(position)).placeholder(R.drawable.ic_baseline_add_photo_alternate_24).into(holder.ProductImage);
       holder.selling_price.setText("₹"+product_selling_price.get(position) );
       holder.details.setText(product_details.get(position));
       holder.MRP.setText("M.R.P. : ₹"+product_mrp.get(position));
       Spannable spannable = (Spannable) holder.MRP.getText() ;
       holder.available.setText("Available : " + Available.get(position) +" " + ProductMeasurement.get(position) );
       spannable.setSpan(new StrikethroughSpan(), 0,holder.MRP.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context , Update_product.class);
               intent.putExtra( "Image1",productImage1.get(position));
               intent.putExtra("Image2", productImage2.get(position));
               intent.putExtra("Image3", productImage3.get(position));
               intent.putExtra("product_details" , product_details.get(position));
               intent.putExtra("product_mrp" , product_mrp.get(position));
               intent.putExtra("product_selling_price" , product_selling_price.get(position));
               intent.putExtra("ProductMeasurement" , ProductMeasurement.get(position));
               intent.putExtra("Available",Available.get(position));
               intent.putExtra("product_type",product_type.get(position));
               context.startActivity(intent);
               notifyDataSetChanged();
           }
       });
    }

    @Override
    public int getItemCount() {
        return product_details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ProductImage ;
        LinearLayout linearLayout ;
        TextView MRP , selling_price, details , available ;
        public ViewHolder(@NonNull View itemView) {
             super(itemView);
             linearLayout = itemView.findViewById(R.id.linearLayout);
             ProductImage = itemView.findViewById(R.id.product_image);
             MRP  = itemView.findViewById(R.id.M_R_P);
             selling_price = itemView.findViewById(R.id.Selling_Prise);
             details = itemView.findViewById(R.id.Product_Details);
             available = itemView.findViewById(R.id.available);

        }
    }
}
