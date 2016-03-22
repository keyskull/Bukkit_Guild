package cn._Guild;


import cn.Guild;
import cn.Guild_Setup;
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
    private Guild_Setup setup;

    public Guild_Commands(Guild_Setup setup) {
        this.setup = setup;
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
        if (Guild_Setup.inGuild(player.getName())) {
            if (Guild_Setup.isVIP(player.getName())) {
                player.sendMessage(Language.get_bar(show_info.get("kick")));
                player.sendMessage(Language.get_bar(show_info.get("invite")));
                player.sendMessage(Language.get_bar(show_info.get("up")));
                player.sendMessage(Language.get_bar(show_info.get("jieshou")));
                player.sendMessage(Language.get_bar(show_info.get("pvp")));
                if (Guild_Setup.isOwner(player.getName())) {
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
            Guild_Setup.log.info("Only players are supported for this Example Plugin, but you should not do this!!!");
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
                    player.sendMessage(Language.get_bar(show_info.get(strings[0])));
                } catch (IllegalAccessException e) {
                    player.sendMessage(Language.get_bar(show_info.get(strings[0])));
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


    public static Boolean create(Player player,String[] args){
            if(Guild_Setup.econ != null)
                if(!Guild_Setup.econ.withdrawPlayer(player,(double)Guild.get_Config_asJava().get("Create_Guild_Money")).transactionSuccess()){
                    player.sendMessage(Language.get_bar("No_Money"));
                    return false;
                }
            if(Guild_Setup.inGuild(player.getName())){
                player.sendMessage(Language.get_bar("Have_Guild"));
                return false;
            }else if(Guild_Setup.Add_Guild(player.getName(),args[1])){
                player.sendMessage(Language.get_bar("Guild.create","Guild_Created"));
                return true;
            }
            return false;
    }

    public static Boolean jiesan(Player player) {
        if(Guild_Setup.isOwner(player.getName())){
          return Guild_Setup.Remove_Guild(player.getName());
        }else{
            player.sendMessage(Language.get_bar("Guild.create","No_Permission_For"));
            return false;
        }
    }

    public static Boolean leave(Player player) {
        if(Guild_Setup.inGuild(player.getName())){

        }
        return true;
    }
    public static Boolean kick(Player player,String[] args) {

        return true;
    }

    public static Boolean join(Player player,String[] args) {
        return true;

    }

    public static Boolean invite(Player player,String[] args) {
        return true;

    }

    public static Boolean info(Player player,String[] args) {
        return true;
    }
    public static Boolean cz(Player player,String[] args) {
        return true;
    }
    public static Boolean up(Player player,String[] args) {
        return true;
    }
    public static Boolean sj(Player player,String[] args) {
        return true;
    }
    public static Boolean jn(Player player,String[] args) {
        return true;
    }
    public static Boolean invade(Player player,String[] args) {
        return true;
    }
    public static Boolean js(Player player,String[] args) {
        return true;
    }
    public static Boolean claim(Player player,String[] args) {
        return true;
    }
    public static Boolean sethome(Player player,String[] args) {
        return true;
    }
    public static Boolean home(Player player,String[] args) {
        return true;
    }
    public static Boolean pvp(Player player,String[] args) {
        return true;
    }
    public static Boolean zr(Player player,String[] args) {
        return true;
    }

    public static Boolean list(Player player,String[] args) {
        if (player.hasPermission("Guild.basic.list") && args[1].equals("all")) {
            for(Guild_Struct s:Guild_Setup.Get_Guild_Data())player.sendRawMessage(s.getGuild_Name());
            return true;
        }else return false;
    }



}
