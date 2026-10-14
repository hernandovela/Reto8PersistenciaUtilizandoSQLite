package com.hernandovela.reto8;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectoryScrollBarTest {
    private void touch(View view,int action,float y){long time=SystemClock.uptimeMillis();MotionEvent event=MotionEvent.obtain(time,time,action,view.getWidth()/2f,y,0);try{view.dispatchTouchEvent(event);}finally{event.recycle();}}
    @Test public void dragBothDirectionsAndResize(){
        Instrumentation i=InstrumentationRegistry.getInstrumentation();
        Activity activity=i.startActivitySync(new Intent(i.getTargetContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        ScrollView[] scroll=new ScrollView[1];View[] content=new View[1];DirectoryScrollBar[] bar=new DirectoryScrollBar[1];
        try {
            i.runOnMainSync(()->{
                LinearLayout layout=new LinearLayout(activity);scroll[0]=new ScrollView(activity);content[0]=new View(activity);
                content[0].setMinimumHeight(6000);content[0].setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1,6000));scroll[0].addView(content[0]);
                bar[0]=new DirectoryScrollBar(activity,scroll[0]);layout.addView(scroll[0],new LinearLayout.LayoutParams(0,-1,1));layout.addView(bar[0],new LinearLayout.LayoutParams(100,-1));activity.setContentView(layout);
            });
            i.waitForIdleSync();
            i.runOnMainSync(()->{
                assertTrue(bar[0].isShown());assertTrue(scroll[0].getHeight()>0);
                touch(bar[0],MotionEvent.ACTION_DOWN,0);touch(bar[0],MotionEvent.ACTION_MOVE,bar[0].getHeight());touch(bar[0],MotionEvent.ACTION_UP,bar[0].getHeight());
                assertEquals(content[0].getHeight()-scroll[0].getHeight(),scroll[0].getScrollY());
                touch(bar[0],MotionEvent.ACTION_DOWN,bar[0].getHeight()-1);touch(bar[0],MotionEvent.ACTION_MOVE,0);touch(bar[0],MotionEvent.ACTION_UP,0);
                assertEquals(0,scroll[0].getScrollY());
                scroll[0].scrollTo(0,3000);content[0].setMinimumHeight(40);content[0].setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1,40));
            });
            i.waitForIdleSync();
            i.runOnMainSync(()->{
                assertEquals(0,scroll[0].getScrollY());assertTrue(bar[0].isShown());
                touch(bar[0],MotionEvent.ACTION_DOWN,bar[0].getHeight());touch(bar[0],MotionEvent.ACTION_UP,bar[0].getHeight());assertEquals(0,scroll[0].getScrollY());
            });
        }finally{i.runOnMainSync(activity::finish);}
    }
}
