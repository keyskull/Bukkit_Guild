package cn.Guild.Use_Support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by key_q on 2016/3/23.
 */
public class Invite {
    private Map<String,HashSet<String>> Invite_Box=new HashMap<>();

    public boolean create_invite_box(String player_name){
        if(Invite_Box.containsKey(player_name)){
            Invite_Box.put(player_name,new HashSet<String>());
            return true;
        }else return false;
    }

    public boolean delete_invite_box(String player_name){
        if(Invite_Box.containsKey(player_name)){
            Invite_Box.remove(player_name);
            return true;
        }else return false;
    }

    public boolean add_invite(String player_name,String Guild_name){
        if(Invite_Box.containsKey(player_name)){
            Invite_Box.get(player_name).add(Guild_name);
            return true;
        }else return false;
    }
    public boolean check_invite(String player_name,String Guild_name){
        if(Invite_Box.containsKey(player_name)){
            return Invite_Box.get(player_name).contains(Guild_name);
        }else return false;

    }
    public boolean clear_invite(String player_name){
        if(Invite_Box.containsKey(player_name)) {
            Invite_Box.get(player_name).clear();
            return true;
        }else return false;
    }

}
