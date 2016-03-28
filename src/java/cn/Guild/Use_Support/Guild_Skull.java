package cn.Guild.Use_Support;

import cn.Guild.Guild_Setup;
import org.bukkit.entity.Player;

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

    public Map<String,Integer> Hook_Guild_Skull(Player player, String Guild_name){
        if(skull_cache.containsKey(Guild_name)){


        }else {
            Map<String,Integer> map=new HashMap<>();
            for(String s:Guild_Setup.Get_Guild_Yaml(Guild_name).getStringList(Guild_name+".Skull")){

            }
            skull_cache.put(Guild_name,map);
        }
    return null;
    }
    public void Heath_Bonus(Player player,String Guild_name){

    }
    public void Damage_Bonus(Player player,String Guild_name){

    }
    public void Electric_Shock_Probability(Player player,String Guild_name){

    }

    public void Defense_Bonus(Player player,String Guild_name){

    }

}
