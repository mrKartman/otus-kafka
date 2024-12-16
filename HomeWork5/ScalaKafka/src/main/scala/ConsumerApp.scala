//package ru.otusHomework.cherkashin
//
//import akka.NotUsed
//import akka.actor.ActorSystem
//import akka.kafka.scaladsl.Consumer
//import akka.kafka.{ConsumerSettings, Subscriptions}
//import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, ZipWith}
//import akka.stream.{ActorMaterializer, ClosedShape, Materializer}
//import ch.qos.logback.classic.{Level, Logger}
//import com.typesafe.config.ConfigFactory
//import org.apache.kafka.common.serialization.{IntegerDeserializer, StringDeserializer}
//import org.slf4j.LoggerFactory
//
//import scala.concurrent.ExecutionContextExecutor
//import scala.util.{Failure, Success}
//
//object ConsumerApp {
//  implicit val system: ActorSystem = ActorSystem("consumer-sys")
//  implicit val mat: Materializer = ActorMaterializer()
//  implicit val ec: ExecutionContextExecutor = system.dispatcher
//  LoggerFactory
//    .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
//    .asInstanceOf[Logger]
//    .setLevel(Level.ERROR)
//
//  val config = ConfigFactory.load()
//  val consumerConfig = config.getConfig("akka.kafka.consumer")
//  val consumerSettings = ConsumerSettings(consumerConfig, new StringDeserializer, new IntegerDeserializer)
//
//  val consume = Consumer
//    .plainSource(consumerSettings, Subscriptions.topics("test"))
//    .runWith(Sink.fromGraph(graph))
//
//  consume onComplete {
//    case Success(_) => println("Done"); system.terminate()
//    case Failure(err) => println(err.toString); system.terminate()
//  }
//
//  def main(args: Array[String]): Unit = {
//    RunnableGraph.fromGraph(graph).run()
//    system.terminate()
//  }
//
//  val graph =
//    GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
//      import GraphDSL.Implicits._
//
////      val input = builder.add(Source(1 to 10))
//      val broadcast = builder.add(Broadcast[Int](3))
//
//      val multiplyOnTen = builder.add(Flow[Int].map(_ * 10))
//      val multiplyOnTwo = builder.add(Flow[Int].map(_ * 2))
//      val multiplyOnThree = builder.add(Flow[Int].map(_ * 3))
//
//      val zip = builder.add(ZipWith[Int, Int, Int, String]((i1, i2, i3) => s"($i1, $i2, $i3)"))
//
//      val output = builder.add(Sink.foreach[String](println))
//
//      input ~> broadcast
//      broadcast.out(0) ~> multiplyOnTen ~> zip.in0
//      broadcast.out(1) ~> multiplyOnTwo ~> zip.in1
//      broadcast.out(2) ~> multiplyOnThree ~> zip.in2
//
//      zip.out ~> output
//
//      ClosedShape
//    }
//
//}