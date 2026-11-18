package com.example.tcc.ui.aulas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcc.R

class RecyclerFragment : Fragment() {

    private lateinit var lista: RecyclerView
    private lateinit var nomePdfs: Array<String>
    private lateinit var descrPdfs: Array<String>
    private var modo: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recycler_fragment, container, false) // pode manter esse layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lista = view.findViewById(R.id.Lista)
        modo = arguments?.getInt("opcao") ?: 0

        when (modo) {
            0 -> {
                nomePdfs = resources.getStringArray(R.array.teoAnalog)
                descrPdfs = resources.getStringArray(R.array.descAnalog)
            }
            1 -> {
                nomePdfs = resources.getStringArray(R.array.exeAnalog)
                descrPdfs = resources.getStringArray(R.array.descAnalog)
            }
            2 -> {
                nomePdfs = resources.getStringArray(R.array.teoDig)
                descrPdfs = resources.getStringArray(R.array.descDigital)
            }
            3 -> {
                nomePdfs = resources.getStringArray(R.array.exeDig)
                descrPdfs = resources.getStringArray(R.array.descDigital)
            }
        }

        val adaptador = RecyclerAdapter(requireContext(), nomePdfs, descrPdfs)
        lista.adapter = adaptador
        lista.layoutManager = LinearLayoutManager(requireContext())
    }

}
