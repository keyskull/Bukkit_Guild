package cn.Guild.Use_Support;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Guild_Territory {
    public Map<String,Set<Index>> Territory_cache=new HashMap<>();

    public Guild_Territory(Cache cache){
        cache.Create_Cache_Row("Territory_cache",Territory_cache);
    }

    public boolean Create_Territory(String Guild_name,Location e){

        return true;
    }

}
