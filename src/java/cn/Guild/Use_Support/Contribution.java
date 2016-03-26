package cn.Guild.Use_Support;


import cn.Guild.Guild_Setup;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by key_q on 2016/3/23.
 */
public class Contribution {
    private static Map<String,Integer> People_Sponsor_cache=new HashMap<>();
    private static Map<String,Integer> Guild_Contribution_cache=new HashMap<>();
    private static Cache cache=null;
    public Contribution(Cache cache){
        this.cache=cache;
        cache.Create_Cache_Row("People_Sponsor_cache",People_Sponsor_cache);
        cache.Create_Cache_Row("People_Sponsor_cache",Guild_Contribution_cache);
    }

    protected static int Get_Contribution_info(String player_name,String Guild_name,String guild_position){
        if(!People_Sponsor_cache.containsKey(player_name))
            People_Sponsor_cache.put(player_name,
                Guild_Setup.Get_Guild_Yaml(Guild_name).getInt(Guild_name+"."+guild_position+"."+player_name));
        return People_Sponsor_cache.get(player_name);
    }

    protected static int Set_Contribution_info(String player_name,String Guild_name,String guild_position,int pay){
        Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+"."+guild_position+"."+player_name,pay);
        People_Sponsor_cache.put(player_name,pay);
        return Get_Contribution_info(player_name,guild_position,Guild_name);
    }

    public boolean Delete_Old_Owner_Contribution(String player_name){
        Guild_Position_Struct gps=cache.getOnline_People_Data(player_name.toLowerCase());
        if(gps.Position.equals("Owner")){
            Guild_Setup.Get_Guild_Yaml(gps.Guild_name).set(gps.Guild_name+".Owner."+player_name.toLowerCase(),null);
            return true;
        }else return false;
    }

    //Contribution operation start//
    public static int Get_Contribution_info(String player_name){//need Modify
        Guild_Position_Struct gps=cache.getOnline_People_Data(player_name);
        return Get_Contribution_info(player_name,gps.Guild_name,gps.Position.name());
    }
    public int Increase_Contribution(String player_name,int pay){//need Modify
        Guild_Position_Struct gps=cache.getOnline_People_Data(player_name);
        return Set_Contribution_info(player_name,gps.Guild_name,gps.Position.name(),
                Get_Contribution_info(player_name,gps.Guild_name,gps.Position.name())+pay);
    }

    public int Check_Guild_Contribution(String Guild_name){
        Set<String> VIP,People,Owner;
        YamlConfiguration Guild_Yaml=Guild_Setup.Get_Guild_Yaml(Guild_name);
        int Count=0;
        if(Guild_Contribution_cache.containsKey(Guild_name))
            return Guild_Contribution_cache.get(Guild_name);
        else {
            ConfigurationSection vip_config=Guild_Yaml .getConfigurationSection(Guild_name + ".VIP");
            if(vip_config != null) VIP = vip_config.getKeys(false);
            else VIP=new HashSet<>();
            ConfigurationSection people_config= Guild_Yaml.getConfigurationSection(Guild_name + ".People");
            if(people_config != null) People = people_config.getKeys(false);
            else People=new HashSet<>();
            ConfigurationSection owner_config= Guild_Yaml.getConfigurationSection(Guild_name + ".Owner");
            if(owner_config != null) Owner = owner_config.getKeys(false);
            else Owner=new HashSet<>();
            for(String s:People)Count+= Guild_Yaml.getInt(Guild_name + ".People."+s);
            for(String s:VIP)Count+= Guild_Yaml.getInt(Guild_name + ".VIP."+s);
            for(String s:Owner)Count+= Guild_Yaml.getInt(Guild_name + ".Owner."+s);
            Guild_Contribution_cache.put(Guild_name,Count/10);
            return Guild_Contribution_cache.get(Guild_name);
        }
    }

    //Contribution operation end//

}




