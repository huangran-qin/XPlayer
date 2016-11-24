package com.xapp.jjh.xplayer;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danikula.videocache.HttpProxyCacheServer;
import com.jiajunhui.xapp.medialoader.loader.MediaLoader;
import com.xapp.jjh.xplayer.bean.PlayerMenu;
import com.xapp.jjh.xui.activity.TopBarActivity;
import cn.ikan.libs.player.inter.OnErrorListener;
import cn.ikan.libs.player.inter.OnPlayerEventListener;
import cn.ikan.libs.player.setting.HttpCacheSetting;
import cn.ikan.libs.player.setting.ViewType;
import cn.ikan.libs.player.widget.BaseBindControllerPlayer;
import cn.ikan.libs.player.widget.BaseExtPlayer;
import cn.ikan.libs.player.widget.BasePlayerController;


public class PlayerActivity extends TopBarActivity implements OnErrorListener, OnPlayerEventListener {

    private String TAG = "PlayerActivity";
    private BaseExtPlayer mXPlayer;
    private String url;
    private boolean isLocal;

    @Override
    public void parseIntent() {
        super.parseIntent();
        url = getIntent().getStringExtra("path");
        if(TextUtils.isEmpty(url)){
            isLocal = true;
            url = MediaLoader.getPathFromUri(getApplicationContext(),getIntent().getData());
            Log.d(TAG,"url:" + url);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View getContentView(LayoutInflater layoutInflater, ViewGroup container) {
        return inflate(R.layout.activity_main);
    }

    public void setListener() {

    }

    public void findViewById() {
        mXPlayer = findView(R.id.player);
    }

    @Override
    public void initData() {
        setSwipeBackEnable(false);
        if(TextUtils.isEmpty(url))
            return;
        String name = getIntent().getStringExtra("name");
        if(TextUtils.isEmpty(name)){
            name = getName(url);
        }
        setTopBarTitle(name);
        int decodeMode = getIntent().getIntExtra("decode_mode",0);
        /** 设置解码模式*/
        mXPlayer.setDecodeMode(new PlayerMenu().getDecodeMode(decodeMode));
        /** 设置渲染视图类型*/
        mXPlayer.setViewType(ViewType.SURFACEVIEW);
        /** 播放事件监听*/
        mXPlayer.setOnPlayerEventListener(this);
        /** 播放错误监听*/
        mXPlayer.setOnErrorListener(this);
        mXPlayer.setOnLoadingTimerChangeListener(new BaseBindControllerPlayer.OnLoadingTimerChangeListener() {
            @Override
            public void onLoadingTimeCounter(int seconds) {

            }

            @Override
            public void onLoadingTimeout() {

            }
        });
        mXPlayer.setOnNetWorkStateChangeListener(new BasePlayerController.OnNetWorkStateChangeListener() {
            @Override
            public void onNetWorkError() {

            }

            @Override
            public void onNetWorkConnected() {

            }

            @Override
            public int getErrorIconResId() {
                return 0;
            }

            @Override
            public String getErrorTipText() {
                return null;
            }
        });

        mXPlayer.setOnControllerStateChangeListener(new BasePlayerController.OnControllerStateChangeListener() {
            @Override
            public void onPlayControllerShow() {

            }

            @Override
            public void onPlayControllerHidden() {

            }
        });

        mXPlayer.setOnPlayStateChangeListener(new BaseBindControllerPlayer.OnPlayStateChangeListener() {
            @Override
            public void onUserPaused() {

            }

            @Override
            public void onUserResumed() {

            }

            @Override
            public void onSourceChanged(boolean isFrontVideo) {

            }

            @Override
            public void onPlayerStop() {

            }

            @Override
            public void onPlayerRePlay() {

            }
        });

        mXPlayer.setOnGestureTapListener(new BaseBindControllerPlayer.OnGestureTapListener() {
            @Override
            public void onGestureSingleTap() {

            }

            @Override
            public void onGestureDoubleTap() {

            }
        });

        HttpProxyCacheServer httpProxyCacheServer = HttpCacheSetting.getHttpProxyCacheServer();
        if(httpProxyCacheServer!=null && !isLocal){
            url = httpProxyCacheServer.getProxyUrl(url);
        }
        /** 播放指定的资源*/
        mXPlayer.setData(url);
        /** 启动播放*/
        mXPlayer.start();
//        setMenuType(MenuType.TEXT,R.string.setting);
        mXPlayer.post(new Runnable() {
            @Override
            public void run() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });
    }

    private String getName(String path){
        if(TextUtils.isEmpty(path))
            return "";
        int len = path.length();
        int index = path.lastIndexOf("/");
        if(index==-1 || index>=len-1){
            return path;
        }
        return path.substring(index+1,len);
    }

    @Override
    public void onMenuClick() {
        super.onMenuClick();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mXPlayer !=null){
            Log.d(TAG,"doConfigChanged ... ... ...");
            if(newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                quitFullScreen();
            }else{
                fullScreen();
            }
            mXPlayer.doConfigChange(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if(mXPlayer.isFullScreen()){
            mXPlayer.toggleFullScreen();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mXPlayer !=null){
            mXPlayer.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mXPlayer !=null){
            mXPlayer.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mXPlayer !=null){
            mXPlayer.pause();
        }
    }

    @Override
    public void onError(int errorCode) {
        Log.d(TAG,"-------------------ERROR !!!--------------------");
        showSnackBar("occur error !",null,null);
    }

    @Override
    public void onPlayerEvent(int eventCode) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START:
                Log.d(TAG,"EVENT_CODE_ON_INTENT_TO_START");
                break;

            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                Log.d(TAG,"EVENT_CODE_PREPARED");
                break;

            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                Log.d(TAG,"EVENT_CODE_RENDER_START");
                break;

            case OnPlayerEventListener.EVENT_CODE_BUFFERING_START:
                Log.d(TAG,"EVENT_CODE_BUFFERING_START");
                break;

            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                Log.d(TAG,"EVENT_CODE_BUFFERING_END");
                break;

            case OnPlayerEventListener.EVENT_CODE_SEEK_COMPLETE:
                Log.d(TAG,"EVENT_CODE_SEEK_COMPLETE");
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE:
                Log.d(TAG,"EVENT_CODE_PLAY_PAUSE");
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAY_RESUME:
                Log.d(TAG,"EVENT_CODE_PLAY_RESUME");
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE:
                Log.d(TAG,"EVENT_CODE_PLAY_COMPLETE");
                showSnackBar("Play Complete",null,null);
                break;
        }
    }
}
