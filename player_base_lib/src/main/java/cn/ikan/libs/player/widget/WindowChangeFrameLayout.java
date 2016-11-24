package cn.ikan.libs.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Taurus on 2016/10/24.
 */

public class WindowChangeFrameLayout extends FrameLayout {

    private String TAG = "_WindowChangeF";
    private OnWindowVisibilityChangedListener mOnWindowVisibilityChangedListener;

    public void setOnWindowVisibilityChangedListener(OnWindowVisibilityChangedListener mOnWindowVisibilityChangedListener) {
        this.mOnWindowVisibilityChangedListener = mOnWindowVisibilityChangedListener;
    }

    public WindowChangeFrameLayout(Context context) {
        super(context);
    }

    public WindowChangeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowChangeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == View.INVISIBLE || visibility == View.GONE){
            Log.d(TAG,"onWindowChanged_UNVISIBLE......");
            if(mOnWindowVisibilityChangedListener!=null){
                mOnWindowVisibilityChangedListener.onUnVisible();
            }
        }else if(visibility == View.VISIBLE){
            Log.d(TAG,"onWindowChanged_VISIBLE......");
            if(mOnWindowVisibilityChangedListener!=null){
                mOnWindowVisibilityChangedListener.onVisible();
            }
        }
    }

    public interface OnWindowVisibilityChangedListener{
        void onVisible();
        void onUnVisible();
    }
}
