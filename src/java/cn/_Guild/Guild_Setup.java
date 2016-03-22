package cn._Guild;


import cn.Guild;
import cn.Language;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by key_q on 2016/3/22.
 */
public class Guild_Setup {
    public static Logger log = null;
    public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
    private static Map<String,String[]> Online_People_Fast_Seach=new HashMap<>();
    private List<Guild_Struct> All_Guild=new ArrayList<>();
    private Set<String> Guild_Any_People=new HashSet<>();
    private Set<String> Guild_Any_VIP=new HashSet<>();
    private Set<String> Guild_Any_Owner=new HashSet<>();
    private Guild plugin=null;
    private YamlConfiguration Guild_Yaml = new YamlConfiguration();

    public Guild_Setup(Guild guild){
        this.plugin=guild;
        this.log=guild.getLogger();
    }

    public List<Guild_Struct> Get_Guild_Data(){
        return All_Guild;
    }

    private boolean Save_Guild_Data()  {
        try{
            Guild_Yaml.save(new File(plugin.getDataFolder(),"data.yml"));
        }catch (IOException e){
            plugin.getLogger().log(Level.WARNING, Language.get_bar("Save_File_Error"));
            return false;
        }
        return true;
    }

    public boolean inGuild(String player_name){
        return Guild_Any_People.contains(player_name);
    }
    public boolean isVIP(String player_name){
        return Guild_Any_VIP.contains(player_name);
    }
    public boolean isOwner(String player_name){
        return Guild_Any_Owner.contains(player_name);
    }

    protected static Boolean addOnline_People_Data(Map<String,String[]> online_player_data){
        if(online_player_data ==null)return false;
        Online_People_Fast_Seach.putAll(online_player_data);
        return true;
    }

    protected static Boolean delOnline_People_Data(String player_name){
        if(!Online_People_Fast_Seach.containsKey(player_name))return false;
        else Online_People_Fast_Seach.remove(player_name);
        return true;
    }

    protected  Map<String,String[]> Slow_Seach_People(final String play_name){
        for(final Guild_Struct g:  All_Guild)
            if(g.getOwner().equals(play_name))return new HashMap<String,String[]>(){{put(play_name ,new String[]{"Owner",g.getGuild_Name()} );}};
            else if(g.getVIP().contains(play_name))return new HashMap<String,String[]>(){{put(play_name ,new String[]{"VIP",g.getGuild_Name()} );}};
            else if(g.getPeople().contains(play_name))return new HashMap<String,String[]>(){{put(play_name ,new String[]{"People",g.getGuild_Name()} );}};
        return null;
    }

    public String getGuild_Name(String player_name){
        if(Online_People_Fast_Seach.containsKey(player_name)){
           return Online_People_Fast_Seach.get(player_name)[1];
        }else{
            for (Guild_Struct g:All_Guild){
                if(g.getOwner().equals(player_name))return g.getOwner();
                else {
                    for(String s: g.getVIP())if(s.equals(player_name))return s;
                    for(String s:g.getPeople())if(s.equals(player_name))return s;
                }
            }
            return null;
        }
    }

    public boolean Remove_People(String Guild_Name,String play_name){
        if(!this.isOwner(play_name))
            for(Guild_Struct g:All_Guild)
                if(g.getPeople().contains(play_name)){
                    g.delPeople(play_name);
                    Guild_Any_People.remove(play_name);
                    return true;
                }else if(g.getVIP().contains(play_name)){
                    g.delVIP(play_name);
                    Guild_Any_VIP.remove(play_name);
                    Guild_Any_People.remove(play_name);
                    return true;
                }else return false;
           return false;         
    }
    
    public boolean Remove_People(String play_name){
        if(Online_People_Fast_Seach.containsKey(play_name)) {
            String[] map=Online_People_Fast_Seach.get(play_name);
            if(map[0].equals("People")){
                Guild_Any_People.remove(play_name);
                for(Guild_Struct g:All_Guild) if(g.getGuild_Name().equals(map[1])) g.delPeople(play_name);
                return true;
            }
            else if(map[0].equals("VIP")){
                Guild_Any_VIP.remove(play_name);
                for(Guild_Struct g:All_Guild) if(g.getGuild_Name().equals(map[1])) g.delPeople(play_name);
                return true;
            }
            else return false;
        }else  return false;
    }

    public boolean Remove_Guild(String owner){
        for(Guild_Struct g: All_Guild)
            if(g.getOwner().equals(owner)){
                Guild_Yaml.set(g.getGuild_Name(), null);
                All_Guild.remove(g);
                Guild_Any_Owner.remove(owner);
                Guild_Any_VIP.remove(owner);
                Guild_Any_People.remove(owner);
                for(String s :g.getVIP())Guild_Any_VIP.remove(s);
                for(String s :g.getPeople())Guild_Any_People.remove(s);
                return Save_Guild_Data();
            }
        return false;
    }

    public  boolean Add_Guild(final String owner, final String Guild_name) {
        for(Guild_Struct s : this.Get_Guild_Data()) if(s.getGuild_Name().equals(Guild_name))return false;
        All_Guild.add(new Guild_Struct(Guild_name,owner,new ArrayList<String>(),new ArrayList<String>()));
        Guild_Any_People.add(owner);
        Guild_Any_VIP.add(owner);
        Guild_Any_Owner.add(owner);
        Guild_Yaml.set(Guild_name+".Owner",owner);
        return Save_Guild_Data();
    }


    public boolean setupGuild_Data() throws IOException {
        File file=new File(plugin.getDataFolder(),"data.yml");
        if(!file.createNewFile()){
            Guild_Yaml = Guild_Yaml.loadConfiguration(file);
            for (String s:Guild_Yaml.getKeys(false)){
                String Owner=Guild_Yaml.getString(s+".Owner");
                List<String> VIP=Guild_Yaml.getStringList(s+".CEO");
                List<String> People=Guild_Yaml.getStringList(s+".People");
                All_Guild.add(new Guild_Struct(s, Owner, VIP, People));
                Guild_Any_Owner.add(Owner);
                Guild_Any_VIP.add(Owner);
                Guild_Any_People.add(Owner);
                for (String dd : VIP) {
                    Guild_Any_VIP.add(dd);
                    Guild_Any_People.add(dd);
                }
                for (String ss : People) Guild_Any_People.add(ss);
            }
        }
        return true;
    }

    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        try {
            chat = rsp.getProvider();
        }catch (Exception ex){

        }
        return chat != null;
    }

    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

}
