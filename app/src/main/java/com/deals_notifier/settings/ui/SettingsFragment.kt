package com.deals_notifier.settings.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deals_notifier.R
import com.deals_notifier.scraper.ui.ScraperInputModal
import com.deals_notifier.settings.controller.SettingsFragmentController
import com.deals_notifier.settings.model.SettingsSingleton
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment(val controller: SettingsFragmentController) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        /* notification switch button from view */
        val notificationSwitch: SwitchCompat = view.notificationSwitch

        /* Power Saver Switch reference from view */
        val powerSaverSwitch: SwitchCompat = view.powerSaverSwitch

        /* Dark Mode switch reference from view */
        val darkModeSwitch: SwitchCompat = view.darkModeSwitch

        val frequencySpinner: Spinner = view.frequencySpinner

        notificationSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            controller.setNotifications(isChecked)
            frequencySpinner.isEnabled = controller.frequencySpinnerEnabled()
        }

        /*  Power Saver Listener */
        powerSaverSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            controller.setPowerSaver(isChecked)
        }

        darkModeSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            controller.setDarkMode(isChecked)
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

        val scraperAdapter = controller.getScraperAdapter()
        val recyclerView: RecyclerView = view.scraperRecyclerView
        recyclerView.apply {
            adapter = scraperAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        val addScraperButton: Button = view.addScrapperButton
        addScraperButton.setOnClickListener {
            ScraperInputModal.launchModal(this.context as Context) {
                scraperAdapter.controller.add(it)
            }
        }

        val resetToDefaultScraperButton: Button = view.resetScraperToDefaultButton
        resetToDefaultScraperButton.setOnClickListener {
            scraperAdapter.controller.resetToDefault()
        }

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