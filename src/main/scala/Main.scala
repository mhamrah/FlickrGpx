package com.michaelhamrah.flickrgpx

import scala.Array.canBuildFrom

import com.github.nscala_time.time.Imports.DateTime
import com.github.nscala_time.time.Imports.DateTimeZone
import com.github.nscala_time.time.Imports.Duration
import com.michaelhamrah.flickrgpx.FlickrUpdater.searchByDay
import com.michaelhamrah.flickrgpx.GpxFile.GetWaypointsFromGpxFile

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.RoundRobinRouter
import dispatch.Defaults.executor
import dispatch.Http
import dispatch.as
import dispatch.implyRequestHandlerTuple
import dispatch.url

object Main {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("flickr-updater-system")
    val updater = system.actorOf(Props(new FlickrUpdateActor()).withRouter(RoundRobinRouter(nrOfInstances = 4))
, "flickr-updater")
    val files = Array(getClass.getResource("/PointFile.gpx").getFile, getClass.getResource("/Se2.gpx").getFile, getClass.getResource("/NorwaySEAsia.gpx").getFile)

    //Loop through all files, pull out waypoints, and get distinct results
    //We need the getOrElse otherwise flatMap won't concat the lists.
    //Opportunity to refator this into a for-comp or something more scala-esque
    val waypoints = files.flatMap(GetWaypointsFromGpxFile(_).getOrElse(Nil)).distinct.groupBy(_.time.toString("yyyy-MM-dd"))

    //waypoints.map(waypoint => println(s"${waypoint._1} - ${waypoint._2.size}"))

    waypoints.map(day => {
      val results = searchByDay(day._1)
      val count = results \ "@total"
      println(s"${day._1} - ${day._2.size} - ${count}")

      val loc = day._2.head
      val svc = url(s"https://maps.googleapis.com/maps/api/timezone/xml?location=${loc.lat},${loc.long}&timestamp=1331161200&sensor=false")
        val locTz = Http(svc OK as.xml.Elem)

        for(tzx <- locTz) {
          val tz = tzx \ "time_zone_id" text

          println(s"Timezone: $tz")

          (results \ "photo").map(photo => {
            val dateTaken = new DateTime((photo \ "@datetaken").text.replace(' ', 'T'), DateTimeZone.forID(tz))

            var currentDiff = 1000000000
            var currentWaypoint:Waypoint = null 

            day._2.map(wpt => {
              val diff = Math.abs(new Duration(wpt.time, dateTaken).toStandardSeconds.getSeconds()) 
              if(diff < currentDiff) {
                currentDiff = diff
                currentWaypoint = wpt
              }
            })

          //println(s"${currentDiff} - ${photo \ "@id"} - ${currentWaypoint.time}" )
          //

          updater ! UpdatePhoto((photo \ "@id").text, currentWaypoint)

          })
          }
    })
    }
  }

case class UpdatePhoto(photoId: String, waypoint: Waypoint)

class FlickrUpdateActor extends Actor {
  override def receive: Receive = {
    case UpdatePhoto(photoId, waypoint) => {
          FlickrUpdater.updateGpx(photoId, waypoint)
    }
  }
}
