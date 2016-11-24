package com.xapp.jjh.base_ijk.config;

import android.content.Context;
import java.lang.reflect.Constructor;
import cn.ikan.libs.player.setting.PlayerType;

/**
 * Created by Taurus on 2016/11/16.
 */

public class ConfigLoader {

    public static Object getPlayerInstance(Context context, int playerType){
        Object instance = null;
        try{
            Class clz = null;
            switch (playerType){
                case PlayerType.PLAYER_TYPE_BF:
                    clz = getSDKClass(ClassProperty.CLASS_PATH_PLAYER_BF);
                    break;
                case PlayerType.PLAYER_TYPE_IJK:
                    clz = getSDKClass(ClassProperty.CLASS_PATH_PLAYER_IJK);
                    break;
            }
            if(clz!=null){
                Constructor constructor = getConstructor(clz);
                if(constructor!=null){
                    instance = constructor.newInstance(context);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return instance;
    }

    private static Constructor getConstructor(Class clz){
        Constructor result = null;
        try{
            result = clz.getConstructor(Context.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static Class getSDKClass(String classPath){
        Class result = null;
        try {
            result = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
