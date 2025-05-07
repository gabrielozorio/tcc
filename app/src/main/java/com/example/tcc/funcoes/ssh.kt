package com.example.tcc.funcoes

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.tcc.ui.simulador.ResultadoSimulacaoActivity
import com.jcraft.jsch.*
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SSH {
    private var nomeArquivo: String="iniciada"
    private var nomeArquivoRaw: String = "iniciada"
    private var gerarImagem: Boolean=false
    private var nomeWR: String="iniciada"

    fun setNomeArquivo(nome:String) { nomeArquivo=nome}
    fun setNomeArquivoRaw(nome:String) { nomeArquivoRaw=nome}
    fun setGerarImagem(estado:Boolean) {gerarImagem=estado}
    fun setNomeWR(nome:String) {nomeWR = nome}
    private fun ajustarNetlistComWR(netlistContent: String, nomeWR: String): String {
        val regex = Regex("wrdata .*\\.txt") // Procura por "wrdata" seguido de um nome de arquivo
        return netlistContent.replace(regex, "wrdata $nomeWR")
    }


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

            // Obter o nome do dispositivo e a hora atual
            val deviceName = Build.MODEL.replace(" ", "_") // Nome do dispositivo sem espaços
            val sdf = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
            val currentTime = sdf.format(Date())

            // Criar o nome do arquivo no formato circuit_'nome-do-dispositivo'_'hora'.cir
            val fileName = "circuit_${deviceName}_$currentTime.cir"
            setNomeArquivo(fileName) // arquivo com extensao .cir
            setNomeArquivoRaw("circuit_${deviceName}_$currentTime") // arquivo sem extensao

            // ajuste
            val nomeWR = "circuit_${deviceName}_${currentTime}_wrdata.txt"
            setNomeWR(nomeWR) // Armazena o nome do arquivo wrdata
            val netlistContent2 = ajustarNetlistComWR(netlistContent,nomeWR)

            // Criar o arquivo temporário com esse nome
            val tempFile = File.createTempFile(fileName, null)
            val fos = FileOutputStream(tempFile)
            if (netlistContent == netlistContent2) fos.write(netlistContent.toByteArray())
            else fos.write(netlistContent2.toByteArray())
            fos.close()

            Log.d("SSH", "Enviando arquivo Netlist: $fileName")
            channel.put(tempFile.absolutePath, fileName)

            tempFile.delete()

            channel.disconnect()
            Log.d("SSH", "Arquivo Netlist enviado com sucesso.")

            if(netlistContent.contains("ac",ignoreCase = true) ||
                    netlistContent.contains("dc",ignoreCase=true) ||
                        netlistContent.contains("tran",ignoreCase = true))
                setGerarImagem(true) else setGerarImagem(false)
            if (gerarImagem) Log.d("SSH", "Vai ter .jpg") else Log.d("SSH","nao tem .jpg")

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

    fun executarNgspice(usuario: String, servidor: String, senha: String, diretorioLocal: File, context: Context) {
        val jsch = JSch()
        var session: Session? = null
        try {
            session = jsch.getSession(usuario, servidor, 22)
            session.setPassword(senha)

            val config = Properties()
            config["StrictHostKeyChecking"] = "no"
            session.setConfig(config)

            Log.d("SSH", "Iniciando a conexão com o servidor...")
            session.connect()

            Log.d("SSH", "Conexão estabelecida com sucesso.")

            // Execute o comando ngspice
            val channelExec = session.openChannel("exec") as ChannelExec
            //channelExec.setCommand("ngspice_con -b $nomeArquivo")
            //channelExec.setCommand("ngspice -b $nomeArquivo > resultado_$nomeArquivoRaw.txt")
            channelExec.setCommand("ngspice_con -b $nomeArquivo > resultado_$nomeArquivoRaw.txt")
            channelExec.connect()
            while (!channelExec.isClosed) {
                Thread.sleep(100)
            }
            channelExec.disconnect()

            Log.d("SSH", "Ngspice executado com sucesso.")



            // Verificar o tipo de análise
            if (gerarImagem) {
                // Executar ssvtojpg.py
                val ssvChannel = session.openChannel("exec") as ChannelExec
                //ssvChannel.setCommand("python ssvtojpg.py")

                Log.d("SSH", "Executou metodo python.")
                ssvChannel.setCommand("python ssvtojpg3.py $nomeArquivoRaw")
                ssvChannel.connect()
            //    while (!ssvChannel.isClosed) {
            //        Thread.sleep(100)
            //    }
                ssvChannel.disconnect()
                Log.d("SSH", "Conversão para imagem realizada com sucesso.")
            }
        transferGeneratedFiles(session, diretorioLocal, context)
        Log.d("TRANSFER","Chegou no Transfer Generated Files")

        } catch (e: Exception) {
            Log.e("SSH", "Erro: ${e.message}")
        } finally {
            session?.disconnect()
            Log.d("SSH", "Sessão desconectada.")
        }


        }



private fun transferGeneratedFiles(session: Session, diretorioLocal: File, context: Context) {
    try {
        val channelSftp = session.openChannel("sftp") as ChannelSftp
        channelSftp.connect()

        var pathServidor = channelSftp.pwd()

        // Create local directory if it doesn't exist
        if (!diretorioLocal.exists()) {
            diretorioLocal.mkdirs()
        }

        // Transfer the simulation results file
        val resultadoLocal = File(diretorioLocal, "resultado_$nomeArquivoRaw.txt")
        Log.d("SSH", "Transferindo arquivo resultado.txt...")

        Log.d("SSH","arquivo buscado: $pathServidor/resultado_$nomeArquivoRaw.txt")
        channelSftp.get("resultado_$nomeArquivoRaw.txt", FileOutputStream(resultadoLocal))

        // Transfer the graph image if it was generated
        if (gerarImagem) {
            try {
                val imageFileName = "$nomeArquivoRaw.jpg"
                val imageLocal = File(diretorioLocal, imageFileName)
                Log.d("SSH", "Transferindo arquivo $imageFileName...")
                TimeUnit.SECONDS.sleep(2)
                channelSftp.get(imageFileName, FileOutputStream(imageLocal))
                Log.d("SSH", "Arquivo de imagem transferido com sucesso")
            } catch (e: SftpException) {
                Log.e("SSH", "Erro ao transferir arquivo de imagem: ${e.message}")
            }
        }
        channelSftp.disconnect()
        Log.d("SSH", "Todos os arquivos foram transferidos com sucesso")

        // Iniciar a atividade de resultado
        val intent = Intent(context, ResultadoSimulacaoActivity::class.java).apply {
            putExtra("arquivoResultado", resultadoLocal.absolutePath)
            putExtra("nomeArquivoRaw", nomeArquivoRaw) // Passar o nome do arquivo raw
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)

    } catch (e: Exception) {
        Log.e("SSH", "Erro durante a transferência de arquivos: ${e.message}")
        throw e
    }
}

}






