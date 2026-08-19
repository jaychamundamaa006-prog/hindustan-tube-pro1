package app.htp.pro

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.androidbrowser.trusted.TrustedWebActivityIntentBuilder

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge — let the TWA content draw behind system bars.
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = Color.parseColor("#0F1115")
        window.navigationBarColor = Color.parseColor("#0F1115")
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        launchTwa()
        finish()
    }

    private fun launchTwa() {
        val launchUrl = "https://${BuildConfig.HOST_NAME}/splash"
        val builder = TrustedWebActivityIntentBuilder(launchUrl)
            .setColorScheme(CustomTabsIntent.COLOR_SCHEME_DARK)
            .setColorSchemeParams(
                CustomTabsIntent.COLOR_SCHEME_DARK,
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(Color.parseColor("#0F1115"))
                    .setSecondaryToolbarColor(Color.parseColor("#0F1115"))
                    .build()
            )
            .setAdditionalTrustedOrigins(
                listOf("https://www.${BuildConfig.HOST_NAME}")
            )

        startActivity(builder.build(this))
    }
}
