package com.example.kpmdk0103

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private var orders: List<OrderItem>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imageView2)
        val price: TextView = view.findViewById(R.id.textView)
        val desc: TextView = view.findViewById(R.id.textView2)
        val btn: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_main2, parent, false)
        view.layoutParams.width = (parent.resources.displayMetrics.widthPixels * 0.7).toInt()
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(h: OrderViewHolder, p: Int) {
        val o = orders[p]
        h.price.text = "${o.price} ₽"
        h.desc.text = o.productDescription
        h.btn.visibility = View.GONE
        val name = when {
            o.productDescription.lowercase().contains("смартфон") -> "phone"
            o.productDescription.lowercase().contains("наушники") -> "headphones"
            o.productDescription.lowercase().contains("зарядка") -> "charger"
            else -> "basket"
        }

        val context = h.itemView.context
        val resId = context.resources.getIdentifier(name, "drawable", context.packageName)

        if (resId != 0) {
            h.img.setImageResource(resId)
        } else {
            h.img.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    override fun getItemCount() = orders.size

    fun updateData(newList: List<OrderItem>) {
        this.orders = newList
        notifyDataSetChanged()
    }
}