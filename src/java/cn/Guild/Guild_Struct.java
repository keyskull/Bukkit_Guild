package cn.Guild;

import java.util.List;
import java.util.Set;

public class Guild_Struct {
    private String Guild_Name;
    private String Owner;
    private Set<String> VIP;
    private Set<String> People;
    private Boolean PVP=false;
    public Guild_Struct(String Guild_Name, String Owner, Set<String> VIP, Set<String> People,Boolean PVP) {
        this.Guild_Name = Guild_Name;
        this.Owner = Owner.toLowerCase();
        this.VIP = VIP;
        this.People = People;
        this.PVP=PVP;
    }
    public Guild_Struct(String Guild_Name, String Owner, Set<String> VIP, Set<String> People) {
        this.Guild_Name = Guild_Name;
        this.Owner = Owner.toLowerCase();
        this.VIP = VIP;
        this.People = People;
    }

    public String getOwner() {
        return Owner;
    }

    public Set<String> getVIP() {
        return VIP;
    }

    public Set<String> getPeople() {
        return People;
    }

    public String getGuild_Name() {
        return Guild_Name;
    }

    public boolean getPVP()  {
        return PVP;
    }

    public boolean addPeople(String play_name) {
        return this.People.add(play_name.toLowerCase());
    }

    public boolean addVIP(String play_name) {
        return this.VIP.add(play_name.toLowerCase());
    }

    public boolean delPeople(String play_name) {
        return this.People.remove(play_name.toLowerCase());
    }

    public boolean delVIP(String play_name) {
        return this.VIP.remove(play_name.toLowerCase());
    }

    public boolean isVIP(String play_name){return this.VIP.contains(play_name.toLowerCase()); }

    public boolean isPeople(String play_name){return this.People.contains(play_name.toLowerCase()); }

    public void Change_Owner(String play_name){this.Owner=play_name.toLowerCase();}
    public boolean Change_PVP(){
        if(PVP)PVP=false;
        else PVP=true;
        return PVP;
    }
}