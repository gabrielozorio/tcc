package com.example.tcc.ui.sobre

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tcc.R
import com.example.tcc.databinding.FragmentSobreBinding

class SobreFragment : Fragment() {

    private var _binding: FragmentSobreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sobreViewModel =
            ViewModelProvider(this).get(SobreViewModel::class.java)

        _binding = FragmentSobreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val input = binding.inputNetlistId
        val btn = binding.btnCopiar
        val status = binding.textStatus

        val netlists = resources.getStringArray(R.array.netlists_spice)

        btn.setOnClickListener {
            val inputText = input.text.toString()
            val index = inputText.toIntOrNull()

            if (index == null || index !in netlists.indices) {
                status.text = "ID inválido!"
            } else {
                val netlist = netlists[index]

                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Netlist SPICE", netlist)
                clipboard.setPrimaryClip(clip)

                status.text = "Netlist #$index copiado!"
                Toast.makeText(requireContext(), "Copiado para o clipboard", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}