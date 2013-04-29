import org.scalatest.FreeSpec
import org.scalatest.matchers._
import com.michaelhamrah.flickrgpx._
import org.scalamock.scalatest.MockFactory

class FlickrUpdaterSpec extends FreeSpec with ShouldMatchers with MockFactory {

  "FlickrUpdater" - {
    "should return images for a particular day" in {
      val fu = new FlickrUpdater

      val images = fu.getPhotos("2012-12-26")

      images should not be Nil
      images.size should be > (0)

    }
  }
}

