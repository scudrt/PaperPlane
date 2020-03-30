package com.paperplane.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paperplane.Activity.ChatWindow;
import com.paperplane.Data.PrivateChat;
import com.paperplane.R;

import java.util.ArrayList;

public class ChatWindowAdapter extends RecyclerView.Adapter<ChatWindowAdapter.ViewHolder> {
    private ArrayList<PrivateChat> dataList;
    private Context context;

    public ChatWindowAdapter(Context context, ArrayList<PrivateChat> dataList){
        this.dataList = dataList;
        this.context = context;
    }

    public void setDataList(ArrayList<PrivateChat> dataList) {
        this.dataList = dataList;
    }

    /**
     * 加载chat_list_item布局，以之实例化ViewHolder并返回
     */
    @Override
    public ChatWindowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.chatView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(context, ChatWindow.class);
                intent.putExtra("privateChat", position);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    /**
     * 对RecyclerView的子项进行赋值，在每个子项被滚动到屏幕内时执行
     * @param viewHolder
     * @param i 数据项的index
     */
    @Override
    public void onBindViewHolder(ChatWindowAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setData(dataList.get(i));
    }

    @Override
    public int getItemCount(){
        return dataList != null ? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View chatView;

        ImageView icon;
        TextView name;
        TextView text;

        /**
         *
         * @param view RecyclerView 子项的最外层布局
         *             chat_list_item.xml
         */
        public ViewHolder(View view){
            super(view);
            chatView = view;
            icon = (ImageView)view.findViewById(R.id.chat_user_icon);
            name = (TextView)view.findViewById(R.id.chat_user_name);
            text = (TextView)view.findViewById(R.id.last_text);
        }

        public void setData(Object object){
            PrivateChat privateChat = (PrivateChat)object;
            //icon.setImageResource(privateChat.getTargetUser().getIcon());
            icon.setImageResource(R.drawable.blue);
            name.setText(privateChat.getTargetUser().getUserID());
            if(privateChat.getMessageSize()!=0)
                text.setText(privateChat.getMessages().get(privateChat.getMessageSize()-1).getContent());
            else
                text.setText("");
        }
    }
}
