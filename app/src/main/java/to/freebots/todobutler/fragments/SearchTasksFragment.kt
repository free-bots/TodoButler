package to.freebots.todobutler.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.content_tasks.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.common.fragment.BaseTaskFragment
import to.freebots.todobutler.databinding.FragmentSearchTasksBinding

/**
 * A simple [Fragment] subclass.
 */
class SearchTasksFragment : BaseTaskFragment() {

    override fun setTasksObserver(adapter: TasksAdapter) {
        this.viewModel.flatTasks.observe(viewLifecycleOwner, Observer {  })
        this.viewModel.searchedTasks.observe(viewLifecycleOwner, Observer {
            println(it.size)
            adapter.tasks = it
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSearchTasksBinding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_search_tasks, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTaskAdapter(rv_tasks, R.id.action_searchTasksFragment_to_taskFragment)

        viewModel.searchQuery.observe(viewLifecycleOwner, Observer {
            onSearch(it)
        })
    }

    private fun onSearch(query: String) {
            viewModel.search(query)
    }
}
