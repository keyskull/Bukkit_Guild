package cn._Guild;


import cn.Guild;
import cn.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by key_q on 2016/3/22.
 */
public class Guild_Commands implements CommandExecutor {
    private HashMap<String, String> show_info = new HashMap();
    private HashMap<String, String> permission_map = new HashMap();
    Guild_Setup guild_setup;

    public Guild_Commands(Guild_Setup setup) {
        guild_setup=setup;
        show_info.put("_help", "Guild_help_Info");
        show_info.put("create", "Guild_create_Info");
        show_info.put("join", "Guild_join_Info");
        show_info.put("list", "Guild_list_Info");
        show_info.put("leave", "Guild_leave_Info");
        show_info.put("info", "Guild_Info");
        show_info.put("kick", "Guild_kick_Info");
        show_info.put("invite", "Guild_invite_Info");
        show_info.put("cz", "Guild_cz_Info");
        show_info.put("up", "Guild_up_Info");
        show_info.put("sj", "Guild_sj_Info");
        show_info.put("jj", "Guild_jj_Info");
        show_info.put("jn", "Guild_jn_Info");
        show_info.put("invade", "Guild_invade_Info");
        show_info.put("jieshou", "Guild_jieshou_Info");
        show_info.put("claim", "Guild_claim_Info");
        show_info.put("sethome", "Guild_sethome_Info");
        show_info.put("home", "Guild_home_Info");
        show_info.put("pvp", "Guild_pvp_Info");
        show_info.put("jiesan", "Guild_jiesan_Info");
        show_info.put("zr", "Guild_zr_Info");
        //permission_row
        permission_map.put("_help", "Guild.basic.help");
        permission_map.put("create", "Guild.basic.create");
        permission_map.put("join", "Guild.basic.join");
        permission_map.put("list", "Guild.basic.list");
        permission_map.put("leave", "Guild.People.leave");
        permission_map.put("info", "Guild.People.info");
        permission_map.put("kick", "Guild.VIP.kick");
        permission_map.put("invite", "Guild.VIP.invite");
        permission_map.put("cz", "Guild.People.cz");
        permission_map.put("up", "Guild.VIP.up");
        permission_map.put("sj", "Guild.Owner.sj");
        permission_map.put("jj", "Guild.Owner.jj");
        permission_map.put("jn", "Guild.People.jn");
        permission_map.put("invade", "Guild_invade_Info");
        permission_map.put("jieshou", "Guild.VIP.jieshou");
        permission_map.put("claim", "Guild.Owner.claim");
        permission_map.put("sethome", "Guild.Owner.sethome");
        permission_map.put("home", "Guild.People.home");
        permission_map.put("pvp", "Guild.VIP.pvp");
        permission_map.put("jiesan", "Guild.Owner.jiesan");
        permission_map.put("zr", "Guild.Owner.zr");
    }

