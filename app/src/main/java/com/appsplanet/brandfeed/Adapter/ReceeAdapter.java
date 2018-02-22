package com.appsplanet.brandfeed.Adapter;

/**
 * Created by surajk44437 on 8/12/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsplanet.brandfeed.Model.DeployItem;

import com.appsplanet.brandfeed.R;

import java.util.ArrayList;
import java.util.List;

public class ReceeAdapter extends RecyclerView.Adapter<ReceeAdapter.MyViewHolder> {
    private static final int CAMERA_REQUEST = 1888;
    private List<DeployItem> homeItems;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_deployment_photo_category;
        public LinearLayout ll_home_item_deploy;


        public MyViewHolder(View view) {
            super(view);

            tv_deployment_photo_category = (TextView) view.findViewById(R.id.tv_deployment_photo_category);
            ll_home_item_deploy = (LinearLayout) view.findViewById(R.id.ll_home_item_deploy);


        }
    }


    public ReceeAdapter(Context context, ArrayList<DeployItem> homeItems) {
        this.context = context;
        this.homeItems = homeItems;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DeployItem homeItem = homeItems.get(position);
        holder.tv_deployment_photo_category.setText(homeItem.getStr_name());
        holder.ll_home_item_deploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity) context).startActivityForResult(Intent.createChooser(cameraIntent, "Select Picture"), CAMERA_REQUEST);
                //onActivityResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });


    }

    @Override
    public int getItemCount() {
        return homeItems.size();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // imageView.setImageBitmap(photo);
        }
    }


}
