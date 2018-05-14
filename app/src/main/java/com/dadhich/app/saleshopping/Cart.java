package com.dadhich.app.saleshopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity implements ImageAdapter.OnItemClickListener{
    private RecyclerView mReCyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressbar;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBlistener;
    private DatabaseReference mDatabaseref;
    private List<Upload> mUploads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mReCyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressbar = (ProgressBar)findViewById(R.id.progress_circle);
        mReCyclerView.setHasFixedSize(true);
        mReCyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mUploads = new ArrayList<>();

        mAdapter = new ImageAdapter(Cart.this, mUploads);
        mReCyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(Cart.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseref = FirebaseDatabase.getInstance().getReference("user");
        mDBlistener = mDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    Upload upload = postsnap.getValue(Upload.class);
                    upload.setmKey(postsnap.getKey());

                    mUploads.add(upload);


                }
                mAdapter.notifyDataSetChanged();

                mProgressbar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Cart.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                mProgressbar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getApplicationContext(), "Normal click at position" + position,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(getApplicationContext(), "onWhat click at position" + position,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        //Toast.makeText(getApplicationContext(), "delete click at position" + position,Toast.LENGTH_SHORT).show();
       Upload selectItem = mUploads.get(position);
        final String selectkey = selectItem.getmKey();
        StorageReference imageref = mStorage.getReferenceFromUrl(selectItem.getmImageUrl());
        imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseref.child(selectkey).removeValue();
                Toast.makeText(getApplicationContext(), "Item deleted Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }


    protected  void OnDestroy(){
        super.onDestroy();
        mDatabaseref.removeEventListener(mDBlistener);


    }
}
