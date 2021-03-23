package com.deals_notifier.deal.model

interface DealServiceInterface {

    fun stopDealService()

    fun setNotificationFrequency(seconds:Int)
    fun setPowerSaving(enabled: Boolean)
}