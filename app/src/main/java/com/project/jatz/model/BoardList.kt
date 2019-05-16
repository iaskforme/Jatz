package com.project.jatz.model

import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("BoardList")
data class BoardList (var list: ArrayList<BoardItem>): ParseObject() {

}