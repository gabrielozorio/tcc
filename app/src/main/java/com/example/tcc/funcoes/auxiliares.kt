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




fun ajustarUnidade(valor: Double, unidadeBase: String): String {
    val valorAbsoluto = Math.abs(valor) // Trabalhar com o valor absoluto para determinar a unidade
    val sinal = if (valor < 0) "-" else "" // Preservar o sinal do valor

    return when (unidadeBase) {
        "V" -> {
            when {
                valorAbsoluto >= 1e3 -> "$sinal%.1f kV".format(valorAbsoluto / 1e3) // Quilovolts
                valorAbsoluto >= 1 -> "$sinal%.1f V".format(valorAbsoluto) // Volts
                valorAbsoluto >= 1e-3 -> "$sinal%.1f mV".format(valorAbsoluto * 1e3) // Milivolts
                else -> "$sinal%.1f µV".format(valorAbsoluto * 1e6) // Microvolts
            }
        }
        "A" -> {
            when {
                valorAbsoluto >= 1 -> "$sinal%.1f A".format(valorAbsoluto) // Amperes
                valorAbsoluto >= 1e-3 -> "$sinal%.1f mA".format(valorAbsoluto * 1e3) // Miliamperes
                valorAbsoluto >= 1e-6 -> "$sinal%.1f µA".format(valorAbsoluto * 1e6) // Microamperes
                else -> "$sinal%.1f nA".format(valorAbsoluto * 1e9) // Nanoamperes
            }
        }
        else -> "$sinal%.1f $unidadeBase".format(valorAbsoluto) // Unidade desconhecida
    }
}



