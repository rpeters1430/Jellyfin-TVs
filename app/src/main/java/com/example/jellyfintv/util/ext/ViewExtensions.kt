package com.example.jellyfintv.util.ext

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.jellyfintv.R

/**
 * Hides the soft keyboard.
 */
fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    // Find the currently focused view
    var view = currentFocus
    // If no view currently has focus, create a new one to prevent NPE
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Hides the soft keyboard from a view.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Shows the soft keyboard for a view.
 */
fun View.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Sets the visibility of the view to VISIBLE.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Sets the visibility of the view to INVISIBLE.
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Sets the visibility of the view to GONE.
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Sets the visibility of the view based on a condition.
 *
 * @param isVisible If true, sets visibility to VISIBLE, otherwise sets it to GONE.
 */
fun View.visibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * Sets the visibility of the view based on a condition.
 *
 * @param isVisible If true, sets visibility to VISIBLE, otherwise sets it to INVISIBLE.
 */
fun View.visibleOrInvisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

/**
 * Sets a click listener with debounce to prevent multiple rapid clicks.
 *
 * @param debounceTime The minimum time between clicks in milliseconds.
 * @param onClick The action to perform when the view is clicked.
 */
fun View.setSafeOnClickListener(debounceTime: Long = 500L, onClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(debounceTime) {
        onClick(it)
    }
    setOnClickListener(safeClickListener)
}

/**
 * A click listener that prevents multiple rapid clicks.
 */
class SafeClickListener(
    private val debounceTime: Long = 500L,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {
    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            onSafeClick(v)
        }
    }
}

/**
 * Replaces a fragment in the specified container.
 *
 * @param containerId The ID of the container to replace the fragment in.
 * @param fragment The fragment to replace with.
 * @param addToBackStack Whether to add the transaction to the back stack.
 * @param tag Optional tag for the fragment.
 */
fun FragmentActivity.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String? = null
) {
    supportFragmentManager.beginTransaction()
        .replace(containerId, fragment, tag)
        .apply {
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
        .commitAllowingStateLoss()
}

/**
 * Replaces a fragment in the specified container.
 *
 * @param containerId The ID of the container to replace the fragment in.
 * @param fragment The fragment to replace with.
 * @param addToBackStack Whether to add the transaction to the back stack.
 * @param tag Optional tag for the fragment.
 */
fun Fragment.replaceFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String? = null
) {
    parentFragmentManager.beginTransaction()
        .replace(containerId, fragment, tag)
        .apply {
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
        .commitAllowingStateLoss()
}

/**
 * Adds a fragment to the specified container.
 *
 * @param containerId The ID of the container to add the fragment to.
 * @param fragment The fragment to add.
 * @param addToBackStack Whether to add the transaction to the back stack.
 * @param tag Optional tag for the fragment.
 */
fun FragmentActivity.addFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String? = null
) {
    supportFragmentManager.beginTransaction()
        .add(containerId, fragment, tag)
        .apply {
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
        .commitAllowingStateLoss()
}

/**
 * Adds a fragment to the specified container.
 *
 * @param containerId The ID of the container to add the fragment to.
 * @param fragment The fragment to add.
 * @param addToBackStack Whether to add the transaction to the back stack.
 * @param tag Optional tag for the fragment.
 */
fun Fragment.addFragment(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String? = null
) {
    childFragmentManager.beginTransaction()
        .add(containerId, fragment, tag)
        .apply {
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
        .commitAllowingStateLoss()
}

/**
 * Shows a snackbar with the given message.
 *
 * @param message The message to show.
 * @param duration How long to display the message. Default is [androidx.core.widget.ToastCompat.LENGTH_SHORT].
 */
fun View.showSnackbar(
    message: String,
    duration: Int = android.widget.Toast.LENGTH_SHORT
) {
    val snackbar = com.google.android.material.snackbar.Snackbar.make(this, message, duration)
    snackbar.show()
}

/**
 * Shows a snackbar with the given string resource.
 *
 * @param messageRes The string resource ID of the message to show.
 * @param duration How long to display the message. Default is [androidx.core.widget.ToastCompat.LENGTH_SHORT].
 */
fun View.showSnackbar(
    @androidx.annotation.StringRes messageRes: Int,
    duration: Int = android.widget.Toast.LENGTH_SHORT
) {
    showSnackbar(context.getString(messageRes), duration)
}
