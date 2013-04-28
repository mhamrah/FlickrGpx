package com.michaelhamrah.flickrgpx
import org.specs2.mutable._
import com.github.nscala_time.time.Imports._
import com.michaelhamrah.flickrgpx.GpxFile._

class GpxFileSpec extends Specification {
  "The GpxFile class" should {
    "return all waypoints from an input file" in {
      val data = GetWaypointsFromGpxFile(getClass.getResource("/Se2.gpx").getFile).get

      data.length must_== 102
    }

    "return none if the file isn't there" in {
      val data = GetWaypointsFromGpxFile("nofile")

      data must_== None
    }

    "return a waypoint from an xml node" in {
      val waypoint = BuildWaypoint(
        <wpt lat="13.427439332" lon="103.859596252">
          <ele>29.132250</ele>
          <time>2012-12-08T03:22:17Z</time>
          <name>Angkor</name>
          <sym>Green Flag</sym>
          <extensions>
            <delorme:objectid>1dbf453a-1dd2-11b2-8546-4d4845395644</delorme:objectid>
            <delorme:pnsymbol>17</delorme:pnsymbol>
          </extensions>
        </wpt>)

      waypoint must_!= null
      waypoint.lat mustEqual 13.427439332
      waypoint.long mustEqual 103.859596252
      waypoint.elev mustEqual 29.132250
      waypoint.name mustEqual "Angkor"
      waypoint.uniqueId mustEqual "1dbf453a-1dd2-11b2-8546-4d4845395644"
      waypoint.time mustEqual "2012-12-08T03:22:17Z".toDateTime
    }
  }
}
