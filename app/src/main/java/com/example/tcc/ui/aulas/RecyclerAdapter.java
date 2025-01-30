package com.example.tcc.ui.aulas;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    String s1[], s2[];
    Context context;

    public RecyclerAdapter(Context ct, String nomes[], String descr[]) {
        context = ct;
        s1 = nomes; // nome dos arquivos
        s2 = descr; // descriçao do topico
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.texto1.setText(s2[position]);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PdfActivity.class);
                intent.putExtra("nomArq",s1[position]); // passa o nome do arquivo pdf
                context.startActivity(intent);  // inicia atividade do pdf
            }

        });
    }

    @Override
    public int getItemCount() {
        return s1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView texto1;
        ConstraintLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            texto1 = itemView.findViewById(R.id.topCard);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
