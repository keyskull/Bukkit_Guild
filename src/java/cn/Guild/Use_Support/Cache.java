package cn.Guild.Use_Support;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by key_q on 2016/3/23.
 */
public class Cache {
    public static Map<String,Guild_Position_Struct> Online_People_Fast_Seach=new HashMap<>();
    public static Map<String,Map> Cache=new HashMap<>();

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
    public Boolean addOnline_People_Data(Map<String,Guild_Position_Struct> online_player_data){
        if(online_player_data ==null)return false;
        Online_People_Fast_Seach.putAll(online_player_data);
        return true;
    }
    public Boolean setOnline_People_Data(String player_name,Guild_Position_Struct gps){
        if(Online_People_Fast_Seach.containsKey(player_name)){
            Online_People_Fast_Seach.put(player_name,gps);
            return true;
        }else return false;
    }
    public Boolean delOnline_People_Data(String player_name){
        if(!Online_People_Fast_Seach.containsKey(player_name))return false;
        else Online_People_Fast_Seach.remove(player_name);
        return true;
    }
    // online cache end//
}
