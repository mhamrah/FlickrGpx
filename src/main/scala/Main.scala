package com.michaelhamrah.flickrgpx

import com.michaelhamrah.flickrgpx.GpxFile._

object Main {
  def main(args: Array[String]) {

    //Loop through all files, pull out waypoints, and get distinct results
    //We need the getOrElse otherwise flatMap won't concat the lists.
    //Opportunity to refator this into a for-comp or something more scala-esque
    val waypoints = args.flatMap(GetWaypointsFromGpxFile(_).getOrElse(Nil)).distinct.groupBy(_.time.toString("yyyy-MM-dd"))
   


  }
}
