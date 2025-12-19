package com.example.nextmic.ui.elements

import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nextmic.ui.state.HomeViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nextmic.util.AudioSender
import com.example.nextmic.ui.state.HomeEvent
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope


@Composable
fun Home(
    modifier: Modifier = Modifier
) {


    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val serverIp = state.serverIp
    val context = LocalContext.current
    val activity = context as? Activity // Transforma o contexto em Activity
    //val microphoneAccess = state.microphoneAccess

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize()
    ){
        OutlinedTextField(
            value = serverIp,
            onValueChange = {
                viewModel.onEvent(HomeEvent.ServerIpChanged(it))
            },
            label = {
                Text("Type the IP showed on your PC")
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextButton(
                onClick = {

                    when(
                        ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.RECORD_AUDIO
                        )
                    ){
                        PackageManager.PERMISSION_GRANTED -> {
                            coroutineScope.launch {
                                val audioSender = AudioSender(
                                    serverIp = serverIp,
                                    serverPort = 30000
                                )
                                audioSender.start()
                            }
                        }
                        PackageManager.PERMISSION_DENIED -> {
                            if (activity != null) {
                                ActivityCompat.requestPermissions(
                                    activity,
                                    arrayOf(android.Manifest.permission.RECORD_AUDIO),
                                    1
                                )
                            }
                        }
                    }
                }
            ) {
                Text("Start")
            }
            Spacer(
                modifier = Modifier
                    .padding(10.dp)
            )
            TextButton(
                onClick = {

                }
            ) {
                Text("Stop")
            }
        }
    }
}