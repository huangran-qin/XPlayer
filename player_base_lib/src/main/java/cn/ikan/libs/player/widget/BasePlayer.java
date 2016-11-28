package cn.ikan.libs.player.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.AttributeSet;
import android.util.Log;

import cn.ikan.libs.player.inter.OnErrorListener;
import cn.ikan.libs.player.inter.OnPlayerEventListener;
import cn.ikan.libs.player.setting.DecodeMode;
import cn.ikan.libs.player.setting.VideoRate;
import cn.ikan.libs.player.setting.ViewType;
import cn.ikan.libs.player.utils.CommonUtils;
import cn.ikan.libs.player.utils.PlayerCollections;

/**
 * Created by Taurus on 2016/8/30.
 */
public abstract class BasePlayer extends BaseBindControllerPlayer {

    private String TAG = "_BasePlayer";
    private DecodeMode mDecodeMode = DecodeMode.SOFT;
    private ViewType mViewType = ViewType.SURFACEVIEW;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorListener mOnErrorListener;

    protected VideoRate mCurrRate;

    private boolean gestureDoubleTapEnable = true;

    protected int startSeekPos = -1;

    protected int startPos;

    private NetChangeReceiver mNetChangeReceiver;

    protected boolean mNetError;

    /** not include net error*/
    protected boolean isOccurError;

    public BasePlayer(Context context) {
        super(context);
    }

    public BasePlayer(Context context, int width, int height) {
        super(context, width, height);
    }

    public BasePlayer(Context context, int type, int width, int height) {
        super(context, type, width, height);
    }

    public BasePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initController(Context context) {
        super.initController(context);
        PlayerCollections.putPlayer(this);
    }

    @Override
    protected void initSystemInfo() {
        super.initSystemInfo();
        registerNetChangeReceiver();
    }

    private void registerNetChangeReceiver() {
        if(mContext!=null){
            mNetChangeReceiver = new NetChangeReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mNetChangeReceiver,intentFilter);
        }
    }

    private void unRegisterNetChangeReceiver(){
        try {
            if(mContext!=null && mNetChangeReceiver!=null){
                mContext.unregisterReceiver(mNetChangeReceiver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnNetWorkStateChangeListener(OnNetWorkStateChangeListener mOnNetWorkStateChangeListener) {
        this.mOnNetWorkStateChangeListener = mOnNetWorkStateChangeListener;
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
        return (getBufferPercentage()*getDuration()/100) > getCurrentPosition();
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
                mStatus = STATUS_PLAYING;
                isOccurError = false;
                sendPlayingMsg();
                setLoadingState(false);
                setPlayState(true);

                break;

            case OnPlayerEventListener.EVENT_CODE_BUFFERING_START:
                sendDelayLoadingMsg();
                break;

            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                sendPlayingMsg();
                removeDelayLoadingMsg();
                setLoadingState(false);
                if(mStatus == STATUS_PLAYING){
                    setPlayState(true);
                }
                break;

            case OnPlayerEventListener.EVENT_CODE_SEEK_COMPLETE:
                setLoadingState(false);
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE:
                setLoadingState(false);
                setPlayControlState(false);
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
                setLoadingState(true);
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                removePlayingMsg();
                resetPlayTime();
                resetSeekBar();
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP:
                removePlayingMsg();
                break;
        }
    }

    public int getStartPos() {
        return startPos;
    }

    protected void onStartSeek() {
        if(startSeekPos > 0){
            seekTo(startSeekPos);
            startSeekPos = -1;
        }
    }

    protected void onErrorEvent(int errorCode){
        Log.e(TAG,"----------Error---------");
        handleErrorEvent(errorCode);
        if(mOnErrorListener!=null){
            mOnErrorListener.onError(errorCode);
        }
    }

    private void handleErrorEvent(int errorCode) {
        switch (errorCode){
            case OnErrorListener.ERROR_CODE_COMMON:
                isOccurError = true;
                setPlayState(false);
                break;
            case OnErrorListener.ERROR_CODE_NET_ERROR:

                break;
        }
    }

    public boolean isOccurError(){
        return isOccurError;
    }

    @Override
    public void destroy() {
        super.destroy();
        unRegisterNetChangeReceiver();
        PlayerCollections.removePlayer(this);
        mContext = null;
        mOnErrorListener = null;
        mOnPlayerEventListener = null;
        mOnNetWorkStateChangeListener = null;
        mNetChangeReceiver = null;
    }

    private class NetChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if(CommonUtils.isNetworkConnected(context)){
                    if(mNetError){
                        onNormalConnection();
                        if(mOnNetWorkStateChangeListener!=null){
                            mOnNetWorkStateChangeListener.onNetWorkConnected();
                        }
                    }
                }else{
                    onErrorConnection();
                    if(mOnNetWorkStateChangeListener!=null){
                        mOnNetWorkStateChangeListener.onNetWorkError();
                    }
                }
            }
        }
    }

    protected void onErrorConnection() {
        mNetError = true;
    }

    protected void onNormalConnection() {
        mNetError = false;
    }

    /**
     * change player core , such as ijkplayer or other player.
     * @param type
     */
    public void updatePlayerType(int type){
        boolean needNotify = this.mPlayerType!=type;
        this.mPlayerType = type;
        if(needNotify && isNeedSwitchPlayerWidget()){
            notifyPlayerWidget();
        }
    }

    private void notifyPlayerWidget(){
        getPlayerWidget(mContext);
    }

    public int getPlayerType() {
        return mPlayerType;
    }

}
