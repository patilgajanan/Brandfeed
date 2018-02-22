package com.appsplanet.brandfeed.Acitvity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appsplanet.brandfeed.Adapter.ReceeAdapter;
import com.appsplanet.brandfeed.Model.DeployItem;
import com.appsplanet.brandfeed.R;

import java.util.ArrayList;

public class ReceeStore extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar_deployment_store;
    private RecyclerView recycler_view_deployment;
    private ReceeAdapter deployAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recee_store);
        toolbar_deployment_store = findViewById(R.id.toolbar_deployment_store);
        setSupportActionBar(toolbar_deployment_store);
        getSupportActionBar().setTitle("Recee");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        recycler_view_deployment = findViewById(R.id.recycler_view_deployment);
        recycler_view_deployment.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ReceeStore.this, 3);
        deployAdapter = new ReceeAdapter(ReceeStore.this, getDeployPhotoCategory());
        recycler_view_deployment.setLayoutManager(layoutManager);
        recycler_view_deployment.setAdapter(deployAdapter);

        toolbar_deployment_store.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceeStore.this, MainActivity.class));
                finish();
            }
        });


    }

    public ArrayList<DeployItem> getDeployPhotoCategory() {
        ArrayList<DeployItem> bookingItems = new ArrayList<>();
        bookingItems.add(new DeployItem("DEALER BOARD"));
        bookingItems.add(new DeployItem("COUNTER"));
        bookingItems.add(new DeployItem("SHELFSTRIP"));
        bookingItems.add(new DeployItem("COUNTERBACK"));
        bookingItems.add(new DeployItem("PILLER"));
        bookingItems.add(new DeployItem("BEAM"));
        bookingItems.add(new DeployItem("ONEWAY"));
        bookingItems.add(new DeployItem("SHUTTERGATE"));
        bookingItems.add(new DeployItem("OUTSIDEWALL1"));
        bookingItems.add(new DeployItem("OUTSIDEWALL2"));
        bookingItems.add(new DeployItem("FULLSHOP"));
        bookingItems.add(new DeployItem("FULLSHOP"));
        if (deployAdapter != null) {
            deployAdapter.notifyDataSetChanged();
        }
        return bookingItems;
    }

}
