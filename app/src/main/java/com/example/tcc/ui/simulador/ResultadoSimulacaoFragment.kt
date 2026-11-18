package com.example.tcc.ui.simulador


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tcc.R
import com.example.tcc.funcoes.ajustarUnidade
import com.github.chrisbanes.photoview.PhotoView
import java.io.File

class ResultadoSimulacaoFragment : Fragment() {

    private lateinit var layoutPrincipal: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_resultado_simulacao, container, false)
        layoutPrincipal = rootView.findViewById(R.id.layoutPrincipal)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPrincipal.removeAllViews()



        val arquivoResultado = arguments?.getString("arquivoResultado")
        val nomeArquivoRaw = arguments?.getString("nomeArquivoRaw")

        if (arquivoResultado != null && nomeArquivoRaw != null) {
            val fileResultado = File(arquivoResultado)
            val fileImagem = File(fileResultado.parent, "$nomeArquivoRaw.jpg")
            Log.d("IMG","fileImagem = $fileImagem")
            if (fileResultado.exists()) {
                val textoResultado = fileResultado.readText()
                Log.d("RESULTADO","Achou resultado: $textoResultado")
                val valores = extrairValores(textoResultado)
                if (valores.isNotEmpty()) {
                    val textViewValores = TextView(requireContext()).apply {
                        text = valores.joinToString("\n")
                        textSize = 18f
                        setPadding(16, 16, 16, 16)
                    }
                    layoutPrincipal.addView(textViewValores)
                }

                if (fileImagem.exists()) {
                    val bitmap = BitmapFactory.decodeFile(fileImagem.absolutePath)
                    val imageView = PhotoView(requireContext()).apply {//ImageView(requireContext()).apply {
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
                exibirErro("Arquivo de resultado não encontrado.")
            }
        } else {
            exibirErro("Nenhum arquivo de resultado fornecido.")
        }
    }

    private fun exibirErro(mensagem: String) {
        val textViewErro = TextView(requireContext()).apply {
            text = mensagem
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        layoutPrincipal.addView(textViewErro)
    }

    private fun extrairValores(textoResultado: String): List<String> {
        val valores = mutableListOf<String>()

        val regexTensao = Regex("v\\(n\\d+\\)\\s*=\\s*([-]?[\\d.]+e[+-]\\d+)")
        val matchResultTensao = regexTensao.findAll(textoResultado)
        matchResultTensao.forEach {
            val tensao = it.groupValues[1].toDouble()
            valores.add("Tensão ${it.groupValues[0]}: ${ajustarUnidade(tensao, "V")}")
        }

        val regexCorrente = Regex("@.*\\[i\\]\\s*=\\s*([-]?[\\d.]+e[+-]\\d+)")
        val matchResultCorrente = regexCorrente.findAll(textoResultado)
        matchResultCorrente.forEach {
            val corrente = it.groupValues[1].toDouble()
            valores.add("Corrente ${it.groupValues[0]}: ${ajustarUnidade(corrente, "A")}")
        }

        return valores
    }
}
