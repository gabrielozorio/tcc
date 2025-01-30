package com.example.tcc.ui.aulas;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tcc.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PdfActivity extends AppCompatActivity {

    PDFView novoPdf;
    String tituloArq;
    int modo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        getData();
        novoPdf = findViewById(R.id.novopdf);
        setData();

    }


    private void getData()
    {
        if(getIntent().hasExtra("nomArq"))
        {
            tituloArq = getIntent().getStringExtra("nomArq");


        }
        else
        {
            Toast.makeText(this,"no data.",Toast.LENGTH_SHORT).show();
        }
    }

    private void setData()
    {
        novoPdf.fromAsset(tituloArq).load();

    }
}