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
    private lateinit var editTextNetlist: EditText
    private lateinit var buttonConectar: Button
    private lateinit var buttonAddResistor: Button
    private lateinit var buttonAddCapacitor: Button
    private lateinit var buttonAddIndutor: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_simulador, container, false)

        // Inicialize os elementos de UI
        editTextUsuario = root.findViewById(R.id.editTextUsuario)
        editTextServidor = root.findViewById(R.id.editTextServidor)
        editTextSenha = root.findViewById(R.id.editTextSenha)
        editTextNetlist = root.findViewById(R.id.editTextNetlist)
        buttonConectar = root.findViewById(R.id.buttonConectar)
        buttonAddResistor = root.findViewById(R.id.buttonAddResistor)
        buttonAddCapacitor = root.findViewById(R.id.buttonAddCapacitor)
        buttonAddIndutor = root.findViewById(R.id.buttonAddIndutor)

        // Configure o listener do botão conectar
        buttonConectar.setOnClickListener {
            val usuario = editTextUsuario.text.toString()
            val servidor = editTextServidor.text.toString()
            val senha = editTextSenha.text.toString()
            val netlistContent = editTextNetlist.text.toString()

            // Verifique se todos os campos foram preenchidos
            if (usuario.isNotEmpty() && servidor.isNotEmpty() && senha.isNotEmpty() && netlistContent.isNotEmpty()) {
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

        // Configure os listeners dos botões de adicionar componentes
        buttonAddResistor.setOnClickListener {
            editTextNetlist.append("\nR? N? N? ?")
        }

        buttonAddCapacitor.setOnClickListener {
            editTextNetlist.append("\nC? N? N? ?")
        }

        buttonAddIndutor.setOnClickListener {
            editTextNetlist.append("\nL? N? N? ?")
        }

        return root
    }
}