package com.sout.custom_apple_watch.Recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sout.custom_apple_watch.Model.StoreModel
import com.sout.custom_apple_watch.R
import com.sout.custom_apple_watch.Util.gorselyap
import kotlinx.android.synthetic.main.store_row.view.*

class StoreRecycler(var list : ArrayList<StoreModel>, var listener:Listener):RecyclerView.Adapter<StoreRecycler.Holder>() {

    interface Listener{
        fun OnItemClickListener(storeModel: StoreModel)
    }

    class Holder(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun Bind(storeModel: StoreModel, listener: Listener) {
            itemView.setOnClickListener {
                listener.OnItemClickListener(storeModel)
            }
            itemView.name.text = storeModel.name
            //itemView.ss.gorselyap(storeModel.ss)
            itemView.discount.text = storeModel.discount
            itemView.ico.gorselyap(storeModel.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context).inflate(R.layout.store_row,parent,false)
        return Holder(inflater)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Bind(list[position],listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}