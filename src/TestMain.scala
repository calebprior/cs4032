/**
 * Created by Caleb Prior on 13-Oct-15.
 */
object TestMain {
  def main (args: Array[String]){
    new ThreadedServer(9000, 2).run();
  }
}
