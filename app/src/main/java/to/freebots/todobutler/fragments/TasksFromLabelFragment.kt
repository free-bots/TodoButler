package to.freebots.todobutler.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.content_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks_from_label.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label

/**
 * [Fragment] to show Task assigned to the label.
 */
class TasksFromLabelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks_from_label, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var tasksAdapter = TasksAdapter()

        var tasks: MutableList<FlatTaskDTO> = mutableListOf(
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(),
                mutableListOf()
            ),
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(),
                mutableListOf()
            ),
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(),
                mutableListOf()
            ),
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(),
                mutableListOf()
            )
        )

        tasksAdapter.tasks = tasks

        tasksAdapter.action = object : TasksAdapter.Action {
            override fun edit(flatTaskDTO: FlatTaskDTO) {

            }

            override fun open(flatTaskDTO: FlatTaskDTO) {

            }
        }

        rv_tasks.adapter = tasksAdapter

        addTaskFab.setOnClickListener { }
    }
}
