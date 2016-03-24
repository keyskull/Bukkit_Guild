package cn.Guild.Use_Support;



import cn.Guild.Guild_Setup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by key_q on 2016/3/24.
 */
public class Guild_Home {
    Map<String,Index> Guild_Home_Cache=new HashMap<>();
    public Guild_Home(Cache cache){
       cache.Create_Cache_Row("Guild_Home_Cache",Guild_Home_Cache);
       cache.set_Cache_Clean_time(5000);
    }

    public boolean Set_Home(String Guild_name,Index index){
        Guild_Setup.Get_Guild_Yaml(Guild_name).set(Guild_name+".Home",index.x+","+index.y+","+index.z);
        Guild_Home_Cache.put(Guild_name,index);
        return true;
    }

    public Index Get_Home(String Guild_name){
        if(Guild_Home_Cache.containsKey(Guild_name))
            return Guild_Home_Cache.get(Guild_name);
        else{
            String[] list=  Guild_Setup.Get_Guild_Yaml(Guild_name).getString(Guild_name+".Home").split(",");
            if(list.length==3){
                Index index= new Index(Double.valueOf(list[0]),Double.valueOf(list[1]),Double.valueOf(list[2]));
                Guild_Home_Cache.put(Guild_name,index);
                return index;
            }
            else return null;
        }
    }

}
