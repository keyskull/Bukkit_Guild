package cn.Guild.Use_Support;

import cn.Guild.Guild_Setup;
import com.bekvon.bukkit.residence.ConfigManager;
import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.economy.EconomyInterface;
import com.bekvon.bukkit.residence.permissions.PermissionManager;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import com.bekvon.bukkit.residence.protection.ResidenceManager;
import com.bekvon.bukkit.residence.protection.WorldFlagManager;
import com.bekvon.bukkit.residence.selection.SelectionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by key_q on 2016/3/25.
 */
public class Guild_Claim {
    public Map<String,String> Guild_Claim_Cache=new HashMap<>();
    ResidenceManager rmanager = Residence.getResidenceManager();
    SelectionManager smanager = Residence.getSelectionManager();
    EconomyInterface econ = Residence.getEconomyManager();
    ConfigManager cmanager = Residence.getConfigManager();
    PermissionManager pmanager = Residence.getPermissionManager();
    WorldFlagManager wmanager = Residence.getWorldFlags();

    public Guild_Claim(Cache cache){
        cache.Create_Cache_Row("Guild_Claim_Cache",Guild_Claim_Cache);
    }

    public boolean Create_Claim_Block(Player player,String owner, Location location){
        ClaimedResidence res = Residence.getResidenceManager().getByLoc(location);

        //cmanager.
        //rmanager.addResidence(player,owner,location,null,true);
        return true;
    }

    public Map<Integer,String> Check_Guild_Claim(String Guild_name){

        return null;
    }

    public boolean Delete_Guild_Claim_Block(String Guild_name,int Block_number){
     return true;
    }

    public boolean Move_Guild_Claim_Block(Location location,String Guild_name){
        return true;
    }

    public boolean Move_Guild_Claim_Block(Location location,String Guild_name,int Block_number){
        return true;
    }

}
