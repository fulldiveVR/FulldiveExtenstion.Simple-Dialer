package com.full.dialer.top.secure.encrypted.dialogs

import android.annotation.SuppressLint
import android.telecom.PhoneAccountHandle
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import com.simplemobiletools.commons.activities.BaseSimpleActivity
import com.simplemobiletools.commons.extensions.getAlertDialogBuilder
import com.simplemobiletools.commons.extensions.setupDialogStuff
import com.full.dialer.top.secure.encrypted.R
import com.full.dialer.top.secure.encrypted.extensions.config
import com.full.dialer.top.secure.encrypted.extensions.getAvailableSIMCardLabels
import kotlinx.android.synthetic.main.dialog_select_sim.view.*

@SuppressLint("MissingPermission")
class SelectSIMDialog(val activity: BaseSimpleActivity, val phoneNumber: String, val callback: (handle: PhoneAccountHandle?) -> Unit) {
    private var dialog: AlertDialog? = null
    private val view = activity.layoutInflater.inflate(R.layout.dialog_select_sim, null)

    init {
        val radioGroup = view.select_sim_radio_group
        view.apply {
            select_sim_remember_holder.setOnClickListener {
                select_sim_remember.toggle()
            }
        }

        activity.getAvailableSIMCardLabels().forEachIndexed { index, SIMAccount ->
            val radioButton = (activity.layoutInflater.inflate(R.layout.radio_button, null) as RadioButton).apply {
                text = "${index + 1} - ${SIMAccount.label}"
                id = index
                setOnClickListener { selectedSIM(SIMAccount.handle, SIMAccount.label) }
            }
            radioGroup!!.addView(radioButton, RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }

        activity.getAlertDialogBuilder()
            .apply {
                activity.setupDialogStuff(view, this) { alertDialog ->
                    dialog = alertDialog
                }
            }
    }

    private fun selectedSIM(handle: PhoneAccountHandle, label: String) {
        if (view.select_sim_remember.isChecked) {
            activity.config.saveCustomSIM(phoneNumber, label)
        }

        callback(handle)
        dialog?.dismiss()
    }
}
