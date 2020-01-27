package org.omgcobra.matchthese.fragments.pantry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.omgcobra.matchthese.dao.AppRepository

class PantryViewModel(application: Application): AndroidViewModel(application) {
    private val pantryList = AppRepository.getPantry()

    fun getPantryList() = pantryList
}