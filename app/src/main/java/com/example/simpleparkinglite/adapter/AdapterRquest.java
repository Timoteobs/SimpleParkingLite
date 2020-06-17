package com.example.simpleparkinglite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleparkinglite.R;
import com.example.simpleparkinglite.model.Destiny;
import java.util.List;

/**
 * Created by Jamilton
 */

public class AdapterRquest extends RecyclerView.Adapter<AdapterRquest.MyViewHolder>{

    private List<Destiny> destiny;
    private Context context;

    public AdapterRquest(List<Destiny> destinys, Context context) {
        this.destiny = destinys;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Destiny newDestiny = destiny.get(i);
        holder.rua.setText(newDestiny.getStreet());
        holder.descricao.setText((newDestiny.getCity() == null ? "" : newDestiny.getCity())  + " | " + (newDestiny.getNeighborhood() == null ? "" : newDestiny.getNeighborhood()));
        holder.cep.setText(newDestiny.getZipCode());
    }

    @Override
    public int getItemCount() {
        return destiny.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rua;
        TextView descricao;
        TextView cep;

        public MyViewHolder(View itemView) {
            super(itemView);
            rua = itemView.findViewById(R.id.textNomeRua);
            descricao = itemView.findViewById(R.id.textDescricao);
            cep = itemView.findViewById(R.id.textCep);
        }
    }
}
