package com.paperplane.Data;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

public class FriendListView extends ExpandableListView implements AbsListView.OnScrollListener, ExpandableListView.OnGroupClickListener {

    public FriendListView (Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        registerListener();
    }
    public FriendListView(Context context,AttributeSet attributeSet,int defStyle){
        super(context,attributeSet,defStyle);
        registerListener();
    }
    public FriendListView(Context context) {
        super(context);
        registerListener();
    }
/**
 * adapter接口
 */
    public interface HeaderAdapter{
        public static final int PINNED_HEADER_GONE =0;
        public static final int PINNED_HEADER_VISIBLE=1;
        public static final int PINNED_HEADER_PUSHED_UP=2;

    /**
     * Header状态
     * @param groupPosition
     * @return
     */
         int getHeaderState(int groupPosition,int childPosition);

    /**
     * 配置header，header知道显示内容
     * @param header
     * @param groupPosition
     * @param childPosition
     * @param alpha
     */
        void configureHeader(View header, int groupPosition, int childPosition, int alpha);

    /**
     * 获取点击按下的状态
     * @param groupPosition
     * @return
     */
    int getGroupClickStatus(int groupPosition);

    /**
     * 设置按下的状态
     * @param groupPosition
     * @param i
     */
    void setGroupClickStatus(int groupPosition, int i);
}
///////////////////////////////////////////////
    private  static final int MAX_ALPHA =255;
    private HeaderAdapter mAdapter;
    /**
     *列表头显示，true可见
     */
    private View mHeaderView;
    private boolean mHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    ///////////////////////////////////

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter=(HeaderAdapter)adapter;
    }

    public void setHeaderView(View view){
        mHeaderView=view;
        AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        if(mHeaderView!=null){
            setFadingEdgeLength(0);

        }
        requestLayout();
    }

    private void registerListener() {
        setOnScrollListener(this);
        setOnGroupClickListener(this);
    }

    /**
     * Headerview触发
     * @param
     */
    private void headerViewClick(){
        long packedPosition=getExpandableListPosition(this.getFirstVisiblePosition());
        int groupPosition= (int) getExpandableListPosition((int) packedPosition);

        if (mAdapter.getGroupClickStatus(groupPosition)==1)
        {
            this.collapseGroup(groupPosition);
            mAdapter.setGroupClickStatus(groupPosition,0);
        }
        else{
            this.expandGroup(groupPosition);
            mAdapter.setGroupClickStatus(groupPosition,1);
        }

        this.setSelectedGroup(groupPosition);

    }

    private float mDownX;
    private float mDownY;

    /**
     * 判断是否点击headerview，控制
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mHeaderViewVisible) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    float x = ev.getX();
                    float y = ev.getY();
                    float offsetX = Math.abs(x - mDownX);
                    float offsetY = Math.abs(y - mDownY);
                    // 如果 HeaderView 是可见的 , 点击在 HeaderView 内 , 那么触发 headerClick()
                    if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
                            && offsetX <= mHeaderViewWidth && offsetY <= mHeaderViewHeight) {
                        if (mHeaderView != null) {
                            headerViewClick();
                        }

                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final long flatPods =getExpandableListPosition(firstVisibleItem);//第一个item的position
        int groupPosition = ExpandableListView.getPackedPositionGroup(flatPods);
        int childPosition = ExpandableListView.getPackedPositionChild(flatPods);
        configureHeaderView(groupPosition,childPosition);
    }

    private void configureHeaderView(int groupPosition, int childPosition) {
        if (mHeaderView == null || mAdapter == null
                || ((ExpandableListAdapter) mAdapter).getGroupCount() == 0) {
            return;
        }

        int state = mAdapter.getHeaderState(groupPosition, childPosition);

        switch (state) {
            case HeaderAdapter.PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case HeaderAdapter.PINNED_HEADER_VISIBLE: {
                mAdapter.configureHeader(mHeaderView, groupPosition,childPosition, MAX_ALPHA);

                if (mHeaderView.getTop() != 0){
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }

                mHeaderViewVisible = true;

                break;
            }

            case HeaderAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();

                // intitemHeight = firstView.getHeight();
                int headerHeight = mHeaderView.getHeight();

                int y;

                int alpha;

                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }

                mAdapter.configureHeader(mHeaderView, groupPosition,childPosition, alpha);

                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }

                mHeaderViewVisible = true;
                break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView!=null){
            measureChild(mHeaderView,widthMeasureSpec,heightMeasureSpec);
            mHeaderViewWidth=mHeaderView.getMeasuredWidth();
            mHeaderViewHeight=mHeaderView.getMeasuredHeight();

        }
    }
    private int mOldState=-1;
    /**
     *点击group的事件
     * @param parent
     * @param v
     * @param groupPosition
     * @param id
     * @return
     */
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        if (mAdapter.getGroupClickStatus(groupPosition) == 0) {
            mAdapter.setGroupClickStatus(groupPosition, 1);
            parent.expandGroup(groupPosition);
            //Header自动置顶
            //parent.setSelectedGroup(groupPosition);

        } else if (mAdapter.getGroupClickStatus(groupPosition) == 1) {
            mAdapter.setGroupClickStatus(groupPosition, 0);
            parent.collapseGroup(groupPosition);
        }

        // 弹回第一行 ,
        return true;
    }

    /**
     * 列表更新调用
     * @param
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            //分组栏直接绘制到界面中，不加入到ViewGroup中
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
    protected void onLayout (boolean changed, int left,int top,int right,int bottom){
        super.onLayout(changed, left, top, right, bottom);
        final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
        final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
        final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
        int state = mAdapter.getHeaderState(groupPos, childPos);
        if (mHeaderView != null && mAdapter != null && state != mOldState) {
            mOldState = state;
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
        }

        configureHeaderView(groupPos, childPos);
    }
}
