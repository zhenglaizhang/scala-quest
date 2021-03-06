package net.zhenglai.quest.akka.extensions

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{ BeforeAndAfterAll, FunSuiteLike, Matchers }

class SettingsExtensionTest extends TestKit(ActorSystem("SettingsExtensionTest"))
  with FunSuiteLike
  with BeforeAndAfterAll
  with Matchers {

  override def afterAll() = TestKit.shutdownActorSystem(system)

  val settings = SettingsExtension(system)

  test("SettingsExtension to return the correct config values for application") {
    settings.application.name shouldBe "sample-application"
  }

  test("SettingsExtension to return the correct config values for kafka.producer") {
    val kakfaProducer = settings.kafka.producer

    kakfaProducer.bootstrapServers shouldBe "localhost:9092"
    kakfaProducer.acks shouldBe "all"
    kakfaProducer.batchSize shouldBe 16834
    kakfaProducer.bufferMemory shouldBe 33554432
    kakfaProducer.retries shouldBe 0
    kakfaProducer.lingerMs shouldBe 1
  }

  test("SettingsExtension to return the correct config values for kafka.consumer") {
    val kafkaConsumer = settings.kafka.consumer

    kafkaConsumer.bootstrapServers shouldBe "localhost:9092"
    kafkaConsumer.groupId shouldBe "sample-application"
    kafkaConsumer.topics shouldBe List("sample-topic")
  }
}
