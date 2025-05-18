package com.example.jellyfintv.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.jellyfintv.R
import com.example.jellyfintv.util.UiMessageManager
import com.example.jellyfintv.util.UiMessageManager
import com.example.jellyfintv.util.ext.hideKeyboard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Base activity that provides common functionality for all activities in the app.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val uiMessageManager by lazy { UiMessageManager(this) }

    @get:LayoutRes
    protected abstract val layoutResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        setupActionBar()
        setupViews()
        setupViewModel()
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard()
    }

    /**
     * Sets up the action bar with default settings.
     */
    protected open fun setupActionBar() {
        // Can be overridden by subclasses to customize the action bar
    }

    /**
     * Sets up views and click listeners.
     */
    protected open fun setupViews() {
        // Can be overridden by subclasses to set up views
    }

    /**
     * Sets up the ViewModel and initializes any required data.
     */
    protected open fun setupViewModel() {
        // Can be overridden by subclasses to set up ViewModel
    }

    /**
     * Observes ViewModel LiveData and StateFlow objects.
     */
    protected open fun observeViewModel() {
        // Can be overridden by subclasses to observe ViewModel
    }

    /**
     * Shows a loading indicator.
     *
     * @param show Whether to show or hide the loading indicator.
     * @param message Optional message to display with the loading indicator.
     */
    protected open fun showLoading(show: Boolean, message: String? = null) {
        // Can be overridden by subclasses to show loading state
    }

    /**
     * Shows an error message to the user.
     *
     * @param message The error message to display.
     * @param actionText Optional action text for the error message.
     * @param action Optional action to perform when the action is clicked.
     */
    protected open fun showError(
        message: String,
        actionText: String? = null,
        action: (() -> Unit)? = null
    ) {
        // Can be overridden by subclasses to show error messages
        uiMessageManager.showError(message, actionText, action)
    }

    /**
     * Navigates to a new fragment.
     *
     * @param containerViewId The ID of the container to replace the fragment in.
     * @param fragment The fragment to navigate to.
     * @param addToBackStack Whether to add the transaction to the back stack.
     * @param tag Optional tag for the fragment.
     */
    protected fun navigateToFragment(
        @IdRes containerViewId: Int,
        fragment: Fragment,
        addToBackStack: Boolean = true,
        tag: String? = null
    ) {
        supportFragmentManager.beginTransaction()
            .replace(containerViewId, fragment, tag)
            .apply {
                if (addToBackStack) {
                    addToBackStack(tag)
                }
            }
            .commitAllowingStateLoss()
    }

    /**
     * Collects a Flow in a lifecycle-aware manner.
     *
     * @param flow The Flow to collect.
     * @param minActiveState The minimum lifecycle state in which the collection should occur.
     * @param collect The action to perform when a new value is emitted.
     */
    protected fun <T> Flow<T>.collectIn(
        owner: LifecycleOwner = this@BaseActivity,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        collect: suspend (T) -> Unit
    ) {
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(minActiveState) {
                collect(collect)
            }
        }
    }

    /**
     * Collects the latest value from a Flow in a lifecycle-aware manner.
     *
     * @param flow The Flow to collect.
     * @param minActiveState The minimum lifecycle state in which the collection should occur.
     * @param collect The action to perform when a new value is emitted.
     */
    protected fun <T> Flow<T>.collectLatestIn(
        owner: LifecycleOwner = this@BaseActivity,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        collect: suspend (T) -> Unit
    ) {
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(minActiveState) {
                collectLatest(collect)
            }
        }
    }

    /**
     * Sets the visibility of a view.
     *
     * @param viewId The ID of the view to update.
     * @param isVisible Whether the view should be visible.
     * @param isGone If true, view will be GONE when not visible, otherwise INVISIBLE.
     */
    protected fun setViewVisibility(@IdRes viewId: Int, isVisible: Boolean, isGone: Boolean = true) {
        findViewById<View>(viewId)?.visibility = when {
            isVisible -> View.VISIBLE
            isGone -> View.GONE
            else -> View.INVISIBLE
        }
    }

    /**
     * Sets a click listener on a view.
     *
     * @param viewId The ID of the view to set the click listener on.
     * @param onClick The action to perform when the view is clicked.
     */
    protected fun setOnClickListener(@IdRes viewId: Int, onClick: () -> Unit) {
        findViewById<View>(viewId)?.setOnClickListener { onClick() }
    }

    companion object {
        // Common request codes
        const val REQUEST_CODE_LOGIN = 1001
        const val REQUEST_CODE_PLAYER = 1002
        const val REQUEST_CODE_SETTINGS = 1003
    }
}
