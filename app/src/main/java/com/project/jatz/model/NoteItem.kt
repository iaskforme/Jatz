package com.project.jatz.model

class NoteItem(title: String, description: String, comment: String){

    var noteTitle = ""
    var noteDescription = ""
    var noteComment = ""

    init {
        noteTitle = title
        noteDescription = description
        noteComment = comment
    }
}
