package com.techiart.collegeconnect.screen

import android.content.Intent
import android.os.Bundle
import com.techiart.collegeconnect.R
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import androidx.core.content.edit

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingScreen(
                onFinish = {
                    // Save onboarding flag
                    getSharedPreferences("app_prefs", MODE_PRIVATE).edit {
                        putBoolean("onboarding_done", true)
                    }

                    // Navigate to AuthActivity
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            )
        }
    }
}

// Data class representing each onboarding page
data class OnboardingPage(val imageRes: Int, val title: String, val description: String)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.onboarding1,
            title = "Discover Campus",
            description = "Find clubs, events, and resources at your fingertips."
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding2,
            title = "Mentorship & AI",
            description = "Get paired with mentors and AI-driven guidance."
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding3,
            title = "Competitions & Hackathons",
            description = "Join competitions, form teams, and track progress."
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding4,
            title = "Community & Calendar",
            description = "Chat, share, and keep track of all campus events."
        )
    )

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    // Observe current page to update dots dynamically
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Swipeable pages
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val p = pages[page]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = p.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        p.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        p.description,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Page indicators (dots)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                pages.forEachIndexed { index, _ ->
                    val isSelected = index == currentPage
                    val size by animateDpAsState(targetValue = if (isSelected) 12.dp else 8.dp)

                    Box(
                        modifier = Modifier
                            .size(size)
                            .padding(4.dp)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Next / Get Started button
            Button(
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onFinish()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (pagerState.currentPage < pages.size - 1) "Next" else "Get Started")
            }
        }

        // Skip button top-right
        TextButton(
            onClick = { onFinish() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Skip", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    OnboardingScreen(onFinish = {})
}
