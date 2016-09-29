package com.xapp.jjh.base_ijk.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.xapp.jjh.base_ijk.R;
import com.xapp.jjh.base_ijk.inter.IController;
import com.xapp.jjh.base_ijk.utils.TimeUtil;

/**
 * Created by Taurus on 2016/8/29.
 */
public abstract class BasePlayerController extends FrameLayout implements IController {

    private final String TAG = "BasePlayController";
    protected Activity mActivity;
    private View mTopStatusBarView;
    private View mProgressBar;
    private View mPlayControlView;
    private ImageView mIvPlayStateIcon;
    private SeekBar mSeekBar;
    private TextView mTvPlayAllTime;
    private TextView mTvPlayCurrTime;
    private TextView mTvPlayTotalTime;
    private TextView mTvSystemTime;
    private View mTouchLayout;

    private boolean mGestureEnable = true;
    private boolean mControllerEnable = true;
    private boolean mTopBarEnable = true;
    private boolean mTapEnable = true;

    private boolean mVolumeGestureEnable = true;
    private boolean mBrightGestureEnable = true;
    private boolean mSeekGestureEnable = true;

    protected int mWidthPixels;
    protected int mHeightPixels;

    protected int mOriginalWidth;
    protected int mOriginalHeight;
    private View controllerView;

    private OnControllerStateChangeListener mOnControllerStateChangeListener;

    public BasePlayerController(Context context) {
        super(context);
        initController(context);
    }

    public BasePlayerController(Context context, int width, int height){
        super(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width==-1?ViewGroup.LayoutParams.MATCH_PARENT:width,height);
        setLayoutParams(params);
        initController(context);
    }

