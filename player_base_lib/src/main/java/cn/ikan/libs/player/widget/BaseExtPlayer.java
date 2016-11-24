package cn.ikan.libs.player.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ikan.libs.player.R;
import cn.ikan.libs.player.inter.IExtPlayer;
import cn.ikan.libs.player.inter.OnErrorListener;
import cn.ikan.libs.player.inter.OnFrontVideoListener;
import cn.ikan.libs.player.inter.OnPlayerEventListener;
import cn.ikan.libs.player.setting.BaseFrontVideo;
import cn.ikan.libs.player.setting.VideoRate;

/**
 * Created by Taurus on 2016/9/29.
 */
public abstract class BaseExtPlayer extends BasePlayer implements IExtPlayer {

    /**
     * 广告前贴列表
     */
    protected List<? extends BaseFrontVideo> mFrontVideos = new ArrayList<>();
    /**
     * 当前播放的广告索引
     */
    private int mFrontVideoPlayIndex;
    private OnFrontVideoListener mOnFrontVideoListener;
    /**
     * 前贴是否播放完成
     */
    private boolean isFrontVideosOver = true;

    private boolean isNeedCornerCut = false;

    private CornerCutFrameLayout mCornerCutLayout;
    private ViewGroup.LayoutParams cornerCutLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    private BaseExtendHolder mBaseExtHolder;

    private ParamsBean originParams;
    private View mExtContainer;

    private TextView mTvAdCounter;
    private View mAdTimeView;
    private int preTime = -1;

    public BaseExtPlayer(Context context, int width, int height) {
        super(context, width, height);
    }

    public BaseExtPlayer(Context context) {
        super(context);
    }

    public BaseExtPlayer(Context context, int type, int width, int height) {
        super(context, type, width, height);
    }

