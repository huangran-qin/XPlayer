package cn.ikan.libs.player.setting;

import android.app.Application;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;

import java.io.File;

/**
 * Created by Taurus on 2016/11/24.
 */

public class HttpCacheSetting {

    private static HttpProxyCacheServer httpProxyCacheServer;

    public static void initHttpCache(Application application, CacheConfig cacheConfig){
        httpProxyCacheServer = new HttpProxyCacheServer
                .Builder(application.getApplicationContext())
                .cacheDirectory(cacheConfig.getCacheDir())
                .maxCacheFilesCount(cacheConfig.getMaxFileCount())
                .maxCacheSize(cacheConfig.getMaxCacheSize())
                .fileNameGenerator(cacheConfig.getFileNameGenerator())
                .build();
    }

    public static HttpProxyCacheServer getHttpProxyCacheServer(){
        return httpProxyCacheServer;
    }

    public static class CacheConfig{
        private File cacheDir;
        private int maxFileCount;
        private long maxCacheSize;
        private FileNameGenerator fileNameGenerator;

        public File getCacheDir() {
            return cacheDir;
        }

        public CacheConfig setCacheDir(File cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public int getMaxFileCount() {
            return maxFileCount;
        }

        public CacheConfig setMaxFileCount(int maxFileCount) {
            this.maxFileCount = maxFileCount;
            return this;
        }

        public long getMaxCacheSize() {
            return maxCacheSize;
        }

        public CacheConfig setMaxCacheSize(long maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        public FileNameGenerator getFileNameGenerator() {
            return fileNameGenerator;
        }

        public CacheConfig setFileNameGenerator(FileNameGenerator fileNameGenerator) {
            this.fileNameGenerator = fileNameGenerator;
            return this;
        }
    }
}
