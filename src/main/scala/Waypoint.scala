package com.michaelhamrah.flickrgpx
import com.github.nscala_time.time.Imports._

case class Waypoint(lat: Double, long: Double, elev: Double, name: String, uniqueId: String, time: DateTime) 
