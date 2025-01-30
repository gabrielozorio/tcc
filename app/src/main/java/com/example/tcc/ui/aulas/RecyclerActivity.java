package com.example.tcc.ui.aulas;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;


public class RecyclerActivity extends AppCompatActivity {

    RecyclerView Lista; // declaracao do recycler view
    String nomePdfs[], descrPdfs[]; // nomes e descricoes
    int modo; // seleciona nomes e descricoes diferentes



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        Lista = findViewById(R.id.Lista);

        getData();
        switch (modo){
            case 0:
                nomePdfs = getResources().getStringArray(R.array.teoAnalog);
                descrPdfs = getResources().getStringArray(R.array.descAnalog);
            break;
            case 1:
                nomePdfs = getResources().getStringArray(R.array.exeAnalog);
                descrPdfs = getResources().getStringArray(R.array.descAnalog);
            break;
            case 2:
                nomePdfs = getResources().getStringArray(R.array.teoDig);
                descrPdfs = getResources().getStringArray(R.array.descDigital);
            break;
            case 3:
                nomePdfs = getResources().getStringArray(R.array.exeDig);
                descrPdfs = getResources().getStringArray(R.array.descDigital);
            break;
        }

        // nomePdfs = getResources().getStringArray(R.array.arq_dig);

        RecyclerAdapter adaptador = new RecyclerAdapter(this, nomePdfs, descrPdfs);
        Lista.setAdapter(adaptador);
        Lista.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getData()
    {
        if(getIntent().hasExtra("opcao"))
        {

            modo = getIntent().getIntExtra("opcao",modo);
        }
        else
        {

            Toast.makeText(this,"no data.",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Navigate back to the previous fragment in the back stack
        getSupportFragmentManager().popBackStack();
        finish();
    }
}
