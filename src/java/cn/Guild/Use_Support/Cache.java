package cn.Guild.Use_Support;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by key_q on 2016/3/23.
 */
public class Cache {
    private Map<String,Guild_Position_Struct> Online_People_Fast_Seach=new HashMap<>();
    private Map<String,Map> Cache=new HashMap<>();
    private Map<String,Player> Return_Channel_Cache=new HashMap<>();


    protected Boolean Create_Cache_Row(String name,Map data){
        if(this.Cache.containsKey(name))return false;
        else this.Cache.put(name,data);
        return true;
    }
    protected Boolean Clean_Cache_Row(String name){
       this.Cache.put(name,null);
        return true;
    }
    protected Boolean set_Cache_Clean_time(int time) {
        return true;
    }
    protected Map Get_Cache_Row(String name) {
        return this.Cache.get(name);
    }


    // online cache first//
    public boolean isOnline(String player_name){
        return true;
    }

    public boolean SendMessage_To(String player_name, String message){
        if(Return_Channel_Cache.containsKey(player_name.toLowerCase())){
            Return_Channel_Cache.get(player_name.toLowerCase()).sendMessage(message);
            return true;
        }
        else return false;
    }

    public boolean hasOnline_People_Data(String player_name){
        return Online_People_Fast_Seach.containsKey(player_name.toLowerCase());
    }

    public Guild_Position_Struct getOnline_People_Data(String player_name){
        if(Online_People_Fast_Seach.containsKey(player_name.toLowerCase()))
            return Online_People_Fast_Seach.get(player_name.toLowerCase());
        else return null;
    }

    public Boolean addOnline_People_Data(Map<String,Guild_Position_Struct> online_player_data,Player player){
        Return_Channel_Cache.put(player.getName().toLowerCase(),player);
        Online_People_Fast_Seach.putAll(online_player_data);
        return true;
    }
    public Boolean setOnline_People_Data(String player_name,Guild_Position_Struct gps){
        if(Online_People_Fast_Seach.containsKey(player_name.toLowerCase())){
            Online_People_Fast_Seach.put(player_name.toLowerCase(),gps);
            return true;
        }else return false;
    }

    public Boolean delOnline_People_Data(String player_name){
        if(!Online_People_Fast_Seach.containsKey(player_name.toLowerCase()))return false;
        else{
            Online_People_Fast_Seach.remove(player_name.toLowerCase());
            if(Return_Channel_Cache.containsKey(player_name.toLowerCase()))
                Return_Channel_Cache.remove(player_name.toLowerCase());
        }
        return true;
    }
    // online cache end//
}
