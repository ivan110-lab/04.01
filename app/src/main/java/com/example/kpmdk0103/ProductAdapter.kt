package com.example.kpmdk0103

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var products: List<Product>,
    private val onCartClick: (Product) -> Unit,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var allProductsFull: List<Product> = emptyList()

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val price: TextView = view.findViewById(R.id.textView)
        val desc: TextView = view.findViewById(R.id.textView2)
        val btnAdd: Button = view.findViewById(R.id.button)
        val img: ImageView = view.findViewById(R.id.imageView2)
        val root: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_main2, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.price.text = "${product.price} ₽"
        holder.desc.text = product.description
        val context = holder.itemView.context
        val imageResId = context.resources.getIdentifier(
            product.imageUri,
            "drawable",
            context.packageName
        )

        if (imageResId != 0) {
            holder.img.setImageResource(imageResId)
        } else {
            holder.img.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.btnAdd.setOnClickListener { onCartClick(product) }
        holder.root.setOnClickListener { onItemClick(product) }
    }

    override fun getItemCount() = products.size

    fun updateData(newList: List<Product>) {
        this.products = newList
        this.allProductsFull = newList
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        products = if (query.isEmpty()) {
            allProductsFull
        } else {
            allProductsFull.filter { it.description.lowercase().contains(query.lowercase()) }
        }
        notifyDataSetChanged()
    }
}