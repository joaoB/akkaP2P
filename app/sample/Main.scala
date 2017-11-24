package sample

import scala.util.Failure
import scala.util.Success
import CountingActor.GETMESSAGES
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout

object Main extends App {

  import scala.util.Random
  val r = new Random()
  val sourceId = 9999999
  val messageNum = 500
  for (i <- 1 to messageNum) {
    World.actors(Math.abs(r.nextInt() % World.dimention)) ! Packet(i, sourceId)
  }

  import Config._
  implicit val timeOut : Timeout = Timeout(20000)
  //val result = (World.actors(0) ? GETMESSAGES).mapTo[scala.collection.mutable.ListBuffer[Int]]


  // print the result
 /* result onComplete {
    case Success(result) => {
      //val ordered = result.sorted
      println("Size of messages: " + messageNum)
      println("received: " + result.size)

    }
    case Failure(failure) => println(s"Got an exception $failure")
  }*/

  World.generateFriends
  
  World.await
}

case class Packet(message: Int, senderId : Int)