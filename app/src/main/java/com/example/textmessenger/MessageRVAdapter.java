package com.example.textmessenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageRVAdapter extends RecyclerView.Adapter<MessageRVAdapter.MessageListViewHolder> {
    Context context;
    List<MessagesModel> messagesList;

    public MessageRVAdapter(Context context, List<MessagesModel> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_messages, parent, false);
        MessageListViewHolder viewHolder = new MessageListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {
        holder.sender.setText(messagesList.get(position).getSender());
        holder.messageInfo.setText(messagesList.get(position).getInformation());
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    static class MessageListViewHolder extends RecyclerView.ViewHolder {
        TextView sender, messageInfo;

        public MessageListViewHolder(@NonNull View itemView) {
            super(itemView);

            sender = itemView.findViewById(R.id.sender_tv);
            messageInfo = itemView.findViewById(R.id.information_tv);
        }
    }
}
