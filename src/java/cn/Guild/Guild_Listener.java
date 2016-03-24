package cn.Guild;

import cn.Language;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by key_q on 2016/3/23.
 */
public class Guild_Listener implements Listener {
    Guild_Setup guild_setup;
    public Guild_Listener(Guild_Setup setup){
    this.guild_setup=setup;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        guild_setup.Cache.addOnline_People_Data(guild_setup.Guild.Slow_Seach_People(event.getPlayer().getName()),event.getPlayer());
        guild_setup.Invite.create_invite_box(event.getPlayer().getName(),event.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        guild_setup.Cache.delOnline_People_Data(event.getPlayer().getName());
        guild_setup.Invite.delete_invite_box(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)//not test
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            String entity_guild_name=guild_setup.Guild.getGuild_Name(((Player)event.getEntity()).getPlayer().getName());
            if(entity_guild_name!=null &&
                    guild_setup.Guild.getGuild_Name(((Player) event.getDamager())
                            .getPlayer().getName())== entity_guild_name) {
                       if(!guild_setup.Guild.isPVP(entity_guild_name)){
                           ((Player) event.getDamager()).sendMessage(Language.get_bar("Not_Open_Guild_PVP"));
                           event.setCancelled(true);
                       }
            }

        }

    }
}
