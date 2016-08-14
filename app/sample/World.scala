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
import scala.collection.mutable.ListBuffer

object World {
  val dimention = 50
  val minfriendsAmount = 10
  val system = ActorSystem("FastRank")
  val emptyList = Nil
  val actors = for (i <- 0 until dimention) yield system.actorOf(Props(new CountingActor(emptyList)), name = "node" + i.toString)

  def await = {
    system.shutdown
    system.awaitTermination
  }

  def generateFriends = {
    var connections = new ListBuffer[ListBuffer[Int]]()
    def addFriend(index: Int): Int = {
      var r = new Random()
      val f = r.nextInt(dimention)
      connections(index).+=:(connections(index) match {
        case c if !c.contains(f) => f
        case _                   => addFriend(index)
      })
      f
    }
    val friends = for (i <- 0 until dimention) yield {
      while (connections(i).length < minfriendsAmount) {
        addFriend(i)
      }
    }

  }

}