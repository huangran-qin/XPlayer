package cn.ikan.libs.player.setting;

import java.io.Serializable;

/**
 * Created by Taurus on 2016/9/29.
 */
public class BaseFrontVideo implements Serializable {
    private String videoUrl;
    private VideoRate videoRate;

    public BaseFrontVideo() {
    }

    public BaseFrontVideo(String videoUrl, VideoRate videoRate) {
        this.videoUrl = videoUrl;
        this.videoRate = videoRate;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public VideoRate getVideoRate() {
        return videoRate;
    }

    public void setVideoRate(VideoRate videoRate) {
        this.videoRate = videoRate;
    }
}
