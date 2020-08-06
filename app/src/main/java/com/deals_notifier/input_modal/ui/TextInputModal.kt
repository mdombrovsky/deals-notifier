package com.deals_notifier.input_modal.ui

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.EditText


fun textInputModal(
    context: Context, title: String, defaultValue:String = "", onSuccess: (String) -> Unit
): View.OnClickListener {
    return View.OnClickListener {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)

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
}
