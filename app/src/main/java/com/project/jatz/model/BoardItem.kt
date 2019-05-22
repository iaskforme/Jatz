package com.project.jatz.model

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("BoardItem")
class BoardItem: ParseObject() {

    /**
     * This method sets the board's title  put() is an inbuilt method in the ParseObject class
     */
    fun setTitle(title: String) = put("title", title)

    /**
     * This method sets the board's user put() is an inbuilt method in the ParseObject class
     */
    fun setUser(user: ParseUser) = put("createdBy",user)

    /**
     * This method returns the note's title getString() is an inbuilt method in the ParseObject class
     */
    fun getTitle() = getString("title")

    /**
     * This method returns the note's details getString() is an inbuilt method in the ParseObject class
     */
    fun getUser() = getParseUser("createdBy")
}