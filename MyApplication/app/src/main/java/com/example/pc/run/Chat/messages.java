package com.example.pc.run.Chat;

import android.content.BroadcastReceiver;
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

public class messages extends Fragment {

    private ArrayList<ChatRoom> chatRoomArrayList;
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public messages() {
        // Required empty public constructor
    }

    public static messages newInstance(String param1, String param2) {
        messages fragment = new messages();
        Bundle args = new Bundle();
        //  args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatRoomArrayList = new ArrayList<>();

        mAdapter = new ChatRoomsAdapter(this.getContext(), chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.messages_recycler_view);
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
        String url = "http://t-simkus.com/run/getChatRooms.php";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);

        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Loop through all the chat rooms
                    JSONArray rooms = response.getJSONArray("result");
                    for (int i = 0; i < rooms.length(); i++) {
                        JSONObject current = rooms.getJSONObject(i);

                        String email = current.getString("email");
                        String name = current.getString("name");
                        String message = current.getString("message");
                        String roomId = current.getString("chat_room_id");
                        String createdAt = current.getString("created_at");

                        //Create the new chat room object containing these details
                        ChatRoom room = new ChatRoom(roomId, name, email, message, createdAt, 0);

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
