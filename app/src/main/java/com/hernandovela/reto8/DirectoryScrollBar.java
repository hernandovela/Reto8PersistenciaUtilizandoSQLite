package com.hernandovela.reto8;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ScrollView;

/** Persistent, draggable navigation synchronized with the actual content height. */
public final class DirectoryScrollBar extends View {
    private final ScrollView scroll;
    private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private float grabOffset;
    private boolean dragging;
    private float dp(float n){return n*getResources().getDisplayMetrics().density;}
    public DirectoryScrollBar(Context context,ScrollView scroll){
        super(context);this.scroll=scroll;setFocusable(true);setClickable(true);
        setContentDescription("Desplazar listado de empresas");
        scroll.setOnScrollChangeListener((v,x,y,oldX,oldY)->invalidate());
        scroll.addOnLayoutChangeListener((v,a,b,c,d,e,f,g,h)->invalidate());
        scroll.getChildAt(0).addOnLayoutChangeListener((v,a,b,c,d,e,f,g,h)->invalidate());
    }
    private int range(){return Math.max(0,scroll.getChildAt(0).getHeight()-scroll.getHeight()+scroll.getPaddingTop()+scroll.getPaddingBottom());}
    private float track(){return Math.max(0,getHeight()-dp(12));}
    private float thumb(){int range=range();return range==0?track():Math.min(track(),Math.max(dp(40),track()*scroll.getHeight()/(scroll.getHeight()+(float)range)));}
    private float top(){int range=range();return dp(6)+(range==0?0:(track()-thumb())*Math.max(0,Math.min(range,scroll.getScrollY()))/range);}
    @Override protected void onDraw(Canvas canvas){
        super.onDraw(canvas);float center=getWidth()/2f;
        paint.setColor(Color.rgb(219,228,233));canvas.drawRoundRect(center-dp(5),dp(6),center+dp(5),getHeight()-dp(6),dp(5),dp(5),paint);
        paint.setColor(range()==0?Color.rgb(169,184,193):Color.rgb(8,126,112));
        canvas.drawRoundRect(center-dp(7),top(),center+dp(7),top()+thumb(),dp(7),dp(7),paint);
    }
    private void move(float y){float travel=track()-thumb();if(travel<=0)return;float fraction=Math.max(0,Math.min(1,(y-dp(6)-grabOffset)/travel));scroll.scrollTo(0,Math.round(range()*fraction));invalidate();}
    @Override public boolean onTouchEvent(MotionEvent event){
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                if(range()==0)return true;
                scroll.fling(0);dragging=true;getParent().requestDisallowInterceptTouchEvent(true);
                grabOffset=event.getY()>=top()&&event.getY()<=top()+thumb()?event.getY()-top():thumb()/2;
                move(event.getY());return true;
            case MotionEvent.ACTION_MOVE:if(dragging)move(event.getY());return true;
            case MotionEvent.ACTION_UP:if(dragging)move(event.getY());dragging=false;getParent().requestDisallowInterceptTouchEvent(false);performClick();return true;
            case MotionEvent.ACTION_CANCEL:dragging=false;getParent().requestDisallowInterceptTouchEvent(false);return true;
            default:return super.onTouchEvent(event);
        }
    }
    @Override public boolean performClick(){super.performClick();return true;}
    private void step(int direction){scroll.fling(0);scroll.scrollTo(0,Math.max(0,Math.min(range(),scroll.getScrollY()+direction*Math.max(1,scroll.getHeight()*3/4))));}
    @Override public boolean onKeyDown(int key,KeyEvent event){if(key==KeyEvent.KEYCODE_DPAD_DOWN){step(1);return true;}if(key==KeyEvent.KEYCODE_DPAD_UP){step(-1);return true;}return super.onKeyDown(key,event);}
    @Override public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info){
        super.onInitializeAccessibilityNodeInfo(info);info.setClassName("android.widget.SeekBar");info.setScrollable(range()>0);
        info.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(AccessibilityNodeInfo.RangeInfo.RANGE_TYPE_INT,0,range(),Math.min(range(),scroll.getScrollY())));
        if(range()>0){info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);}
    }
    @Override public boolean performAccessibilityAction(int action,Bundle args){
        if(action==AccessibilityNodeInfo.ACTION_SCROLL_FORWARD){step(1);return true;}
        if(action==AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD){step(-1);return true;}
        if(action==AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS.getId()&&args!=null){scroll.fling(0);scroll.scrollTo(0,Math.max(0,Math.min(range(),Math.round(args.getFloat(AccessibilityNodeInfo.ACTION_ARGUMENT_PROGRESS_VALUE)))));return true;}
        return super.performAccessibilityAction(action,args);
    }
}
