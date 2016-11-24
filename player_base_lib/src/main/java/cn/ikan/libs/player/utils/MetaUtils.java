package cn.ikan.libs.player.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by Taurus on 2016/8/25.
 */
public class MetaUtils {
    public static String getMetaValue(Context context, String metaKey) {
        if (context == null || metaKey == null) {
            return "";
        }
        try {
            ApplicationInfo aiApplicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);

            if (null != aiApplicationInfo) {
                if (null != aiApplicationInfo.metaData) {
                    Object value = aiApplicationInfo.metaData.get(metaKey);
                    if(value!=null){
                        return value.toString();
                    }else{
                        return "";
                    }
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
