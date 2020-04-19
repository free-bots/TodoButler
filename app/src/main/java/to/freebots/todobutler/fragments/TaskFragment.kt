package to.freebots.todobutler.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
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
import kotlinx.android.synthetic.main.layout_empty.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.TasksAdapter
import to.freebots.todobutler.common.BindingConverter
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

    override fun onPrepareOptionsMenu(menu: Menu) {
        viewModel.isPinned.observe(viewLifecycleOwner, Observer {
            with(menu.findItem(R.id.menu_pinned)) {
                setIcon(
                    if (it) {
                        R.drawable.ic_notifications_none_black_18dp
                    } else {
                        R.drawable.ic_notifications_off_black_18dp
                    }
                )
                setTitle(
                    if (it) {
                        getString(R.string.pinned)
                    } else {
                        getString(R.string.not_pinned)
                    }
                )
            }
        })

    }

    @SuppressLint("ResourceType")
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
                    .setColors(
                        listOf(
                            getString(R.color.nord0),
                            getString(R.color.nord1),
                            getString(R.color.nord2),
                            getString(R.color.nord3),
                            getString(R.color.nord4),
                            getString(R.color.nord5),
                            getString(R.color.nord6),
                            getString(R.color.nord7),
                            getString(R.color.nord8),
                            getString(R.color.nord9),
                            getString(R.color.nord10),
                            getString(R.color.nord11),
                            getString(R.color.nord12),
                            getString(R.color.nord13),
                            getString(R.color.nord14),
                            getString(R.color.nord15)
                        )
                    )
                    .setColorListener { _, colorHex ->
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
                    arg.getLong("flatTaskDTO").let { flatTaskDTO ->
                        val bundle = Bundle().apply { putLong("flatTaskDTO", flatTaskDTO) }
                        findNavController().navigate(
                            R.id.action_taskFragment_to_locationFragment,
                            bundle
                        )
                    }
                }
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
                    arg.getLong("flatTaskDTO").let { flatTaskDTO ->
                        val bundle = Bundle().apply { putLong("flatTaskDTO", flatTaskDTO) }
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
            arg.getLong("flatTaskDTO").let { flatTaskDTO ->
                viewModel.getUpdated(flatTaskDTO)
            }
        }


        viewModel._task.observe(viewLifecycleOwner, Observer {
            showSubTasks(it.subTasks)
        })

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            it.consume()?.let { eventWrapper ->
                if (eventWrapper.navigateUp) {
                    findNavController().navigateUp()
                }
                eventWrapper.e?.let {
                    val bundle = Bundle().apply {
                        putLong("flatTaskDTO", it.id!!)
                    }
                    findNavController().navigate(R.id.action_taskFragment_self, bundle)
                }
            }
        })

        applyColor()

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

        b_reminder.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage(getString(R.string.reminder))
                .setPositiveButton(getString(R.string.set_date)) { _, _ ->
                    DatePickerDialog(
                        context!!,
                        this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
                .setNeutralButton(getString(R.string.set_time)) { _, _ ->
                    TimePickerDialog(
                        context!!,
                        this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }
                .setNegativeButton(getString(R.string.delete)) { _, _ ->
                    viewModel.deleteReminder()
                }
                .create()
                .show()
        }
    }

    private fun showSubTasks(subTasks: MutableList<FlatTaskDTO>) {
        BindingConverter.showEmptyIcon(
            iv_empty,
            subTasks,
            tv_empty,
            getString(R.string.empty_sub_tasks)
        )
        if (rv_tasks.adapter == null) {
            rv_tasks.adapter = TasksAdapter().apply {
                action = this@TaskFragment
                tasks = subTasks
            }
        }
    }

    override fun edit(flatTaskDTO: FlatTaskDTO) {
    }

    override fun open(flatTaskDTO: FlatTaskDTO) {
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
                task_root.setBackgroundResource(R.color.colorPrimary)
                return@Observer
            }

            task_root.setBackgroundColor(Color.parseColor(it))
        })
    }
}
