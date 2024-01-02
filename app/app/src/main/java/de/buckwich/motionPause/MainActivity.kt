package de.buckwich.motionPause


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSessionManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import de.buckwich.motionPause.ui.theme.MotionPauseTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val showDialog = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")

        MainActivity.appContext = applicationContext

        setContent {
            MotionPauseTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                    if (showDialog.value) {
                        AlertDialog(
                            onDismissRequest = { showDialog.value = false },

                            title = { Text(text = "Alert") },
                            text = { Text(text = "This is an alert dialog.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    showDialog.value = false
                                    startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                                }) {
                                    Text(text = "OK")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    fun getActiveMediaSessions() {
        val m = applicationContext.getSystemService<MediaSessionManager>()!!
        val component = ComponentName(applicationContext, "NotificationService")
        val sessions = m.getActiveSessions(component)
        Log.d("Sessions", "count: ${sessions.size}")
        sessions.forEach {
            Log.d("Sessions", "$it -- " + (it?.metadata?.keySet()?.joinToString()))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_TITLE)))

            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_AUTHOR)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_WRITER)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_COMPOSER)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_COMPILATION)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_DATE)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_GENRE)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_ART_URI)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_DESCRIPTION)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI)))
            Log.d("Sessions", "$it -- " + (it?.metadata?.getString(MediaMetadata.METADATA_KEY_MEDIA_ID)))
            Log.d("Sessions", "$it -- " + formatTime(it?.metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION)))
            Log.d("Sessions", "$it -- " + formatTime(it.playbackState?.position))


        }


    }

    fun formatTime(millis: Long?): String {
        if (millis == null)
            return "00:00"
        val seconds = millis / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    override fun onStart() {
        Log.d("MainActivity", "onStart")
        super.onStart()
        val abc = NotificationManagerCompat
            .getEnabledListenerPackages(MainActivity.appContext)
            .contains(MainActivity.appContext.packageName)
        Log.d("MainActivity", abc.toString())



        if (!abc) {

            showDialog.value = true
        }

        if (abc) {
            getActiveMediaSessions()
        }


    }

    companion object {
        lateinit var appContext: Context
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
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

        TextButton(onClick = {
            Log.d("MainActivity", "hello")

        }

        ) {
            Text(text = "hello")
        }


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
                Icons.Filled.PlayArrow
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
