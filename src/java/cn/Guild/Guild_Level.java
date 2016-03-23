package cn.Guild;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by key_q on 2016/3/24.
 */
public class Guild_Level {
    private Map<String,Integer> Guild_Level_Cache= new HashMap<>();

    public Guild_Level(Cache cache){
        cache.Create_Cache_Row("Guild_Level_Cache",Guild_Level_Cache);
        cache.set_Cache_Clean_time(5000);
    }

    public int get_Guild_Level(String Guild_name){
        if(Guild_Level_Cache.containsKey(Guild_name)) return Guild_Level_Cache.get(Guild_name);
        int value=Guild_Setup.Get_Guidl_Yaml().getInt(Guild_name+".Level");
        Guild_Level_Cache.put(Guild_name,value);
        return value;
    }

    public boolean Up_Guild_Level(String Guild_name){
        if(Guild_Level_Cache.containsKey(Guild_name)){
            Guild_Level_Cache.put(Guild_name,Guild_Level_Cache.get(Guild_name)+1);
            Guild_Setup.Get_Guidl_Yaml().set(Guild_name+".Level",Guild_Level_Cache.get(Guild_name));
        }else {
            Guild_Setup.Get_Guidl_Yaml().set(Guild_name+".Level",Guild_Setup.Get_Guidl_Yaml().getInt(Guild_name+".Level")+1);
        }
        return true;
    }


}
