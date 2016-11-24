package cn.ikan.libs.player.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.List;

import cn.ikan.libs.player.inter.OnFullScreenHolderListener;
import cn.ikan.libs.player.inter.OnPlayerKeyListener;
import cn.ikan.libs.player.setting.VideoRate;

/**
 * Created by Taurus on 2016/10/17.
 */

public abstract class BaseExtendHolder<T> extends FrameLayout implements OnPlayerKeyListener, View.OnFocusChangeListener,
        View.OnKeyListener, BasePlayerController.OnControllerStateChangeListener, BaseBindControllerPlayer.OnPlayStateChangeListener, BaseBindControllerPlayer.OnLoadingTimerChangeListener, BasePlayerController.OnNetWorkStateChangeListener, ViewTreeObserver.OnGlobalFocusChangeListener {

    private String TAG = "_BaseExtendHolder";
    protected BaseExtPlayer mPlayer;
    protected OnFullScreenHolderListener mOnFullScreenHolderListener;
    protected T mHolderData;
    /** the holder visible state*/
    private boolean mHolderVisible;

    public BaseExtendHolder(Context context, BaseExtPlayer extPlayer, OnFullScreenHolderListener onFullScreenHolderListener) {
        super(context);
        this.mPlayer = extPlayer;
        this.mOnFullScreenHolderListener = onFullScreenHolderListener;
        init(context);
    }

    public void setHolderData(T holderData){
        this.mHolderData = holderData;
    }

    public T getHolderData(){
        return mHolderData;
    }

    public BaseExtendHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseExtendHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.TRANSPARENT);
        getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        mPlayer.setOnFocusChangeListener(this);
        mPlayer.setOnControllerStateChangeListener(this);
        mPlayer.setOnPlayStateChangeListener(this);
        mPlayer.setOnLoadingTimerChangeListener(this);
        mPlayer.setOnNetWorkStateChangeListener(this);
    }

    protected boolean onKeyActionUp(View v, int keyCode , KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                return onKeyUpPadUp(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_DOWN:
                return onKeyUpPadDown(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_LEFT:
                return onKeyUpPadLeft(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return onKeyUpPadRight(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                return onKeyUpPadEnter(v, keyCode, event);

            case KeyEvent.KEYCODE_BACK:
                return onKeyUpBack(v, keyCode, event);

            case KeyEvent.KEYCODE_MENU:
                return onKeyUpMenu(v, keyCode, event);
        }
        return false;
    }

    protected boolean onKeyActionDown(View v, int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                return onKeyDownPadUp(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_DOWN:
                return onKeyDownPadDown(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_LEFT:
                return onKeyDownPadLeft(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return onKeyDownPadRight(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                return onKeyDownPadEnter(v, keyCode, event);

            case KeyEvent.KEYCODE_BACK:
                return onKeyDownBack(v, keyCode, event);

            case KeyEvent.KEYCODE_MENU:
                return onKeyDownMenu(v, keyCode, event);
        }
        return false;
    }

    @Override
    public boolean onKeyDownPadUp(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyDownPadDown(View v, int keyCode, KeyEvent event){
        return defaultReturn();
    }

    @Override
    public boolean onKeyDownPadLeft(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyDownPadRight(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyDownPadEnter(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyDownBack(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyDownMenu(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyUpPadUp(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyUpPadDown(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyUpPadLeft(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyUpPadRight(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyUpPadEnter(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyUpBack(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyUpMenu(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    /***
     * when player get focus , default consume all on key event
     * @return
     */
    private boolean defaultReturn(){
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG,"onFocusChange......");
        if(hasFocus){
            mPlayer.setOnKeyListener(this);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (event.getAction()){
            case KeyEvent.ACTION_DOWN:
                return onKeyActionDown(v, keyCode, event);

            case KeyEvent.ACTION_UP:
                return onKeyActionUp(v, keyCode, event);
        }
        return false;
    }

    /**
     * listen the holder visible state change, and update state
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility == View.INVISIBLE || visibility == View.GONE){
            Log.d(TAG,"onWindowChanged_UNVISIBLE......");
            mHolderVisible = false;
        }else if(visibility == View.VISIBLE){
            Log.d(TAG,"onWindowChanged_VISIBLE......");
            mHolderVisible = true;
        }
    }

    public final boolean isHolderVisible() {
        return mHolderVisible;
    }

    @Override
    public void onPlayControllerShow() {

    }

    @Override
    public void onPlayControllerHidden() {

    }

    /**
     * player full screen , called by {@link BaseExtPlayer#fullScreen(View)} }
     */
    public void onPlayerFullScreen(){

    }

    public void onPlayerQuitFullScreen(){

    }

    /**
     * get current definition index
     * */
    protected int getCurrDefinitionIndex(){
        List<VideoRate> videoRates = getVideoRates();
        if(videoRates==null || (videoRates!=null && videoRates.size()==0))
            return -1;
        VideoRate currRate = getCurrVideoRate();
        if(currRate==null)
            return -1;
        int index = -1;
        int len = videoRates.size();
        for(int i=0;i<len;i++){
            if(currRate.getDefinition().equals(videoRates.get(i).getDefinition())){
                index = i;
                break;
            }
        }
        return index;
    }

    protected VideoRate getCurrVideoRate(){
        if(mPlayer!=null)
            return mPlayer.getCurrentDefinition();
        return null;
    }

    protected List<VideoRate> getVideoRates(){
        if(mPlayer!=null)
            return mPlayer.getVideoDefinitions();
        return null;
    }

    protected String getString(int resId){
        return getContext().getString(resId);
    }

    protected String[] getStringArray(int resId){
        return getContext().getResources().getStringArray(resId);
    }

    /***
     * player play event
     * @param eventCode
     */
    public abstract void onPlayerEvent(int eventCode);

    /***
     * player error event
     * @param eventCode
     */
    public abstract void onPlayerErrorEvent(int eventCode);

    /***
     * player progress time counter
     * @param curr
     * @param duration
     * @param bufferPercentage
     */
    public abstract void onPlayerTimeCounter(int curr, int duration, int bufferPercentage);

    public void destroy(){
        getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
    }

    @Override
    public void onUserPaused() {

    }

    @Override
    public void onUserResumed() {

    }

    @Override
    public void onSourceChanged(boolean isFrontVideo) {
        //
    }

    @Override
    public void onPlayerStop() {

    }

    @Override
    public void onPlayerRePlay() {

    }

    @Override
    public void onLoadingTimeCounter(int seconds) {

    }

    @Override
    public void onLoadingTimeout() {

    }

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

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {

    }
}
