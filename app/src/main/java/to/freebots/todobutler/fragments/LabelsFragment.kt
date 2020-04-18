package to.freebots.todobutler.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.content_labels.*
import kotlinx.android.synthetic.main.fragment_labels.*
import kotlinx.android.synthetic.main.layout_empty.*
import to.freebots.iconhelper.IconHelper
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.LabelsAdapter
import to.freebots.todobutler.common.BindingConverter
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.viewmodels.LabelViewModel

/**
 * [Fragment] for browse and editing labels.
 */
class LabelsFragment : Fragment(), LabelDialogFragment.EditListener {

    private val viewModel: LabelViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application!!)
            .create(LabelViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_search_and_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                findNavController()
                    .navigate(R.id.action_labelsFragment_to_searchTasksFragment)
                return true
            }
            R.id.menu_info -> {
                Toast.makeText(context, "INFO", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_labels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val labelsAdapter = LabelsAdapter()
        labelsAdapter.action = object : LabelsAdapter.Action {
            override fun edit(label: Label) {
                showInfoOfLabel(label)
            }

            override fun open(label: Label) {
                val bundle = Bundle().apply {
                    putParcelable("label", label)
                }
                this@LabelsFragment.findNavController()
                    .navigate(
                        R.id.action_labelsFragment_to_tasksFromLabelFragment, bundle
                    )
            }
        }

        rv_label.adapter = labelsAdapter

        addLabelFab.setOnClickListener {
            showInfoOfLabel(Label("new", IconHelper.labelIcons().random().toString()))
        }


        viewModel.labels.observe(viewLifecycleOwner, Observer { t: MutableList<Label> ->
            BindingConverter.showEmptyIcon(iv_empty, t, tv_empty, getString(R.string.empty_labels))
            labelsAdapter.labels = t
        })
    }

    private fun showInfoOfLabel(label: Label) {
        this.activity?.supportFragmentManager?.let {
            LabelDialogFragment
                .instance(label, this)
                .show(it, "label")
        }
    }

    override fun labelInfo(label: Label) {
        viewModel.newLabelValues(label)
    }

    override fun delete(label: Label) {
        viewModel.delete(label)
    }
}
