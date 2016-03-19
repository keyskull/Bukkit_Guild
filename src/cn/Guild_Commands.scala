package cn

import org.bukkit.command.{Command, CommandSender, CommandExecutor}

/**
  * Created by key_q on 2016/3/19.
  */
class Guild_Commands extends CommandExecutor{
  override def onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array[String]): Boolean = {
    return true
  }
}
