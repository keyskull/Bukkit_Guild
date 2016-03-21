package Guild


import cn.Guild
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
  import scala.collection.JavaConverters._


  /**
    * Created by key_q on 2016/3/19.
    */
class Guild_Commands extends CommandExecutor {
    override def onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): Boolean = {
      if (strings.length > 0) strings(0) match {
        case "create" => commandSender.getEffectivePermissions.asScala.map(m => commandSender.sendMessage(m.getPermission))
        case "add" => commandSender.hasPermission(Guild_Permission.Guild_Admin_Permission)
        case  _ =>Guild_Commands.show_help(commandSender)
      }
      else Guild_Commands.show_help(commandSender)

      return true
    }
  }

object Guild_Commands {
    def show_help(commandSender: CommandSender): Unit = {
      if (commandSender.hasPermission(Guild_Permission.Guild_Basics_Permission))
        commandSender.sendMessage(Array[String](
          Language.get_bar("Guild_Create_Info"),
          Language.get_bar("Guild_Join_Info")
        ))
    }
  }

