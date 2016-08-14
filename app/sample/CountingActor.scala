package sample

import akka.actor.Actor
import sample.SpringExtension._
import scala.collection.mutable.ListBuffer
import scala.util.Random


object CountingActor {

  object COUNT
  object GETMESSAGES
  object GET

}

class CountingActor extends Actor {

  import CountingActor._

  var countingService: CountingService = _
  private var count = 0
  private var messages = new ListBuffer[Int]()

  def receive = {
    case i: Int if !messages.contains(i) => {
      messages.+=(i)
      //find two random actors and forward the message
      Random.shuffle(World.actors).take(2).map { _ ! i }
    }
    case COUNT       => count = countingService.increment(count)
    case GET         => sender ! count
    case GETMESSAGES => sender ! messages
  }
}
