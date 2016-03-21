package cn

import java.io.File
import java.util.logging.Level

import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by key_q on 2016/3/19.
  */
class Language(guild: Guild,language: String) {
  val language_folder= new File(guild.getDataFolder,"language")
  Language.bar_macros("file")=language+".yml"
    if(language_folder.mkdir){
      val Yaml=new YamlConfiguration()
      Language.bars.foreach(f =>Yaml.set(f._1,f._2))
      Yaml.formatted("UTF-8")
      Yaml.save(new File(guild.getDataFolder,"language/zh.yml"))
    }
  else {
      if(!new File(language_folder,language+".yml").canRead)guild.getLogger.log(Level.WARNING,Language.get_bar("No_Lang"))
      val Yaml = YamlConfiguration.loadConfiguration(new File(language_folder,language+".yml"))
      Yaml.formatted("UTF-8")
      Yaml.getKeys(false).asScala.map(m=>Language.bars(m)=Yaml.getString(m))
    }

}

object Language{
  private lazy val bar_macros:mutable.Map[String,String]=mutable.Map()
  private lazy val bars:mutable.Map[String,String]=mutable.Map(
    "Finish_Load_Config"->"读取配置完成.",
    "No_Lang"->" %file% 没有这个语言文件.",
    "Guild_Create_Info" ->"创建 /guild create <公会名>  玩家可创建公会，创建公会需消耗金币50000",
    "Guild_Join_Info" ->"申请 /guild join <公会名> 玩家只能通过申请或者邀请进入公会"
  )

  def get_bar(bar:String):String={
    var bar_string =try{ Language.bars(bar)}catch{case ex:Exception => "" }
    for(match_string <- """%[a-zA-Z+]%""".r.findAllIn(bar_string))yield
      bar_string = """%[a-zA-Z+]%""".r.replaceFirstIn(bar_string,bar_macros(match_string))
    return bar_string
  }
  def get_bar(commandSender: CommandSender,bar:String):String={
    return ""
  }
}