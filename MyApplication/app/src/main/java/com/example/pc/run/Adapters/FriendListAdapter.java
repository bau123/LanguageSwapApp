package com.example.pc.run.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Chat.ChatRoomActivity;
import com.example.pc.run.CreateReview_act;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.Profile_act;
import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;
import com.example.pc.run.VideoChat.setUpCallActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FriendListAdapter extends BaseAdapter {
    private Activity context;

    private ArrayList<Profile> profiles = new ArrayList<>();

    public FriendListAdapter(Activity context, ArrayList<Profile> profiles) {
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_friendlist_row, null);
            viewHolder = new ViewHolder();

            viewHolder.profileImg = (ImageView) convertView.findViewById(R.id.frProfileImage);
            viewHolder.name = (TextView) convertView.findViewById(R.id.frNameText);
            viewHolder.chatButton = (Button) convertView.findViewById(R.id.frMessageButton);
            viewHolder.callButton = (Button)convertView.findViewById(R.id.frCallButton);
            viewHolder.reviewButton = (Button)convertView.findViewById(R.id.reviewButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(profiles.get(position).getName());
        if (profiles.get(position).getProfilePicture() != null) {
            viewHolder.profileImg.setImageBitmap(profiles.get(position).getProfilePicture());
        }

        viewHolder.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Profile_act.class);
                intent.putExtra("email", profiles.get(position).getEmail());
                context.startActivity(intent);
            }
        });

        viewHolder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getIdUrl = "http://t-simkus.com/run/processChatRoom.php";

                try {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("user1", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
                    parameters.put("user2", profiles.get(position).getEmail());

                    Requests jsObjRequest = new Requests(Request.Method.POST, getIdUrl, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                processResult(response, position);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        viewHolder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myEmail = ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0];
                Log.d("CHECKING EMAIL", myEmail);
                Intent intent = new Intent(context, setUpCallActivity.class);
                intent.putExtra("myEmail", myEmail);
                intent.putExtra("userEmail", profiles.get(position).getEmail());
                context.startActivity(intent);
            }
        });

        viewHolder.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myEmail = ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0];
                Intent intent = new Intent(context, CreateReview_act.class);
                intent.putExtra("myEmail", myEmail);
                intent.putExtra("userEmail", profiles.get(position).getEmail());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    //Start new chat activity
    private void processResult(JSONObject input, int position) throws InterruptedException {
        String result ="";
        try{
            result = input.getString("message");
            Intent intent = new Intent(this.context, ChatRoomActivity.class);
            intent.putExtra("email", profiles.get(position).getEmail());
            intent.putExtra("chat_room_id", result);
            this.context.startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class ViewHolder {
        public ImageView profileImg;
        public TextView name;
        public Button chatButton;
        public Button callButton;
        public Button reviewButton;
    }
}
