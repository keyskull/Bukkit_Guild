package cn.Guild;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        guild_setup.Cache.addOnline_People_Data(guild_setup.Guild.Slow_Seach_People(event.getPlayer().getName()));
        guild_setup.Invite.create_invite_box(event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        guild_setup.Cache.delOnline_People_Data(event.getPlayer().getName());
        guild_setup.Invite.delete_invite_box(event.getPlayer().getName());
    }
}
