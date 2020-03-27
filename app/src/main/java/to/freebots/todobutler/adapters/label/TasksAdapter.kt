package to.freebots.todobutler.adapters.label

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import to.freebots.todobutler.R
import to.freebots.todobutler.models.entities.FlatTaskDTO

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskHolder>(), View.OnClickListener,
    View.OnLongClickListener {

    var tasks: MutableList<FlatTaskDTO> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var action: Action? = null

    private var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_task_item,
                parent,
                false
            )
        ).apply {
            setIsRecyclable(false)
            setListener(this@TasksAdapter, this@TasksAdapter)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(
            tasks[position].name,
            "",
            tasks[position].label.icon.toIntOrNull() ?: run { 0 }
        )
    }

    class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.iv_icon)
        var info: TextView = itemView.findViewById(R.id.tv_task_info)
        var time: TextView = itemView.findViewById(R.id.tv_time)

        fun setListener(
            clickListener: View.OnClickListener,
            longClickListener: View.OnLongClickListener
        ) {
            itemView.setOnClickListener(clickListener)
            itemView.setOnLongClickListener(longClickListener)
        }

        fun bind(
            info: String,
            time: String? = null,
            icon: Int
        ) {
            this.info.text = info
            this.time.text = time
            this.icon.setImageResource(icon)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onClick(v: View?) {
        action?.let { action ->
            handleClickedLabel(v)?.let { task ->
                action.open(task)
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        action?.let { action ->
            handleClickedLabel(v)?.let { task ->
                action.edit(task)
            }
            return true
        }
        return false
    }

    private fun handleClickedLabel(view: View?): FlatTaskDTO? {
        return view?.let { view ->
            recyclerView?.let {
                tasks[it.getChildAdapterPosition(view)]
            }
        }
    }

    interface Action {
        fun edit(flatTaskDTO: FlatTaskDTO)
        fun open(flatTaskDTO: FlatTaskDTO)
    }
}