package sample

import akka.actor.{ ActorRef, ActorSystem }
import sample.SpringExtension._
import scala.concurrent.duration._
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent._
import scala.util._
import org.springframework.scala.context.function._
import CountingActor._

object Main extends App {

  // tell it to count three times
  World.actors(0) ! COUNT

  import scala.util.Random
  val r = new Random()
  for (i <- 1 to 50) {
    World.actors(Math.abs(r.nextInt() % 5)) ! i
  }
  implicit val ctx = FunctionalConfigApplicationContext(classOf[AppConfiguration])
  import Config._
  val result = (World.actors(0) ? GETMESSAGES).mapTo[scala.collection.mutable.ListBuffer[Int]]

  // print the result
  result onComplete {
    case Success(result)  => {
      val ordered = result.sorted
      println(s"Got back $ordered")
      println("Size of messages: " + ordered.length)
      
    }
    case Failure(failure) => println(s"Got an exception $failure")
  }

  World.await
}
