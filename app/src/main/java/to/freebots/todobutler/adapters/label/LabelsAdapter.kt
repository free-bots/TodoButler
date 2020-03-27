package to.freebots.todobutler.adapters.label

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import to.freebots.todobutler.R
import to.freebots.todobutler.models.entities.Label

class LabelsAdapter : RecyclerView.Adapter<LabelsAdapter.LabelHolder>(), View.OnLongClickListener,
    View.OnClickListener {

    var action: Action? = null

    var labels: List<Label> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        return LabelHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_icon,
                parent,
                false
            )
        ).apply {
            setIsRecyclable(false)
            setListener(this@LabelsAdapter, this@LabelsAdapter)
        }
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        holder.bind(
            labels[position].name,
            labels[position].icon.toIntOrNull() ?: run { 0 }
        )
    }


    class LabelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var name: TextView = itemView.findViewById(R.id.name)

        fun setListener(
            clickListener: View.OnClickListener,
            longClickListener: View.OnLongClickListener
        ) {
            itemView.setOnClickListener(clickListener)
            itemView.setOnLongClickListener(longClickListener)
        }

        fun bind(
            name: String,
            icon: Int
        ) {
            this.name.text = name
            this.icon.setImageResource(icon)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onLongClick(v: View?): Boolean {
        action?.let { action ->
            handleClickedLabel(v)?.let { label ->
                action.edit(label)
            }
            return true
        }
        return false
    }

    override fun onClick(v: View?) {
        action?.let { action ->
            handleClickedLabel(v)?.let { label ->
                action.open(label)
            }
        }
    }

    private fun handleClickedLabel(view: View?): Label? {
        return view?.let { view ->
            recyclerView?.let {
                labels[it.getChildAdapterPosition(view)]
            }
        }
    }

    interface Action {
        fun edit(label: Label)
        fun open(label: Label)
    }
}