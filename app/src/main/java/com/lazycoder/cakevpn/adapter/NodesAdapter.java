package com.lazycoder.cakevpn.adapter;


import static com.lazycoder.cakevpn.data.FirebaseData.VPN_SERVERS;
import static com.lazycoder.cakevpn.util.Utils.getCountryFlag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lazycoder.cakevpn.R;
import com.lazycoder.cakevpn.bean.ServerBean;
import com.lazycoder.cakevpn.interfaces.NavItemClickListener;
import com.lazycoder.cakevpn.util.Utils;
import com.lazycoder.cakevpn.view.ActMain;

import java.util.ArrayList;

public class NodesAdapter extends RecyclerView.Adapter<NodesAdapter.ViewHolder> {

    private Context context;
    private NavItemClickListener listener;
    private ArrayList<ServerBean> list;
    private int temp;

    public NodesAdapter(Context context, ArrayList<ServerBean> list) {
        this.context = context;
        this.list = list;
        listener = (NavItemClickListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server, parent, false);
        return new NodesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ServerBean serverBean = list.get(position);
        String wa_ser_city = serverBean.getWa_ser_city();
        String wa_ser_country = serverBean.getWa_ser_country();
        Utils.MyLog(serverBean.getSelected());

        if (serverBean.getSelected()) {
            temp = position;
            holder.isChoose.setImageResource(R.drawable.choose_yes);
            holder.tv_vpn_title.setTextColor(ContextCompat.getColor(holder.tv_vpn_title.getContext(), R.color.blue));
        } else {
            holder.isChoose.setImageResource(R.drawable.choose_no);
            holder.tv_vpn_title.setTextColor(ContextCompat.getColor(holder.tv_vpn_title.getContext(), R.color.black));
        }
        holder.tv_vpn_title.setText(wa_ser_city);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.clickedItem(position);


                list.get(temp).setSelected(false);
                serverBean.setSelected(true);
                Intent intent = new Intent(context, ActMain.class);
                intent.putExtra(VPN_SERVERS, list);
                context.startActivity(intent);


            }
        });

        holder.iv_flag.setImageResource(getCountryFlag(wa_ser_country));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_flag;
        ImageView isChoose;
        TextView tv_vpn_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_flag = itemView.findViewById(R.id.iv_flag);
            isChoose = itemView.findViewById(R.id.isChoose);
            tv_vpn_title = itemView.findViewById(R.id.tv_vpn_title);
        }

    }

}
