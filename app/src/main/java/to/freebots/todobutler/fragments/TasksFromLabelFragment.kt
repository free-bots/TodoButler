package to.freebots.todobutler.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.content_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks_from_label.*
import kotlinx.android.synthetic.main.layout_empty.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.common.BindingConverter
import to.freebots.todobutler.common.fragment.BaseTaskFragment

/**
 * [Fragment] to show Task assigned to the label.
 */
class TasksFromLabelFragment : BaseTaskFragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                findNavController()
                    .navigate(R.id.action_tasksFromLabelFragment_to_searchTasksFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
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

    override fun setTasksObserver(adapter: TasksAdapter) {
        viewModel.flatTasks.observe(viewLifecycleOwner, Observer { t ->
            BindingConverter.showEmptyIcon(iv_empty, t, tv_empty, getString(R.string.empty_tasks))
            adapter.tasks = viewModel.filterByLabel(t)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTaskAdapter(rv_tasks, R.id.action_tasksFromLabelFragment_to_taskFragment)
        initNewTaskAction(addTaskFab, R.id.action_tasksFromLabelFragment_to_taskFragment)
    }
}
