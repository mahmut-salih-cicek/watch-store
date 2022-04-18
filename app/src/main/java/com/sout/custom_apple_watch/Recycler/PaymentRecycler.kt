package com.sout.custom_apple_watch.Recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sout.custom_apple_watch.Model.PaymentModel
import com.sout.custom_apple_watch.R
import kotlinx.android.synthetic.main.payment_item.view.*

class PaymentRecycler(var list: ArrayList<PaymentModel>,var listener:Listener):RecyclerView.Adapter<PaymentRecycler.Holder>() {


    interface Listener{
        fun onItemClick(paymentModel: PaymentModel)
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun Binder(paymentModel: PaymentModel, listener: Listener) {
            itemView.setOnClickListener {
                listener.onItemClick(paymentModel)
            }
            itemView.payment_item_img.setImageResource(paymentModel.pay_img)
            itemView.payment_store_Subitle.text = paymentModel.pay_SubTitle
            itemView.payment_store_Title.text = paymentModel.pay_Title
            itemView.payment_price.text = paymentModel.pay_Price

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context).inflate(R.layout.payment_item,parent,false)
        return Holder(inflater)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Binder(list[position],listener)
    }

    override fun getItemCount(): Int {
        return list.size;
    }

}