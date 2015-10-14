import java.io.PrintStream
import java.net.{Socket, ServerSocket}
import java.util.concurrent.{Executors, ExecutorService}

import scala.io.BufferedSource

/**
 * Created by Caleb Prior on 12-Oct-15.
 */
object Server{
  var poolSize = 8
  val running = true
  var serverSocket : ServerSocket = null

  def main (args: Array[String]){
    val port = args(0)
    serverSocket = new ServerSocket(Integer.parseInt(port))
    val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)

    try{
      while(running){
        val socket = serverSocket.accept()
        println("received")
        pool.execute(new SocketHandler(socket, shutdown))
      }
    } catch {
      case e: Exception =>
        pool.shutdownNow()
    } finally {
      if(serverSocket != null){
        serverSocket.close()
      }
    }
  }

  def shutdown(): Unit ={
    serverSocket.close()
  }
}

class SocketHandler(socket: Socket, shutdown: () => Unit) extends Runnable{
  var studentId = "12345678"
  def helloMsg = "IP:"+ socket.getInetAddress + "\nPort:" + socket.getPort + "\nStudentID:" + studentId + "\n"
  var out : PrintStream = null

  override def run(){
    try{
      val in = new BufferedSource(socket.getInputStream).getLines()
      out = new PrintStream(socket.getOutputStream)

      val msg = in.next()
      println("Thread " + Thread.currentThread.getName + " Received:" + msg)

      handleMessage(msg)
    } catch {
      case e: Exception =>
        println("Thread " + Thread.currentThread.getName + " threw Exception:\n" + e)
    } finally {
      socket.close()
    }
  }

  def handleMessage(message: String): Unit ={
    if(isKillService(message)){
      killService()
    }else if (isHELO(message)){
      handleHELO(message)
    }
  }

  def isKillService(message: String): Boolean ={
    message.equals("KILL SERVICE")
  }

  def killService(): Unit ={
    shutdown()
  }

  def isHELO(message: String): Boolean ={
    message.startsWith("HELO ")
  }

  def handleHELO(message: String): Unit ={
    out.println(message+"\n"+helloMsg)
    out.flush()
  }
}
