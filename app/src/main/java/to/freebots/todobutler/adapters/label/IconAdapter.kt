package to.freebots.todobutler.adapters.label

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import to.freebots.todobutler.R

class IconAdapter : RecyclerView.Adapter<IconAdapter.IconHolder>(), View.OnClickListener {

    var items: MutableList<Int> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var action: Action? = null

    private var recyclerView: RecyclerView? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        return IconHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_icon,
                parent,
                false
            )
        ).apply {
            setIsRecyclable(false)
            setListener(this@IconAdapter)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: IconHolder, position: Int) {
        holder.bind(items[position])
    }


    class IconHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var name: TextView = itemView.findViewById(R.id.name)

        fun setListener(
            clickListener: View.OnClickListener
        ) {
            itemView.setOnClickListener(clickListener)
        }

        fun bind(
            icon: Int
        ) {
            this.name.visibility = View.GONE
            this.icon.setImageResource(icon)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        }

        action?.let { action ->
            recyclerView?.let {
                action.onOpen(items[it.getChildAdapterPosition(v)])
            }
        }
    }

    interface Action {
        fun onOpen(icon: Int)
    }
}