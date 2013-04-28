package com.michaelhamrah.flickrgpx
import scala.xml._
import com.github.nscala_time.time.Imports._
import scala.language.implicitConversions

object GpxFile {
  implicit def NodeSeqToDouble(value: NodeSeq) = {
    value.text match {
      case "" => 0.0
      case s:String => s.toDouble
    }
  }

  def GetWaypointsFromGpxFile(file: String): Option[Seq[Waypoint]] = {

    try {
      val data = XML.loadFile(file) 
      
      val waypoints = (data \ "wpt") filter (node => (node \ "time").nonEmpty) map (node => BuildWaypoint(node))

      Some(waypoints)
    }
    catch {
      case ex : Throwable => println(ex); return None
    }

  }

  def BuildWaypoint(node: Node) = {
    Waypoint(
      long = (node \ "@lon")
      ,lat = (node \ "@lat")
      ,elev = (node \ "ele")
      ,name = (node \ "name").text
      ,uniqueId = (node \ "extensions" \ "objectid").text
      ,time = (node \ "time").text.toDateTime 
      )
  }

}

