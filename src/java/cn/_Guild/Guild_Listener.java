package cn._Guild;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by key_q on 2016/3/23.
 */
public class Guild_Listener implements Listener {
    Guild_Setup Guild_Setup;
    public Guild_Listener(Guild_Setup setup){
    this.Guild_Setup=setup;

    }
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Guild_Setup.addOnline_People_Data(Guild_Setup.Slow_Seach_People(event.getPlayer().getName()));

    }

    public void onQuit(PlayerQuitEvent event) {
        Guild_Setup.delOnline_People_Data(event.getPlayer().getName());
    }
}
