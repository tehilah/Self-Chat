package com.example.selfchat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<Message> messageList;

    public RecyclerViewAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    //todo: remove notify...
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }
//todo: remove notify...
    public void addMessage(String message){
        this.messageList.add(new Message(message));
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
        Message currMessage = messageList.get(position);
        holder.message.setText(currMessage.getMessage());
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
