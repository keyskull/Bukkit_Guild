package cn.Guild.Use_Support;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by key_q on 2016/3/24.
 */
public class Guild_Skull {
    public Map<String,Map<String,Integer>> skull_cache=new HashMap<>();
    public Guild_Skull(Cache cache){
        cache.Create_Cache_Row("Guild_Skull",skull_cache);
    }
    /*
    public Map<String,Integer> Get_Skull(String Guild_name){
        if(skull_cache.containsKey(Guild_name)){

        }else {
            Map<String,Integer> map=new HashMap();
            for(Guild_Setup.Get_Guidl_Yaml().getStringList(Guild_name+".Skull"));
          skull_cache.put(Guild_name,map);

        }

    }*/

}
