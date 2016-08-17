package sample

object TestRunner extends App {
  
  val world = new TestWorld
  world.generateFriends
  world.await
  
}