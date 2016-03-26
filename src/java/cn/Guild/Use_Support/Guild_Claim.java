package cn.Guild.Use_Support;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by key_q on 2016/3/25.
 */
public class Guild_Claim {
    public Map<String,String> Guild_Claim_Cache=new HashMap<>();

    public Guild_Claim(Cache cache){
        cache.Create_Cache_Row("Guild_Claim_Cache",Guild_Claim_Cache);
    }

    public boolean Create_Claim_Block(Location location){
        ClaimedResidence res = Residence.getResidenceManager().getByLoc(location);

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
