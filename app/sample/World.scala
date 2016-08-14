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
  val actors = for (i <- 0 until dimention) yield system.actorOf(Props[CountingActor], name = "node" + i.toString)

  def await = {
    println("shuting down")
    system.shutdown
    println("waiting");
    system.awaitTermination
    
  }

}