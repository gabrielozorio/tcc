package com.example.tcc.funcoes  // Defina o pacote corretamente para sua aplicação

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

// Função para carregar o conteúdo do arquivo netlist.txt
fun carregarNetlist(context: Context): String {
    return try {
        // Acessando o arquivo no diretório assets
        val inputStream = context.assets.open("netlist.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.use { it.readText() }  // Lê e retorna o conteúdo
    } catch (e: Exception) {
        e.printStackTrace()
        "* Erro ao carregar Netlist"  // Caso ocorra um erro, retorna uma mensagem de erro
    }
}


