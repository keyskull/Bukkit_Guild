package cn.Guild;


import java.util.*;

/**
 * Created by key_q on 2016/3/23.
 */
public class Guild {
    public Map<String,Guild_Struct> All_Guild=new HashMap<>();
    public Set<String> Guild_Any_People=new HashSet<>();
    public Set<String> Guild_Any_VIP=new HashSet<>();
    public Set<String> Guild_Any_Owner=new HashSet<>();
    private Cache cache;

    public Guild(Cache cache){
        this.cache=cache;
    }

    //Guild operation first//

    public Map<String,Guild_Struct> Get_Guild_Data(){
        return All_Guild;
    }

    public Boolean HasGuild(String Guild_Name){return All_Guild.containsKey(Guild_Name);}

    public String getGuild_Name(String player_name){
        if(Cache.Online_People_Fast_Seach.containsKey(player_name)){
            return Cache.Online_People_Fast_Seach.get(player_name).Guild_name;
        }else{
            for (Guild_Struct g: All_Guild.values()){
                if(g.getOwner().equals(player_name))return g.getOwner();
                else {
                    for(String s: g.getVIP())if(s.equals(player_name))return s;
                    for(String s:g.getPeople())if(s.equals(player_name))return s;
                }
            }
            return null;
        }
    }

    public boolean Remove_Guild(String owner){
        for(Guild_Struct g: All_Guild.values())
            if(g.getOwner().equals(owner)){
                Guild_Setup.Get_Guidl_Yaml().set(g.getGuild_Name(), null);
                All_Guild.remove(g);
                Guild_Any_Owner.remove(owner);
                Guild_Any_VIP.remove(owner);
                Guild_Any_People.remove(owner);
                for(String s :g.getVIP())Guild_Any_VIP.remove(s);
                for(String s :g.getPeople())Guild_Any_People.remove(s);
                return true;
            }
        return false;
    }

    public  boolean Add_Guild(final String owner, final String Guild_name) {
        for(Guild_Struct s : All_Guild.values()) if(s.getGuild_Name().equals(Guild_name))return false;
        cache.setOnline_People_Data(owner,new Guild_Position_Struct(Guild_Position.Owner,Guild_name));
        All_Guild.put(Guild_name,new Guild_Struct(Guild_name,owner,new ArrayList<String>(),new ArrayList<String>()));
        Guild_Any_People.add(owner);
        Guild_Any_VIP.add(owner);
        Guild_Any_Owner.add(owner);
        Guild_Setup.Get_Guidl_Yaml().set(Guild_name+".Owner",owner);
        return true;
    }
    //Guild operation end//


    //People operation first//

    public boolean inGuild(String player_name){
        return Guild_Any_People.contains(player_name);
    }
    public boolean isVIP(String player_name){
        return Guild_Any_VIP.contains(player_name);
    }
    public boolean isOwner(String player_name){
        return Guild_Any_Owner.contains(player_name);
    }

    protected Map<String,Guild_Position_Struct> Slow_Seach_People(final String play_name){
        for(final Guild_Struct g:  All_Guild.values())
            if(g.getOwner().equals(play_name))return new HashMap<String,Guild_Position_Struct>(){{put(play_name ,new Guild_Position_Struct(Guild_Position.Owner,g.getGuild_Name()));}};
            else if(g.getVIP().contains(play_name))return new HashMap<String,Guild_Position_Struct>(){{put(play_name ,new Guild_Position_Struct(Guild_Position.VIP,g.getGuild_Name()));}};
            else if(g.getPeople().contains(play_name))return new HashMap<String,Guild_Position_Struct>(){{put(play_name ,new Guild_Position_Struct(Guild_Position.People,g.getGuild_Name()));}};
        return null;
    }

    public boolean Add_People(String play_name,String Guild_Name){
        if(!this.inGuild(play_name)){
           if(All_Guild.containsKey(Guild_Name)){
               All_Guild.get(Guild_Name).addPeople(play_name);
               cache.setOnline_People_Data(play_name,new Guild_Position_Struct(Guild_Position.People,play_name));
               return true;
           }else return false;
        }else return false;
    }

    public boolean People_Upgrade(String play_name,String Guild_Name){
        if(this.inGuild(play_name)) {
            if (!this.isVIP(play_name)) {

            }
        }
            return true;
    }

    public boolean People_Downgrade(String play_name,String Guild_Name){
    return true;
    }

    public boolean Remove_People(String play_name){
        return Kick_People(play_name,getGuild_Name(play_name));
    }
    public boolean Kick_People(String play_name,String Guild_Name){
        if(!this.isOwner(play_name))
            for(Guild_Struct g:All_Guild.values())
                if(g.getPeople().contains(play_name)){
                    g.delPeople(play_name);
                    Guild_Any_People.remove(play_name);
                    cache.setOnline_People_Data(play_name,null);
                    Guild_Setup.Get_Guidl_Yaml().set(Guild_Name+".People"+"."+play_name , null);
                    return true;
                }else if(g.getVIP().contains(play_name)){
                    g.delVIP(play_name);
                    Guild_Any_VIP.remove(play_name);
                    Guild_Any_People.remove(play_name);
                    cache.setOnline_People_Data(play_name,null);
                    Guild_Setup.Get_Guidl_Yaml().set(Guild_Name+".VIP"+"."+play_name , null);
                    return true;
                }else return false;
        return false;
    }
    //People operation end//

}
