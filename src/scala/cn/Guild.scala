package cn

/**
  * Created by key_q on 2016/3/19.
  */

import java.io.File
import java.util
import cn._Guild.Guild_Commands
import org.bukkit.plugin.java.JavaPlugin
import scala.collection.JavaConverters._
import scala.collection.mutable

class Guild extends JavaPlugin{
  val select_language =new Language(this,_:String)
  override  def onLoad(): Unit ={
    val config_file=  new File(getDataFolder,"config.yml")
    this.getDataFolder.mkdir
    if(!config_file.createNewFile)getConfig.getKeys(false).asScala.map(m => Guild.config(m)=getConfig.get(m))
    else getLogger.info("initialized config.yml")
    Guild.config.map( m =>getConfig.set(m._1,m._2))
    saveConfig
    select_language(Guild.config("language").asInstanceOf[String])
    Language.set_macro("money",Guild.config.get("Create_Guild_Money").asInstanceOf[String])
    getLogger.info(Language.get_bar("Finish_Load_Config"))

  }
  override def onEnable():Unit= {
    val setup=new Guild_Setup(this,this.getLogger)
    setup.setupPermissions
    setup.setupChat
    setup.setupEconomy
    setup.setupGuild_Data
    this.getCommand("Guild").setExecutor(new Guild_Commands(setup))
  }
  override def onDisable():Unit={
    getLogger.info("onDisable has been invoked!")
  }
}

object Guild {
  trait Massge_type
  case object INFO extends Massge_type
  case object WARN extends Massge_type
  case object ERROR extends Massge_type
  lazy val config: mutable.Map[String, Any] = mutable.Map(
    "language"->"zh",
    "yooo" -> "dsadsa",
    "Create_Guild_Money" -> 50000,
    "Guild-Max-People" -> 50,
    "yo" -> true
  )
  def get_Config_asJava():util.Map[String, Any] = config.asJava
}