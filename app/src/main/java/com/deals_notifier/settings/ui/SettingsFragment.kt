package com.deals_notifier.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
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

        val notificationSwitch = view.notificationSwitch

        notificationSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            controller.setNotifications(isChecked)
        }

        return view
    }

}