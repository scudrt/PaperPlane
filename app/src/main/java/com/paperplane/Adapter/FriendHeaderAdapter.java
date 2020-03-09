package com.paperplane.Adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paperplane.Data.FriendListView;
import com.paperplane.R;

public class FriendHeaderAdapter extends BaseExpandableListAdapter implements FriendListView.HeaderAdapter {
    private String[][] childrenData;
    private String[] groupData;
    private Context context;
    private FriendListView listView;
    private LayoutInflater inflater;

    public FriendHeaderAdapter(String[][] childrenData,String[] groupData
            ,Context context,FriendListView listView){
        this.groupData = groupData;
        this.childrenData = childrenData;
        this.context = context;
        this.listView = listView;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getGroupCount() {
        return groupData.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childrenData[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenData[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createGroupView();
        }

        ImageView iv = (ImageView)view.findViewById(R.id.groupIcon);

        if (isExpanded) {
            iv.setImageResource(R.drawable.bt_b2);
        }
        else{
            iv.setImageResource(R.drawable.bt_b1);
        }

        TextView text = (TextView)view.findViewById(R.id.groupto);
        text.setText(groupData[groupPosition]);
        return view;
    }

    private View createGroupView() {
        return inflater.inflate(R.layout.frined_list_group,null);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createChildrenView();
        }
        TextView text = (TextView)view.findViewById(R.id.childto);
        text.setText(childrenData[groupPosition][childPosition]);
        return view;
    }

    private View createChildrenView() {
        return inflater.inflate(R.layout.friend_list_child, null);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void configureHeader(View header, int groupPosition, int childPosition, int alpha) {
        String groupData = this.groupData[groupPosition];
        ((TextView) header.findViewById(R.id.groupto)).setText(groupData);
    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.keyAt(groupPosition)>=0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }

    @Override
    public void setGroupClickStatus(int groupPosition, int i) {
        groupStatusMap.put(groupPosition, i);
    }
}
