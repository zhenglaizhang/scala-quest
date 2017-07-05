package net.zhenglai.quest.akka.extensions

import scala.collection.JavaConverters._

import akka.actor.{ Actor, ActorLogging, ExtendedActorSystem, Extension, ExtensionId }
import com.typesafe.config.Config

/**
  * Extensions are loaded once per ActorSystem, either on-demand or at ActorSystem creation time.
  *
  * By default extensions are loaded on-demand.
  */

class SettingsExtensionImpl(config: Config) extends Extension {

  object application {
    private[this] val appConfig = config.getConfig("application")
    val name = appConfig.getString("name")
  }

  object kafka {
    private[this] val kafkaConfig = config.getConfig("kafka")

    object producer {
      private[this] val producerConfig = kafkaConfig.getConfig("producer")
      val bootstrapServers = producerConfig.getString("bootstrap.servers")
      val acks = producerConfig.getString("acks")
      val retries = producerConfig.getInt("retries")
      val batchSize = producerConfig.getInt("batch.size")
      val lingerMs = producerConfig.getInt("linger.ms")
      val bufferMemory = producerConfig.getInt("buffer.memory")
    }

    object consumer {
      private val consumerConfig = kafkaConfig.getConfig("consumer")
      val bootstrapServers = consumerConfig.getString("bootstrap.servers")
      val groupId = consumerConfig.getString("group.id")
      val topics = consumerConfig.getStringList("topics").asScala.toList
    }

  }

}

object SettingsExtension extends ExtensionId[SettingsExtensionImpl] {
  override def createExtension(system: ExtendedActorSystem) = new SettingsExtensionImpl(system.settings.config)
}

trait SettingsActor {
  _: Actor =>

  val settings = SettingsExtension(context.system)
}


// demo client usage
class ConsumerActor extends Actor with SettingsActor with ActorLogging {
  import settings.kafka._

  override def receive = ???
}

