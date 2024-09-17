package com.example.tcc.ui.aulas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tcc.databinding.FragmentAulasBinding

class AulasFragment : Fragment() {

    private var _binding: FragmentAulasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val aulasViewModel =
            ViewModelProvider(this).get(AulasViewModel::class.java)

        _binding = FragmentAulasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configure os botões
        binding.buttonAnalogica.setOnClickListener {
            // Ação para o botão "Eletrônica Analógica"
        }

        binding.buttonDigital.setOnClickListener {
            // Ação para o botão "Eletrônica Digital"
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}