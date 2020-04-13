package to.freebots.todobutler.fragments


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import kotlinx.android.synthetic.main.content_tasks.*
import kotlinx.android.synthetic.main.fragment_task.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.databinding.FragmentTaskBinding
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Reminder
import to.freebots.todobutler.viewmodels.TaskViewModel
import java.util.*

/**
 * [Fragment] to show the details of a task.
 */
class TaskFragment : Fragment(), TasksAdapter.Action, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
        ).get(TaskViewModel::class.java)
    }

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
                viewModel.delete()
                return true
            }
            R.id.menu_color -> {
                val defaultColor = this.viewModel.color.value ?: run { "" }
                MaterialColorPickerDialog
                    .Builder(activity!!)                        // Pass Activity Instance
                    .setColorShape(ColorShape.SQAURE)    // Default ColorShape.CIRCLE
                    .setColorSwatch(ColorSwatch._300)    // Default ColorSwatch._500
                    .setDefaultColor(defaultColor)
                    .setColorListener { color, colorHex ->
                        // Handle Color Selection
                        this.viewModel.color.postValue(colorHex)
                    }
                    .show()
                return true
            }
            R.id.menu_pinned -> {
                this.viewModel.isPinned.postValue(!this.viewModel.isPinned.value!!)
                return true
            }
            R.id.menu_location -> {
                arguments?.let { arg ->
                    arg.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                        val bundle = Bundle().apply { putParcelable("flatTaskDTO", flatTaskDTO) }
                        findNavController().navigate(
                            R.id.action_taskFragment_to_locationFragment,
                            bundle
                        )
                    }
                }
//                findNavController().navigate(R.id.action_taskFragment_to_locationFragment)
                return true
            }
            R.id.menu_subtask -> {
                viewModel.createSubTask()
                return true
            }
            R.id.menu_clone -> {
                viewModel.copyIntoParent()
                return true
            }
            R.id.menu_attachment -> {
                arguments?.let { arg ->
                    arg.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                        val bundle = Bundle().apply { putParcelable("flatTaskDTO", flatTaskDTO) }
                        findNavController().navigate(
                            R.id.action_taskFragment_to_attachmentFragment,
                            bundle
                        )
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
        val binding: FragmentTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)
        binding.model = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { arg ->
            arg.getParcelable<FlatTaskDTO>("flatTaskDTO")?.let { flatTaskDTO ->
                viewModel.getUpdated(flatTaskDTO.id!!)
            }
        }


        viewModel._task.observe(viewLifecycleOwner, Observer {
            showSubTasks(it.subTasks)
            Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
        })

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            it.consume()?.let { eventWrapper ->
                if (eventWrapper.navigateUp) {
                    findNavController().navigateUp()
                }
                eventWrapper.e?.let {
                    val bundle = Bundle().apply {
                        putParcelable("flatTaskDTO", it)
                    }
                    findNavController().navigate(R.id.action_taskFragment_self, bundle)
                }
            }
        })

        applyColor()

        viewModel.isPinned.observe(viewLifecycleOwner, Observer {
            // todo set icon and text on menu
        })

        viewModel.labelIcon.observe(viewLifecycleOwner, Observer {
            imageView.setImageResource(it)
        })

        viewModel.reminder.observe(viewLifecycleOwner, Observer {
            applyReminder(it)
        })
    }

    private fun applyReminder(reminder: Reminder?) {

        val calendar = Calendar.getInstance()
        calendar.time = reminder?.date ?: run { Date() }

        tv_date.setOnClickListener {
            DatePickerDialog(
                context!!,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        tv_time.setOnClickListener {
            TimePickerDialog(
                context!!,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        b_remove_reminder.setOnClickListener {
            viewModel.deleteReminder()
        }


        if (reminder != null) {
            // todo format
            tv_date.text = calendar.time.toString()
            tv_time.text = calendar.time.toString()
        } else {
            tv_date.text = "date"
            tv_time.text = "time"
        }
    }

    private fun showSubTasks(subTasks: MutableList<FlatTaskDTO>) {
        if (rv_tasks.adapter == null) {
            rv_tasks.adapter = TasksAdapter().apply {
                action = this@TaskFragment
                tasks = subTasks
            }
        }
    }

    override fun edit(flatTaskDTO: FlatTaskDTO) {
        Toast.makeText(this.context, "edit subtask", Toast.LENGTH_LONG).show()
    }

    override fun open(flatTaskDTO: FlatTaskDTO) {
        Toast.makeText(this.context, "open subtask", Toast.LENGTH_LONG).show()
        viewModel.navigate(flatTaskDTO)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.updateReminderDate(year, month, dayOfMonth)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.updateReminderTime(hourOfDay, minute)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.update()
    }

    private fun applyColor() {
        this.viewModel.color.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                // todo reset color ?
                return@Observer
            }

            task_root.setBackgroundColor(Color.parseColor(it))
        })
    }
}
