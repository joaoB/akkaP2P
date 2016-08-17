package sample

import scala.collection.mutable.ListBuffer
import scala.util.Random
import akka.actor.Actor

object CountingActor {

  object COUNT
  object GETMESSAGES
  object GET
  object GETID
}

class CountingActor(id: Int, friends: List[Int]) extends Actor {

  import CountingActor._

  var countingService: CountingService = _
  private var count = 0
  private var messages = new ListBuffer[Int]()
  private var scores : Map[Int, Int] = _
    
  def receive = {
    case i: Int if !messages.contains(i) => {

      messages.+=(i)
      
      //find n random actors and forward the message 
      Random.shuffle(friends).take(3).map { World.actors(_) ! i }

    }
    case GETID       => sender ! id
    case COUNT       => count = countingService.increment(count)
    case GET         => sender ! count
    case GETMESSAGES => sender ! messages
  }
}
