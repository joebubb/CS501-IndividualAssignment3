package com.example.staggeredphotogallery.data

import android.content.Context
import com.example.staggeredphotogallery.R
import org.xmlpull.v1.XmlPullParser

// data class for photos
data class Photo(
    val title: String,
    val file: String
)

class PhotoRepository {
    fun getPhotos(context: Context): List<Photo> {
        // initialize a list for photos
        val photos = mutableListOf<Photo>()

        // set up a parser and event type variable
        val parser = context.resources.getXml(R.xml.photos)
        var event = parser.eventType

        // store title and filename in variables
        var nextTitle: String = ""
        var nextFile: String = ""

        while (event != XmlPullParser.END_DOCUMENT) { // loop until end of xml
            if (event == XmlPullParser.START_TAG) {
                // get the title and filename. ignore photo tag
                if (parser.name == "title") {
                    nextTitle = parser.nextText()
                } else if (parser.name == "file") {
                    nextFile = parser.nextText()
                }
            } else if (event == XmlPullParser.END_TAG) {
                // if we see the photo end tag, add one to the list
                if (parser.name == "photo") {
                    photos.add(Photo(title = nextTitle, file = nextFile))
                }
            }
            event = parser.next()
        }

        return photos
    }
}