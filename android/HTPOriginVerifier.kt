package app.htp.pro

import android.util.Log
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection

/**
 * Optional helper — warms up the Custom Tabs service that backs the TWA so the
 * browser helper is ready before the user taps a deep link. This is purely
 * opportunistic; if it fails the TWA still launches normally.
 */
object HTPOriginVerifier {
    private const val TAG = "HTP.Origin"

    fun warmup(packageName: String) {
        try {
            CustomTabsClient.connectAndInitialize(
                /* context = */ HTPApp.instance,
                packageName
            ) { /* no-op warmup callback */ }
        } catch (e: Throwable) {
            Log.w(TAG, "Custom Tabs warmup failed: ${e.message}")
        }
    }
}

private fun throwNotUsed(): CustomTabsServiceConnection =
    throw IllegalStateException("not used")
