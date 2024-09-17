package com.example.tcc.funcoes

import android.util.Log
import com.jcraft.jsch.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class SSH {
    fun enviarArquivoNetlist(usuario: String, servidor: String, senha: String, netlistContent: String) {
        val jsch = JSch()
        var session: Session? = null
        try {
            session = jsch.getSession(usuario, servidor, 22)
            session.setPassword(senha)

            val config = java.util.Properties()
            config.put("StrictHostKeyChecking", "no")
            session.setConfig(config)

            Log.d("SSH", "Iniciando a conexão com o servidor...")
            session.connect()

            Log.d("SSH", "Conexão estabelecida com sucesso.")

            val channel: ChannelSftp = session.openChannel("sftp") as ChannelSftp
            channel.connect()

            val tempFile = File.createTempFile("circuit", ".cir")
            val fos = FileOutputStream(tempFile)
            fos.write(netlistContent.toByteArray())
            fos.close()

            Log.d("SSH", "Enviando arquivo Netlist...")
            channel.put(tempFile.absolutePath, "circuit.cir")

            tempFile.delete()

            channel.disconnect()
            Log.d("SSH", "Arquivo Netlist enviado com sucesso.")
        } catch (e: JSchException) {
            Log.e("SSH", "Erro na conexão SSH: ${e.message}")
        } catch (e: SftpException) {
            Log.e("SSH", "Erro ao enviar arquivo: ${e.message}")
        } catch (e: Exception) {
            Log.e("SSH", "Erro: ${e.message}")
        } finally {
            session?.disconnect()
            Log.d("SSH", "Sessão desconectada.")
        }
    }

    fun executarNgspice(usuario: String, servidor: String, senha: String) {
        val jsch = JSch()
        var session: Session? = null
        try {
            session = jsch.getSession(usuario, servidor, 22)
            session.setPassword(senha)

            val config = java.util.Properties()
            config.put("StrictHostKeyChecking", "no")
            session.setConfig(config)

            Log.d("SSH", "Iniciando a conexão com o servidor...")
            session.connect()

            Log.d("SSH", "Conexão estabelecida com sucesso.")

            val channel: ChannelExec = session.openChannel("exec") as ChannelExec
            // Redireciona a saída do ngspice para log.txt e depois remove o arquivo .cir
            //channel.setCommand("ngspice -b circuit.cir > log.txt && del circuit.cir")
            channel.setCommand("ngspice_con -b circuit.cir > log.txt")
            val byteArrayOutputStream = ByteArrayOutputStream()
            channel.outputStream = byteArrayOutputStream

            channel.connect()

            Log.d("SSH", "Comando ngspice enviado. Aguardando resposta...")

            // Aguarde até que o comando seja executado
            while (!channel.isClosed) {
                Thread.sleep(100)
            }

            val output = byteArrayOutputStream.toString()
            Log.d("SSH", "Resposta do comando ngspice: $output")  // Você pode processar a saída conforme necessário

            channel.disconnect()
        } catch (e: JSchException) {
            Log.e("SSH", "Erro na conexão SSH: ${e.message}")
        } catch (e: Exception) {
            Log.e("SSH", "Erro: ${e.message}")
        } finally {
            session?.disconnect()
            Log.d("SSH", "Sessão desconectada.")
        }
    }
}