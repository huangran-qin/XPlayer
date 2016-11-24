package com.xapp.jjh.xplayer.application;

import com.danikula.videocache.file.Md5FileNameGenerator;
import com.xapp.jjh.xui.application.XUIApplication;
import com.xapp.jjh.xui.config.XUIConfig;
import cn.ikan.libs.player.setting.HttpCacheSetting;

/**
 * Created by Taurus on 16/8/20.
 */
public class XPlayerApplication extends XUIApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        XUIConfig.setXUIRedStyle();
        initHttpCache();
    }

    private void initHttpCache() {
        HttpCacheSetting.initHttpCache(this,
                new HttpCacheSetting.CacheConfig()
                        .setCacheDir(getExternalCacheDir())
                        .setMaxFileCount(1000)
                        .setMaxCacheSize(2000*1024*1024)
                        .setFileNameGenerator(new Md5FileNameGenerator()));
    }
}
