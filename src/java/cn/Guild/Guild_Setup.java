package cn.Guild;


import cn.Guild_Launch;
import cn.Language;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.YamlConfiguration;
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
    private static YamlConfiguration Guild_Yaml=null;

    public final Invite Invite=new Invite();
    public final Cache Cache=new Cache();
    public final Contribution contribution=new Contribution();
    public final Guild_Level Guild_Level=new Guild_Level(Cache);
    public final Guild Guild=new Guild(Cache);


    public Guild_Setup(Guild_Launch guild_launch) throws IOException {
        this.plugin=guild_launch;
        this.log=guild_launch.getLogger();
        this.setupGuild_Data();
        this.Save_Guild_Data();
        new BukkitRunnable() {
            int time = (int)Guild_Launch.get_Config_asJava().get("auto_save");  // 六十秒
            @Override
            public void run() {
                if(--time < 0) {
                    Guild_Setup.Save_Guild_Data();
                    plugin.getLogger().info(Language.get_bar("Save_File_Success"));
                    time = (int)Guild_Launch.get_Config_asJava().get("auto_save");
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    protected static YamlConfiguration Get_Guidl_Yaml(){
        return Guild_Yaml;
    }

    public static boolean Save_Guild_Data(){
        try{
            Guild_Yaml.save(new File(plugin.getDataFolder(),"data.yml"));
        }catch (IOException e){
            plugin.getLogger().log(Level.WARNING, Language.get_bar("Save_File_Error"));
            return false;
        }
        return true;
    }


    private boolean setupGuild_Data() throws IOException {
        File file=new File(plugin.getDataFolder(),"data.yml");
        if(!file.createNewFile()){
            Guild_Yaml = YamlConfiguration.loadConfiguration(file);
            for (String s:Guild_Yaml.getKeys(false)){
                String Owner=Guild_Yaml.getString(s+".Owner");
                List<String> VIP=Guild_Yaml.getStringList(s+".CEO");
                List<String> people=Guild_Yaml.getStringList(s+".People");
                boolean pvp=Guild_Yaml.getBoolean(s+".PVP");
                Guild.All_Guild.put(s,new Guild_Struct(s, Owner, VIP, people,pvp));
                Guild.Guild_Any_Owner.add(Owner);
                Guild.Guild_Any_VIP.add(Owner);
                Guild.Guild_Any_People.add(Owner);
                for (String dd : VIP) {
                    Guild.Guild_Any_VIP.add(dd);
                    Guild.Guild_Any_People.add(dd);
                }
                for (String ss : people) Guild.Guild_Any_People.add(ss);
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
