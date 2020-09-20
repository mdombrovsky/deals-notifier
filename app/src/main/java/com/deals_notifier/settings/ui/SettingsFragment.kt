package com.deals_notifier.settings.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.deals_notifier.R
import com.deals_notifier.settings.controller.SettingsFragmentController
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment(val controller: SettingsFragmentController) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val notificationSwitch: SwitchCompat = view.notificationSwitch

        val frequencySpinner: Spinner = view.frequencySpinner

        notificationSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            controller.setNotifications(isChecked)
            frequencySpinner.isEnabled = controller.frequencySpinnerEnabled()

        }

        ArrayAdapter.createFromResource(
            this.context as Context,
            R.array.notification_frequency_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            frequencySpinner.adapter = it
        }

        frequencySpinner.onItemSelectedListener = FrequencySpinnerListener()
        frequencySpinner.isEnabled.also {
            controller.setFrequency(frequencySpinner.selectedItem.toString())
        }
        frequencySpinner.isEnabled = controller.frequencySpinnerEnabled()

        return view
    }


    private inner class FrequencySpinnerListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            val item: String = parent?.getItemAtPosition(position)?.toString() as String
            controller.setFrequency(item)
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

    }
}