package com.xapp.jjh.base_ijk.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.xapp.jjh.base_ijk.config.ConfigLoader;
import com.xapp.jjh.base_ijk.config.DebugPrintHelper;

import java.util.List;
import cn.ikan.libs.player.inter.OnErrorListener;
import cn.ikan.libs.player.inter.OnPlayerEventListener;
import cn.ikan.libs.player.setting.AspectRatio;
import cn.ikan.libs.player.setting.DecodeMode;
import cn.ikan.libs.player.setting.VideoRate;
import cn.ikan.libs.player.widget.BaseExtPlayer;
import cn.ikan.libs.player.widget.BaseSinglePlayer;

/**
 * Created by Taurus on 2016/11/14.
 */

public class MixPlayer extends BaseExtPlayer {

    private final String TAG = "MixPlayer";
    private BaseSinglePlayer mInternalPlayer;
    private String[] dataSource;

    public MixPlayer(Context context) {
        super(context);
    }

    public MixPlayer(Context context,int type, int width, int height){
        super(context,type,width,height);
    }

    public MixPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MixPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getPlayerWidget(Context context) {
        destroyPlayer();
        Log.d(TAG,"playerType : " + DebugPrintHelper.getPlayerDebugStr(getPlayerType()));
        mInternalPlayer = (BaseSinglePlayer) ConfigLoader.getPlayerInstance(mContext,getPlayerType());
        if(mInternalPlayer !=null){
            mInternalPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(int errorCode) {
                    onErrorEvent(errorCode);
                }
            });
            mInternalPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode) {
                    onEventPlayer(eventCode);
                }
            });
        }else{
            return new FrameLayout(mContext);
        }
        return mInternalPlayer;
    }

    private void onEventPlayer(int eventCode) {
        onPlayerEvent(eventCode);
    }

    private boolean available(){
        return mInternalPlayer !=null;
    }

    @Override
    public void setData(String... data) {
        if(available() && data!=null && data.length>0){
            if(dataSource==null || (dataSource!=null && dataSource.length>0 && !dataSource[0].equals(data[0]))){
                if(mOnPlayStateChangeListener!=null){
                    mOnPlayStateChangeListener.onSourceChanged(!isFrontVideosOver());
                }
            }
            this.dataSource = data;
            mInternalPlayer.setData(data);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START);
        }
    }

    @Override
    public void start() {
        if(available()){
            startPos = 0;
            mInternalPlayer.start();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START);
        }
    }

    @Override
    public void start(int msc) {
        if(available()){
            startPos = msc;
            mInternalPlayer.start(msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START);
        }
    }

    @Override
    public void pause() {
        if(available()){
            mInternalPlayer.pause();
            mStatus = STATUS_PAUSE;
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE);
        }
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSE){
            mInternalPlayer.resume();
            mStatus = STATUS_PLAYING;
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME);
        }
    }

    @Override
    public void seekTo(int msc) {
        if(available()){
            mInternalPlayer.seekTo(msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO);
        }
    }

    @Override
    public void stop() {
        if(available()){
            mInternalPlayer.stop();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP);
            if(mOnPlayStateChangeListener!=null){
                mOnPlayStateChangeListener.onPlayerStop();
            }
        }
    }

    @Override
    protected void onClickResume() {
        if(available() && !isPlaying()){
            mInternalPlayer.onClickResume();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(available()){
            if(dataSource!=null && available()){
                stop();
                if(mCurrRate!=null && !TextUtils.isEmpty(mCurrRate.getDefinition()) && dataSource.length>1){
                    dataSource[1] = mCurrRate.getDefinition();
                }
                setData(dataSource);
                start(msc);
                if(mOnPlayStateChangeListener!=null){
                    mOnPlayStateChangeListener.onPlayerRePlay();
                }
            }
        }
    }

    @Override
    public boolean isPlaying() {
        if(available()){
            return mInternalPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(available()){
            return mInternalPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(available()){
            return mInternalPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(available()){
            return mInternalPlayer.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public VideoRate getCurrentDefinition() {
        if(available()){
            return mInternalPlayer.getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<VideoRate> getVideoDefinitions() {
        if(available()){
            return mInternalPlayer.getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(VideoRate videoRate) {
        if(available()){
            mCurrRate = videoRate;
            mInternalPlayer.changeVideoDefinition(videoRate);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CHANGE_DEFINITION);
        }
    }

    @Override
    public void setDecodeMode(DecodeMode mDecodeMode) {
        super.setDecodeMode(mDecodeMode);
        if(available()){
            mInternalPlayer.setDecodeMode(mDecodeMode);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(available()){
            mInternalPlayer.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyPlayer();
    }

    @Override
    public void destroyPlayer() {
        if(available()){
            mInternalPlayer.setOnErrorListener(null);
            mInternalPlayer.setOnPlayerEventListener(null);
            mInternalPlayer.destroy();
        }
    }

    @Override
    public boolean isNeedSwitchPlayerWidget() {
        return true;
    }
}
