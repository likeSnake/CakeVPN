package com.lazycoder.cakevpn.view;

import static com.lazycoder.cakevpn.data.FirebaseData.VPN_SERVERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.lazycoder.cakevpn.R;
import com.lazycoder.cakevpn.adapter.NodesAdapter;
import com.lazycoder.cakevpn.bean.ServerBean;
import com.lazycoder.cakevpn.interfaces.NavItemClickListener;
import com.lazycoder.cakevpn.util.SpacesItemDecoration;

import java.util.ArrayList;

import soup.neumorphism.NeumorphImageView;

public class ActNode extends AppCompatActivity implements View.OnClickListener, NavItemClickListener {

    private NeumorphImageView iv_back;
    private RecyclerView recycler_view;
    ArrayList<ServerBean> list;

    private NodesAdapter ap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_node);
        initUi();

    }

    private void initUi(){

        list = (ArrayList<ServerBean>) getIntent().getSerializableExtra(VPN_SERVERS);
        if (list==null){
            return;
        }

        iv_back = findViewById(R.id.iv_back);
        recycler_view = findViewById(R.id.recycler_view);
        iv_back.setOnClickListener(this);

        initAdapter(false,list);
    }

    private void initAdapter(Boolean b,ArrayList<ServerBean> list){

        LinearLayoutManager manager = new LinearLayoutManager(ActNode.this, LinearLayoutManager.VERTICAL, false);
        ap = new NodesAdapter(this,list);
        if(b){
            recycler_view.scrollToPosition(ap.getItemCount()-1);
        }
        recycler_view.addItemDecoration(new SpacesItemDecoration(30));
        recycler_view.setLayoutManager(manager);
        recycler_view.setAdapter(ap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

        }
    }

    @Override
    public void clickedItem(int index) {

    }
}