package Guild

import org.bukkit.permissions.Permission
import scala.collection.JavaConverters._
/**
  * Created by key_q on 2016/3/21.
  */

object Guild_Permission {
  lazy val Guild_Admin_Permission= new Permission("Guild.Admin","Admin permissions for Guild plugin.",
      Map[String,java.lang.Boolean](
        "Guild.Basics"->true,
        "Guild.Delete.*"->true,
        "Guild.ChangeOwner.*"->true
      ).asJava
    )
  lazy val Guild_Basics_Permission=new Permission("Guild.Basics"," Basic permissions for Guild plugin.",
        Map[String,java.lang.Boolean](
          "Guild.Create"->true,
          "Guild.Info.List"->true,
          "Guild.Info"->true,
          "Guild.Invite"->true,
          "Guild.Join"->true,
          "Guild.Leave"->true,
          "Guild.SKULL"->true
        ).asJava
  )

}
