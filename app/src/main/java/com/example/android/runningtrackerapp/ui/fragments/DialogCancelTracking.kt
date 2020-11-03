package com.example.android.runningtrackerapp.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.android.runningtrackerapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogCancelTracking : DialogFragment() {


    private var myListener: (() -> Unit)? = null


    fun setListener(listener: () -> Unit) {
        myListener = listener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel Run")
            .setMessage("Are you sure you want to cancel the Run?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                myListener?.let { it() }
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
    }
}