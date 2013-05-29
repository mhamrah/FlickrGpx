package com.michaelhamrah.flickrgpx

import com.michaelhamrah.flickrgpx.GpxFile._
import com.michaelhamrah.flickrgpx.FlickrUpdater._

import com.github.nscala_time.time.Imports._

object Main {
  def main(args: Array[String]) {


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
       
        (results \ "photo").map(photo => {
          val dateTaken = new DateTime((photo \ "@datetaken").text.replace(' ', 'T'), DateTimeZone.forID("EST"))

          var currentDiff = 1000000000
          var currentWaypoint:Waypoint = null 

          day._2.map(wpt => {
            val diff = Math.abs(new Duration(wpt.time, dateTaken).toStandardSeconds.getSeconds()) 
            if(diff < currentDiff) {
              currentDiff = diff
              currentWaypoint = wpt
            }
          })

          println(s"${currentDiff} - ${photo \ "@id"} - ${currentWaypoint.time}" )
        })
    })
  }
}
