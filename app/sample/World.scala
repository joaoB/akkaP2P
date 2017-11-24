package sample

import java.util.concurrent.TimeUnit

import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Random, Success}
import akka.actor.ActorSystem
import akka.actor.Props
import sample.CountingActor.GETMESSAGES
import akka.pattern.ask
import akka.util.Timeout
import akka.actor.Actor
import akka.actor.Props

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

class World {
  val dimention = 500
  val minfriendsAmount = 10
  val system = ActorSystem("FastRank")
  val connections = generateFriends
  val actors = 0.until(dimention).map(id => system.actorOf(Props(new CountingActor(id, connections(id).toList)), name = "node" + id.toString))



  def await = {
    println("here")
    system.scheduler.schedule(FiniteDuration(3, "s"), FiniteDuration(3, "s"), World.actors(1), GETMESSAGES)
    //system.shutdown
    //system.awaitTermination
  }

  def generateFriends = {
    var connections = 0.until(dimention).map { _ => new ArrayBuffer[Int] }

    def addFriend(index: Int): Int = {
      val r = new Random()
      val f = r.nextInt(dimention)

      connections(index) match {
        case c if !c.contains(f) && index != f =>
          connections(index).+=(f)
          connections(f).+=(index)
          f
        case _ => addFriend(index)
      }

    }

    def generateFriendsAux(pivot: Int) : Unit = {
      if (connections(pivot).length < minfriendsAmount) {
        addFriend(pivot)
        generateFriendsAux(pivot)
      }

    }

    0.until(dimention) foreach {
      i => generateFriendsAux(i)
/*        while (connections(i).length < minfriendsAmount) {
          addFriend(i)
        }*/
    }

    connections.zipWithIndex map {
      case (node, index) =>
        node map {
          id =>
            connections(id) match {
              case list if !list.contains(index) => println("Error in generating friends")
              case _                          =>
            }
        }
    }

    connections
  }


}

object World extends World