    private void help(Player player) {
        if (guild_setup.inGuild(player.getName())) {
            if (guild_setup.isVIP(player.getName())) {
                player.sendMessage(Language.get_bar(show_info.get("kick")));
                player.sendMessage(Language.get_bar(show_info.get("invite")));
                player.sendMessage(Language.get_bar(show_info.get("up")));
                player.sendMessage(Language.get_bar(show_info.get("jieshou")));
                player.sendMessage(Language.get_bar(show_info.get("pvp")));
                if (guild_setup.isOwner(player.getName())) {
                    player.sendMessage(Language.get_bar(show_info.get("sj")));
                    player.sendMessage(Language.get_bar(show_info.get("jj")));
                    player.sendMessage(Language.get_bar(show_info.get("claim")));
                    player.sendMessage(Language.get_bar(show_info.get("sethome")));
                    player.sendMessage(Language.get_bar(show_info.get("jiesan")));
                    player.sendMessage(Language.get_bar(show_info.get("zr")));
                } else player.sendMessage(Language.get_bar(show_info.get("leave")));
            }
            player.sendMessage(Language.get_bar(show_info.get("cz")));
            player.sendMessage(Language.get_bar(show_info.get("jn")));
            player.sendMessage(Language.get_bar(show_info.get("home")));
            player.sendMessage(Language.get_bar(show_info.get("info")));
        } else {
            player.sendMessage(Language.get_bar(show_info.get("create")));
            player.sendMessage(Language.get_bar(show_info.get("join")));
        }
        player.sendMessage(Language.get_bar(show_info.get("list")));
        player.sendMessage(Language.get_bar(show_info.get("_help")));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            guild_setup.log.info("Only players are supported for this Example Plugin, but you should not do this!!!");
            return true;
        }
        Player player = (Player) commandSender;
        if (strings.length > 0)
            if (!show_info.containsKey(strings[0])) {
                help(player);
                return false;
            } else {
                try {
                    return run_func(player, strings[0], strings);
                } catch (NoSuchMethodException ex) {
                    if (show_info.containsKey(strings[0]))
                        player.sendMessage(Language.get_bar(show_info.get(strings[0])));
                    else {
                        player.sendMessage(Language.get_bar("Not_func"));
                        help(player);
                    }
                } catch (InvocationTargetException e) {
                    player.sendMessage(Language.get_bar(show_info.get(strings[0])+"_Error"));
                } catch (IllegalAccessException e) {
                    player.sendMessage(Language.get_bar(show_info.get(strings[0])+"_Error"));
                }
                return false;
            }
        else {
            help(player);
            return false;
        }
    }


    private boolean run_func(Player player, String func_s, String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method func;
        if (player.hasPermission(permission_map.get(func_s))) {
            if (args.length > 1) {
                func = this.getClass().getDeclaredMethod(func_s, Player.class, String[].class);
                return (Boolean) func.invoke(this, player, args);
            } else {
                func = this.getClass().getDeclaredMethod(func_s, Player.class);
                return (Boolean) func.invoke(this, player);
            }
        }else {
            player.sendMessage(Language.get_bar(permission_map.get(func_s),"No_Permission_For"));
            return false;
        }
    }


    public Boolean create(Player player,String[] args){
            if(Guild_Setup.econ != null)
                if(!Guild_Setup.econ.withdrawPlayer(player,(double)Guild.get_Config_asJava().get("Create_Guild_Money")).transactionSuccess()){
                    player.sendMessage(Language.get_bar("No_Money"));
                    return false;
                }
            if(guild_setup.inGuild(player.getName())){
                player.sendMessage(Language.get_bar("Have_Guild"));
                return false;
            }else if(guild_setup.Add_Guild(player.getName(),args[1])){
                player.sendMessage(Language.get_bar("Guild.create","Guild_Created"));
                return true;
            }
            return false;
    }

    public Boolean jiesan(Player player) {
        if(guild_setup.isOwner(player.getName())){
            if(guild_setup.Remove_Guild(player.getName())) {
                player.sendMessage(Language.get_bar("jiesan_Success"));
            return true;
            }else {
                player.sendMessage(Language.get_bar("Failed"));
                return false;
            }
        }else{
            player.sendMessage(Language.get_bar("No_Owner"));
            return false;
        }
    }

    public Boolean leave(Player player) {
        if(!guild_setup.Remove_People(player.getName())){
             player.sendMessage(Language.get_bar("No_Owner"));
             return false;
         }
        return true;
    }

    public Boolean kick(Player player,String[] args) {
        if(guild_setup.isVIP(player.getName())){
          guild_setup.Remove_People(guild_setup.getGuild_Name(player.getName()),args[0]);
        }else {
            player.sendMessage(Language.get_bar("No_Permission"));
            return false;
        }
        return true;
    }
    
    public Boolean invite(Player player,String[] args) {
        
        return true;

    }
    
    public Boolean join(Player player,String[] args) {

        return true;
    }
    
    public Boolean info(Player player,String[] args) {
        return true;
    }
    public Boolean cz(Player player,String[] args) {
        return true;
    }
    public Boolean up(Player player,String[] args) {
        return true;
    }
    public Boolean sj(Player player,String[] args) {
        return true;
    }
    public Boolean jn(Player player,String[] args) {
        return true;
    }
    public Boolean invade(Player player,String[] args) {
        return true;
    }
    public Boolean js(Player player,String[] args) {
        return true;
    }
    public Boolean claim(Player player,String[] args) {
        return true;
    }
    public Boolean sethome(Player player,String[] args) {
        return true;
    }
    public Boolean home(Player player,String[] args) {
        return true;
    }
    public Boolean pvp(Player player,String[] args) {
        return true;
    }
    public Boolean zr(Player player,String[] args) {
        return true;
    }

    public Boolean list(Player player,String[] args) {
        if (player.hasPermission("Guild.basic.list") && args[1].equals("all")) {
            for(Guild_Struct s:guild_setup.Get_Guild_Data())player.sendRawMessage(s.getGuild_Name());
            return true;
        }else return false;
    }



}
