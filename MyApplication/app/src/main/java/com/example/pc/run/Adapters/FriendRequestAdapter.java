package com.example.pc.run.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.run.Objects.Profile;
import com.example.pc.run.R;
import com.example.pc.run.RequestModification;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import java.util.ArrayList;


public class FriendRequestAdapter extends BaseAdapter{
    private Activity context;

    private ArrayList<Profile> profiles = new ArrayList<>();

    public FriendRequestAdapter(Activity context, ArrayList<Profile> profiles){
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

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_friend_request_row, null);
            viewHolder = new ViewHolder();

            viewHolder.profileImg = (ImageView)convertView.findViewById(R.id.fqProfileImage);
            viewHolder.name = (TextView)convertView.findViewById(R.id.fqNameView);
            viewHolder.acceptButton = (Button)convertView.findViewById(R.id.acceptButton);
            viewHolder.rejectButton = (Button)convertView.findViewById(R.id.rejectButton);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(profiles.get(position).getName());
        if(profiles.get(position).getProfilePicture() != null){
            viewHolder.profileImg.setImageBitmap(profiles.get(position).getProfilePicture());
        }

        viewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Friend Request Accepted!", Toast.LENGTH_LONG).show();
                RequestModification requestMod = new RequestModification(profiles.get(position).getEmail(),
                        ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0], "true");
                context.finish();
                context.startActivity(context.getIntent());
            }
        });

        viewHolder.rejectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Friend Request Rejected!", Toast.LENGTH_LONG).show();
                RequestModification requestMod = new RequestModification(profiles.get(position).getEmail(),
                        ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0], "false");
                context.finish();
                context.startActivity(context.getIntent());
            }
        });

        return convertView;
    }


    public class ViewHolder{
        public ImageView profileImg;
        public TextView name;
        public Button acceptButton;
        public Button rejectButton;
    }

}

