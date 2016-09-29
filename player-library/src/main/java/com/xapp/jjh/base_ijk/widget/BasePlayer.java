package com.xapp.jjh.base_ijk.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.xapp.jjh.base_ijk.config.DecodeMode;
import com.xapp.jjh.base_ijk.config.ViewType;
import com.xapp.jjh.base_ijk.inter.OnErrorListener;
import com.xapp.jjh.base_ijk.inter.OnPlayerEventListener;

/**
 * Created by Taurus on 2016/8/30.
 */
public abstract class BasePlayer extends BaseBindControllerPlayer {

    private DecodeMode mDecodeMode = DecodeMode.SOFT;
    private ViewType mViewType = ViewType.SURFACEVIEW;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorListener mOnErrorListener;

    private boolean gestureDoubleTapEnable = true;

    protected int startSeekPos = -1;

    public BasePlayer(Context context) {
        super(context);
    }

    public BasePlayer(Context context, int width, int height) {
        super(context, width, height);
    }

    public BasePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DecodeMode getDecodeMode() {
        return mDecodeMode;
    }

    public void setDecodeMode(DecodeMode mDecodeMode) {
        this.mDecodeMode = mDecodeMode;
    }

    public ViewType getViewType() {
        return mViewType;
    }

    public void setViewType(ViewType mViewType) {
        this.mViewType = mViewType;
    }

    public void setGestureDoubleTapEnable(boolean gestureDoubleTapEnable) {
        this.gestureDoubleTapEnable = gestureDoubleTapEnable;
    }

    public boolean isGestureDoubleTapEnable() {
        return gestureDoubleTapEnable;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    protected void onPlayerEvent(int eventCode){
        updateController(eventCode);
        if(mOnPlayerEventListener!=null){
            mOnPlayerEventListener.onPlayerEvent(eventCode);
        }
    }

    public boolean isBufferAvailable(){
        return (getBufferPercentage()*getDuration()/100) >= getCurrentPosition();
    }

    private void updateController(int eventCode) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START:
                setLoadingState(true);
                break;

            case OnPlayerEventListener.EVENT_CODE_PREPARED:

                break;

            case OnPlayerEventListener.EVENT_CODE_VIDEO_INFO_READY:

                break;

            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                sendPlayingMsg();
                setLoadingState(false);
                setPlayState(true);
                onStartSeek();
                break;

            case OnPlayerEventListener.EVENT_CODE_BUFFERING_START:
                setLoadingState(true);
                break;

            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                sendPlayingMsg();
                setLoadingState(false);
                setPlayState(true);
                break;

            case OnPlayerEventListener.EVENT_CODE_SEEK_COMPLETE:
                setLoadingState(false);
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE:
                setLoadingState(false);
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE:
                setPlayState(false);
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAY_RESUME:
                setPlayState(true);
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAYER_CHANGE_DEFINITION:
                setLoadingState(true);
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO:
                removePlayingMsg();
                break;
        }
    }

    protected void onStartSeek() {
        if(startSeekPos > 0){
            seekTo(startSeekPos);
            startSeekPos = -1;
        }
    }

    protected void onErrorEvent(int errorCode){
        handleErrorEvent(errorCode);
        if(mOnErrorListener!=null){
            mOnErrorListener.onError(errorCode);
        }
    }

    private void handleErrorEvent(int errorCode) {
        switch (errorCode){
            case OnErrorListener.ERROR_CODE_COMMON:
                setPlayState(false);
                setLoadingState(false);
                break;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_DESTROY);
    }
}
