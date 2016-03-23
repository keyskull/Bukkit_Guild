package cn.Guild;

import java.util.List;

public class Guild_Struct {
    private String Guild_Name;
    private String Owner;
    private List<String> VIP;
    private List<String> People;

    public Guild_Struct(String Guild_Name, String Owner, List<String> VIP, List<String> People) {
        this.Guild_Name = Guild_Name;
        this.Owner = Owner;
        this.VIP = VIP;
        this.People = People;
    }

    public String getOwner() {
        return Owner;
    }

    public List<String> getVIP() {
        return VIP;
    }

    public List<String> getPeople() {
        return People;
    }

    public String getGuild_Name() {
        return Guild_Name;
    }

    public Boolean addPeople(String play_name) {
        return this.People.add(play_name);
    }

    public Boolean addVIP(String play_name) {
        return this.VIP.add(play_name);
    }

    public Boolean delPeople(String play_name) {
        return this.People.remove(play_name);
    }

    public Boolean delVIP(String play_name) {
        return this.VIP.remove(play_name);

    }
}