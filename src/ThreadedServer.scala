import java.io.PrintStream
import java.net.{Socket, ServerSocket}
import java.util.concurrent.{Executors, ExecutorService}

import scala.io.BufferedSource

/**
 * Created by Caleb Prior on 12-Oct-15.
 */
class ThreadedServer(port: Int, poolSize: Int) extends Runnable{
  val serverSocket = new ServerSocket(port);
  val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)

  //  def main(args: Array[String]) {
  //    val server = new ServerSocket(800)
  //    while (true) {
  //      val s = server.accept()
  //      val in = new BufferedSource(s.getInputStream).getLines()
  //      val out = new PrintStream(s.getOutputStream)
  //
  //      println(in.next())
  //      out.println("Server says hi")
  //      out.flush()
  //      s.close()
  //    }
  //  }

  override def run(){
    try{
      println("running!")
      while(true){
        val socket = serverSocket.accept()
        println("received")
        pool.execute(new SocketHandler(socket))
      }
    } finally {
      pool.shutdown()
    }
  }
}

class SocketHandler(socket: Socket) extends Runnable{
  def message = socket.getRemoteSocketAddress + " " + socket.getInetAddress + " " + socket.getLocalAddress + socket.getPort+ "\n"

  override def run(){
    val in = new BufferedSource(socket.getInputStream).getLines()
    val out = new PrintStream(socket.getOutputStream)
    println(in.next())
    out.println(message)
    out.flush()

    socket.close()
  }
}
