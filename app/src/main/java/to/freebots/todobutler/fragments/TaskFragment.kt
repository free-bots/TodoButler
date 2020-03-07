package to.freebots.todobutler.fragments


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_task.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.viewmodels.TaskViewModel
import java.time.LocalDateTime


/*

todo add swipe refresh layout and update with swipe




 */


/**
 * [Fragment] to show the details of a task.
 */
class TaskFragment : Fragment(), TasksAdapter.Action, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var flatTaskDTO: FlatTaskDTO

    private val viewModel by lazy {
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
                arguments?.let {
                    it.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                        viewModel.createSubTask(flatTaskDTO).subscribe { t: FlatTaskDTO? ->
                            val bundle = Bundle().apply {
                                putParcelable("flatTaskDTO", t)
                            }
                            findNavController().navigate(R.id.action_taskFragment_self, bundle)
                        }
                    }
                }
                return true
            }
            R.id.menu_clone -> {
                // TODO create a copy of the current task
                arguments?.let {
                    it.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                        viewModel.copyIntoParent(flatTaskDTO).subscribe { t ->
                            val bundle = Bundle().apply {
                                putParcelable("flatTaskDTO", t)
                            }
                            findNavController().navigate(R.id.action_taskFragment_self, bundle)
                        }
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

        arguments?.let { arg ->
            arg.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                viewModel.getUpdated(flatTaskDTO.id!!)
            }

            viewModel.TEST().observe(this, Observer {
                applyTaskOnView(it)
                applyListeners(it)
                this.flatTaskDTO = it
            })
        }
    }

    private fun applyTaskOnView(flatTaskDTO: FlatTaskDTO) {

        et_name.setText(flatTaskDTO.name)
        et_desc.setText(flatTaskDTO.description)

        sw_completed.isChecked = flatTaskDTO.isCompleted

        showSubTasks(flatTaskDTO.subTasks)
    }

    private fun applyListeners(flatTaskDTO: FlatTaskDTO) {

        val now = LocalDateTime.now()

        tv_date.setOnClickListener {
            DatePickerDialog(context!!, this, now.year, now.monthValue - 1, now.dayOfMonth).show()
        }

        tv_time.setOnClickListener {
            TimePickerDialog(context!!, this, now.hour, now.minute, true).show()
        }

        sw_completed.setOnCheckedChangeListener { _, isChecked ->
            viewModel.update(flatTaskDTO.apply { isCompleted = isChecked })
        }

        et_name.doOnTextChanged { text, _, _, _ ->
            viewModel.update(flatTaskDTO.apply { name = text.toString() })
        }

        et_desc.doOnTextChanged { text, _, _, _ ->
           viewModel.update(flatTaskDTO.apply { description = text.toString() })
        }
    }

    private fun showSubTasks(subTasks: MutableList<FlatTaskDTO>) {

        if (subTasks.isEmpty().not().and(inflatedView == null)) {
            inflatedView = vs_sub_tasks.inflate()
        }

        if (inflatedView != null) {
            val view = inflatedView?.findViewById<RecyclerView>(R.id.rv_tasks)

            view?.adapter = TasksAdapter().apply {
                action = this@TaskFragment
                tasks = subTasks
            }
        }

//        inflatedView?.let {
//            rv_tasks?.adapter = TasksAdapter().apply {
//                action = this@TaskFragment
//                tasks = subTasks
//            }
//        }
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
