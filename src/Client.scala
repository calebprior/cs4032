import java.io.PrintWriter
import java.net.{InetAddress, Socket}

import scala.io.BufferedSource

/**
 * Created by Caleb Prior on 12-Oct-15.
 */
object EchoClient {
  def main(args: Array[String]) {
    val s = new Socket(InetAddress.getByName("localhost"), 9000)
    lazy val in = new BufferedSource(s.getInputStream).getLines()
    val out = new PrintWriter(s.getOutputStream)

    val message = io.StdIn.readLine("Enter Message> ")
    out.println("GET /echo.php?message=" + message +" \n")
    out.flush()

    println("Sent: " + message)
    println("Received: " + in.next)
    s.close()
  }
}