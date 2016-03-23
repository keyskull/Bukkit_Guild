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
    public boolean isPVP(String Guild_name){
        if(All_Guild.containsKey(Guild_name)) return  All_Guild.get(Guild_name).getPVP();
        else return false;
    }
    public boolean setPVP(String Guild_name){
        if(All_Guild.containsKey(Guild_name)) return  All_Guild.get(Guild_name).Change_PVP();
        else return false;
    }


    public String getGuild_Name(String player_name){
        if(Cache.Online_People_Fast_Seach.containsKey(player_name.toLowerCase())){
            return Cache.Online_People_Fast_Seach.get(player_name.toLowerCase()).Guild_name;
        }else{
            for (Guild_Struct g: All_Guild.values()){
                if(g.getOwner().equals(player_name.toLowerCase()))return g.getGuild_Name();
                else {
                    for(String s: g.getVIP())if(s.equals(player_name.toLowerCase()))return g.getGuild_Name();
                    for(String s:g.getPeople())if(s.equals(player_name.toLowerCase()))return  g.getGuild_Name();
                }
            }
            return null;
        }
    }

    public boolean Remove_Guild(String owner){
        for(Guild_Struct g: All_Guild.values())
            if(g.getOwner().equals(owner.toLowerCase())){
                Guild_Setup.Get_Guidl_Yaml().set(g.getGuild_Name(), null);
                All_Guild.remove(g);
                Guild_Any_Owner.remove(owner.toLowerCase());
                Guild_Any_VIP.remove(owner.toLowerCase());
                Guild_Any_People.remove(owner.toLowerCase());
                for(String s :g.getVIP())Guild_Any_VIP.remove(s.toLowerCase());
                for(String s :g.getPeople())Guild_Any_People.remove(s.toLowerCase());
                return true;
            }
        return false;
    }

    public boolean Add_Guild(final String owner, final String Guild_name) {
        for(Guild_Struct s : All_Guild.values()) if(s.getGuild_Name().equals(Guild_name))return false;
        cache.setOnline_People_Data(owner.toLowerCase(),new Guild_Position_Struct(Guild_Position.Owner,Guild_name));
        All_Guild.put(Guild_name,new Guild_Struct(Guild_name,owner.toLowerCase(),new ArrayList<String>(),new ArrayList<String>()));
        Guild_Any_People.add(owner.toLowerCase());
        Guild_Any_VIP.add(owner.toLowerCase());
        Guild_Any_Owner.add(owner.toLowerCase());
        Guild_Setup.Get_Guidl_Yaml().set(Guild_name+".Owner",owner);
        Guild_Setup.Get_Guidl_Yaml().set(Guild_name+".Level",1);
        Guild_Setup.Get_Guidl_Yaml().set(Guild_name+".PVP",false);
        return true;
    }
    //Guild operation end//


    //People operation first//
    public boolean inGuild(String player_name){
        return Guild_Any_People.contains(player_name.toLowerCase());
    }
    public boolean isVIP(String player_name){
        return Guild_Any_VIP.contains(player_name.toLowerCase());
    }
    public boolean isOwner(String player_name){return Guild_Any_Owner.contains(player_name.toLowerCase());}
    protected Map<String,Guild_Position_Struct> Slow_Seach_People(String name){
        final String player_name=name.toLowerCase();
        for(final Guild_Struct g:  All_Guild.values())
            if(g.getOwner().equals(player_name))return new HashMap<String,Guild_Position_Struct>(){{put(player_name ,new Guild_Position_Struct(Guild_Position.Owner,g.getGuild_Name()));}};
            else if(g.getVIP().contains(player_name))return new HashMap<String,Guild_Position_Struct>(){{put(player_name ,new Guild_Position_Struct(Guild_Position.VIP,g.getGuild_Name()));}};
            else if(g.getPeople().contains(player_name))return new HashMap<String,Guild_Position_Struct>(){{put(player_name ,new Guild_Position_Struct(Guild_Position.People,g.getGuild_Name()));}};
        return null;
    }

    public boolean Add_People(String p_name,String Guild_Name){
        String player_name=p_name.toLowerCase();
        if(!this.inGuild(player_name)){
           if(All_Guild.containsKey(Guild_Name)){
               All_Guild.get(Guild_Name).addPeople(player_name.toLowerCase());
               cache.setOnline_People_Data(player_name,new Guild_Position_Struct(Guild_Position.People,player_name.toLowerCase()));
               Guild_Setup.Get_Guidl_Yaml().set(Guild_Name+".People",
                       Guild_Setup.Get_Guidl_Yaml().getStringList(Guild_Name+".People")
                               .add(player_name.toLowerCase()));
               return true;
           }else return false;
        }else return false;
    }

    public boolean People_Upgrade(String player_name){
        String player_names=player_name.toLowerCase();
        if(this.inGuild(player_names)) {
         String Guild_Name= this.getGuild_Name(player_names);
            if(!this.isVIP(player_names)) return  All_Guild.get(Guild_Name).getVIP().add(player_names);
            else return false;
        }else return false;
    }

    public boolean People_Downgrade(String player_name){
    return true;
    }

    public boolean Remove_People(String player_name){
        return Kick_People(player_name,getGuild_Name(player_name));
    }
    public boolean Kick_People(String p_name,String Guild_Name){
        String player_name=p_name.toLowerCase();
        if(!this.isOwner(player_name))
            for(Guild_Struct g:All_Guild.values())
                if(g.getPeople().contains(player_name)){
                    g.delPeople(player_name);
                    Guild_Any_People.remove(player_name.toLowerCase());
                    cache.setOnline_People_Data(player_name,null);
                    Guild_Setup.Get_Guidl_Yaml().set(Guild_Name+".People"+"."+player_name, null);
                    return true;
                }else if(g.getVIP().contains(player_name)){
                    g.delVIP(player_name);
                    Guild_Any_VIP.remove(player_name);
                    Guild_Any_People.remove(player_name);
                    cache.setOnline_People_Data(player_name,null);
                    Guild_Setup.Get_Guidl_Yaml().set(Guild_Name+".VIP"+"."+player_name , null);
                    return true;
                }else return false;
        return false;
    }
    //People operation end//

}
