package to.freebots.todobutler.fragments


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.content_tasks.*
import kotlinx.android.synthetic.main.fragment_task.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.common.mock.Mock
import to.freebots.todobutler.models.entities.FlatTaskDTO

/**
 * [Fragment] to show the details of a task.
 */
class TaskFragment : Fragment(), TasksAdapter.Action, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var inflatedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_task, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_delete -> {
                // TODO delete and close fragment
                findNavController().navigateUp()
                return true
            }
            R.id.menu_color -> {

                return true
            }
            R.id.menu_location -> {
                // TODO show location fragment
                return true
            }
            R.id.menu_subtask -> {
                val bundle = Bundle().apply {
                    putParcelable("flatTaskDTO", Mock.flatTaskDTOWithSubTask)
                }
                findNavController().navigate(R.id.action_taskFragment_self, bundle)
                return true
            }
            else -> {
                println("no menu id")
                return false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            it.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                applyTaskOnView(flatTaskDTO)
            }
        }

        tv_date.setOnClickListener { v ->
            DatePickerDialog(context!!, this, 2020, 2, 19).show()
        }

        tv_time.setOnClickListener { v ->
            TimePickerDialog(context!!, this, 24, 21, true).show()
        }
    }

    private fun applyTaskOnView(flatTaskDTO: FlatTaskDTO) {

        et_name.setText(flatTaskDTO.name)
        et_desc.setText(flatTaskDTO.description)

        showSubTasks(flatTaskDTO.subTasks)
    }

    private fun showSubTasks(subTasks: MutableList<FlatTaskDTO>) {

        if (inflatedView == null) {
            inflatedView = vs_sub_tasks.inflate()
        }

        inflatedView?.let { view ->
            rv_tasks?.adapter = TasksAdapter().apply {
                action = this@TaskFragment
                tasks = subTasks
            }
        }
    }

    override fun edit(flatTaskDTO: FlatTaskDTO) {
        Toast.makeText(this.context, "edit subtask", Toast.LENGTH_LONG).show()
    }

    override fun open(flatTaskDTO: FlatTaskDTO) {
        val bundle = Bundle()
        bundle.putParcelable("flatTaskDTO", flatTaskDTO)
        Toast.makeText(this.context, "open subtask", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_taskFragment_self, bundle)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // TODO update reminder
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        // TODO update reminder
    }
}
