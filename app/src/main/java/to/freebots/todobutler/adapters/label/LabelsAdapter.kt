package to.freebots.todobutler.adapters.label

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import to.freebots.todobutler.R
import to.freebots.todobutler.models.entities.Label

class LabelsAdapter : RecyclerView.Adapter<LabelsAdapter.LabelHolder>() {

    var labels: List<Label> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelHolder {
        return LabelHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_icon,
                parent,
                false
            )
        ).apply {
            setIsRecyclable(false)
        }
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(holder: LabelHolder, position: Int) {
        holder.bind(
            labels[position].name,
            View.OnClickListener {
                Snackbar.make(it, "Todo", Snackbar.LENGTH_LONG).show()
            },
            View.OnLongClickListener {
                Snackbar.make(it, "Todo",Snackbar.LENGTH_LONG).show()
                true
            }
        )
    }


    class LabelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var name: TextView = itemView.findViewById(R.id.name)

        fun bind(
            name: String,
            clickListener: View.OnClickListener,
            longClickListener: View.OnLongClickListener
        ) {
            this.name.text = name
            this.icon.setImageResource(R.drawable.ic_add_24px)
            itemView.setOnClickListener(clickListener)
            itemView.setOnLongClickListener(longClickListener)
        }
    }
}