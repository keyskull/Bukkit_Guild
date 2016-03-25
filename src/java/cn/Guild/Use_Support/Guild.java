package cn.Guild.Use_Support;


import cn.Guild.Guild_Setup;
import cn.Guild.Guild_Struct;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.*;

/**
 * Created by key_q on 2016/3/23.
 *
 * 一开始写蠢左,日后扩展才将这页框架改成依赖注入
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

    public boolean HasGuild(String Guild_Name){return All_Guild.containsKey(Guild_Name);}

    public boolean isPVP(String Guild_name){
        if(All_Guild.containsKey(Guild_name)) return  All_Guild.get(Guild_name).getPVP();
        else return false;
    }
    public boolean setPVP(String Guild_name){
        if(All_Guild.containsKey(Guild_name)){
            boolean pvp =All_Guild.get(Guild_name).Change_PVP();
             Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".PVP",pvp);
            return pvp;
        }
        else return false;
    }



    public String getGuild_Name(String player_name){
        if(cache.hasOnline_People_Data(player_name.toLowerCase())){
            return cache.getOnline_People_Data(player_name.toLowerCase()).Guild_name;
        }else{
            for (Guild_Struct g: All_Guild.values())
                if(g.getOwner().equals(player_name.toLowerCase()))return g.getGuild_Name();
                else if(g.getVIP().contains(player_name.toLowerCase()))return g.getGuild_Name();
                else if(g.getPeople().contains(player_name.toLowerCase()))return  g.getGuild_Name();
            return null;
            }
        }

    public boolean Remove_Guild(String owner){
        Guild_Setup.Get_Guild_Yaml(getGuild_Name(owner)).set(getGuild_Name(owner), null);
        Guild_Any_Owner.remove(owner.toLowerCase());
        Guild_Any_VIP.remove(owner.toLowerCase());
        Guild_Any_People.remove(owner.toLowerCase());
        for(String s :All_Guild.get(getGuild_Name(owner)).getVIP())Guild_Any_VIP.remove(s.toLowerCase());
        for(String s :All_Guild.get(getGuild_Name(owner)).getPeople())Guild_Any_People.remove(s.toLowerCase());
        All_Guild.remove(getGuild_Name(owner));
        return true;
    }

    public boolean Add_Guild(final String owner, final String Guild_name) {
        if(HasGuild(Guild_name))return false;
        cache.setOnline_People_Data(owner.toLowerCase(),new Guild_Position_Struct(Guild_Position.Owner,Guild_name));
        All_Guild.put(Guild_name,new Guild_Struct(Guild_name,owner.toLowerCase(),new HashSet<String>(),new HashSet<String>()));
        Guild_Any_People.add(owner.toLowerCase());
        Guild_Any_VIP.add(owner.toLowerCase());
        Guild_Any_Owner.add(owner.toLowerCase());
        Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".Owner",owner);
        Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".Level",1);
        Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".PVP",false);
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

    public Map<String,Guild_Position_Struct> Slow_Seach_People(String name){
        final String player_name=name.toLowerCase();
        for(final Guild_Struct g:  All_Guild.values())
            if(g.getOwner().equals(player_name))return new HashMap<String,Guild_Position_Struct>(){{put(player_name ,new Guild_Position_Struct(Guild_Position.Owner,g.getGuild_Name()));}};
            else if(g.getVIP().contains(player_name))return new HashMap<String,Guild_Position_Struct>(){{put(player_name ,new Guild_Position_Struct(Guild_Position.VIP,g.getGuild_Name()));}};
            else if(g.getPeople().contains(player_name))return new HashMap<String,Guild_Position_Struct>(){{put(player_name ,new Guild_Position_Struct(Guild_Position.People,g.getGuild_Name()));}};
        return new HashMap<String,Guild_Position_Struct>(){{put(player_name ,null);}};
    }

    public boolean Change_Owner(String Owner,String Target_name){
        final String owner=Owner.toLowerCase(),target_name=Target_name.toLowerCase();
        String Guild_name= this.getGuild_Name(owner);
        YamlConfiguration Guild_Yaml=Guild_Setup.Get_Guild_Yaml(Guild_name);
        Guild_Struct guild_struct=All_Guild.get(getGuild_Name(owner));
        if(guild_struct.isPeople(target_name)){
            guild_struct.delPeople(target_name);
            guild_struct.Change_Owner(target_name);
            guild_struct.addPeople(Owner);

            //cache
            Guild_Any_Owner.add(target_name);
            Guild_Any_VIP.add(target_name);
            Guild_Any_Owner.remove(Owner);
            Guild_Any_VIP.remove(Owner);
            cache.setOnline_People_Data(Owner,new Guild_Position_Struct(Guild_Position.People,Guild_name));
            cache.setOnline_People_Data(Target_name,new Guild_Position_Struct(Guild_Position.Owner,Guild_name));

            //file
            ConfigurationSection config = Guild_Yaml.getConfigurationSection(Guild_name + ".People");
            Map<String,Object> map =config ==null ? new HashMap<String,Object>() : config.getValues(false);
            map.remove(target_name);
            map.put(Owner,Contribution.Get_Contribution_info(Owner));
            Guild_Yaml.set(Guild_name+".People",map);
            Guild_Yaml.set(Guild_name+".Owner",new HashMap<String,Integer>(){{put(target_name,Contribution.Get_Contribution_info(target_name));}});

        }else if(guild_struct.isVIP(target_name)){
            guild_struct.delVIP(target_name);
            guild_struct.Change_Owner(target_name);
            guild_struct.addVIP(Owner);

            //cache
            Guild_Any_Owner.add(target_name);
            Guild_Any_Owner.remove(Owner);
            cache.setOnline_People_Data(Owner,new Guild_Position_Struct(Guild_Position.VIP,Guild_name));
            cache.setOnline_People_Data(Target_name,new Guild_Position_Struct(Guild_Position.Owner,Guild_name));

            //file
            ConfigurationSection config = Guild_Yaml.getConfigurationSection(Guild_name + ".VIP");
            Map<String,Object> map =config ==null ? new HashMap<String,Object>() : config.getValues(false);
            map.remove(target_name);
            map.put(Owner,Contribution.Get_Contribution_info(Owner));
            Guild_Yaml.set(Guild_name+".VIP",map);
            Guild_Yaml.set(Guild_name+".Owner",new HashMap<String,Integer>(){{put(target_name,Contribution.Get_Contribution_info(target_name));}});
        }else return false;
        return true;
    }

    public boolean People_Upgrade(String Player_name){//wrong
        String player_name=Player_name.toLowerCase();
        if(this.inGuild(player_name)) {
         String Guild_name= this.getGuild_Name(player_name);
            if(!this.isVIP(player_name) && !this.isOwner(player_name)) {
               boolean find= All_Guild.get(Guild_name).addVIP(player_name);
                All_Guild.get(Guild_name).delPeople(player_name);
                Guild_Any_VIP.add(player_name);
                ConfigurationSection config = Guild_Setup.Get_Guild_Yaml(Guild_name).getConfigurationSection(Guild_name + ".VIP");
                Map<String,Object> map =config ==null ? new HashMap<String,Object>() : config.getValues(false);
                map.put(player_name,0);
                Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".VIP",map);
                config = Guild_Setup.Get_Guild_Yaml(Guild_name).getConfigurationSection(Guild_name + ".People");
                map =config ==null ? new HashMap<String,Object>() : config.getValues(false);
                map.remove(player_name);
                Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".People", map);
                return  find;
            }
            else return false;
        }else return false;
    }

    public boolean People_Downgrade(String Player_name){//wrong
        String player_name=Player_name.toLowerCase();
        if(this.inGuild(player_name)){
            String Guild_name= this.getGuild_Name(player_name);
            if(this.isVIP(player_name) && !this.isOwner(player_name)){
                boolean find= All_Guild.get(Guild_name).delVIP(player_name);
                All_Guild.get(Guild_name).addPeople(player_name);
                Guild_Any_VIP.remove(player_name);
                ConfigurationSection config = Guild_Setup.Get_Guild_Yaml(Guild_name).getConfigurationSection(Guild_name + ".People");
                Map<String,Object> map =config ==null ? new HashMap<String,Object>() : config.getValues(false);
                map.put(player_name,0);
                Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".People",map);
                config = Guild_Setup.Get_Guild_Yaml(Guild_name).getConfigurationSection(Guild_name + ".VIP");
                map =config ==null ? new HashMap<String,Object>() : config.getValues(false);
                map.remove(player_name);
                Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".VIP", map);
                return  find;
            }else return false;
        }else
            return false;
    }

    public boolean Add_People(String p_name,String Guild_name){
        String player_name=p_name.toLowerCase();
        if(!this.inGuild(player_name)){
            if(All_Guild.containsKey(Guild_name)){
                All_Guild.get(Guild_name).addPeople(player_name.toLowerCase());
                cache.setOnline_People_Data(player_name,new Guild_Position_Struct(Guild_Position.People,Guild_name));
                Guild_Any_People.add(player_name);
                ConfigurationSection config = Guild_Setup.Get_Guild_Yaml(Guild_name).getConfigurationSection(Guild_name + ".People");
                Map<String,Object> list = config ==null ? new HashMap<String,Object>() : config.getValues(false);
                list.put(player_name,0);
                Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".People",list);
                return true;
            }else return false;
        }else return false;
    }

    public boolean Remove_People(String Player_name){return Kick_People(Player_name,getGuild_Name(Player_name.toLowerCase()));}

    public boolean Kick_People(String Player_name,String Guild_name){
        String player_name=Player_name.toLowerCase();
        if(!this.isOwner(player_name))
            for(Guild_Struct g:All_Guild.values())
                if(g.getPeople().contains(player_name)){
                    g.delPeople(player_name);
                    Guild_Any_People.remove(player_name.toLowerCase());
                    cache.setOnline_People_Data(player_name,null);
                    ConfigurationSection config = Guild_Setup.Get_Guild_Yaml(Guild_name).getConfigurationSection(Guild_name + ".People");
                    Map<String,Object> map =config ==null ? new HashMap<String,Object>() : config.getValues(false);
                    map.remove(player_name);
                    Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".People", map);
                    return true;
                }else if(g.getVIP().contains(player_name)){
                    g.delVIP(player_name);
                    Guild_Any_VIP.remove(player_name);
                    Guild_Any_People.remove(player_name);
                    cache.setOnline_People_Data(player_name,null);
                    ConfigurationSection config = Guild_Setup.Get_Guild_Yaml(Guild_name).getConfigurationSection(Guild_name + ".VIP");
                    Map<String,Object> map =config ==null ? new HashMap<String,Object>() : config.getValues(false);
                    map.remove(player_name);
                    Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".VIP", map);
                    return true;
                }else return false;
        return false;
    }
    //People operation end//

}
