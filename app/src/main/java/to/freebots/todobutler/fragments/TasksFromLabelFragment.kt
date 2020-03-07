package to.freebots.todobutler.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.content_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks_from_label.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.common.mock.Mock
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.viewmodels.TaskViewModel

/**
 * [Fragment] to show Task assigned to the label.
 */
class TasksFromLabelFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application!!)
            .create(TaskViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLabel()?.let {
            viewModel.filterByLabel(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks_from_label, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tasksAdapter = TasksAdapter()

        tasksAdapter.action = object : TasksAdapter.Action {
            override fun edit(flatTaskDTO: FlatTaskDTO) {

            }

            override fun open(flatTaskDTO: FlatTaskDTO) {
                val bundle = Bundle().apply {
                    putParcelable("flatTaskDTO", flatTaskDTO)
                }
                findNavController().navigate(
                    R.id.action_tasksFromLabelFragment_to_taskFragment,
                    bundle
                )
            }
        }

        rv_tasks.adapter = tasksAdapter

        addTaskFab.setOnClickListener {
            getLabel()?.let { label ->
                viewModel.createDefaultFlatTask(label).subscribe {
                    val bundle = Bundle().apply {
                        putParcelable("flatTaskDTO", it)
                    }
                    findNavController().navigate(
                        R.id.action_tasksFromLabelFragment_to_taskFragment,
                        bundle
                    )
                }
            }
        }

        viewModel.flatTasks.observe(this, Observer { t ->
            tasksAdapter.tasks = viewModel.filterByLabel(t)
        })
    }

    private fun getLabel(): Label? {
        return arguments?.getParcelable("label")
    }
}
