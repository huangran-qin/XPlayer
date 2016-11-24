package cn.ikan.libs.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.ikan.libs.player.inter.ISinglePlayer;
import cn.ikan.libs.player.inter.OnErrorListener;
import cn.ikan.libs.player.inter.OnPlayerEventListener;
import cn.ikan.libs.player.setting.DecodeMode;
import cn.ikan.libs.player.setting.ViewType;

/**
 * Created by Taurus on 2016/11/14.
 */

public abstract class BaseSinglePlayer extends FrameLayout implements ISinglePlayer{

    protected int mStatus = STATUS_IDLE;
    private OnErrorListener mOnErrorListener;
    private OnPlayerEventListener mOnPlayerEventListener;
    protected int startSeekPos;
    private DecodeMode mDecodeMode;
    private ViewType mViewType;

    public BaseSinglePlayer(Context context) {
        super(context);
        init(context);
    }

    public BaseSinglePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseSinglePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void onStartSeek() {
        if(startSeekPos > 0){
            seekTo(startSeekPos);
            startSeekPos = -1;
        }
    }

    protected void init(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initPlayerView(context);
    }

    private void initPlayerView(Context context) {
        addView(getPlayerView(context),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public abstract View getPlayerView(Context context);

    public void setDecodeMode(DecodeMode mDecodeMode){
        this.mDecodeMode = mDecodeMode;
    }

    public void setViewType(ViewType mViewType) {
        this.mViewType = mViewType;
    }

    public DecodeMode getDecodeMode() {
        return mDecodeMode;
    }

    public ViewType getViewType() {
        return mViewType;
    }

    public void onClickResume() {

    }

    public void setOnErrorListener(OnErrorListener onErrorListener){
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    protected void onErrorEvent(int eventCode){
        if(mOnErrorListener!=null){
            mOnErrorListener.onError(eventCode);
        }
    }

    protected void onPlayerEvent(int eventCode){
        if(mOnPlayerEventListener!=null){
            mOnPlayerEventListener.onPlayerEvent(eventCode);
        }
    }

    public int getStatus() {
        return mStatus;
    }

    @Override
    public void destroy() {
        mOnErrorListener = null;
        mOnPlayerEventListener = null;
    }
}
