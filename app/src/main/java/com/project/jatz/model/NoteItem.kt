package com.project.jatz.model


import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("NoteItem")
class NoteItem: ParseObject() {

    /**
     * This method sets the board's title put() is an inbuilt method in the ParseObject class
     */
    fun setTitle(title: String) = put("title", title)

    /**
     * This method sets the board's user put() is an inbuilt method in the ParseObject class
     */
    fun setUser(user: ParseUser) = put("createdBy",user)

    /**
     * This method sets the board's board put() is an inbuilt method in the ParseObject class
     */
    fun setBoard(board: ParseObject) = put("parentBoard",board)

    /**
     * This method sets the board's state put() is an inbuilt method in the ParseObject class
     */
    fun setState(state: String) = put("state", state)

    /**
     * This method sets the board's description put() is an inbuilt method in the ParseObject class
     */
    fun setDescription(description: String) = put("description", description)

    /**
     * This method sets the board's comment put() is an inbuilt method in the ParseObject class
     */
    fun setComment(comment: String) = put("comment", comment)

    /**
     * This method returns the note's title getString() is an inbuilt method in the ParseObject class
     */
    fun getTitle() = getString("title")

    /**
     * This method returns the note's user getParseUser() is an inbuilt method in the ParseObject class
     */
    fun getUser() = getParseUser("createdBy")

    /**
     * This method returns the note's board getString() is an inbuilt method in the ParseObject class
     */
    fun getBoard() = getParseObject("parentBoard")

    /**
     * This method returns the note's state getString() is an inbuilt method in the ParseObject class
     */
    fun getState() = getString("state")

    /**
     * This method sets the board's description put() is an inbuilt method in the ParseObject class
     */
    fun getDescription() = getString("description")

    /**
     * This method sets the board's comment put() is an inbuilt method in the ParseObject class
     */
    fun getComment() = getString("comment")
}
