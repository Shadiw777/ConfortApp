package com.confortapp.leon.confortapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.confortapp.leon.confortapp.Common.Common;
import com.confortapp.leon.confortapp.Database.Database;
import com.confortapp.leon.confortapp.Model.Product;
import com.confortapp.leon.confortapp.ViewHolder.ProductsViewHolder;
import com.confortapp.leon.confortapp.Interface.ItemClickListener;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductsList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Product, ProductsViewHolder> adapter;

    //Search functional
    FirebaseRecyclerAdapter<Product, ProductsViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //Favorites
    Database localDB;

    //Facebook Share
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    //Create Target from Picasso
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //Create Photo from Bitmap
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class)) {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    //SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/FuturaPTDemi.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_products_list);

        //Init Facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Products");

        //Local DB
        localDB = new Database(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Get Intent here
                if (getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if (!categoryId.isEmpty() && categoryId != null) {
                    if (Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoryId);
                    else {
                        Toast.makeText(ProductsList.this, "Please check your Internet connection !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //Get Intent here
                if (getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if (!categoryId.isEmpty() && categoryId != null) {
                    if (Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoryId);
                    else {
                        Toast.makeText(ProductsList.this, "Please check your Internet connection !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //Search
                materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
                materialSearchBar.setHint("Enter your product");
                //Write function to load Suggest from Firebase
                loadSuggest();
                materialSearchBar.setLastSuggestions(suggestList);
                materialSearchBar.setCardViewElevation(10);
                materialSearchBar.addTextChangeListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        //When user type their text, we will change suggest list
                        List<String> suggest = new ArrayList<>();
                        for (String search : suggestList) {
                            if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                                suggest.add(search);
                        }
                        materialSearchBar.setLastSuggestions(suggest);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                    @Override
                    public void onSearchStateChanged(boolean enabled) {
                        //When Search bar is close
                        //Restore original suggest adapter
                        if (!enabled)
                            recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onSearchConfirmed(CharSequence text) {
                        //When search finish
                        //Show result of search adapter
                        startSearch(text);
                    }

                    @Override
                    public void onButtonClicked(int buttonCode) {
                    }
                });
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_products);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(
                Product.class,
                R.layout.products_item,
                ProductsViewHolder.class,
                foodList.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(final ProductsViewHolder viewHolder, final Product model, final int position) {
                viewHolder.products_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.products_image);


                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent foodDetail = new Intent(ProductsList.this, ProductsDetail.class);
                        //Send FoodId to new Activity
                        foodDetail.putExtra("ProductsId", searchAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };
        //Set adapter for Recycler View is Search result
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        foodList.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Product item = postSnapshot.getValue(Product.class);
                            //Add name of food to suggest list
                            suggestList.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(Product.class,
                R.layout.products_item,
                ProductsViewHolder.class,
                //like: Select * from Foods where menuId =
                foodList.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(final ProductsViewHolder viewHolder, final Product model, final int position) {
                viewHolder.products_name.setText(model.getName());
                viewHolder.products_discount.setText(model.getDiscount());
                viewHolder.products_price.setText(String.format("Prețul de la %s \t", model.getPrice().toString() + " MDL"));
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.products_image);


                //Add Favorites
                if (localDB.isFavorite(adapter.getRef(position).getKey()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

                //Click to Share Facebook
                viewHolder.shareImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getApplicationContext())
                                .load(model.getImage())
                                .into(target);
                    }
                });


                //Click to change state of Favorites
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!localDB.isFavorite(adapter.getRef(position).getKey())) {
                            localDB.addToFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ProductsList.this, "" + model.getName() + " was added to Favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(ProductsList.this, "" + model.getName() + " was removed from Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent foodDetail = new Intent(ProductsList.this, ProductsDetail.class);
                        //Send FoodId to new Activity
                        foodDetail.putExtra("ProductsId", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };

        //Set Adapter
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
