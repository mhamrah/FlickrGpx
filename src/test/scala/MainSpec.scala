import org.scalatest.FreeSpec
import org.scalatest.matchers._
import com.michaelhamrah.flickrgpx._
import org.scalamock.scalatest.MockFactory

class MainSpec extends FreeSpec with ShouldMatchers with MockFactory {

  "The Main Class" - {
    "should accept a list of files" in {
      val files = Array(getClass.getResource("/Se2.gpx").getFile, getClass.getResource("/NorwaySEAsia.gpx").getFile)

      files should have size 2 

      //TODO: We need to mock gpxfactory.
    }
    }
  }
