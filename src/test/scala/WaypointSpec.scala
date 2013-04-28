package com.michaelhamrah.flickrgpx
import org.specs2.mutable._
import com.michaelhamrah.flickrgpx.GpxFile._

class WaypointSpec extends Specification {
  "A waypoint list" should {
    "combine to form unique elements" in {
      val data1 = GetWaypointsFromGpxFile(getClass.getResource("/NorwaySEAsia.gpx").getFile).get
      val data2 = GetWaypointsFromGpxFile(getClass.getResource("/PointFile.gpx").getFile).get


      val result = (data1 ++ data2).distinct

      result.size must_==999
    }
  }
}
