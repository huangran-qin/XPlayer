package cn.ikan.libs.player.inter;

import cn.ikan.libs.player.setting.BaseFrontVideo;

/**
 * Created by Taurus on 2016/9/29.
 */
public interface OnFrontVideoListener<T extends BaseFrontVideo> {
    void onVideoComplete(T frontVideo, boolean isAllComplete);
    void onFrontVideoClick(T frontVideo);
}
