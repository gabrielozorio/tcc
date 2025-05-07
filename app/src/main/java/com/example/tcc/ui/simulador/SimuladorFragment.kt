package com.example.tcc.ui.simulador

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tcc.R
import com.example.tcc.funcoes.SSH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SimuladorFragment : Fragment() {


    private lateinit var editTextUsuario: EditText
    private lateinit var editTextServidor: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var editTextNetlist: EditText
    private lateinit var buttonConectar: Button





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


        editTextUsuario.setText("openssh2")
        editTextServidor.setText("192.168.1.17")
        editTextSenha.setText("openssh2")

        val sharedPreferences = requireContext().getSharedPreferences("SimuladorPrefs", Context.MODE_PRIVATE)
        val ultimoNetlist = sharedPreferences.getString("ultimoNetlist", "")
        editTextNetlist.setText(ultimoNetlist)

        //val netlistContent = carregarNetlist(requireContext())  // Chama a função do arquivo Auxiliar.kt
        fun getSimulacoesDirectory():File {
            val context = requireContext()
            // Cria o diretório 'simulacoes' no armazenamento interno
            val directory = File(context.filesDir, "simulacoes")
            if (!directory.exists()) {
                directory.mkdirs()  // Cria o diretório caso não exista
                Log.d("Storage","Criou diretorio simulacoes")
            } else
                Log.d("Storage","Nao criou diretorio simulacoes")
            return directory
        }


        //editTextNetlist.setText(netlistContent)
        // Configure o listener do botão conectar
        buttonConectar.setOnClickListener {

            val simulacoesDir = getSimulacoesDirectory()
            simulacoesDir.listFiles()?.forEach { file ->
                if (file.isFile) {
                    file.delete()
                }
            }

            val usuario = editTextUsuario.text.toString()
            val servidor = editTextServidor.text.toString()
            val senha = editTextSenha.text.toString()
            val netlistContent = editTextNetlist.text.toString()


            // Verifique se todos os campos foram preenchidos
            if (usuario.isNotEmpty() && servidor.isNotEmpty() && senha.isNotEmpty() && netlistContent.isNotEmpty()) {
                // Conecte-se ao servidor via SSH

                fun salvarNetlist(netlistContent: String) {
                    val sharedPreferences = requireContext().getSharedPreferences("SimuladorPrefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("ultimoNetlist", netlistContent)
                        apply()
                    }
                }

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                //CoroutineScope(Dispatchers.IO).launch {
                    val ssh = SSH()
                   // val textosExtraidos = ssh.extrairAntesDeTxt(netlistContent)
                   // Log.d("Netlist", "Textos extraídos: $textosExtraidos")
                   // val context = this // 'this' se refere ao contexto da MainActivity

                    salvarNetlist(netlistContent)
                    ssh.enviarArquivoNetlist(usuario, servidor, senha, netlistContent)
                    ssh.executarNgspice(usuario, servidor, senha,getSimulacoesDirectory(),requireContext())

                }
            } else {
                Log.e("SimuladorFragment", "Preencha todos os campos antes de conectar.")
            }
        }




        return root
    }
}