    public BasePlayerController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initController(context);
    }

    public BasePlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initController(context);
    }

    @SuppressLint("NewApi")
    public BasePlayerController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initController(context);
    }

    protected void initController(Context context) {
        if(!(context instanceof Activity)){
            throw new IllegalArgumentException("-----please set activity context !-----");
        }
        mActivity = (Activity) context;
        setBackgroundColor(Color.BLACK);
        initSystemInfo();
        initPlayerWidget(context);
        initGestureLayout(context);
        initPlayerControl(context);
        bindController(context);
        onInitOver(context);
        post(new Runnable() {
            @Override
            public void run() {
                mOriginalWidth = getMeasuredWidth();
                mOriginalHeight = getMeasuredHeight();
                Log.d(TAG,"mOriginalWidth : " + mOriginalWidth + " mOriginalHeight : " + mOriginalHeight);
            }
        });
    }

    protected void onInitOver(Context context){

    }

    protected abstract void bindController(Context context);

    private void initPlayerWidget(Context context){
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        addView(getPlayerWidget(context),params);
    }

    protected abstract View getPlayerWidget(Context context);

    protected void initSystemInfo() {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidthPixels = dm.widthPixels;
        mHeightPixels = dm.heightPixels;
        keepScreenOn();
    }

    protected void keepScreenOn(){
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initPlayerControl(Context context) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        controllerView = getControllerView(context);
        if(controllerView == null)
            throw new IllegalArgumentException("please set player controller view !");
        controllerView.setBackgroundColor(Color.TRANSPARENT);
        mTopStatusBarView = findControllerViewById(R.id.player_top_status_bar_container);
        mPlayControlView = findControllerViewById(R.id.player_bottom_controller_container);
        mProgressBar = findControllerViewById(R.id.player_loading_progress_bar);
        mIvPlayStateIcon = (ImageView) findControllerViewById(R.id.player_image_view_play_icon);
        mSeekBar = (SeekBar) findControllerViewById(R.id.player_seek_bar);
        mTvPlayAllTime = (TextView) findControllerViewById(R.id.player_text_view_all_time);
        mTvPlayCurrTime = (TextView) findControllerViewById(R.id.player_text_view_current_time);
        mTvPlayTotalTime = (TextView) findControllerViewById(R.id.player_text_view_total_time);
        mTvSystemTime = (TextView) findControllerViewById(R.id.player_text_view_system_time);
        addView(controllerView,params);
        addListener();
    }

    public View findControllerViewById(int id){
        if(controllerView == null || id < 0)
            return null;
        return controllerView.findViewById(id);
    }

    public View getControllerView(Context context) {
        return View.inflate(context, R.layout.layout_player_controller,null);
    }

    private void addListener() {
        if(mIvPlayStateIcon!=null){
            mIvPlayStateIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlayIconClick();
                }
            });
        }
        if(mSeekBar!=null){
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    onSeekBarProgressChanged(seekBar, progress, fromUser);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    onSeekBarStartTrackingTouch(seekBar);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    onSeekBarStopTrackingTouch(seekBar);
                }
            });
        }
    }

    protected void onSeekBarProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

    }

    protected void onSeekBarStartTrackingTouch(SeekBar seekBar) {

    }

    protected void onSeekBarStopTrackingTouch(SeekBar seekBar) {

    }

    protected void onPlayIconClick() {

    }

    private void initGestureLayout(Context context){
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTouchLayout = View.inflate(context, R.layout.layout_player_extend_center_box,null);
        mTouchLayout.setBackgroundColor(Color.TRANSPARENT);
        addView(mTouchLayout,params);
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new PlayerGestureListener());
        mTouchLayout.setClickable(true);
        mTouchLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event))
                    return true;
                // 处理手势结束
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }

                return false;
            }
        });
    }

    protected void endGesture(){

    }

    @Override
    public void setGestureEnable(boolean enable) {
        this.mGestureEnable = enable;
    }

    @Override
    public void setVolumeGestureEnable(boolean enable) {
        this.mVolumeGestureEnable = enable;
    }

    @Override
    public void setBrightGestureEnable(boolean enable) {
        this.mBrightGestureEnable = enable;
    }

    @Override
    public void setSeekGestureEnable(boolean enable) {
        this.mSeekGestureEnable = enable;
    }

    @Override
    public void setControllerEnable(boolean enable) {
        this.mControllerEnable = enable;
    }

    @Override
    public void setTopStatusBarEnable(boolean enable) {
        this.mTopBarEnable = enable;
    }

    @Override
    public void setTapEnable(boolean enable){
        this.mTapEnable = enable;
    }

    @Override
    public void setPlayControlState(boolean state) {
        if(mPlayControlView == null)
            return;
        if(state && !mControllerEnable)
            return;
        mPlayControlView.setVisibility(state?View.VISIBLE:View.GONE);
        if(mOnControllerStateChangeListener!=null){
            if(state){
                mOnControllerStateChangeListener.onPlayControllerShow();
            }else{
                mOnControllerStateChangeListener.onPlayControllerHidden();
            }
        }
    }

    @Override
    public boolean isPlayControlShow() {
        if(mPlayControlView==null)
            return false;
        return mPlayControlView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void setTopStatusBarState(boolean state) {
        if(mTopStatusBarView == null)
            return;
        if(state && !mTopBarEnable)
            return;
        if(state)
            setSystemTime();
        mTopStatusBarView.setVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public boolean isTopStatusBarShow() {
        if(mTopStatusBarView == null)
            return false;
        return mTopStatusBarView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void setSystemTime() {
        if(mTvSystemTime==null)
            return;
        mTvSystemTime.setText(TimeUtil.getNowTime());
    }

    @Override
    public void setLoadingState(boolean state) {
        if(mProgressBar == null)
            return;
        mProgressBar.setVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public void setPlayState(boolean isPlaying) {
        if(mIvPlayStateIcon==null)
            return;
        mIvPlayStateIcon.setImageResource(isPlaying?R.mipmap.ic_video_player_btn_pause: R.mipmap.ic_video_player_btn_play);
    }

    @Override
    public void setPlayTime(long curr, long total) {
        if(mTvPlayAllTime != null){
            mTvPlayAllTime.setText(TimeUtil.getTime(curr) + "/" + TimeUtil.getTime(total));
        }else{
            if(mTvPlayCurrTime!=null){
                mTvPlayCurrTime.setText(TimeUtil.getTime(curr));
            }
            if(mTvPlayTotalTime!=null){
                mTvPlayTotalTime.setText(TimeUtil.getTime(total));
            }
        }
    }

    @Override
    public void setSeekMax(int max) {
        if(mSeekBar == null)
            return;
        mSeekBar.setMax(max);
    }

    @Override
    public void setSeekProgress(int progress) {
        if(mSeekBar == null)
            return;
        mSeekBar.setProgress(progress);
    }

    @Override
    public void setSeekSecondProgress(int progress) {
        if(mSeekBar == null)
            return;
        mSeekBar.setSecondaryProgress(progress);
    }

    @Override
    public abstract void leftVerticalSlide(float percent);

    @Override
    public abstract void rightVerticalSlide(float percent);

    @Override
    public abstract void horizontalSlide(float percent);

    @Override
    public abstract void onGestureDoubleTap();

    @Override
    public abstract void onGestureSingleTapUp();

    protected void setVolumeState(boolean state){
        mTouchLayout.findViewById(R.id.app_video_volume_box).setVisibility(state?View.VISIBLE:View.GONE);
    }

    protected void setVolumeText(String volumeText){
        ((TextView)mTouchLayout.findViewById(R.id.app_video_volume)).setText(volumeText);
    }

    protected void setVolumeIcon(int id){
        ((ImageView)mTouchLayout.findViewById(R.id.app_video_volume_icon)).setImageResource(id);
    }

    protected void setLightState(boolean state){
        mTouchLayout.findViewById(R.id.app_video_brightness_box).setVisibility(state?View.VISIBLE:View.GONE);
    }

    protected void setLightText(String lightText){
        ((TextView)mTouchLayout.findViewById(R.id.app_video_brightness)).setText(lightText);
    }

    protected void setFastForwardText(String fastForwardText){
        ((TextView)mTouchLayout.findViewById(R.id.app_video_fastForward)).setText(fastForwardText);
    }

    protected void setFastForwardTargetText(String fastForwardTargetText){
        ((TextView)mTouchLayout.findViewById(R.id.app_video_fastForward_target)).setText(fastForwardTargetText);
    }

    protected void setFastForwardAllText(String fastForwardAllText){
        ((TextView)mTouchLayout.findViewById(R.id.app_video_fastForward_all)).setText(fastForwardAllText);
    }

    protected void setFastForwardState(boolean state){
        mTouchLayout.findViewById(R.id.app_video_fastForward_box).setVisibility(state?View.VISIBLE:View.GONE);
    }

    private class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean volumeControl;
        private boolean toSeek;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(mTapEnable){
                onGestureDoubleTap();
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstTouch = true;
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(!mGestureEnable)
                return super.onScroll(e1, e2, distanceX, distanceY);
            float mOldX = e1.getX(), mOldY = e1.getY();
            float deltaY = mOldY - e2.getY();
            float deltaX = mOldX - e2.getX();
            if (firstTouch) {
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                volumeControl=mOldX > mWidthPixels * 0.5f;
                firstTouch = false;
            }

            if (toSeek) {
                if(mSeekGestureEnable){
                    horizontalSlide(-deltaX / getWidth());
                }
            } else {
                float percent = deltaY / getHeight();
                if (volumeControl) {
                    if(mVolumeGestureEnable){
                        rightVerticalSlide(percent);
                    }
                } else{
                    if(mBrightGestureEnable){
                        leftVerticalSlide(percent);
                    }
                }
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(mTapEnable){
                onGestureSingleTapUp();
            }
            return super.onSingleTapUp(e);
        }
    }

    public void setOnControllerStateChangeListener(OnControllerStateChangeListener onControllerStateChangeListener) {
        this.mOnControllerStateChangeListener = onControllerStateChangeListener;
    }

    public interface OnControllerStateChangeListener{
        void onPlayControllerShow();
        void onPlayControllerHidden();
    }
}
