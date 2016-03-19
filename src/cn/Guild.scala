package cn

/**
  * Created by key_q on 2016/3/19.
  */
import java.util.logging.Level
import org.bukkit.plugin.java.JavaPlugin
import scala.collection.JavaConverters._
import scala.collection.mutable

class Guild extends JavaPlugin{
  lazy val config_handle= this.getConfig

  override  def onLoad(): Unit ={
    getLogger.info("onLoad has been invoked!")
    getLogger.log(Level.WARNING,"yo")


  }
  override def onEnable():Unit= {
    getLogger.info("onEnable has been invoked!")

    this.saveConfig()
    this.saveDefaultConfig()
    config_handle.addDefaults(Guild.config.asJava)
    this.saveConfig()

  }
  override def onDisable():Unit={
    getLogger.info("onDisable has been invoked!")
    getLogger.log(Level.WARNING,"yo")
  }
}

object Guild{
  trait Massge_type
  case object INFO extends Massge_type
  case object WARN extends Massge_type
  case object ERROR extends Massge_type
  import scala.collection.mutable.Map
  lazy val config:mutable.Map[String, Object]=mutable.Map(
    "Create_Guild_Money" -> "50000",
    "Guild-Max-People" -> "50"
  )
}
