package com.example.tcc.ui.simulador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.tcc.R
import com.example.tcc.funcoes.SSH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SimuladorFragment : Fragment() {

    private lateinit var editTextUsuario: EditText
    private lateinit var editTextServidor: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var buttonConectar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_simulador, container, false)

        // Inicialize os elementos de UI
        editTextUsuario = root.findViewById(R.id.editTextText)
        editTextServidor = root.findViewById(R.id.editTextText2)
        editTextSenha = root.findViewById(R.id.editTextTextPassword)
        buttonConectar = root.findViewById(R.id.button)

        // Configure o listener do botão
        buttonConectar.setOnClickListener {
            val usuario = editTextUsuario.text.toString()
            val servidor = editTextServidor.text.toString()
            val senha = editTextSenha.text.toString()

            // Verifique se todos os campos foram preenchidos
            if (usuario.isNotEmpty() && servidor.isNotEmpty() && senha.isNotEmpty()) {
                // Conteúdo do Netlist
                val netlistContent = """
                    * Divisor de Tensão Simples
                    V1 N001 0 DC 10
                    R1 N001 N002 5
                    R2 N002 0 5

                    .control
                    op
                    print all
                    .endc

                    .end
                """.trimIndent()

                // Conecte-se ao servidor via SSH
                CoroutineScope(Dispatchers.IO).launch {
                    val ssh = SSH()
                    ssh.enviarArquivoNetlist(usuario, servidor, senha, netlistContent)
                    ssh.executarNgspice(usuario, servidor, senha)
                }
            } else {
                // Mostre uma mensagem de erro ou faça alguma outra ação adequada
            }
        }

        return root
    }
}