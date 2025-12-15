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
    private val socket = DatagramSocket()

    //sounds variables
    val audioSource = MediaRecorder.AudioSource.MIC//define o dispositivo de audio, vou usar o padrao mesmo, o principal
    val sampleRate = 44100//aqui to definindo em Hz, Hz de cd aqui, aqui é frenquencia, é tipo quantos frames por segundo vai ter o audio
    val channelConfig = AudioFormat.CHANNEL_IN_MONO //vou gravar em mono, nada de Sterio
    val audioFormat = AudioFormat.ENCODING_PCM_16BIT //aqui define a quantidade de bit de cada amostra, no caso 16bit
    val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        channelConfig,
        audioFormat
    )

    // vou simular o envio de numeros inteiros so pra fingi que é audio, tenho que estudar melhor a classe AudioRecord
    suspend fun start(){
        val audioRecord = AudioRecord(
            audioSource,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )
        val udpBuffer = ByteArray(1400) // pra nao haver perca de dados
        audioRecord.startRecording() //aqui ele começa a gravar, por enquanto eu nao estou lendo, mas ele esta gravando

        while (true){
            val bytesRead = audioRecord.read( // essa função vai escrever no buffer e retornarar o numero de bytes lidos
                udpBuffer, // local onde eu quero gravar, aqui é um array de bytes
                0, //onde eu quero começar gravando, eu quero começar no incio do array por isso 0
                udpBuffer.size // tamanho do meu array de bytes
            )

            if(bytesRead > 0){
                val dataPacket = DatagramPacket(udpBuffer, bytesRead, InetAddress.getByName(serverIp), serverPort)

                withContext(Dispatchers.IO){
                    socket.send(dataPacket)
                }
            }
        }
    }
}

