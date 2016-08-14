package sample

import akka.actor.ActorSystem
import akka.actor.{ ActorRef, ActorSystem }
import sample.SpringExtension._
import scala.concurrent.duration._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent._
import scala.util._
import org.springframework.scala.context.function._
import CountingActor._

object World {
  val dimention = 5
  
  // create a spring context
  implicit val ctx = FunctionalConfigApplicationContext(classOf[AppConfiguration])

  import Config._
  // get hold of the actor system
  val system = ctx.getBean(classOf[ActorSystem])

  val prop = SpringExtentionImpl(system).props("countingActor")

  // use the Spring Extension to create props for a named actor bean
  val counter = system.actorOf(prop, "counter")

  val actors = for (i <- 0 until dimention) yield system.actorOf(prop, i.toString)

  def await = {
    system.shutdown
    system.awaitTermination
  }

}