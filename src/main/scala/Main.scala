package com.michaelhamrah.flickrgpx

import com.michaelhamrah.flickrgpx.GpxFile._

object Main {
  def main(args: Array[String]) {


    //Loop through all files, pull out waypoints, and get distinct results
    //We need the getOrElse otherwise flatMap won't concat the lists.
    val waypoints = args.flatMap(file => GetWaypointsFromGpxFile(file).getOrElse(Nil)).distinct
    
    println(waypoints.size)

  }

 }
