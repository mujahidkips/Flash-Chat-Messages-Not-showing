package com.mujahid.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Mujahid on 02-Jul-17.
 */

public class ChatListAdapter extends BaseAdapter {
    private Activity activity;
    private DatabaseReference databaseReference;
    private String displayName;
    private ArrayList<DataSnapshot> dataSnapshots;
    private ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            dataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String displayName) {
        this.activity = activity;
        this.databaseReference = databaseReference.child("messages");
        databaseReference.addChildEventListener(childEventListener);
        this.displayName = displayName;
        dataSnapshots = new ArrayList<>();
    }


    static class ViewHolder {
        TextView AuthorName;
        TextView Body;
        LinearLayout.LayoutParams layoutParams;

    }

    @Override
    public int getCount() {
        return dataSnapshots.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        return dataSnapshots.get(position).getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chat_msg_row, parent, false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.AuthorName = (TextView) convertView.findViewById(R.id.author);
            viewHolder.Body = (TextView) convertView.findViewById(R.id.message);
            viewHolder.layoutParams = (LinearLayout.LayoutParams) viewHolder.AuthorName.getLayoutParams();
            convertView.setTag(viewHolder);
        }

        final InstantMessage message = getItem(position);
        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        String author = message.getAuthor();
        viewHolder.AuthorName.setText(author);
        String mesg = message.getMessage();
        viewHolder.Body.setText(mesg);

        return convertView;
    }

    public void cleanup(){


        databaseReference.removeEventListener(childEventListener);
    }
}
