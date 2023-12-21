package de.buckwich.motionPause


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.buckwich.motionPause.ui.theme.MotionPauseTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotionPauseTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}


@Composable
fun MainScreen() {


    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { /* Handle settings button click */ }
            ) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


        PlayButton()


        Spacer(modifier = Modifier.height(16.dp))


    }


}


@Composable
fun PlayButton() {

    var maxTime = 60
    var isPlaying by remember { mutableStateOf(false) }

    var time by remember { mutableStateOf(maxTime) }
    var progress by remember { mutableStateOf(1f) }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000) // Delay for 1 second

            // Update progress and time

            time -= 1
            progress = time.toFloat() / maxTime

            if (time <= 0f) {
                isPlaying = false
                time = maxTime
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable {
                    // Toggle play/pauset

                    isPlaying = !isPlaying

                },

            contentAlignment = Alignment.Center
        ) {
            val progressColor = MaterialTheme.colorScheme.primary

            Canvas(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
            ) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360 * progress,
                    useCenter = false,
                    style = Stroke(width = 15.dp.toPx())
                )
            }


            //switch icon based on isPlaying

            var vector = if (isPlaying)
                Icons.Filled.Place
            else
                Icons.Filled.Stop
            Icon(
                imageVector = vector,
                contentDescription = "Play Icon",
                tint = progressColor,
                modifier = Modifier
                    .size(240.dp)
                    .padding(4.dp)
                    .clip(CircleShape)


            )

        }

        // Spacer
        Spacer(modifier = Modifier.height(8.dp))

        // Time display
        Text(
            text = formatTime(time),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }


}


fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

@Composable
fun CircularProgressBar(progress: Float) {


}

@Composable
fun TriangleIcon() {

}

@Preview(showBackground = true)
@Composable
fun PlayButtonPreview() {
    PlayButton()
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MotionPauseTheme {
        MainScreen()
    }
}
