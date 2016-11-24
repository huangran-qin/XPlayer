package cn.ikan.libs.player.inter;

import android.view.View;

import java.util.List;

import cn.ikan.libs.player.setting.BaseFrontVideo;

/**
 * Created by Taurus on 2016/9/29.
 */
public interface IExtPlayer {
    void startFrontVideos(List<? extends BaseFrontVideo> frontVideos, OnFrontVideoListener onFrontVideoListener);
    void passFrontVideos();
    View getFrontVideoCover();
}
