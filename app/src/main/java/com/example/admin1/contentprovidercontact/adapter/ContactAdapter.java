package com.example.admin1.contentprovidercontact.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin1.contentprovidercontact.R;
import com.example.admin1.contentprovidercontact.datamodel.Contact;
import com.example.admin1.contentprovidercontact.interfaces.OnAdapterItemClickListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class is used to set the contacts of the phone on the RecyclerView
 *
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Contact> mContactList;
    private OnAdapterItemClickListener mOnAdapterItemClickListener;

    public ContactAdapter(Context context,ArrayList<Contact> mContactList,OnAdapterItemClickListener onAdapterItemClickListener){
        this.mContext = context;
        this.mContactList = mContactList;
        this.mOnAdapterItemClickListener = onAdapterItemClickListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mNameTv.setText(mContactList.get(position).getName());
        holder.mNumberTv.setText(mContactList.get(position).getNumber());
        Glide.with(mContext)
                .load(mContactList.get(position).getPhotoUri())
                .asBitmap()
                .error(R.drawable.ic_account_circle_black_24dp)
                .into(holder.mUserImageIv);

    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    /**
     * ViewHolder class for initializing views on the row layout
     */
    class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mUserImageIv;
        private TextView mNameTv,mNumberTv;
        MyViewHolder(View itemView) {
            super(itemView);
            mUserImageIv = itemView.findViewById(R.id.iv_user_pic);
            mNameTv = itemView.findViewById(R.id.tv_name);
            mNumberTv = itemView.findViewById(R.id.tv_number);

            //OnClickListener on the row of Contacts
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAdapterItemClickListener.onAdapterItemClick(v,getAdapterPosition());

                }
            });
        }
    }

}
