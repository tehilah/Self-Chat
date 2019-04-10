package com.example.selfchat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<String> messageList;

    public RecyclerViewAdapter(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    public ArrayList<String> getMessageList() {
        return messageList;
    }
    public void setMessageList(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    public void addMessage(String message){
        this.messageList.add(message);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messageitem, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.message.setText(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.chatResult);
        }
    }
}
