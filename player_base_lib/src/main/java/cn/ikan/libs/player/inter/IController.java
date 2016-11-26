package cn.ikan.libs.player.inter;

/**
 * Created by Taurus on 2016/8/29.
 */
public interface IController {
    /** Set whether to allow gestures to operate*/
    void setGestureEnable(boolean enable);
    /** Sets whether to allow gestures to control volume operation*/
    void setVolumeGestureEnable(boolean enable);
    /** Sets whether to allow gestures to control brightness operation*/
    void setBrightGestureEnable(boolean enable);
    /** Sets whether to allow the progress bar to gesture*/
    void setSeekGestureEnable(boolean enable);
    /** Set play controller is available*/
    void setControllerEnable(boolean enable);
    void setErrorState(boolean state);
    void setTopStatusBarEnable(boolean enable);
    void setTapEnable(boolean enable);
    /** Set play controller status, display or hide*/
    void setPlayControlState(boolean state);
    boolean isPlayControlShow();
    void setTopStatusBarState(boolean state);
    boolean isTopStatusBarShow();
    void setSystemTime();
    /** Set the status of the loaded prompt to display or hide*/
    void setLoadingState(boolean state);
    /** Set the control button state*/
    void setPlayState(boolean isPlaying);
    /** Set play time*/
    void setPlayTime(long curr, long total);
    void setSeekMax(int max);
    void setSeekProgress(int progress);
    void setSeekSecondProgress(int progress);
    int getSeekProgress();
    void resetSeekBar();
    void resetPlayTime();
    void leftVerticalSlide(float percent);
    void rightVerticalSlide(float percent);
    void horizontalSlide(float percent);
    void onGestureDoubleTap();
    void onGestureSingleTapUp();
}
