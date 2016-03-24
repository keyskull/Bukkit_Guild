package cn.Guild;


import cn.Guild.Use_Support.*;
import cn.Guild_Launch;
import cn.Language;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by key_q on 2016/3/22.
 */
public class Guild_Commands implements CommandExecutor {
    private HashMap<String, String> show_info = new HashMap();
    private HashMap<String, String> permission_map = new HashMap();
    private Guild Guild;
    private Logger log;
    private Economy econ;
    private cn.Guild.Use_Support.Invite Invite;
    private cn.Guild.Use_Support.Contribution Contribution;
    private Guild_Level guild_level;
    private Guild_Territory Guild_Territory;
    private cn.Guild.Use_Support.Guild_Home Guild_Home;
    public Guild_Commands(Guild_Setup setup) {
        guild_level=setup.Guild_Level;
        Contribution=setup.contribution;
        Invite=setup.Invite;
        econ=setup.econ;
        log=setup.log;
        Guild=setup.Guild;
        Guild_Home=setup.Guild_Home;
        Guild_Territory =setup.Guild_Territory;
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
        permission_map.put("up", "Guild.Owner.up");
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
        if (Guild.inGuild(player.getName())) {
            if (Guild.isVIP(player.getName())) {
                player.sendMessage(Language.get_bar(show_info.get("kick")));
                player.sendMessage(Language.get_bar(show_info.get("invite")));
                player.sendMessage(Language.get_bar(show_info.get("jieshou")));
                player.sendMessage(Language.get_bar(show_info.get("pvp")));
                if (Guild.isOwner(player.getName())) {
                    player.sendMessage(Language.get_bar(show_info.get("up")));
                    player.sendMessage(Language.get_bar(show_info.get("sj")));
                    player.sendMessage(Language.get_bar(show_info.get("jj")));
                    player.sendMessage(Language.get_bar(show_info.get("claim")));
                    player.sendMessage(Language.get_bar(show_info.get("sethome")));
                    player.sendMessage(Language.get_bar(show_info.get("jiesan")));
                    player.sendMessage(Language.get_bar(show_info.get("zr")));
                } else player.sendMessage(Language.get_bar(show_info.get("leave")));
            }else player.sendMessage(Language.get_bar(show_info.get("leave")));
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
            log.info("Only players are supported for this Example Plugin, but you should not do this!!!");
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
        String permission=permission_map.get(func_s);
        if (player.hasPermission(permission)) {
            if(Guild.inGuild(player.getName())) {
                if (permission.indexOf("VIP") == 6 && !Guild.isVIP(player.getName())) {
                    player.sendMessage(Language.get_bar("No_VIP"));
                    return false;
                } else if (permission.indexOf("Owner") == 6 && !Guild.isOwner(player.getName())) {
                    player.sendMessage(Language.get_bar("No_Owner"));
                    return false;
                }
            }else if(permission.indexOf("People") == 6||permission.indexOf("VIP") == 6||permission.indexOf("Owner") == 6){
                player.sendMessage(Language.get_bar("No_Guild_People"));
                return false;
            }
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
            if(econ != null)
                if(!econ.withdrawPlayer(player,(double)Guild_Launch.get_Config_asJava().get("Create_Guild_Money")).transactionSuccess()){
                    player.sendMessage(Language.get_bar("No_Money"));
                    return false;
                }
            if(Guild.inGuild(player.getName())){
                player.sendMessage(Language.get_bar("Have_Guild"));
                return false;
            }else if(Guild.Add_Guild(player.getName(),args[1])){
                player.sendMessage(Language.get_bar("Guild_Created"));
                return true;
            }else {
                player.sendMessage(Language.get_bar("Failed"));
                return false;

            }
    }

    public Boolean jiesan(Player player) {
            if(Guild.Remove_Guild(player.getName())) {
                player.sendMessage(Language.get_bar("jiesan_Success"));
            return true;
            }else {
                player.sendMessage(Language.get_bar("Failed"));
                return false;
            }
    }

    public Boolean leave(Player player) {
        return Guild.Remove_People(player.getName());
    }

    public Boolean kick(Player player,String[] args) {
        return  Guild.Kick_People(args[0],Guild.getGuild_Name(player.getName()));
    }
    
    public Boolean invite(Player player,String[] args) {
            if (Invite.add_invite(args[1], Guild.getGuild_Name(player.getName()))) {
                player.sendMessage(Language.get_bar("Invite_Success"));
                return true;
            } else {
                player.sendMessage(Language.get_bar("Not_Online"));
                return false;
            }
    }
    
    public Boolean join(Player player,String[] args) {
        if(Invite.check_invite(player.getName(),args[1])){
            if(Guild.Add_People(player.getName(),args[1])) {
                player.sendMessage(Language.get_bar("Join_Success"));
                return true;
            }else return false;
        }else{
            player.sendMessage(Language.get_bar("Failed"));
            return false;
        }
    }

    public Boolean info(Player player,String[] args) {


        return true;
    }
    public Boolean cz(Player player,String[] args) {
        int contribution= Contribution.Increase_Contribution(player.getName(),Integer.parseInt(args[2]));
        player.sendMessage(Language.get_bar(String.valueOf(contribution),"cz_Success"));
        return true;
    }

    public Boolean up(Player player,String[] args) {
        if(Guild.isOwner(player.getName()))
            guild_level.Up_Guild_Level(args[1]);
        return true;
    }
    public Boolean jieshou(Player player,String[] args) {


        return true;
    }

    public Boolean invade(Player player,String[] args) {

        return true;
    }


    public Boolean jn(Player player,String[] args) {


        return true;
    }

    public Boolean sj(Player player,String[] args) {//not_test
        if(Guild.People_Upgrade(args[1])) {
                player.sendMessage(Language.get_bar("sj_Success"));
                return true;
            }else {
            player.sendMessage(Language.get_bar("Failed"));
            return false;
        }
    }

    public boolean jj(Player player,String[] args) {//not_test
            if(Guild.People_Downgrade(args[1])){
                player.sendMessage(Language.get_bar("jj_Success"));
                return true;
            }
            else {
                player.sendMessage(Language.get_bar("Failed"));
                return false;
            }
    }

    public boolean claim(Player player,String[] args) {//not_test
        if(Guild_Territory.Create_Territory(Guild.getGuild_Name(player.getName()),player.getBedSpawnLocation())){

            player.sendMessage(Language.get_bar("Set_Home_Success"));
            return true;
        }else {
            player.sendMessage(Language.get_bar("Failed"));
        }
        return true;
    }

    public Boolean sethome(Player player) {
        Index index =new Index(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ());
        if(Guild_Home.Set_Home(Guild.getGuild_Name(player.getName()),index)){
            player.sendMessage(Language.get_bar("Set_Home_Success"));
            player.sendMessage(Language.get_bar((int)index.x+":"+(int)index.y+":"+(int)index.z,"Set_Home_For"));

            return true;
        }else {
            player.sendMessage(Language.get_bar("Failed"));
            return false;
        }
    }

    public Boolean home(Player player) {
        Index index=Guild_Home.Get_Home(Guild.getGuild_Name(player.getName()));
        if(index!=null){
            player.teleport(new Location(player.getWorld(),index.x,index.y,index.z,player.getLocation().getYaw(),player.getLocation().getPitch()));
            player.sendMessage(Language.get_bar("Go_Home_Success"));
            player.sendMessage(Language.get_bar((int)index.x+":"+(int)index.y+":"+(int)index.z,"Set_Home_For"));
            return true;
        }else {
            player.sendMessage(Language.get_bar("Go_Home_Faild"));
            return false;
        }
    }

    public Boolean pvp(Player player) {
            if(Guild.setPVP(Guild.getGuild_Name(player.getName()))){
                player.sendMessage(Language.get_bar("Guild_PVP_Open"));
            }else player.sendMessage(Language.get_bar("Guild_PVP_Close"));
        return true;
    }
    public Boolean zr(Player player,String[] args) {


        return true;
    }

    public Boolean list(Player player,String[] args) {
        if (args[1].equals("all")) {
            for(String s:Guild.Get_Guild_Data().keySet())player.sendRawMessage(s);
            return true;
        }else{

        }
        return false;
    }



}
