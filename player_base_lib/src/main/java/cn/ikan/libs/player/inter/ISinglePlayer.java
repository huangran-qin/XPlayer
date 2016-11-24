package cn.ikan.libs.player.inter;

import java.util.List;

import cn.ikan.libs.player.setting.AspectRatio;
import cn.ikan.libs.player.setting.VideoRate;

/**
 * Created by Taurus on 2016/8/29.
 */
public interface ISinglePlayer {

    int STATUS_IDLE = 0;
    int STATUS_PLAYING = 1;
    int STATUS_PAUSE = 2;

    void setData(String... data);
    void start();
    void start(int msc);
    void pause();
    void resume();
    void seekTo(int msc);
    void stop();
    /** replay current data source*/
    void rePlay(int msc);
    boolean isPlaying();
    int getCurrentPosition();
    int getDuration();
    int getBufferPercentage();
    /** get current playing video definition*/
    VideoRate getCurrentDefinition();
    /** get current playing data source all definitions*/
    List<VideoRate> getVideoDefinitions();
    /** change playing video definition*/
    void changeVideoDefinition(VideoRate videoRate);
    /** Switch video fill type , such as 16:9 ,4:3 ,FILL_PARENT , ORIGINAL*/
    void setAspectRatio(AspectRatio aspectRatio);
    void destroy();
}
