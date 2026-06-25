package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.screens.*
import com.example.ui.theme.AyuGuardTheme
import com.example.ui.theme.LocalAppColors
import com.example.util.EmergencyHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    private lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as AyuGuardApplication
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(app.contactRepository, app.settingsRepository)
        )[MainViewModel::class.java]

        setContent {
            val permissionsState = rememberMultiplePermissionsState(
                permissions = listOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.SEND_SMS,
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        android.Manifest.permission.POST_NOTIFICATIONS
                    } else {
                        android.Manifest.permission.ACCESS_FINE_LOCATION // Dummy fallback
                    }
                )
            )

            LaunchedEffect(Unit) {
                if (!permissionsState.allPermissionsGranted) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }

            val settings by viewModel.settings.collectAsStateWithLifecycle()
            val contacts by viewModel.contacts.collectAsStateWithLifecycle()

            var isLocked by remember { mutableStateOf(settings.appLockEnabled) }
            val navController = rememberNavController()

            // Update isLocked state when settings change
            LaunchedEffect(settings.appLockEnabled) {
                if (settings.appLockEnabled && !isLocked) {
                    isLocked = true
                }
            }

            AyuGuardTheme(isDark = settings.themeId == "dark") {
                val t = LocalAppColors.current

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = t.bg
                ) {
                    if (isLocked) {
                        AppLockScreen(
                            appLockPin = settings.appLockPin,
                            fingerprintEnabled = settings.fingerprintEnabled,
                            onUnlock = { isLocked = false },
                            onUseFingerprint = { promptBiometric { isLocked = false } }
                        )
                    } else {
                        val currentRoute = navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(initialValue = navController.currentBackStackEntry).value?.destination?.route ?: "tutorial"

                        Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                            // Navbar
                            if (currentRoute !in listOf("tutorial", "sos_countdown", "sos_alarm", "test_countdown", "test_alarm")) {
                                Row(modifier = Modifier.fillMaxWidth().padding(18.dp, 18.dp, 18.dp, 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    if (currentRoute == "home") {
                                        IconButton(onClick = { /* Open drawer/menu - omitted for simplicity or can navigate to settings */ navController.navigate("settings") }) {
                                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = t.text1)
                                        }
                                        Spacer(Modifier.weight(1f))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Shield, contentDescription = null, tint = t.red, modifier = Modifier.size(19.dp))
                                            Spacer(Modifier.width(8.dp))
                                            Text("AyuGuard", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = t.text1)
                                            Spacer(Modifier.width(8.dp))
                                            Box(modifier = Modifier.background(t.red.copy(alpha = 0.1f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                                Text("BETA", fontSize = 10.sp, color = t.red, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Spacer(Modifier.weight(1f))
                                    } else {
                                        TextButton(onClick = { navController.popBackStack() }) {
                                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = t.text2)
                                            Spacer(Modifier.width(6.dp))
                                            Text(currentRoute.replaceFirstChar { it.uppercase() }, color = t.text2, fontSize = 14.sp)
                                        }
                                    }
                                }
                            }

                            // NavHost
                            Box(modifier = Modifier.weight(1f)) {
                                NavHost(navController = navController, startDestination = "tutorial") {
                                    composable("tutorial") {
                                        TutorialScreen(onDone = {
                                            navController.navigate("home") { popUpTo("tutorial") { inclusive = true } }
                                        })
                                    }
                                    composable("home") {
                                        HomeScreen(
                                            startSOS = { navController.navigate("sos_countdown") },
                                            onJourney = { navController.navigate("journey_setup") },
                                            onContacts = { navController.navigate("contacts") }
                                        )
                                    }
                                    composable("journey_setup") {
                                        JourneySetupScreen(onStart = { dur, veh, pl ->
                                            navController.navigate("journey_active/$dur")
                                        })
                                    }
                                    composable("journey_active/{dur}") { backStackEntry ->
                                        val dur = backStackEntry.arguments?.getString("dur")?.toIntOrNull() ?: 30
                                        var timeLeft by remember { mutableStateOf(dur * 60) }

                                        LaunchedEffect(Unit) {
                                            while(timeLeft > 0) {
                                                kotlinx.coroutines.delay(1000)
                                                timeLeft--
                                                if (timeLeft == 0) {
                                                    navController.navigate("sos_alarm") { popUpTo("home") }
                                                }
                                            }
                                        }

                                        JourneyActiveScreen(
                                            durationMin = dur,
                                            vehicle = "",
                                            plate = "",
                                            timeLeft = timeLeft,
                                            onStop = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                                            onSOS = { navController.navigate("sos_countdown") }
                                        )
                                    }
                                    composable("contacts") {
                                        ContactsScreen(
                                            contacts = contacts,
                                            onAddContact = viewModel::addContact,
                                            onDeleteContact = viewModel::deleteContact
                                        )
                                    }
                                    composable("settings") {
                                        SettingsScreen(
                                            settings = settings,
                                            onThemeChange = viewModel::updateThemeId,
                                            onToggleVibration = viewModel::updateVibration,
                                            onToggleAppLock = { enabled ->
                                                if (enabled && settings.appLockPin.isEmpty()) {
                                                    // Should show PIN setup. For simplicity, setting a default PIN if enabled without UI
                                                    viewModel.updateAppLockPin("1234")
                                                }
                                                viewModel.updateAppLockEnabled(enabled)
                                            },
                                            onToggleFingerprint = viewModel::updateFingerprintEnabled,
                                            onTestSOS = { navController.navigate("test_sos_setup") }
                                        )
                                    }
                                    composable("about") { AboutScreen(onPrivacy = { navController.navigate("privacy") }) }
                                    composable("privacy") { PrivacyScreen() }
                                    
                                    composable("sos_countdown") {
                                        var timeLeft by remember { mutableStateOf(settings.countdownSec) }
                                        LaunchedEffect(Unit) {
                                            while(timeLeft > 0) {
                                                kotlinx.coroutines.delay(1000)
                                                timeLeft--
                                                if (timeLeft == 0) {
                                                    navController.navigate("sos_alarm") { popUpTo("home") }
                                                }
                                            }
                                        }
                                        SOSCountdownScreen(
                                            countdownSec = settings.countdownSec,
                                            isTest = false,
                                            timeLeft = timeLeft,
                                            onCancel = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                                            onAlarm = { navController.navigate("sos_alarm") { popUpTo("home") } }
                                        )
                                    }
                                    composable("sos_alarm") {
                                        var timeLeft by remember { mutableStateOf(settings.alarmDurationSec) }
                                        var smsSent by remember { mutableStateOf(false) }
                                        val coroutineScope = rememberCoroutineScope()
                                        
                                        LaunchedEffect(Unit) {
                                            if (settings.vibrationEnabled) {
                                                EmergencyHelper.vibrate(this@MainActivity, settings.alarmDurationSec * 1000L)
                                            }
                                            while(timeLeft > 0) {
                                                kotlinx.coroutines.delay(1000)
                                                timeLeft--
                                                if (timeLeft == 0) {
                                                    coroutineScope.launch {
                                                        val loc = app.locationHelper.getCurrentLocation()
                                                        val link = loc?.let { "https://maps.google.com/?q=${it.latitude},${it.longitude}" } ?: "unavailable"
                                                        val msg = "🚨 Emergency SOS — I need immediate help!\n📍 Location: $link"
                                                        contacts.forEach { c ->
                                                            EmergencyHelper.sendSms(c.phone, msg)
                                                        }
                                                        smsSent = true
                                                    }
                                                }
                                            }
                                        }
                                        SOSAlarmScreen(
                                            isTest = false,
                                            smsSent = smsSent,
                                            timeLeft = timeLeft,
                                            onStop = { navController.navigate("feedback") { popUpTo("home") } }
                                        )
                                    }
                                    composable("feedback") {
                                        FeedbackScreen(onClose = { navController.navigate("home") { popUpTo("home") { inclusive = true } } })
                                    }
                                    
                                    // TEST SOS
                                    composable("test_sos_setup") {
                                        var testPhone by remember { mutableStateOf("") }
                                        TestSOSSetupScreen(
                                            testPhone = testPhone,
                                            onPhoneChange = { testPhone = it },
                                            onStart = { navController.navigate("test_countdown/$testPhone") },
                                            onBack = { navController.popBackStack() }
                                        )
                                    }
                                    composable("test_countdown/{phone}") { backStackEntry ->
                                        val phone = backStackEntry.arguments?.getString("phone") ?: ""
                                        var timeLeft by remember { mutableStateOf(settings.countdownSec) }
                                        LaunchedEffect(Unit) {
                                            while(timeLeft > 0) {
                                                kotlinx.coroutines.delay(1000)
                                                timeLeft--
                                                if (timeLeft == 0) {
                                                    navController.navigate("test_alarm/$phone") { popUpTo("home") }
                                                }
                                            }
                                        }
                                        SOSCountdownScreen(
                                            countdownSec = settings.countdownSec,
                                            isTest = true,
                                            timeLeft = timeLeft,
                                            onCancel = { navController.popBackStack() },
                                            onAlarm = { navController.navigate("test_alarm/$phone") { popUpTo("home") } }
                                        )
                                    }
                                    composable("test_alarm/{phone}") { backStackEntry ->
                                        val phone = backStackEntry.arguments?.getString("phone") ?: ""
                                        var timeLeft by remember { mutableStateOf(settings.alarmDurationSec) }
                                        var smsSent by remember { mutableStateOf(false) }
                                        val coroutineScope = rememberCoroutineScope()
                                        
                                        LaunchedEffect(Unit) {
                                            if (settings.vibrationEnabled) {
                                                EmergencyHelper.vibrate(this@MainActivity, settings.alarmDurationSec * 1000L)
                                            }
                                            while(timeLeft > 0) {
                                                kotlinx.coroutines.delay(1000)
                                                timeLeft--
                                                if (timeLeft == 0) {
                                                    coroutineScope.launch {
                                                        val msg = "TEST SOS — This is a test alert from AyuGuard. No emergency assistance is required."
                                                        EmergencyHelper.sendSms(phone, msg)
                                                        smsSent = true
                                                    }
                                                }
                                            }
                                        }
                                        SOSAlarmScreen(
                                            isTest = true,
                                            smsSent = smsSent,
                                            timeLeft = timeLeft,
                                            onStop = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun promptBiometric(onSuccess: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@MainActivity, "Biometric error: $errString", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock AyuGuard")
            .setSubtitle("Verify your identity")
            .setNegativeButtonText("Use PIN")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
