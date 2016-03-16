package com.example.pc.run.Chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.ChatRoom;
import com.example.pc.run.Objects.Message;
import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Messages extends Fragment {

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private ArrayList<ChatRoom> chatRoomArrayList;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;


    public Messages() {
        // Required empty public constructor
    }

    public static Messages newInstance(String param1, String param2) {
        Messages fragment = new Messages();
        Bundle args = new Bundle();
        //  args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatRoomArrayList = new ArrayList<>();





    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.messages_recycler_view);

        chatRoomArrayList = new ArrayList<>();

        mAdapter = new ChatRoomsAdapter(this.getContext(), chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getContext().getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(getContext().getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                ChatRoom chatRoom = chatRoomArrayList.get(position);

                    /*
                    intent.putExtra("chat_room_id", chatRoom.getId());
                    intent.putExtra("name", chatRoom.getName());
                    startActivity(intent);
                    */
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //Gets all the past chat history and displays them
        retrieveChats();

        return view;
    }

    /**
     * Updates the chat list unread count and the last message
     */
    private void updateRow(String chatRoomId, Message message) {
        for (ChatRoom cr : chatRoomArrayList) {
            if (cr.getId().equals(chatRoomId)) {
                int index = chatRoomArrayList.indexOf(cr);
                cr.setLastMessage(message.getMessage());
                cr.setUnreadCount(cr.getUnreadCount() + 1);
                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(index, cr);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    //Retrieve all chat rooms that the user has been in
    //Add each room to recycleView
    private void retrieveChats() {
        String url = "http://t-simkus.com/run/GetRooms.php";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);

        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Loop through all the chat rooms
                    JSONArray rooms = response.getJSONArray("result");
                    for (int i = 0; i < rooms.length(); i++) {
                        JSONObject current = rooms.getJSONObject(i);

                        String user1 = current.getString("user1");
                        String user2 = current.getString("user2");
                        String name = current.getString("name");
                        String messageEmail =  current.getString("email");
                        String message = current.getString("message");
                        String roomId = current.getString("chat_room_id");
                        String createdAt = current.getString("created_at");
                        String photo = current.getString("photo");

                        String otherUser = "";
                        if(user1.equals(ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0])){
                            otherUser = user2;
                        }else{
                            otherUser = user2;
                        }

                        //Create the new chat room object containing these details
                        ChatRoom room = new ChatRoom(roomId, name, messageEmail, message, createdAt, otherUser, 0, photo);

                        chatRoomArrayList.add(room);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                System.out.println("Error in processParameters");
                Log.d("Response: ", response.toString());
                //progress.dismiss();
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

}
