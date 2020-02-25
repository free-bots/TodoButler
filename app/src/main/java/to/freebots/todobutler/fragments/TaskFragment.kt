package to.freebots.todobutler.fragments


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.JsonWriter
import android.view.*
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.content_tasks.*
import kotlinx.android.synthetic.main.fragment_task.*
import org.json.JSONObject
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.common.mock.Mock
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.viewmodels.TaskViewModel
import java.time.LocalDateTime
import java.util.*

/**
 * [Fragment] to show the details of a task.
 */
class TaskFragment : Fragment(), TasksAdapter.Action, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application!!)
            .create(TaskViewModel::class.java)
    }

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
                arguments?.let {
                    it.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                        viewModel.delete(flatTaskDTO)
                    }
                }
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
            R.id.menu_clone -> {
                // TODO create a copy of the current task
                arguments?.let {
                    it.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                        viewModel.copyIntoParrent(flatTaskDTO)
                        // navigate to parent or the new child
                    }
                }
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

        applyListeners()
    }

    private fun applyTaskOnView(flatTaskDTO: FlatTaskDTO) {

        et_name.setText(flatTaskDTO.name)
        et_desc.setText(flatTaskDTO.description)

        sw_completed.isChecked = flatTaskDTO.isCompleted

        showSubTasks(flatTaskDTO.subTasks)
    }

    private fun applyListeners() {

        val now = LocalDateTime.now()

        tv_date.setOnClickListener {
            DatePickerDialog(context!!, this, now.year, now.monthValue - 1, now.dayOfMonth).show()
        }

        tv_time.setOnClickListener {
            TimePickerDialog(context!!, this, now.hour, now.minute, true).show()
        }

        sw_completed.setOnCheckedChangeListener { _, isChecked ->

        }

        et_name.doOnTextChanged { text, _, _, _ ->

        }

        et_desc.doOnTextChanged { text, _, _, _ -> }
    }

    private fun showSubTasks(subTasks: MutableList<FlatTaskDTO>) {

        if (subTasks.isEmpty().not()) {
            inflatedView = vs_sub_tasks.inflate()
        }

        inflatedView?.let {
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
        val date = LocalDateTime.now()
            .withYear(year)
            .withMonth(month + 1)
            .withDayOfMonth(dayOfMonth)
        // TODO update reminder
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        var time = LocalDateTime.now()
            .withHour(hourOfDay)
            .withMinute(minute)
        // TODO update reminder
    }
}
