package cn.Guild.Use_Support;


import cn.Guild.Guild_Setup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by key_q on 2016/3/23.
 */
public class Contribution {
    private Map<String,Integer> People_Sponsor_cache=new HashMap<>();


    private int Get_Contribution_info(String player_name,String Guild_name,String guild_position){
        People_Sponsor_cache.put(player_name,
                Guild_Setup.Get_Guidl_Yaml(Guild_name).getInt(Guild_name+"."+guild_position+"."+player_name+".contribution"));
        return People_Sponsor_cache.get(player_name);
    }

    private int Set_Contribution_info(String player_name,String Guild_name,String guild_position,int pay){
        Guild_Setup.Get_Guidl_Yaml(Guild_name).set((Guild_name+"."+guild_position+"."+player_name+".contribution"),pay);
        return Get_Contribution_info(player_name,guild_position,Guild_name);
    }

    //Contribution operation start//
    public int Get_Contribution_info(String play_name){
        Guild_Position_Struct gps=Cache.Online_People_Fast_Seach.get(play_name);
        return Get_Contribution_info(play_name,gps.Guild_name,gps.Position.name());
    }
    public int Increase_Contribution(String play_name,int pay){
        Guild_Position_Struct gps=Cache.Online_People_Fast_Seach.get(play_name);
        return Set_Contribution_info(play_name,gps.Guild_name,gps.Position.name(),
                Get_Contribution_info(play_name,gps.Guild_name,gps.Position.name())+pay);
    }
    //Contribution operation end//

}




