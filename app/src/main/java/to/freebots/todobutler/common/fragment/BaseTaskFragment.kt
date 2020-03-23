package to.freebots.todobutler.common.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.viewmodels.TaskViewModel

abstract class BaseTaskFragment : Fragment() {

    protected val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
        ).get(TaskViewModel::class.java)
    }

    protected fun initNewTaskAction(view: View, navigation: Int) {
        view.setOnClickListener {
            getLabel()?.let { label ->
                viewModel.createDefaultFlatTask(label).subscribe {
                    val bundle = Bundle().apply {
                        putParcelable("flatTaskDTO", it)
                    }
                    findNavController().navigate(
                        navigation,
                        bundle
                    )
                }
            }
        }
    }

    protected fun getLabel(): Label? {
        // Todo overwrite with default if not needed
        return arguments?.getParcelable("label")
    }

    protected fun initTaskAdapter(recyclerView: RecyclerView, navigation: Int) {
        val tasksAdapter = TasksAdapter()

        tasksAdapter.action = object : TasksAdapter.Action {
            override fun edit(flatTaskDTO: FlatTaskDTO) {

            }

            override fun open(flatTaskDTO: FlatTaskDTO) {
                val bundle = Bundle().apply {
                    putParcelable("flatTaskDTO", flatTaskDTO)
                }
                findNavController().navigate(
                    navigation,
                    bundle
                )
            }
        }

        setTasksObserver(tasksAdapter)

        recyclerView.adapter = tasksAdapter
    }

    abstract fun setTasksObserver(adapter: TasksAdapter)
}