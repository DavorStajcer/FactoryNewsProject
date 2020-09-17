package hr.davor_news.factory.fragments.displaying_error

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import hr.davor_news.factory.R


class DisplayingErrorDialog {

    internal var onDialogDismissedListener : IOnDialogDismissedListener? = null
    private val alertDialog : AlertDialog? = null

    fun getAlertDialog(context: Context) : AlertDialog {
        return alertDialog ?:  AlertDialog.Builder(context,R.style.NewsAlertDialog)
                    .setTitle("Greška")
                    .setMessage("Ups, došlo je do pogreške")
                    .setPositiveButton("U REDU",
                        DialogInterface.OnClickListener { dialog, which ->
                            onDialogDismissedListener?.onDialogDismissed()
                        })
            .setCancelable(false)
            .create()
    }
}

interface IOnDialogDismissedListener {
    fun onDialogDismissed()
}