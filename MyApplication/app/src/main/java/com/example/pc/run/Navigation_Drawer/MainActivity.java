package com.example.pc.run.Navigation_Drawer;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pc.run.ActionBar.AboutUs_frag;
import com.example.pc.run.ActionBar.CodeOfConduct_frag;
import com.example.pc.run.Chat.ChatRoomActivity;
import com.example.pc.run.Chat.messages;
import com.example.pc.run.Friends.Friends;
import com.example.pc.run.Gcm.Config;
import com.example.pc.run.Gcm.NotificationUtils;
import com.example.pc.run.Gcm.RegistrationIntentService;
import com.example.pc.run.Navigation_Drawer.FragmentDrawer;
import com.example.pc.run.Profile.MyProfile;
import com.example.pc.run.R;
import com.example.pc.run.Registration.Login_act;
import com.example.pc.run.Search.App_act;
import com.example.pc.run.SharedPref.ApplicationSingleton;
import com.example.pc.run.Video.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * The main Activity which contains the navigation drawer used to navigate throughout the app
 */
public class MainActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = "In MainAct";
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private BroadcastReceiver regReceiver;
    private static Context mContext;
    public Toast toast;public int queryResult;
    private boolean inSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mContext = this;
        inSearch = true;

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        //Display search frag as default
        displayView(0);

        //Setting up broadcast receiver
        regReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");
                  //  Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    //   Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    processPushNotification(intent);

                }
            }
        };

        //Checks if play service is available
        if (checkPlayService()) {
            //Register gcm
            Intent intent = new Intent(this, RegistrationIntentService.class);
            intent.putExtra("key", "register");
            startService(intent);
        }
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Method which processes the input from the SearchView in the actionbar
     * Sends a broadcast to the search activity if it is currently opened
     * else opens it and sends the search input through a intent
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (inSearch) {
                    //Register gcm
                    Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
                    intent.putExtra("key", "search");
                    intent.putExtra("data", query);
                    startService(intent);
                    return false;
                }
                //If not then App act is loaded with query
                else {
                    App_act frag = new App_act();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, frag);
                    frag.externalQuery(query);
                    fragmentTransaction.commit();
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Used to navigate through the options items
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_Code) {
            CodeOfConduct_frag frag = new CodeOfConduct_frag();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, frag);
            fragmentTransaction.commit();
            return true;
        }

        if (id == R.id.action_About) {
            AboutUs_frag frag = new AboutUs_frag();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, frag);
            fragmentTransaction.commit();
            return true;
        }
        /*
        Clears the page, ends the current signed in user for the video and takes the user
        back to the login page
         */
        if (id == R.id.action_signOut) {
            ApplicationSingleton.getInstance().getPrefManager().clear();
            getSinchServiceInterface().stopClient();
            Intent intent = new Intent(this, Login_act.class);
            startActivity(intent);
        }

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    /**
     * Used to navigate through the navigation drawer items
     * @param position
     */
    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new App_act();
                inSearch = true;
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new Friends();
                inSearch = false;
                title = getString(R.string.title_friends);
                break;
            case 2:
                fragment = new messages();
                inSearch = false;
                title = getString(R.string.title_messages);
                break;
            case 3:
                fragment = new MyProfile();
                inSearch = false;
                title = getString(R.string.title_profile);
                break;

            default:
                title = getString(R.string.title_home);
                inSearch = true;
                fragment = new App_act();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Processes push notifications
     * @param intent
     */
    public void processPushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);
        // If push is friend request
        if (type == Config.PUSH_TYPE_FRIEND) {
            Toast.makeText(getApplicationContext(), "Someone has sent you a friend request", Toast.LENGTH_LONG).show();  ///  !!!!!!!!!!!!!!!!!!!
        } else if (type == Config.PUSH_TYPE_USER) {

        }
    }

    /**
     * Checks if the google play store services is supported on the app
     * @return
     */
    public boolean checkPlayService() {
        queryResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (queryResult == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(queryResult)) {
            String errorString = GoogleApiAvailability.getInstance().getErrorString(queryResult);
            Log.d(TAG, "Problem with google play service : " + queryResult + " " + errorString);
            toast = Toast.makeText(getApplicationContext(), "Device is not supported. Please install google play service.", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(regReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(regReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // Clear notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(regReceiver);
        super.onPause();
    }

    /**
     * Open the chat room activity
     * @param userEmail email of the friend in the chat room
     * @param roomId room id of the discussion between the friend and current user
     */
    public static void openChat(String userEmail, String roomId) {
        Intent intent = new Intent(mContext, ChatRoomActivity.class);
        intent.putExtra("email", userEmail);
        intent.putExtra("chat_room_id", roomId);
        mContext.startActivity(intent);
    }


}
