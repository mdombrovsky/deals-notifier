package com.deals_notifier.query.model

class Item(textr: String) {

    var text: String = ""
        set(value) {
            field = value
            textNoSpaces = value.replace("\\s".toRegex(), "")
        }

    var textNoSpaces: String = ""

    init {
        this.text = textr
    }
}