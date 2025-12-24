package com.mnessim.researchtrackerkmp

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mnessim.researchtrackerkmp.domain.data.DBFactory
import com.mnessim.researchtrackerkmp.utils.notifications.NotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        handleIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val detailsId = intent.getStringExtra("details_id")?.toLongOrNull()
        android.util.Log.d("MainActivity", "handleIntent: detailsId = $detailsId")
        if (detailsId != null) {
            NavigationEvents.triggerNavigateToDetails(detailsId)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val androidPlatformModule = module {
            single<DBFactory> { DBFactory(applicationContext) }
            single {
                NotificationManager().apply { init(get<Context>()) }
            }
        }
        startKoin {
            androidContext(this@AndroidApp)
            modules(commonModules + androidPlatformModule)
        }
    }
}