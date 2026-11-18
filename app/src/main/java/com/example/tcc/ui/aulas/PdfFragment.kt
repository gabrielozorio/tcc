package com.example.tcc.ui.aulas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tcc.R
import com.github.barteksc.pdfviewer.PDFView

class PdfFragment : Fragment() {

    private lateinit var pdfView: PDFView
    private var tituloArq: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pdf_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pdfView = view.findViewById(R.id.novopdf)
        tituloArq = arguments?.getString("nomArq")

        if (tituloArq != null) {
            pdfView.fromAsset(tituloArq).load()
        } else {
            Toast.makeText(requireContext(), "Nenhum arquivo fornecido.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(nomeArquivo: String): PdfFragment {
            val fragment = PdfFragment()
            val args = Bundle()
            args.putString("nomArq", nomeArquivo)
            fragment.arguments = args
            return fragment
        }
    }
}
