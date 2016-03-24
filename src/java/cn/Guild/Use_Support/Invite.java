package cn.Guild.Use_Support;

import cn.Language;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by key_q on 2016/3/23.
 */
public class Invite {
    private Map<String,HashSet<String>> Invite_Box_Cache=new HashMap<>();
    private Cache cache;
    public Invite(Cache cache){
        this.cache=cache;
        cache.Create_Cache_Row("Invite_Box_Cache",Invite_Box_Cache);
    }

    public boolean create_invite_box(String player_name,Player player){
        if(!Invite_Box_Cache.containsKey(player_name)){
            Invite_Box_Cache.put(player_name,new HashSet<String>());
            return true;
        }else return false;
    }

    public boolean delete_invite_box(String player_name){
        if(Invite_Box_Cache.containsKey(player_name)){
            Invite_Box_Cache.remove(player_name);
            return true;
        }else return false;
    }

    public boolean add_invite(String player_name,String Guild_name){
        if(Invite_Box_Cache.containsKey(player_name)){
            Invite_Box_Cache.get(player_name).add(Guild_name);
            if(cache.hasOnline_People_Data(player_name))
                cache.SendMessage_To(player_name,Language.get_bar(Guild_name,"You_Have_Guild_Invite_For"));
                return true;
        }else return false;
    }
    public boolean check_invite(String player_name,String Guild_name){
        if(Invite_Box_Cache.containsKey(player_name)){
            boolean find = Invite_Box_Cache.get(player_name).contains(Guild_name);
            Invite_Box_Cache.get(player_name).remove(Guild_name);
            return find;
        }else return false;

    }
    public boolean clear_invite(String player_name){
        if(Invite_Box_Cache.containsKey(player_name)) {
            Invite_Box_Cache.get(player_name).clear();
            return true;
        }else return false;
    }

}
