package com.example.pc.run.Listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Joss on 12/02/2016.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    /*
    Constructor to set Context
     */
    public OnSwipeTouchListener(Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    /*
    Methods to be overriden with gesture motion listeners
     */

    public void onSwipeLeft(){

    }
    public void onSwipeRight(){

    }
    public void onSwipeBottom(){

    }
    public void onSwipeTop(){

    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        /*
        Sets swipe threshold and swipe velocity threshold
         */
        private static final int swipeThreshold = 100;
        private static final int swipeVelocity = 100;

        @Override
        public boolean onDown(MotionEvent event){
            return true;
        }

        /*
        Measures motion and detects direction
         */
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY){
            boolean result = false;

            try {
                /*
                measures motion on x-y axis from onclick to offclick gesture
                 */
                float diffX = event2.getX() - event1.getX();
                float diffY = event2.getY() - event1.getY();

                /*
                differentiates left-right movement over top-bottom movement
                 */
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    /*
                    Detects if swipe crosses velocity/distance threshold to ensure swipe action
                     */
                    if (Math.abs(diffX) > swipeThreshold && Math.abs(velocityX) > swipeVelocity) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }else if (Math.abs(diffY) > swipeThreshold && Math.abs(velocityY) > swipeVelocity) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;
            }catch(Exception e){
                e.printStackTrace();
            }
            return result;
        }

    }
}
