package com.deals_notifier.querry.model

class Item(textr: String) {

    var text: String =""
        public set(value) {
            field = value
            textNoSpaces = value.replace("\\s".toRegex(), "")
        }
        public get

    var textNoSpaces: String=""

    init {
        this.text = textr
    }
}