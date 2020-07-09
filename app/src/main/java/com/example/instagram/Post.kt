package com.example.instagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.util.*

@ParseClassName("Post")
class Post : ParseObject() {

    companion object {
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
        const val KEY_CREATED_AT = "createdAt"
    }

    var description: String?
        get() = getString(KEY_DESCRIPTION)
        set(description) {
            put(KEY_DESCRIPTION, description!!)
        }

    var image: ParseFile?
        get() = getParseFile(KEY_IMAGE)
        set(parseFile) {
            put(KEY_IMAGE, parseFile!!)
        }

    var user: ParseUser?
        get() = getParseUser(KEY_USER)
        set(parseUser) {
            put(KEY_USER, parseUser!!)
        }

    var timeStamp: Date?
        get() = createdAt
        set(value) {
            if (value != null) {
                put(KEY_CREATED_AT, value)
            }
        }



}