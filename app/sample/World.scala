package sample

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

import akka.actor.ActorSystem
import akka.actor.Props

class World {
  val dimention = 50
  val minfriendsAmount = 10
  val system = ActorSystem("FastRank")
  val connections = generateFriends
  val actors = 0.until(dimention).map(id => system.actorOf(Props(new CountingActor(id, connections(id).toList)), name = "node" + id.toString))

  def await = {
    system.shutdown
    system.awaitTermination
  }

  def generateFriends = {
    var connections = 0.until(dimention).map { _ => new ArrayBuffer[Int] }

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

    0.until(dimention) foreach {
      i =>
        while (connections(i).length < minfriendsAmount) {
          addFriend(i)
        }
    }

    connections
  }

}

object World extends World