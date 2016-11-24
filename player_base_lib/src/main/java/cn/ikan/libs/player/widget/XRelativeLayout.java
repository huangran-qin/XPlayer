package cn.ikan.libs.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by Taurus on 2016/10/11.
 */

public class XRelativeLayout extends RelativeLayout {

    private String TAG = "_XRelativeLayout";
    private OnLayoutSizeChangeListener mOnLayoutSizeChangeListener;

    public void setOnLayoutSizeChangeListener(OnLayoutSizeChangeListener mOnLayoutSizeChangeListener) {
        this.mOnLayoutSizeChangeListener = mOnLayoutSizeChangeListener;
    }

    public XRelativeLayout(Context context) {
        super(context);
    }

    public XRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG,"onSizeChanged.........w = " + w + " h = " + h);
        if(mOnLayoutSizeChangeListener!=null){
            mOnLayoutSizeChangeListener.onSizeChange(w,h);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.d(TAG,"onLayout.........r = " + r + " b = " + b);
    }

    public interface OnLayoutSizeChangeListener{
        void onSizeChange(int width, int height);
    }
}
