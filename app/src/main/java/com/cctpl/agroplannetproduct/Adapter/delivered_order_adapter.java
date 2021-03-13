package com.cctpl.agroplannetproduct.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.agroplannetproduct.OrderDetails;
import com.cctpl.agroplannetproduct.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class delivered_order_adapter extends RecyclerView.Adapter<delivered_order_adapter.ViewHolder> {

    List<String>  Phone_no ,Profile , address ,name ,UserId , Total , Product_Count , Date , Time , flag;
    Context context ;
    LayoutInflater inflater ;

    public delivered_order_adapter(Context context, List<String> phone_no, List<String> profile, List<String> address, List<String> name, List<String> total, List<String> userId, List<String> productCount, List<String> time, List<String> date, List<String> flag) {
        this.context = context ;
        this.inflater = LayoutInflater.from(context);
        this.Phone_no = phone_no ;
        this.Profile = profile ;
        this.address = address ;
        this.name = name ;
        this.UserId = userId ;
        this.Total = total ;
        this.Product_Count = productCount ;
        this.Date = date ;
        this.Time = time ;
        this.flag = flag ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pending_order_list ,parent,false);
        return  new delivered_order_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.UserName.setText(name.get(position));
        holder.Amount.setText(Total.get(position));
        holder.TotalProduct.setText("Total Product - "+Product_Count.get(position));
        Picasso.get().load(Profile.get(position)).into(holder.Profile);

        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("UserId" , UserId.get(position));
                intent.putExtra("total" ,Total.get(position));
                intent.putExtra("date",Date.get(position));
                intent.putExtra("Time",Time.get(position));
                intent.putExtra("PhoneNo" ,Phone_no.get(position));
                intent.putExtra("flag" ,flag.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout order ;
        CircleImageView Profile ;
        TextView UserName , TotalProduct , Amount ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order = itemView.findViewById(R.id.order);
            Profile = itemView.findViewById(R.id.Profile);
            UserName = itemView.findViewById(R.id.Username);
            TotalProduct = itemView.findViewById(R.id.TotalProduct);
            Amount = itemView.findViewById(R.id.amount);
        }
    }
}
