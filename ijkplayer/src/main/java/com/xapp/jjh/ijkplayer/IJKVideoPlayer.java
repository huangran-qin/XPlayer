package com.xapp.jjh.ijkplayer;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;
import cn.ikan.libs.player.inter.OnErrorListener;
import cn.ikan.libs.player.inter.OnPlayerEventListener;
import cn.ikan.libs.player.setting.AspectRatio;
import cn.ikan.libs.player.setting.DecodeMode;
import cn.ikan.libs.player.setting.VideoRate;
import cn.ikan.libs.player.setting.ViewType;
import cn.ikan.libs.player.widget.BaseExtPlayer;
import cn.ikan.libs.player.widget.BaseSinglePlayer;

/**
 * Created by Taurus on 2016/8/29.
 */
public class IJKVideoPlayer extends BaseExtPlayer {

    protected BaseSinglePlayer mInternalPlayer;
    private String[] dataSource;

    public IJKVideoPlayer(Context context, int width, int height) {
        super(context, width, height);
    }

    public IJKVideoPlayer(Context context) {
        super(context);
    }

    public IJKVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IJKVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getPlayerWidget(Context context) {
        mInternalPlayer = new IJKSinglePlayer(context);
        initPlayerListener();
        return mInternalPlayer;
    }

    private void initPlayerListener() {
        if(available()){
            mInternalPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode) {
                    IJKVideoPlayer.this.onPlayerEvent(eventCode);
                }
            });
            mInternalPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(int errorCode) {
                    IJKVideoPlayer.this.onErrorEvent(errorCode);
                }
            });
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
    public void setViewType(ViewType mViewType) {
        super.setViewType(mViewType);

    }

    private boolean available(){
        return mInternalPlayer !=null;
    }

    @Override
    public VideoRate getCurrentDefinition() {
        return null;
    }

    @Override
    public List<VideoRate> getVideoDefinitions() {
        return null;
    }

    @Override
    public void changeVideoDefinition(VideoRate videoRate) {

    }

    @Override
    public void setData(String... data) {
        if(available() && data!=null && data.length>0){
            if(dataSource==null || (dataSource!=null && dataSource.length>0 && !dataSource[0].equals(data[0]))){
                if(mOnPlayStateChangeListener!=null){
                    mOnPlayStateChangeListener.onSourceChanged(!isFrontVideosOver());
                }
            }
            dataSource = data;
            mInternalPlayer.setData(data[0]);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE);
        }
    }

    @Override
    public void rePlay(int msc) {
        if(available()){
            stop();
            setData(dataSource);
            start(msc);
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
    public void start(int msc){
        if(available()){
            if(msc > 0){
                startSeekPos = msc;
            }
            startPos = msc;
            start();
        }
    }

    @Override
    public void pause() {
        if(available() && mInternalPlayer.isPlaying()){
            mInternalPlayer.pause();
            mStatus = STATUS_PAUSE;
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE);
        }
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSE){
            mInternalPlayer.start();
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
    public void doConfigChange(Configuration newConfig) {
        if(newConfig!=null){
            super.doConfigChange(newConfig);
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
        if(mInternalPlayer !=null){
            mInternalPlayer.destroy();
        }
    }
}
