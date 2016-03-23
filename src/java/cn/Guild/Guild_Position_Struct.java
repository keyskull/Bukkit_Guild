package cn.Guild;

/**
 * Created by key_q on 2016/3/22.
 */
enum Guild_Position{People,VIP,Owner};
public class Guild_Position_Struct {
    final public Guild_Position Level;
    final public String Guild_name;

    public Guild_Position_Struct(Guild_Position Level,String Guild_name) {
        this.Level = Level;
        this.Guild_name=Guild_name;
    }
}
