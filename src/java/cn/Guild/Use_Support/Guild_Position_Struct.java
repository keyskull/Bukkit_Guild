package cn.Guild.Use_Support;

/**
 * Created by key_q on 2016/3/22.
 */
enum Guild_Position{People,VIP,Owner};
public class Guild_Position_Struct {
    final public Guild_Position Position;
    final public String Guild_name;

    public Guild_Position_Struct(Guild_Position Position,String Guild_name) {
        this.Position = Position;
        this.Guild_name=Guild_name;
    }
}
