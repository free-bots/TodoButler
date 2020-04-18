package to.freebots.todobutler.adapters.label

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import to.freebots.todobutler.R
import to.freebots.todobutler.models.entities.Attachment

class AttachmentsAdapter : RecyclerView.Adapter<AttachmentsAdapter.AttachmentHolder>(),
    View.OnLongClickListener,
    View.OnClickListener {

    var action: Action? = null

    var attachments: MutableList<Attachment> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentHolder {
        return AttachmentHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_icon,
                parent,
                false
            )
        ).apply {
            setIsRecyclable(false)
            setListener(this@AttachmentsAdapter, this@AttachmentsAdapter)
        }
    }

    override fun getItemCount(): Int {
        return attachments.size
    }

    override fun onBindViewHolder(holder: AttachmentHolder, position: Int) {
        holder.bind(attachments[position].name)
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
            handleClickedLabel(v)?.let { attachment ->
                action.open(attachment)
            }
        }
    }

    private fun handleClickedLabel(view: View?): Attachment? {
        return view?.let { view ->
            recyclerView?.let {
                attachments[it.getChildAdapterPosition(view)]
            }
        }
    }

    interface Action {
        fun edit(attachment: Attachment)
        fun open(attachment: Attachment)
    }

    class AttachmentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var name: TextView = itemView.findViewById(R.id.name)

        fun setListener(
            clickListener: View.OnClickListener,
            longClickListener: View.OnLongClickListener
        ) {
            itemView.setOnClickListener(clickListener)
            itemView.setOnLongClickListener(longClickListener)
        }

        fun bind(name: String) {
            this.name.text = name
            this.icon.setImageResource(R.drawable.ic_attachment)
        }
    }
}