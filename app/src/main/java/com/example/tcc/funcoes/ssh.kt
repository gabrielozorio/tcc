package com.example.tcc.funcoes

import android.content.Context
import android.os.Build
import android.util.Log
import com.jcraft.jsch.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SSH {
    private var nomeArquivo: String="iniciada"
    private var gerarImagem: Boolean=false

    fun setNomeArquivo(nome:String) { nomeArquivo=nome}
    fun setGerarImagem(estado:Boolean) {gerarImagem=estado}
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
            setNomeArquivo(fileName)
            // Criar o arquivo temporário com esse nome
            val tempFile = File.createTempFile(fileName, null)
            val fos = FileOutputStream(tempFile)
            fos.write(netlistContent.toByteArray())
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

    fun executarNgspice(usuario: String, servidor: String, senha: String, diretorioLocal: File) {
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
            channelExec.setCommand("ngspice_con -b $nomeArquivo > resultado.txt")
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
                ssvChannel.setCommand("python ssvtojpg.py")
                Log.d("SSH", "Executou metodo python.")
                ssvChannel.connect()
            //    while (!ssvChannel.isClosed) {
            //        Thread.sleep(100)
            //    }
                ssvChannel.disconnect()
                Log.d("SSH", "Conversão para imagem realizada com sucesso.")
            }
        transferGeneratedFiles(session, diretorioLocal)
        Log.d("TRANSFER","Chegou no Transfer Generated Files")

        } catch (e: Exception) {
            Log.e("SSH", "Erro: ${e.message}")
        } finally {
            session?.disconnect()
            Log.d("SSH", "Sessão desconectada.")
        }


        }

    }
    fun transferGeneratedFiles(session: Session, diretorioLocal: File) {
        val osInfo = detectarSistemaOperacional(session)  // Detecta o sistema operacional
        val findCommand = getRecentFilesCommand(osInfo)
        val channelSftp = session.openChannel("sftp") as ChannelSftp
        channelSftp.connect()

        try {
            // Identificar arquivos modificados no servidor nos últimos 15 segundos
            val findCommand = """find . -type f -cmin -0.25"""
            val channelExec = session.openChannel("exec") as ChannelExec
            channelExec.setCommand(findCommand)

            val output = ByteArrayOutputStream()
            channelExec.outputStream = output
            channelExec.connect()

            while (!channelExec.isClosed) {
                Thread.sleep(100)
            }

            val recentFiles = output.toString().trim().split("\n")
            channelExec.disconnect()

            if (recentFiles.isEmpty()) {
                Log.w("SSH", "Nenhum arquivo recente encontrado.")
                return
            }

            // Transferir os arquivos para o diretório local
            for (file in recentFiles) {
                val localFile = File(diretorioLocal, File(file).name)
                FileOutputStream(localFile).use { outputStream ->
                    channelSftp.get(file.trim()).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Log.d("SSH", "Arquivo transferido: ${localFile.absolutePath}")
            }

        } catch (e: Exception) {
            Log.e("SSH", "Erro ao transferir arquivos: ${e.message}")
        } finally {
            channelSftp.disconnect()
        }
    }

    // Função para ler o conteúdo do Netlist





         fun getRecentFilesCommand(osInfo: String): String {
            return if (osInfo.contains("linux")) {
                // Para Linux
                """find . -type f -cmin -0.25"""  // Modificado nos últimos 15 segundos
            } else if (osInfo.contains("windows")) {
                // Para Windows (PowerShell)
                """powershell -Command "Get-ChildItem -Path . -File | 
            Where-Object { ($$.LastWriteTime -gt (Get-Date).AddSeconds(-15)) } |
            ForEach-Object { $$.FullName }" """  // Modificado nos últimos 15 segundos
            } else {
                throw UnsupportedOperationException("Sistema operacional não suportado: $osInfo")
            }
        }

        fun detectarSistemaOperacional(session: Session): String {
            val osCheckCommand = "uname || powershell -Command \"(Get-WmiObject Win32_OperatingSystem).Caption\""
            val channelExec = session.openChannel("exec") as ChannelExec
            channelExec.setCommand(osCheckCommand)

            val output = ByteArrayOutputStream()
            channelExec.outputStream = output
            channelExec.connect()

            while (!channelExec.isClosed) {
                Thread.sleep(100)
            }

            val osInfo = output.toString().trim().lowercase()
            channelExec.disconnect()
            return osInfo
        }




