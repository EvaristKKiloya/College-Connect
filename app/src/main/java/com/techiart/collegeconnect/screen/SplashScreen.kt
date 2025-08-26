package com.techiart.collegeconnect.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import com.techiart.collegeconnect.MainActivity

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen {
                // After splash, decide navigation
                val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                val onboardingDone = sharedPref.getBoolean("onboarding_done", false)
                val loggedIn = sharedPref.getBoolean("logged_in", false)

                when {
                    !onboardingDone -> {
                        startActivity(Intent(this, OnboardingActivity::class.java))
                    }
                    !loggedIn -> {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    else -> {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
                finish()
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1.2f else 0f,
        animationSpec = tween(
            durationMillis = 1200,
            easing = { OvershootInterpolator(4f).getInterpolation(it) }
        ),
        label = "scaleAnim"
    )

    LaunchedEffect(true) {
        startAnimation = true
        delay(2500) // Splash duration
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .size(120.dp)
                .scale(scale),
            shape = CircleShape,
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "CC",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onSplashFinished = {})
}
