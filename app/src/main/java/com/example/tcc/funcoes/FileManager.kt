package com.example.tcc.funcoes


import android.content.Context
import java.io.File

object FileManager {

    private var simulacoesDirectory: File? = null

    // Método para obter ou criar o diretório 'simulacoes' no armazenamento interno
    fun getSimulacoesDirectory(context: Context): File {
        if (simulacoesDirectory == null) {
            // Definir o diretório 'simulacoes' no armazenamento interno
            simulacoesDirectory = File(context.filesDir, "simulacoes")
            if (!simulacoesDirectory!!.exists()) {
                simulacoesDirectory!!.mkdirs()  // Cria o diretório se não existir
            }
        }
        return simulacoesDirectory!!
    }
}
