package com.example.tcc.funcoes

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session

class SSH {
    fun acessarServidor() {
        val jsch = JSch()
        val session: Session = jsch.getSession("usuario", "endereco_do_servidor", 22)
        session.setPassword("senha")

        val config = java.util.Properties()
        config.put("StrictHostKeyChecking", "no")
        session.setConfig(config)

        session.connect()

        val channel: ChannelExec = session.openChannel("exec") as ChannelExec
        channel.setCommand("comando_a_ser_executado")

        channel.connect()

        val inputStream = channel.inputStream
        val output = ByteArray(1024)
        while (inputStream.read(output).also { bytesRead -> if (bytesRead == -1) break } != 0) {
            println(String(output))
        }

        channel.disconnect()
        session.disconnect()
    }
}