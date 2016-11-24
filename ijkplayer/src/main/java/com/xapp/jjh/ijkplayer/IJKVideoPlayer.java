package com.xapp.jjh.ijkplayer;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

import cn.ikan.libs.player.inter.OnErrorListener;
import cn.ikan.libs.player.inter.OnPlayerEventListener;
import cn.ikan.libs.player.setting.AspectRatio;
import cn.ikan.libs.player.setting.DecodeMode;
import cn.ikan.libs.player.setting.VideoRate;
import cn.ikan.libs.player.setting.ViewType;
import cn.ikan.libs.player.widget.BaseExtPlayer;
import ijk_widget.IRenderView;
import ijk_widget.IjkVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Taurus on 2016/8/29.
 */
public class IJKVideoPlayer extends BaseExtPlayer {

    private final String TAG = "IjkVideoPlayer";
    protected IjkVideoView mVideoView;
    private boolean hasLoadLibrary;
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
        loadLibrary();
        mVideoView = new IjkVideoView(context);
        mVideoView.setFocusable(false);
        mVideoView.setBackgroundColor(Color.BLACK);
        initPlayerListener();
        return mVideoView;
    }

    private void loadLibrary() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            hasLoadLibrary = true;
        } catch (Throwable e) {
            Log.e(TAG, "loadLibraries error", e);
        }
    }

    private void initPlayerListener() {
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                Log.d(TAG,"EVENT_CODE_PLAY_COMPLETE");
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE);
            }
        });
        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
//                Log.d(TAG,"onInfo : what = " + what);
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        Log.d(TAG,"EVENT_CODE_BUFFERING_START");
                        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_BUFFERING_START);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        Log.d(TAG,"EVENT_CODE_BUFFERING_END");
                        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_BUFFERING_END);
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        Log.d(TAG,"EVENT_CODE_RENDER_START");
                        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_RENDER_START);
                        break;
                }
                return false;
            }
        });
        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                onErrorEvent(OnErrorListener.ERROR_CODE_COMMON);
                return false;
            }
        });
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                preparedMediaPlayer(mp);
                Log.d(TAG,"EVENT_CODE_PREPARED");
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PREPARED);
                onStartSeek();
            }
        });
    }

    private void preparedMediaPlayer(IMediaPlayer mediaPlayer) {
        if (mediaPlayer == null)
            return;
        mediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(IMediaPlayer mp) {
                Log.d(TAG,"EVENT_CODE_SEEK_COMPLETE");
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_SEEK_COMPLETE);
            }
        });
    }

    @Override
    public void setDecodeMode(DecodeMode mDecodeMode) {
        super.setDecodeMode(mDecodeMode);
        updateVideoViewDecodeMode();
    }

    @Override
    public void setViewType(ViewType mViewType) {
        super.setViewType(mViewType);
        updateVideoViewViewType();
    }

    private void updateVideoViewViewType() {
        if(mVideoView!=null){
            if(getViewType() == ViewType.SURFACEVIEW){
                mVideoView.setEnableSurfaceView();
            }else if(getViewType() == ViewType.TEXTUREVIEW){
                mVideoView.setEnableTextureView();
            }
        }
    }

    private void updateVideoViewDecodeMode() {
        if(mVideoView!=null){
            if(getDecodeMode() == DecodeMode.MEDIA_PLAYER){
                mVideoView.setUsingAndroidPlayer(true);
            }else if(getDecodeMode() == DecodeMode.SOFT){
                mVideoView.setUsingAndroidPlayer(false);
            }else if(getDecodeMode() == DecodeMode.HARD){
                mVideoView.setUsingAndroidPlayer(false);
                mVideoView.setUsingMediaCodec(true);
            }
        }
    }

    @Override
    public void onGestureDoubleTap() {
        super.onGestureDoubleTap();
        if(isGestureDoubleTapEnable()){
            toggleAspectRatio();
        }
    }

    private void toggleAspectRatio() {
        if(available()){
            mVideoView.toggleAspectRatio();
        }
    }

    private boolean available(){
        return mVideoView!=null && hasLoadLibrary;
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
            mVideoView.setVideoPath(data[0]);
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
            mVideoView.start();
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
        if(available() && mVideoView.isPlaying()){
            mVideoView.pause();
            mStatus = STATUS_PAUSE;
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE);
        }
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSE){
            mVideoView.start();
            mStatus = STATUS_PLAYING;
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME);
        }
    }

    @Override
    public void seekTo(int msc) {
        if(available()){
            mVideoView.seekTo(msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO);
        }
    }

    @Override
    public void stop() {
        if(available()){
            mVideoView.stop();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP);
            if(mOnPlayStateChangeListener!=null){
                mOnPlayStateChangeListener.onPlayerStop();
            }
        }
    }

    @Override
    public boolean isPlaying() {
        if(available()){
            return mVideoView.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(available()){
            return mVideoView.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(available()){
            return mVideoView.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(available()){
            return mVideoView.getBufferPercentage();
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
            if(aspectRatio == AspectRatio.AspectRatio_16_9){
                mVideoView.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_4_3){
                mVideoView.setAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_FILL_PARENT){
                mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_ORIGIN){
                mVideoView.setAspectRatio(IRenderView.AR_ASPECT_WRAP_CONTENT);
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyPlayer();
    }

    @Override
    public void destroyPlayer() {
        if(mVideoView!=null){
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
    }
}
