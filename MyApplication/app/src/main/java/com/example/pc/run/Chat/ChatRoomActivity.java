package com.example.pc.run.Chat;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pc.run.Gcm.Config;
import com.example.pc.run.Gcm.NotificationUtils;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Message;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {
    private String TAG = ChatRoomActivity.class.getSimpleName();

    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private Button btnSend;
    private String emailOfOther;
    private String gcmOfOther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputMessage = (EditText) findViewById(R.id.message);
        btnSend = (Button) findViewById(R.id.btn_send);

        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chat_room_id");
        emailOfOther = intent.getStringExtra("email");
        String title = "Chat";

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        messageArrayList = new ArrayList<>();

        // self user id is to identify the message owner
        String selfUserId = ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0];

        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList, selfUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //Gets the gcm registration of other user
        getOtherGcm();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    System.out.println("chat room: got new message");
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        fetchChatThread(chatRoomId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // Clear notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        System.out.println("created user message and chat id");

        if (message != null ) {
            System.out.println("Both are not null");
            messageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }
    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */
    private void sendMessage() {
        final String message = this.inputMessage.getText().toString().trim();
        //Clears the textView containing the message
        this.inputMessage.setText("");

        System.out.println("message from input " + message);

        //Checks if user has entered anything in the chat
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        //Creating params needed to send to database and other user
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
        params.put("user_name", ApplicationSingleton.getInstance().getPrefManager().getProfile().getName());
        params.put("chat_room_id", chatRoomId);
        params.put("message", message);
        params.put("gcmTo", gcmOfOther);

        //TESTING !!!!!!!!!!!! REMOVE!!!!!!!!!!!!!!
        for (String key : params.keySet()) {
            System.out.println(key + " " + params.get(key));
        }
        //TESTING !!!!!!!!!!!! REMOVE!!!!!!!!!!!!!!
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue();
            System.out.println("key, " + key + " value " + value);
        }
        //TESTING !!!!!!!!!!!! REMOVE!!!!!!!!!!!!!!
        System.out.println("made params for message, sending to method now");
        //Send message to database and then notify the user
        sendToDataBase(params);
    }

    //Gets the GCM registration of the other user
    public void getOtherGcm() {
        String getGcm = "http://t-simkus.com/run/getGcm.php";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", emailOfOther);
        Requests jsObjRequest = new Requests(Request.Method.POST, getGcm, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    try {
                        String result = response.getString("message");
                        if (result != "failure") {
                            gcmOfOther = result;
                        } else {
                            System.out.println("Could not get the email of other user in chat");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    public void sendToDataBase(Map<String, String> params) {
        String sendGcm = "http://t-simkus.com/run/processMessage.php";

        Requests jsObjRequest = new Requests(Request.Method.POST, sendGcm, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString() + "this is the message");
                    if (response.getBoolean("error") == false) {

                        Message message = new Message();
                        message.setEmail(response.getString("email"));
                        message.setMessageId(response.getString("message_id"));
                        message.setMessage(response.getString("message"));
                        message.setDateCreated(response.getString("created_at"));
                        message.setName(ApplicationSingleton.getInstance().getPrefManager().getProfile().getName());

                        //Add message to chat
                        messageArrayList.add(message);
                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            // scrolling to bottom of the recycler view
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }
                    } else {
                        // !!!! put error Message!!!
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    /**
     * Fetching all the messages of a single chat room
     */
    private void fetchChatThread(String id) {
        String fetchUrl = "http://t-simkus.com/run/fetchMessages.php";

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("roomId", id);

        System.out.println("FETCHING MESSAGES!!!");

        Requests jsObjRequest = new Requests(Request.Method.POST, fetchUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray messages = response.getJSONArray("result");
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject current = messages.getJSONObject(i);

                        String commentId = current.getString("message_id");
                        String commentText = current.getString("message");
                        String createdAt = current.getString("created_at");
                        String name = current.getString("name");
                        String email = current.getString("email");

                        if (current.getBoolean("passed")) {
                            System.out.println("MESSAGE PASSED");
                            Message message = new Message(email, commentText, commentId, createdAt, name);
                            message.setEmail(email);
                            message.setMessageId(commentId);
                            message.setMessage(commentText);
                            message.setDateCreated(createdAt);
                            message.setName(name);

                            messageArrayList.add(message);
                        }

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
                //progress.dismiss();
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

}