package com.xapp.jjh.base_ijk.config;

/**
 * Created by Taurus on 2016/11/17.
 */

public class DebugPrintHelper {
    public static String getPlayerDebugStr(int playerType){
        if(playerType == 1)
            return "--IJK";
        return "-other";
    }
}
