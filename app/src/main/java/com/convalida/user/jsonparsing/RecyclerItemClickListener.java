package com.convalida.user.jsonparsing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Convalida on 10/4/2017.
 */

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener=listener;
        mGestureDetector=new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        }
        );
    }

    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }

    GestureDetector mGestureDetector;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView=rv.findChildViewUnder(e.getX(),e.getY());
        if(childView!=null && mListener!=null && mGestureDetector.onTouchEvent(e)){
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
