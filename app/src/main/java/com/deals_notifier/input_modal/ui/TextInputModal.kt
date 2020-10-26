package com.deals_notifier.input_modal.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText


fun textInputModal(
    context: Context,
    title: String,
    message: String? = null,
    defaultValue: String = "",
    onSuccess: (String) -> Unit
) {

    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)

    if (message != null) {
        builder.setMessage(message)
    }

    val input = EditText(context)
    input.inputType = InputType.TYPE_CLASS_TEXT
    input.setText(defaultValue)

    builder.setView(input)

    builder.setPositiveButton("OK") { dialog, which ->
        dialog.dismiss()
        onSuccess(input.text.toString())
    }

    builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

    builder.show()
}

fun buttonInputModal(
    context: Context, title: String, items: Array<String>, onSuccess: (Int) -> Unit
) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setItems(
        items
    ) { dialog: DialogInterface, i: Int ->
        dialog.dismiss()
        onSuccess(i)
    }

    builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

    builder.show()
}