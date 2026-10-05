package com.example.kpmdk0103

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private var cartList: List<Pair<CartItem, Product>>,
    private val onQuantityChanged: (CartItem, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imageView4)
        val price: TextView = view.findViewById(R.id.textView8)
        val desc: TextView = view.findViewById(R.id.textView9)
        val count: TextView = view.findViewById(R.id.textView10)
        val btnMinus: Button = view.findViewById(R.id.button6)
        val btnPlus: Button = view.findViewById(R.id.button7)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_main9, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val (cartItem, product) = cartList[position]

        holder.price.text = "${product.price * cartItem.quantity} ₽"
        holder.desc.text = product.description
        holder.count.text = cartItem.quantity.toString()
        val context = holder.itemView.context
        val resId = context.resources.getIdentifier(product.imageUri, "drawable", context.packageName)
        if (resId != 0) holder.img.setImageResource(resId)

        holder.btnPlus.setOnClickListener { onQuantityChanged(cartItem, cartItem.quantity + 1) }
        holder.btnMinus.setOnClickListener { onQuantityChanged(cartItem, cartItem.quantity - 1) }
    }

    override fun getItemCount() = cartList.size
    fun updateData(newList: List<Pair<CartItem, Product>>) {
        this.cartList = newList
        notifyDataSetChanged()
    }
}