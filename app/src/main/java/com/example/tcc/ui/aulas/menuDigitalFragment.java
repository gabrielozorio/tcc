package com.example.tcc.ui.aulas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tcc.R;

public class menuDigitalFragment extends Fragment {



    public menuDigitalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fview = inflater.inflate(R.layout.fragment_menu_digital, container, false);


        final Button btnVoltar = fview.findViewById(R.id.buttonVolt2);
        btnVoltar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View fview) {
                Navigation.findNavController(fview).navigate(R.id.action_navigation_dig_to_navigation_home);
            }
        });

        final Button btnTeoria = fview.findViewById(R.id.buttonTeoDigital);
        btnTeoria.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View fview) {
                Context ct = fview.getContext();
                Intent recycler = new Intent(ct, RecyclerActivity.class);
                recycler.putExtra("opcao",2);
                ct.startActivity(recycler);
            }
        });

        final Button btnExe = fview.findViewById(R.id.buttonExeDigital);
        btnExe.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View fview) {
                Context ct = fview.getContext();
                Intent recycler = new Intent(ct, RecyclerActivity.class);
                recycler.putExtra("opcao",3);
                ct.startActivity(recycler);
            }
        });

        final Button btnSim = fview.findViewById(R.id.buttonSimDigital);
        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Navigation.findNavController(view).navigate(R.id.action_menuDigitalFragment_to_simuladorDigFragment);
            }
        });

        return fview;
    }
}