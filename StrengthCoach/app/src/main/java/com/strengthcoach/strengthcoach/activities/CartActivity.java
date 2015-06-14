package com.strengthcoach.strengthcoach.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.strengthcoach.strengthcoach.R;
import com.strengthcoach.strengthcoach.adapters.CartItemsAdapter;
import com.strengthcoach.strengthcoach.helpers.Constants;
import com.strengthcoach.strengthcoach.models.BlockedSlots;
import com.strengthcoach.strengthcoach.models.SimpleUser;
import com.strengthcoach.strengthcoach.models.Trainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CartActivity extends ActionBarActivity {
    public static ArrayList<BlockedSlots> alSlots;
    public static CartItemsAdapter adSlots;
    public static ListView lvCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        alSlots = new ArrayList<>();
        lvCartItems = (ListView) findViewById (R.id.lvCartItems);
        // adding header to the list view starts
        View header = LayoutInflater.from(CartActivity.this).inflate( R.layout.cart_item_header, null);
        lvCartItems.addHeaderView(header);
        adSlots = new CartItemsAdapter(CartActivity.this, alSlots);
        // adding header to the list view ends
        lvCartItems.setAdapter(adSlots);
        populateCart();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateCart(){
        ParseObject trainer = ParseObject.createWithoutData("Trainer", Trainer.currentTrainerObjectId);
        ParseObject user = ParseObject.createWithoutData("SimpleUser", SimpleUser.currentUserObjectId);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BlockedSlots");
        query.selectKeys(Arrays.asList("slot_date","slot_time"));
        query.include("trainer_id");
        query.whereEqualTo("trainer_id", trainer);
        query.whereEqualTo("user_id", user);
        query.whereEqualTo("status", Constants.ADD_TO_CART);
        adSlots.clear();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trainerSlots, ParseException e) {
                if(e==null) {

                    for (ParseObject slots : trainerSlots) {
                        //  b.trainerName= slots.getString("name");
                        BlockedSlots b = new BlockedSlots();
                        b.setSlotDate(slots.getString("slot_date"));
                        b.setSlotTime(slots.getString("slot_time"));
                        alSlots.add(b);
                    }

                    adSlots.notifyDataSetChanged();


                } else {
                    Log.v("DEBUG!!!!!!!!!!!!!", "Error occured");
                }
            }
        });

    }
}
