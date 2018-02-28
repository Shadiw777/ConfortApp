package com.confortapp.leon.confortapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.confortapp.leon.confortapp.Database.Database;
import com.confortapp.leon.confortapp.Interface.ItemClickListener;
import com.confortapp.leon.confortapp.Model.Product;
import com.confortapp.leon.confortapp.ViewHolder.ProductsViewHolder;
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

public class SearchActivity extends AppCompatActivity {

    //Search
    FirebaseRecyclerAdapter<Product, ProductsViewHolder> adapter;
    FirebaseRecyclerAdapter<Product, ProductsViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ////Init Facebook
//        callbackManager = CallbackManager.Factory.create();
//        shareDialog = new ShareDialog(this);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Products");

        //Local DB
        localDB = new Database(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


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

        //load all food
        loadAllProducts();
    }

    private void loadAllProducts() {
        adapter = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(Product.class,
                R.layout.products_item,
                ProductsViewHolder.class,
                //like: Select * from Foods where menuId =
                foodList
        ) {
            @Override
            protected void populateViewHolder(final ProductsViewHolder viewHolder, final Product model, final int position) {
                viewHolder.products_name.setText(model.getName());
                viewHolder.products_price.setText(String.format("Pretul de la %s", model.getPrice().toString() + " MDL"));
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
                            Toast.makeText(SearchActivity.this, "" + model.getName() + " was added to Favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeFromFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(SearchActivity.this, "" + model.getName() + " was removed from Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent foodDetail = new Intent(SearchActivity.this, ProductsDetail.class);
                        //Send FoodId to new Activity
                        foodDetail.putExtra("ProductsId", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };

        //Set Adapter
        recyclerView.setAdapter(adapter);
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
                        Intent foodDetail = new Intent(SearchActivity.this, ProductsDetail.class);
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
        foodList.addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    protected void onStart() {
        materialSearchBar.enableSearch();
        super.onStart();
    }
}
