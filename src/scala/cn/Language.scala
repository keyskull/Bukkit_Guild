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
    "Guild_create_Info" ->"创建 /guild create <公会名>  玩家可创建公会，创建公会需消耗金币 %money%",
    "Guild_join_Info" ->"申请 /guild join <公会名> 玩家只能通过申请或者邀请进入公会",
    "Guild_list_Info"->"公会列表 /guild list all 玩家可以查询本服所有创建公会",
    "Guild_help_Info"->"帮助说明",
    "Not_func"->"没有这个指令",
    "No_Permission_For"->"%1% 没有这个权限",
    "Have_Guild"->"你已经在一个公会里了.",
    "Guild_Created"->"成功创建公会."
  )
  def set_macro(macros:String,value:String):Unit= bar_macros(macros)=value

  def get_bar(bar:String):String={
    var bar_string =try{ Language.bars(bar)}catch{case ex:Exception => bar }
    for(match_string <- """%\w+%""".r.findAllIn(bar_string))yield
      bar_string = """%\w+%""".r.replaceFirstIn(bar_string,bar_macros(match_string))
    return bar_string
  }

  def get_bar(pre:String,bar:String):String={
    bar_macros("1")=pre;
    var bar_string =try{ Language.bars(bar)}catch{case ex:Exception => bar }
    for(match_string <- """%\w+%""".r.findAllIn(bar_string))yield
      bar_string = """%\w+%""".r.replaceFirstIn(bar_string,bar_macros(match_string))
    return bar_string
  }

}