    public BaseExtPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseExtPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInitOver(Context context) {
        super.onInitOver(context);
        View view = getFrontVideoCover();
        if(view!=null){
            mAdTimeView = view.findViewById(R.id.player_ad_time_counter);
            mTvAdCounter = (TextView) view.findViewById(R.id.player_text_view_ad_time_counter);
            addView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void setAdTimeViewState(boolean state){
        if(mAdTimeView!=null){
            mAdTimeView.setVisibility(state?View.VISIBLE:View.GONE);
            if(!state){
                preTime = -1;
            }
        }
    }

    @Override
    protected void timeCounter(int curr, int duration, int bufferPercentage) {
        super.timeCounter(curr, duration, bufferPercentage);
        if(duration>0 && !isFrontVideosOver() && mTvAdCounter!=null){
            setAdTimeViewState(true);
            int time = (duration - curr)/1000;
            if(time > preTime && preTime != -1){
                time = preTime;
            }
            preTime = time;
            mTvAdCounter.setText(String.valueOf(time));
        }else{
            preTime = -1;
            setAdTimeViewState(false);
        }
        if(mBaseExtHolder!=null){
            mBaseExtHolder.onPlayerTimeCounter(curr, duration, bufferPercentage);
        }
    }

    public boolean isNeedCornerCut() {
        return isNeedCornerCut;
    }

    public void setNeedCornerCut(boolean needCornerCut) {
        isNeedCornerCut = needCornerCut;
        if(mCornerCutLayout!=null){
            mCornerCutLayout.setVisibility(needCornerCut?View.VISIBLE:View.GONE);
        }else{
            mCornerCutLayout = new CornerCutFrameLayout(getContext());
            addView(mCornerCutLayout,cornerCutLayoutParams);
        }
    }

    public void setCornerCutRadius(int mCornerCutRadius) {
        if(!isNeedCornerCut)
            return;
        if(mCornerCutLayout!=null){
            mCornerCutLayout.setCornerRadius(mCornerCutRadius);
        }
    }

    public void setCornerBgColor(int mCornerBgColor) {
        if(!isNeedCornerCut)
            return;
        if(mCornerCutLayout!=null){
            mCornerCutLayout.setCornerBgColor(mCornerBgColor);
        }
    }

    public void setOnFrontVideoListener(OnFrontVideoListener onFrontVideoListener){
        this.mOnFrontVideoListener = onFrontVideoListener;
    }

    @Override
    public void startFrontVideos(List<? extends BaseFrontVideo> frontVideos, final OnFrontVideoListener onFrontVideoListener) {
        this.mFrontVideos = frontVideos;
        if(mFrontVideos!=null){
            this.mFrontVideos.clear();
        }
        setOnFrontVideoListener(new OnFrontVideoListener() {
            @Override
            public void onVideoComplete(final BaseFrontVideo frontVideo, final boolean isAllComplete) {
                setAdTimeViewState(false);
                if(onFrontVideoListener!=null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onFrontVideoListener.onVideoComplete(frontVideo, isAllComplete);
                        }
                    });
                }
            }

            @Override
            public void onFrontVideoClick(BaseFrontVideo frontVideo) {
                if(onFrontVideoListener!=null){
                    onFrontVideoListener.onFrontVideoClick(frontVideo);
                }
            }
        });
        startFrontVideos();
    }

    public List<? extends BaseFrontVideo> getFrontVideos() {
        return mFrontVideos;
    }

    public void startFrontVideos() {
        this.mFrontVideoPlayIndex = 0;
        if(mFrontVideos!=null && mFrontVideos.size()>0){
            setControllerEnable(false);
            setSeekGestureEnable(false);
            this.isFrontVideosOver = false;
            startPlayFrontVideos();
        }else{
            this.isFrontVideosOver = true;
            if(mOnFrontVideoListener!=null){
                mOnFrontVideoListener.onVideoComplete(null,true);
            }
            onFrontVideosAllComplete();
        }
    }

    private void startPlayFrontVideos() {
        final BaseFrontVideo frontVideo = mFrontVideos.get(mFrontVideoPlayIndex);
        if(frontVideo!=null && frontVideo.getVideoUrl()!=null){
            stop();
            post(new Runnable() {
                @Override
                public void run() {
                    String definition = "";
                    VideoRate videoRate = frontVideo.getVideoRate();
                    if(videoRate!=null){
                        definition = videoRate.getDefinition();
                    }
                    Log.d("_BasePlayer","definition = " + definition);
                    setData(frontVideo.getVideoUrl(),definition);
                    Log.d("_BasePlayer","setData......");
                    start();
                    Log.d("_BasePlayer","start......");
                    mFrontVideoPlayIndex++;
                }
            });
        }
    }

    @Override
    protected void onPlayerEvent(int eventCode) {
        super.onPlayerEvent(eventCode);
        if(mBaseExtHolder!=null){
            mBaseExtHolder.onPlayerEvent(eventCode);
        }
        if(!isFrontVideosOver){
            switch (eventCode){
                case OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE:
                    if(mFrontVideoPlayIndex!=0){
                        judgeNextFrontVideo();
                    }
                    break;
            }
        }
    }

    private void judgeNextFrontVideo() {
        if(mFrontVideos==null || (mFrontVideos!=null && mFrontVideos.size()==0)){
            if(mOnFrontVideoListener!=null){
                this.isFrontVideosOver = true;
                mOnFrontVideoListener.onVideoComplete(null,true);
                onFrontVideosAllComplete();
            }
            return;
        }
        if(mFrontVideoPlayIndex >= mFrontVideos.size()-1){
            if(mOnFrontVideoListener!=null){
                this.isFrontVideosOver = true;
                mOnFrontVideoListener.onVideoComplete(mFrontVideos.get(mFrontVideos.size()-1),true);
                onFrontVideosAllComplete();
            }
            return;
        }else{
            if(mOnFrontVideoListener!=null){
                mOnFrontVideoListener.onVideoComplete(mFrontVideos.get(mFrontVideoPlayIndex),false);
            }
        }
        startPlayFrontVideos();
    }

    @Override
    protected void onErrorEvent(int errorCode) {
        super.onErrorEvent(errorCode);
        if(!isFrontVideosOver && !mNetError){
            judgeNextFrontVideo();
        }
        if(mBaseExtHolder!=null){
            mBaseExtHolder.onPlayerErrorEvent(errorCode);
        }
        if(errorCode!= OnErrorListener.ERROR_CODE_NET_ERROR){
            setAdTimeViewState(false);
        }
    }

    @Override
    public void passFrontVideos() {
        this.isFrontVideosOver = true;
        if(mOnFrontVideoListener!=null){
            BaseFrontVideo frontVideo = null;
            if(mFrontVideos!=null && mFrontVideos.size()>0){
                frontVideo = mFrontVideos.get(mFrontVideos.size()-1);
            }
            mOnFrontVideoListener.onVideoComplete(frontVideo,true);
            onFrontVideosAllComplete();
        }
    }

    private void onFrontVideosAllComplete() {
        setControllerEnable(true);
        setSeekGestureEnable(true);
        setAdTimeViewState(false);
    }

    public boolean isFrontVideosOver() {
        return isFrontVideosOver;
    }

    @Override
    public View getFrontVideoCover() {
        return View.inflate(getContext(), R.layout.layout_front_video_cover,null);
    }

    public void fullScreen(View container){
        setFocusable(true);
        this.mExtContainer = container;
        if (originParams ==null){
            originParams = fillParamsBean(container);
        }
        ViewGroup.LayoutParams params = container.getLayoutParams();
        if(params instanceof LinearLayout.LayoutParams){
            ((LinearLayout.LayoutParams) params).leftMargin = 0;
            ((LinearLayout.LayoutParams) params).topMargin = 0;
            ((LinearLayout.LayoutParams) params).rightMargin = 0;
            ((LinearLayout.LayoutParams) params).bottomMargin = 0;
        }else if(params instanceof RelativeLayout.LayoutParams){
            ((RelativeLayout.LayoutParams) params).leftMargin = 0;
            ((RelativeLayout.LayoutParams) params).topMargin = 0;
            ((RelativeLayout.LayoutParams) params).rightMargin = 0;
            ((RelativeLayout.LayoutParams) params).bottomMargin = 0;
        }
        params.width = mWidthPixels;
        params.height = mHeightPixels;
        container.setLayoutParams(params);
        container.setPadding(0,0,0,0);
        isFullScreen = true;
        setControllerEnable(true);
        setNeedCornerCut(false);
        requestFocus();
        if(mBaseExtHolder!=null){
            mBaseExtHolder.onPlayerFullScreen();
        }
    }

    private ParamsBean fillParamsBean(View container) {
        ViewGroup.LayoutParams params = container.getLayoutParams();
        ParamsBean paramsBean = new ParamsBean();
        if(params instanceof LinearLayout.LayoutParams){
            paramsBean.leftMargin   = ((LinearLayout.LayoutParams) params).leftMargin;
            paramsBean.topMargin    = ((LinearLayout.LayoutParams) params).topMargin;
            paramsBean.rightMargin  = ((LinearLayout.LayoutParams) params).rightMargin;
            paramsBean.bottomMargin = ((LinearLayout.LayoutParams) params).bottomMargin;
            paramsBean.params_type = ParamsBean.PARAMS_LINEAR;
        }else if(params instanceof RelativeLayout.LayoutParams){
            paramsBean.leftMargin   = ((RelativeLayout.LayoutParams) params).leftMargin;
            paramsBean.topMargin    = ((RelativeLayout.LayoutParams) params).topMargin;
            paramsBean.rightMargin  = ((RelativeLayout.LayoutParams) params).rightMargin;
            paramsBean.bottomMargin = ((RelativeLayout.LayoutParams) params).bottomMargin;
            paramsBean.params_type = ParamsBean.PARAMS_RELATIVE;
        }
        paramsBean.leftPadding = container.getPaddingLeft();
        paramsBean.topPadding = container.getPaddingTop();
        paramsBean.rightPadding = container.getPaddingRight();
        paramsBean.bottomPadding = container.getPaddingBottom();
        paramsBean.width = params.width;
        paramsBean.height = params.height;
        return paramsBean;
    }

    public void quitFullScreen(){
        setFocusable(false);
        isFullScreen = false;
        setControllerEnable(false);
        setNeedCornerCut(true);
        if(originParams !=null && mExtContainer!=null){
            Log.d("ext_params","width = " + originParams.width + " height = " + originParams.height);
            mExtContainer.setLayoutParams(getParams(originParams));
            mExtContainer.setPadding(originParams.leftPadding,originParams.topPadding,originParams.rightPadding,originParams.bottomPadding);
        }
        if(mBaseExtHolder!=null){
            mBaseExtHolder.onPlayerQuitFullScreen();
        }
    }

    private ViewGroup.LayoutParams getParams(ParamsBean paramsBean) {
        ViewGroup.LayoutParams params = mExtContainer.getLayoutParams();
        if(params instanceof LinearLayout.LayoutParams){
            ((LinearLayout.LayoutParams) params).leftMargin = paramsBean.leftMargin;
            ((LinearLayout.LayoutParams) params).topMargin = paramsBean.topMargin;
            ((LinearLayout.LayoutParams) params).rightMargin = paramsBean.rightMargin;
            ((LinearLayout.LayoutParams) params).bottomMargin = paramsBean.bottomMargin;
        }else if(params instanceof RelativeLayout.LayoutParams){
            ((RelativeLayout.LayoutParams) params).leftMargin = paramsBean.leftMargin;
            ((RelativeLayout.LayoutParams) params).topMargin = paramsBean.topMargin;
            ((RelativeLayout.LayoutParams) params).rightMargin = paramsBean.rightMargin;
            ((RelativeLayout.LayoutParams) params).bottomMargin = paramsBean.bottomMargin;
        }
        params.width = paramsBean.width;
        params.height = paramsBean.height;
        return params;
    }

    public void addExtHolder(BaseExtendHolder extendHolder){
        if(mBaseExtHolder==null){
            mBaseExtHolder = extendHolder;
            addExtendView(extendHolder,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if(mBaseExtHolder!=null){
            mBaseExtHolder.destroy();
            mBaseExtHolder = null;
        }
        mActivity = null;
    }

    @Override
    protected void onErrorConnection() {
        super.onErrorConnection();
        setAdTimeViewState(false);
    }

    @Override
    protected void onLoadingTimeOut() {
        super.onLoadingTimeOut();
        setAdTimeViewState(false);
    }

    public abstract void destroyPlayer();

    public static class ParamsBean{
        public static final int PARAMS_LINEAR = 1;
        public static final int PARAMS_RELATIVE = 2;
        public int leftMargin;
        public int topMargin;
        public int rightMargin;
        public int bottomMargin;
        public int leftPadding;
        public int topPadding;
        public int rightPadding;
        public int bottomPadding;
        public int width;
        public int height;
        public int params_type;
    }

}
