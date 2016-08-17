package sample

import scala.util.Failure
import scala.util.Success

import CountingActor.GETMESSAGES
import akka.actor.actorRef2Scala
import akka.pattern.ask

object Main extends App {

  // tell it to count three times

  import scala.util.Random
  val r = new Random()
  for (i <- 1 to 50) {
    World.actors(Math.abs(r.nextInt() % 5)) ! i
  }

  import Config._
  val result = (World.actors(0) ? GETMESSAGES).mapTo[scala.collection.mutable.ListBuffer[Int]]

  // print the result
  result onComplete {
    case Success(result) => {
      val ordered = result.sorted
      println(s"Got back $ordered")
      println("Size of messages: " + ordered.length)

    }
    case Failure(failure) => println(s"Got an exception $failure")
  }

  World.generateFriends
  
  World.await
}
