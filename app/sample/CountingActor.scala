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
  private var scores: Map[Int, Int] = friends.map(friend => friend -> 40).toMap

  def receive = {
    case p@Packet(i, senderId) if !messages.contains(i) =>

      //println(s"Received message $i from $senderId")
      messages.+=(i)

      gossipMessage(p)

    //find n random actors and forward the message
    /*      Random.shuffle(friends).take(3).foreach { friend =>
        World.actors(friend) ! Packet(i, id)
      }*/


    case GETID => sender ! id
    case COUNT => count = countingService.increment(count)
    case GET => sender ! count
    case GETMESSAGES =>
      println("Size of messages!!!: " + Main.messageNum)
      println("received: " + messages.size)
  }

  def gossipMessage(packet: Packet) {
    if (packet.senderId != Main.sourceId) {
      val newScore = scores(packet.senderId) + 10
      scores = scores.updated(packet.senderId, newScore)
    }

      scores foreach {
        case (friend, score) if score > Random.nextInt(100) =>
          decreaseScore(friend)
          World.actors(friend) ! Packet(packet.message, id)
        case _ => //do nothing
      }
  }

  def decreaseScore(i: Int) {
    val newScore = scores(i) - 2
    scores = scores.updated(i, newScore)
  }
}
