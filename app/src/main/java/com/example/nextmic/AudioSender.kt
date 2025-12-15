package com.example.nextmic

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder


class AudioSender(
    val serverIp: String,
    val serverPort: Int,
){
    val socket = DatagramSocket()

    // vou simular o envio de numeros inteiros so pra fingi que é audio, tenho que estudar melhor a classe AudioRecord
    suspend fun start(){
        var sound = 0
        while (true){
            val buffer = ByteBuffer.allocate(Int.SIZE_BYTES)
            buffer.order(ByteOrder.BIG_ENDIAN)
            buffer.putInt(sound)

            val data = buffer.array()

            val dataPacket = DatagramPacket(data, data.size, InetAddress.getByName(serverIp), serverPort)

            withContext(Dispatchers.IO){
                socket.send(dataPacket)
            }

            sound++
        }
    }




}

