package cn

import java.io.{File, FileNotFoundException}
import java.util.logging.Level

import org.bukkit.configuration.file.YamlConfiguration

/**
  * Created by key_q on 2016/3/19.
  */
class Language(guild: Guild ,path :File) {
  import scala.collection.mutable.Map
  val bar_macros:Map[String,String]=Map()
  lazy val bars:Map[String,String]=Map(
    "Info_Create_Config"->"",
    "No_Lang"->"Not have %file% this language file.",
    "" ->""
  )
  def set_lang(language: String):Unit={
    try{
      val YamConfig = YamlConfiguration.loadConfiguration(new File(path,"language/"+language+".yml"))
      YamConfig.getMapList("language/"+language+".yml")
    }catch {
      case ex:FileNotFoundException => guild.getLogger.log(Level.WARNING,"No_Lang")
    }

  }
  def get_bar(bar:String):String={
    var bar_string = bars(bar)
    for(match_string <- """%[a-zA-Z*]%""".r.findAllIn(bar_string))yield {
      bar_string = """%[a-zA-Z*]%""".r.replaceAllIn(bar_string,bar_macros(match_string))
    }
    return bar_string
  }
}