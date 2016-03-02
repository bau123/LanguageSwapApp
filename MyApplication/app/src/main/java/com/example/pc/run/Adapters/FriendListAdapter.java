package com.example.pc.run.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.run.Chat.ChatRoomThreadAdapter;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.R;

import java.util.ArrayList;

/**
 * Created by Joss on 29/02/2016.
 */
public class FriendListAdapter extends BaseAdapter {
    private Activity context;

    private ArrayList<Profile> profiles = new ArrayList<>();

    public FriendListAdapter(Activity context, ArrayList<Profile> profiles){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_friendlist_row, null);
            viewHolder = new ViewHolder();

            viewHolder.profileImg = (ImageView)convertView.findViewById(R.id.frProfileImage);
            viewHolder.name = (TextView)convertView.findViewById(R.id.frNameText);
            viewHolder.chatButton = (Button)convertView.findViewById(R.id.frMessageButton);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.name.setText(profiles.get(position).getName());
        if(profiles.get(position).getProfilePicture() != null){
            viewHolder.profileImg.setImageBitmap(profiles.get(position).getProfilePicture());
        }

        viewHolder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: YOUR CODE GOES HERE
            }
        });
        return convertView;
    }

    public class ViewHolder{
        public ImageView profileImg;
        public TextView name;
        public Button chatButton;
        public Button callButton;
    }
}
