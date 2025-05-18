package com.example.jellyfintv.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.jellyfintv.R
import com.google.android.material.snackbar.Snackbar

/**
 * Helper class for showing user messages (errors, info, etc.) in a consistent way.
 */
class UiMessageManager(private val context: Context) {

    private var currentSnackbar: Snackbar? = null

    /**
     * Shows an error message to the user.
     *
     * @param message The error message to display.
     * @param actionText Optional action text for the error message.
     * @param action Optional action to perform when the action is clicked.
     */
    fun showError(
        message: String,
        actionText: String? = null,
        action: (() -> Unit)? = null
    ) {
        // Dismiss any currently showing snackbar
        currentSnackbar?.dismiss()

        // Create and show a new snackbar
        val snackbar = Snackbar.make(
            (context as FragmentActivity).findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )

        // Set action if provided
        if (actionText != null && action != null) {
            snackbar.setAction(actionText) {
                action.invoke()
            }
        }

        // Show the snackbar and store a reference to it
        snackbar.show()
        currentSnackbar = snackbar
    }

    /**
     * Shows an error message with a string resource ID.
     *
     * @param messageResId The string resource ID of the error message.
     * @param actionTextResId Optional string resource ID for the action text.
     * @param action Optional action to perform when the action is clicked.
     */
    fun showError(
        @StringRes messageResId: Int,
        @StringRes actionTextResId: Int = 0,
        action: (() -> Unit)? = null
    ) {
        val actionText = if (actionTextResId != 0) {
            context.getString(actionTextResId)
        } else {
            null
        }
        showError(context.getString(messageResId), actionText, action)
    }

    /**
     * Shows an informational message to the user.
     *
     * @param message The message to display.
     * @param duration How long to display the message. One of [Snackbar.LENGTH_SHORT],
     * [Snackbar.LENGTH_LONG], or a custom duration in milliseconds.
     */
    fun showMessage(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        currentSnackbar?.dismiss()
        val snackbar = Snackbar.make(
            (context as FragmentActivity).findViewById(android.R.id.content),
            message,
            duration
        )
        snackbar.show()
        currentSnackbar = snackbar
    }

    /**
     * Shows a confirmation dialog to the user.
     *
     * @param title The title of the dialog.
     * @param message The message to display in the dialog.
     * @param positiveButtonText The text for the positive button.
     * @param negativeButtonText The text for the negative button.
     * @param onPositiveClick Callback for when the positive button is clicked.
     * @param onNegativeClick Callback for when the negative button is clicked.
     * @param cancelable Whether the dialog is cancelable.
     */
    fun showConfirmationDialog(
        title: String,
        message: String,
        positiveButtonText: String = context.getString(android.R.string.ok),
        negativeButtonText: String = context.getString(android.R.string.cancel),
        onPositiveClick: (() -> Unit)? = null,
        onNegativeClick: (() -> Unit)? = null,
        cancelable: Boolean = true
    ) {
        AlertDialog.Builder(context, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                onPositiveClick?.invoke()
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                onNegativeClick?.invoke()
            }
            .setCancelable(cancelable)
            .show()
    }

    /**
     * Shows a confirmation dialog with string resource IDs.
     *
     * @param titleResId The string resource ID for the dialog title.
     * @param messageResId The string resource ID for the dialog message.
     * @param positiveButtonTextResId The string resource ID for the positive button text.
     * @param negativeButtonTextResId The string resource ID for the negative button text.
     * @param onPositiveClick Callback for when the positive button is clicked.
     * @param onNegativeClick Callback for when the negative button is clicked.
     * @param cancelable Whether the dialog is cancelable.
     */
    fun showConfirmationDialog(
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        @StringRes positiveButtonTextResId: Int = android.R.string.ok,
        @StringRes negativeButtonTextResId: Int = android.R.string.cancel,
        onPositiveClick: (() -> Unit)? = null,
        onNegativeClick: (() -> Unit)? = null,
        cancelable: Boolean = true
    ) {
        showConfirmationDialog(
            title = context.getString(titleResId),
            message = context.getString(messageResId),
            positiveButtonText = context.getString(positiveButtonTextResId),
            negativeButtonText = context.getString(negativeButtonTextResId),
            onPositiveClick = onPositiveClick,
            onNegativeClick = onNegativeClick,
            cancelable = cancelable
        )
    }

    /**
     * Dismisses any currently showing snackbar.
     */
    fun dismissCurrentSnackbar() {
        currentSnackbar?.dismiss()
        currentSnackbar = null
    }
}
