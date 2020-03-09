package com.paperplane.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paperplane.Data.ChatWindowMessage;
import com.paperplane.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<ChatWindowMessage> dataList;

    public ChatAdapter(ArrayList<ChatWindowMessage> dataList){
        this.dataList = dataList;
    }

    public void setDataList(ArrayList<ChatWindowMessage> dataList) {
        this.dataList = dataList;
    }

    public void replaceAll(ArrayList<ChatWindowMessage> list){
        dataList.clear();
        if(list != null && list.size() > 0){
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ChatWindowMessage> list){
        if(dataList != null && list != null){
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch(viewType){
            case ChatWindowMessage.SEND:
                return new RightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false));
            case ChatWindowMessage.RECEIVE:
                return new LeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position){
        holder.setData(dataList.get(position));
    }

    @Override
    public int getItemViewType(int position){
        return dataList.get(position).getType();
    }

    @Override
    public int getItemCount(){
        return dataList != null ? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view){
            super(view);
        }

        void setData(Object object){

        }
    }

    private class LeftViewHolder extends ViewHolder{
        private ImageView user_icon;
        private TextView text;

        public LeftViewHolder(View view){
            super(view);
            user_icon = (ImageView)view.findViewById(R.id.user_icon);
            text = (TextView)view.findViewById(R.id.text);
        }

        @Override
        void setData(Object object){
            super.setData(object);
            ChatWindowMessage message = (ChatWindowMessage)object;
            //user_icon.setImageResource(message.getIcon());
            user_icon.setImageResource(R.drawable.blue);
            text.setText(message.getContent());
        }
    }

    private class RightViewHolder extends ViewHolder {
        private ImageView user_icon;
        private TextView text;

        public RightViewHolder(View view) {
            super(view);
            user_icon = (ImageView) view.findViewById(R.id.user_icon);
            text = (TextView) view.findViewById(R.id.text);
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatWindowMessage message = (ChatWindowMessage)object;
            //user_icon.setImageResource(message.getIcon());
            user_icon.setImageResource(R.drawable.blue);
            text.setText(message.getContent());
        }
    }

}
