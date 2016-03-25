package cn.Guild;


import cn.Guild.Use_Support.*;
import cn.Guild_Launch;
import cn.Language;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

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
    private static Guild_Launch plugin =null;
    private static Map<String,YamlConfiguration> Guild_Yaml_Data_File_Map = new HashMap<>();
    private static File DataFolder=null;
    public final Cache Cache=new Cache();
    public final Contribution contribution=new Contribution(Cache);
    public final Invite Invite=new Invite(Cache);
    public final Guild_Level Guild_Level=new Guild_Level(Cache);
    public final Guild Guild=new Guild(Cache);
    public final cn.Guild.Use_Support.Guild_Skull Guild_Skull=new Guild_Skull(Cache);
    public final Guild_Territory Guild_Territory=new Guild_Territory(Cache);
    public final cn.Guild.Use_Support.Guild_Home Guild_Home=new Guild_Home(Cache);

    public Guild_Setup(Guild_Launch guild_launch) {
        this.plugin=guild_launch;
        this.DataFolder=plugin.getDataFolder();
        this.log=guild_launch.getLogger();
    }

    public static YamlConfiguration Get_Guild_Yaml(String Guild_Name){
        if(Guild_Yaml_Data_File_Map.containsKey(Guild_Name))
        return Guild_Yaml_Data_File_Map.get(Guild_Name);
        else{
            File file=new File(DataFolder,"Guild_data/"+Guild_Name+".yml");
            try {
                    file.createNewFile();
                    Guild_Yaml_Data_File_Map.put(Guild_Name, YamlConfiguration.loadConfiguration(file));
                    return Guild_Yaml_Data_File_Map.get(Guild_Name);
            }catch (IOException ex){
                plugin.getLogger().warning(Language.get_bar(Guild_Name+".yml","Load_File_Error"));
            }
            return null;
        }
    }

    public static boolean Save_Guild_Data(){
        try{
            for(String s:Guild_Yaml_Data_File_Map.keySet()){
                Guild_Yaml_Data_File_Map.get(s).save(new File(DataFolder,"Guild_data/"+s+".yml"));
            }
        }catch (IOException e){
            plugin.getLogger().log(Level.WARNING, Language.get_bar("Save_File_Error"));
            return false;
        }
        return true;
    }


    public boolean setupLoad_Guild_Data()  {
        Set<String> VIP,people;
        File file=new File(Guild_Setup.DataFolder,"Guild_Data");
        if(file.exists()){
            for(File f :file.listFiles())
                if (f.isFile()) {
                    if (f.length() == 0) {f.delete();continue;}
                    final String[] path = f.getName().split("\\\\");
                    final String s = path[path.length - 1].split("\\.")[0];
                    Guild_Yaml_Data_File_Map.put(s, YamlConfiguration.loadConfiguration(f));
                    YamlConfiguration Guild_Yaml = YamlConfiguration.loadConfiguration(f);
                    String Owner="";
                    for(String owner :Guild_Yaml.getConfigurationSection(s + ".Owner").getKeys(false))Owner=owner;
                    ConfigurationSection vip_config= Guild_Yaml.getConfigurationSection(s + ".VIP");
                    if(vip_config != null) VIP = vip_config.getKeys(false);
                    else VIP=new HashSet<>();
                    ConfigurationSection people_config= Guild_Yaml.getConfigurationSection(s + ".People");
                    if(people_config != null) people = people_config.getKeys(false);
                    else people=new HashSet<>();
                    boolean pvp = Guild_Yaml.getBoolean(s + ".PVP");
                    final Set<Index> territory = new HashSet<>();
                    for (String ss : Guild_Yaml.getStringList(s + ".Territory")) {
                        Index cordinates = new Index();
                        String[] index = ss.split(",");
                        cordinates.x = Double.valueOf(index[0]);
                        cordinates.y = Double.valueOf(index[0]);
                        cordinates.z = Double.valueOf(index[0]);
                        territory.add(cordinates);
                    }
                    Guild_Territory.Territory_cache.putAll(new HashMap<String, Set<Index>>() {{put(s, territory);}});
                    Guild.All_Guild.put(s, new Guild_Struct(s, Owner, VIP, people, pvp));
                    Guild.Guild_Any_Owner.add(Owner);
                    Guild.Guild_Any_VIP.add(Owner);
                    Guild.Guild_Any_People.add(Owner);
                    for (String dd : VIP) {
                        Guild.Guild_Any_VIP.add(dd);
                        Guild.Guild_Any_People.add(dd);
                    }
                    for (String ss : people) Guild.Guild_Any_People.add(ss);
                }
            }else file.mkdir();
        return true;
    }

    public boolean setupEnable(){
        new BukkitRunnable() {
            int time = (int)Guild_Launch.get_Config_asJava().get("auto_save");
            @Override
            public void run() {
                if(--time < 0) {
                    Guild_Setup.Save_Guild_Data();
                    plugin.getLogger().info(Language.get_bar("Save_File_Success"));
                    time = (int)Guild_Launch.get_Config_asJava().get("auto_save");
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
        this.Save_Guild_Data();
        return true;
    }

    public boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    public void loadResidence() {
        PluginManager pm = plugin.getServer().getPluginManager();
        Plugin p = pm.getPlugin("Residence");
        if(p!=null) {
            if(!p.isEnabled()) {
                plugin.getLogger().info(" - Manually Enabling Residence!");
                pm.enablePlugin(p);
            }
        }
        else {
            plugin.getLogger().warning(" - Residence NOT Installed, DISABLED!");
            //   plugin.setEnabled(false);
        }
    }

/*
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
*/

}
