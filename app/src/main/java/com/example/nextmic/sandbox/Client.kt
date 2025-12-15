package com.example.nextmic.sandbox

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

//see more on https://developer.android.com/reference/java/net/DatagramSocket#DatagramSocket()
class Client(
    val serverIp: String,
    val serverPort: Int
) {
    private val clientSocket = DatagramSocket() //aqui to criando um socket UDP que vai se ligar a qualquer pporta, se eu quisesse criar um porta especifica deveria passar


    suspend fun send(data: String) {


        val dataBytes = data.toByteArray(Charsets.UTF_8)

        val datagramPacket = DatagramPacket(
            dataBytes,
            dataBytes.size,
            InetAddress.getByName(serverIp),
            serverPort
        )
        withContext(Dispatchers.IO) { // basicamente eu to falando pra coroutina ao inves e usar a main thead usar outra
            clientSocket.send(datagramPacket)

        }
    }
}