package com.example.tcc.ui.simulador

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tcc.MainActivity
import com.example.tcc.R
import com.example.tcc.funcoes.ajustarUnidade
import java.io.File


class ResultadoSimulacaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_simulacao)

        // Layout principal
        val layoutPrincipal = findViewById<LinearLayout>(R.id.layoutPrincipal)
        layoutPrincipal.removeAllViews()
        // Obter o caminho do arquivo de resultado e o nome base do arquivo
        val arquivoResultado = intent.getStringExtra("arquivoResultado")
        val nomeArquivoRaw = intent.getStringExtra("nomeArquivoRaw")

        if (arquivoResultado != null && nomeArquivoRaw != null) {
            val fileResultado = File(arquivoResultado)
            val fileImagem = File(fileResultado.parent, "$nomeArquivoRaw.jpg")

            // Verificar se o arquivo de resultado existe
            if (fileResultado.exists()) {
                val textoResultado = fileResultado.readText()

                // Extrair e exibir valores de tensão/corrente
                val valores = extrairValores(textoResultado)
                if (valores.isNotEmpty()) {
                    val textViewValores = TextView(this).apply {
                        text = valores.joinToString("\n")
                        textSize = 18f
                        setPadding(16, 16, 16, 16)
                    }
                    layoutPrincipal.addView(textViewValores)
                }

                // Verificar e exibir a imagem, se existir
                if (fileImagem.exists()) {
                    val bitmap = BitmapFactory.decodeFile(fileImagem.absolutePath)
                    val imageView = ImageView(this).apply {
                        setImageBitmap(bitmap)
                        adjustViewBounds = true
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(16, 16, 16, 16)
                        }
                    }
                    layoutPrincipal.addView(imageView)
                }
            } else {
                // Caso o arquivo de resultado não exista
                val textViewErro = TextView(this).apply {
                    text = "Arquivo de resultado não encontrado."
                    textSize = 18f
                    setPadding(16, 16, 16, 16)
                }
                layoutPrincipal.addView(textViewErro)
            }
        } else {
            // Caso os extras não tenham sido fornecidos
            val textViewErro = TextView(this).apply {
                text = "Nenhum arquivo de resultado fornecido."
                textSize = 18f
                setPadding(16, 16, 16, 16)
            }
            layoutPrincipal.addView(textViewErro)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun extrairValores(textoResultado: String): List<String> {
        val valores = mutableListOf<String>()

        // Extrair tensões (v(nX))
        val regexTensao = Regex("v\\(n\\d+\\)\\s*=\\s*([-]?[\\d.]+e[+-]\\d+)")
        val matchResultTensao = regexTensao.findAll(textoResultado)
        matchResultTensao.forEach {
            val tensao = it.groupValues[1].toDouble()
            valores.add("Tensão ${it.groupValues[0]}: ${ajustarUnidade(tensao, "V")}")
        }

        // Extrair correntes (i(nX))
        val regexCorrente = Regex("@.*\\[i\\]\\s*=\\s*([-]?[\\d.]+e[+-]\\d+)")
        val matchResultCorrente = regexCorrente.findAll(textoResultado)
        matchResultCorrente.forEach {
            val corrente = it.groupValues[1].toDouble()
            valores.add("Corrente ${it.groupValues[0]}: ${ajustarUnidade(corrente, "A")}")
        }

        return valores
    }

    override fun onBackPressed() {
        super.onBackPressed()
        voltarParaSimuladorFragment()
    }

    override fun onSupportNavigateUp(): Boolean {
        // Navegar de volta ao SimuladorFragment
        voltarParaSimuladorFragment()
        return true
    }

    private fun voltarParaSimuladorFragment() {
        // Criar um Intent para retornar à MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            // Adicionar uma flag para limpar a pilha de atividades
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            // Passar um extra para indicar que deve navegar para o SimuladorFragment
            putExtra("navigateToSimulador", true)
        }
        startActivity(intent)
        finish() // Fechar a ResultadoSimulacaoActivity
    }

}