package sample

import java.util.concurrent.atomic.AtomicInteger
import org.specs2.internal.scalaz.Forall

class TestWorld extends World {

  override def generateFriends = {
    val friends = super.generateFriends

    friends.zipWithIndex map {
      case (node, index) =>
        node map {
          id =>
            friends(id) match {
              case list if !list.contains(index) => println("Error in generating friends")
              case _                          =>
            }
        }
    }

    friends
  }

}
