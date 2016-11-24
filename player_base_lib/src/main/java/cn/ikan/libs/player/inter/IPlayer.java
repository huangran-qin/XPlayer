package cn.ikan.libs.player.inter;

import android.content.res.Configuration;

/**
 * Created by Taurus on 2016/8/29.
 */
public interface IPlayer extends ISinglePlayer{
    void doConfigChange(Configuration newConfig);
    void toggleFullScreen();
    boolean isFullScreen();
}
