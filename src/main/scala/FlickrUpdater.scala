package com.michaelhamrah.flickrgpx

import scala.xml._
import com.github.nscala_time.time.Imports._

import org.scribe.builder._
import org.scribe.builder.api._
import org.scribe.model._
import org.scribe.oauth._

import java.util._

object FlickrUpdater {

  def searchByDay(day: String) = {

    val request = getRequest

    request.addQuerystringParameter("method", "flickr.photos.search");
    request.addQuerystringParameter("user_id", "me")
    request.addQuerystringParameter("min_taken_date", s"${day} 00:00:00")
    request.addQuerystringParameter("max_taken_date", s"${day} 23:59:59")
    request.addQuerystringParameter("extras", "date_taken")
    request.addQuerystringParameter("per_page", "500")

    val response = getResponse(request)

    XML.loadString(response.getBody()) \ "photos"
    }
  
  def updateGpx(photo_id:String, waypoint:Waypoint) = {

    val request = getRequest

    request.addQuerystringParameter("method", "flickr.photos.geo.setLocation")
    request.addQuerystringParameter("photo_id", photo_id)
    request.addQuerystringParameter("lat", waypoint.lat.toString)
    request.addQuerystringParameter("lon", waypoint.long.toString)
    request.addQuerystringParameter("accuracy", "13")

    val response = getResponse(request)

    val stat = XML.loadString(response.getBody()) \ "@stat"
    println(s"photoId: ${photo_id}, ${stat}")

  }

  def getResponse(request: OAuthRequest) = {
    val secret = "7573c50cc6fb1820"
    val key = "eb1fe64060550dec4eec1519bf24ae9f"

    val service = new ServiceBuilder().provider(classOf[FlickrApi]).apiKey(key).apiSecret(secret).build()
    val token = new Token("72157633724877320-679bb3e4ffbad588" , "61e8d20f28cfe1c0")

    service.signRequest(token, request);
    request.send();


  }

  def getRequest() = {

    //val token = service.getRequestToken()

    //val authUrl = service.getAuthorizationUrl(token)

    //println(authUrl + "&perms=write")

    //val code = readLine()
    //val verifier = new Verifier(code)

    //val accessToken = service.getAccessToken(token, verifier)

    //println(accessToken)

    new OAuthRequest(Verb.GET, "http://api.flickr.com/services/rest/");

  }

  }

