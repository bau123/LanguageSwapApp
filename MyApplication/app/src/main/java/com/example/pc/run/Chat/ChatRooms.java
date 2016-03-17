package com.example.pc.run.Chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.run.Objects.ChatRoom;
import com.example.pc.run.Objects.Message;
import com.example.pc.run.R;

import java.util.ArrayList;

public class ChatRooms extends Fragment {

    private ArrayList<ChatRoom> chatRoomArrayList;
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public ChatRooms() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat_rooms, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_ChatRooms);

        chatRoomArrayList = new ArrayList<>();
        mAdapter = new ChatRoomsAdapter(this.getContext(), chatRoomArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                this.getContext().getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(this.getContext().getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                ChatRoom chatRoom = chatRoomArrayList.get(position);
                Intent intent = new Intent(getContext(), ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chatRoom.getId());
                intent.putExtra("name", chatRoom.getName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    //    fetchChatRooms(); FIXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        return v;
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


    /**
     * fetching the chat rooms by making http call
     */
    /*
    private void fetchChatRooms() {
        String getRoomsUrl = "";

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("user", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);

        Requests jsObjRequest = new Requests(Request.Method.POST, getRoomsUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());

                    JSONArray result = response.getJSONArray("result");
                    JSONObject current = result.getJSONObject(0);
                    Profile p = new Profile();
                    //Sets all the pull info into the temporally profile object
                    p.setEmail(email.getText().toString());
                    p.setProfilePicture(current.getString("photo"));
                    p.updateName(current.getString("name"));
                    p.updateInterests(current.getString("interests"));
                    p.updateLanguagesKnown(current.getString("languagesKnown"));
                    p.updateLanguagesLearning(current.getString("languagesLearning"));

                    //Stores Profile into the shared pref
                    ApplicationSingleton.getInstance().getPrefManager().storeProfile(p);

                } catch (JSONException e) {
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


        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.CHAT_ROOMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("chat_rooms");
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                            ChatRoom cr = new ChatRoom();
                            cr.setId(chatRoomsObj.getString("chat_room_id"));
                            cr.setName(chatRoomsObj.getString("name"));
                            cr.setLastMessage("");
                            cr.setUnreadCount(0);
                            cr.setTimeStamp(chatRoomsObj.getString("created_at"));

                            chatRoomArrayList.add(cr);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                mAdapter.notifyDataSetChanged();

                // subscribing to all chat room topics
                subscribeToAllTopics();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
*/
}
