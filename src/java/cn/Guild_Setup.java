package cn;


import cn._Guild.Guild_Struct;
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
    private static List<Guild_Struct> All_Guild=new ArrayList<>();
    private static Set<String> Guild_People=new HashSet<>();
    private static Set<String> Guild_VIP=new HashSet<>();
    private static Set<String> Guild_Owner=new HashSet<>();
    private static Guild plugin=null;
    private static YamlConfiguration Guild_Yaml = new YamlConfiguration();

    protected Guild_Setup(Guild guild, Logger log){
        this.plugin=guild;
        this.log=log;
    }

    public static List<Guild_Struct> Get_Guild_Data(){
        return All_Guild;
    }

    private static boolean Save_Guild_Data()  {
        try{
            Guild_Yaml.save(new File(plugin.getDataFolder(),"data.yml"));
        }catch (IOException e){
            plugin.getLogger().log(Level.WARNING,Language.get_bar("Save_File_Error"));
            return false;
        }
        return true;
    }

    public static boolean inGuild(String player_name){
        return Guild_People.contains(player_name);
    }
    public static boolean isVIP(String player_name){
        return Guild_VIP.contains(player_name);
    }
    public static boolean isOwner(String player_name){
        return Guild_Owner.contains(player_name);
    }

    public static boolean Remove_Guild(String owner){
        for(Guild_Struct g: All_Guild)
            if(g.getOwner().equals(owner)){
                Guild_Owner.remove(owner);
                Guild_VIP.remove(owner);
                Guild_People.remove(owner);
                for(String s :g.getVIP())Guild_VIP.remove(s);
                for(String s :g.getPeople())Guild_People.remove(s);
                Guild_Yaml.set(g.getGuild_Name(), null);
                All_Guild.remove(g);
                return Save_Guild_Data();
            }
        return false;
    }

    public static boolean Add_Guild(final String owner, final String Guild_name) {
        for(Guild_Struct s : Guild_Setup.Get_Guild_Data()) if(s.getGuild_Name().equals(Guild_name))return false;
        All_Guild.add(new Guild_Struct(Guild_name,owner,null,null));
        Guild_People.add(owner);
        Guild_VIP.add(owner);
        Guild_Owner.add(owner);
        Guild_Yaml.set(Guild_name+".Owner",owner);
        return Save_Guild_Data();
    }


    protected boolean setupGuild_Data() throws IOException {
        File file=new File(plugin.getDataFolder(),"data.yml");
        if(!file.createNewFile()){
            Guild_Yaml = Guild_Yaml.loadConfiguration(file);
            for (String s:Guild_Yaml.getKeys(false)){
                String Owner=Guild_Yaml.getString(s+".Owner");
                List<String> VIP=Guild_Yaml.getStringList(s+".CEO");
                List<String> People=Guild_Yaml.getStringList(s+".People");
                All_Guild.add(new Guild_Struct(s, Owner, VIP, People));
                Guild_Owner.add(Owner);
                Guild_VIP.add(Owner);
                Guild_People.add(Owner);
                for (String dd : VIP) {
                    Guild_VIP.add(dd);
                    Guild_People.add(dd);
                }
                for (String ss : People) Guild_People.add(ss);
            }
        }
        return true;
    }

    protected boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    protected boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        try {
            chat = rsp.getProvider();
        }catch (Exception ex){

        }
        return chat != null;
    }

    protected boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

}
