package sample

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

import akka.actor.ActorSystem
import akka.actor.Props

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
    var connections = new ArrayBuffer[ArrayBuffer[Int]]

    def addFriend(index: Int): Int = {
      val r = new Random()
      val f = r.nextInt(dimention)
      val toAdd = connections(index) match {
        case c if !c.contains(f) && index != f => {
          connections(index).+=(f)
          connections(f).+=(index)
          f
        }
        case _ => addFriend(index)
      }
      toAdd
    }

    for (i <- 0 until dimention)
      connections.+=:(new ArrayBuffer[Int])

    for (i <- 0 until dimention) {
      while (connections(i).length < minfriendsAmount) {
        addFriend(i)
      }
      println("asdasd: " + i + " ==== " + connections(i))
    }
    connections
  }

}