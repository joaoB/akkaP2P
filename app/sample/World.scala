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
import akka.actor.Props

object World {
  val dimention = 5
  val system = ActorSystem("FastRank")
  val emptyList = Nil
  val actors = for (i <- 0 until dimention) yield system.actorOf(Props(new CountingActor(emptyList)), name = "node" + i.toString)

  def await = {
    system.shutdown
    system.awaitTermination
    
  }

}