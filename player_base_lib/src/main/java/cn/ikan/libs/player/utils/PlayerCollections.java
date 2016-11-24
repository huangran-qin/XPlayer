package cn.ikan.libs.player.utils;

import java.util.ArrayList;
import java.util.List;

import cn.ikan.libs.player.widget.BasePlayer;

/**
 * Created by Taurus on 2016/11/15.
 */

public class PlayerCollections {

    private static List<BasePlayer> players;

    public static void putPlayer(BasePlayer player){
        if(player==null) return;
        if(players==null){
            players = new ArrayList<>();
        }
        players.add(player);
    }

    public static void removePlayer(BasePlayer player){
        if(player!=null && players!=null){
            players.remove(player);
        }
    }

    public static void destroyPlayers(){
        if(players!=null){
            for(BasePlayer player:players){
                player.destroy();
            }
            players.clear();
        }
    }
}
