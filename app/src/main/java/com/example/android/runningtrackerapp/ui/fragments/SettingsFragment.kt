package com.example.android.runningtrackerapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.runningtrackerapp.R
import com.example.android.runningtrackerapp.other.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPref: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayDataFields()

        btnApplyChanges.setOnClickListener {
            saveDataToSharedPrefs()
        }
    }

    private fun displayDataFields() {
        etName.setText(sharedPref.getString(Constants.KEY_NAME, ""))
        etWeight.setText(sharedPref.getFloat(Constants.KEY_WEIGHT, 80f).toString())
    }

    private fun saveDataToSharedPrefs() {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()

        if (name.isBlank() || weight.isBlank()) {
            Snackbar.make(requireView(), "Please fill in fields", Snackbar.LENGTH_SHORT).show()
        } else {
            savePrefs(name, weight)
            Snackbar.make(requireView(), "Saved!", Snackbar.LENGTH_SHORT).show()

            requireActivity().tvToolbarTitle.text = "Let's go, $name!"

            navigateAndPopBackStack()
        }

    }


    private fun savePrefs(name: String, weight: String) {
        sharedPref.edit()
            .putString(Constants.KEY_NAME, name)
            .putFloat(Constants.KEY_WEIGHT, weight.toFloat()) // inputType="numberDecimal"
            .putBoolean(Constants.KEY_FIRST_TIME, false)
            .apply()
    }


    private fun navigateAndPopBackStack() {
        findNavController().popBackStack(R.id.settingsFragment, true)
    }


}