package to.freebots.todobutler.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_labels.*
import kotlinx.android.synthetic.main.fragment_labels.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.LabelsAdapter
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
                Snackbar.make(this@LabelsFragment.view!!, label.name, Snackbar.LENGTH_LONG).show()
                showInfoOfLabel(label)
            }

            override fun open(label: Label) {
                Snackbar.make(this@LabelsFragment.view!!, label.name, Snackbar.LENGTH_LONG).show()
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

            findNavController().navigate(R.id.action_labelsFragment_to_tasksFragment)

//            Snackbar.make(it, "Todo", Snackbar.LENGTH_LONG).show()
//            showInfoOfLabel(Label("new"))
        }


        viewModel.labels.observe(this, Observer { t: MutableList<Label> ->
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
        Toast.makeText(context, label.name, Toast.LENGTH_LONG).show()
        viewModel.newLabelValues(label)
    }

    override fun delete(label: Label) {
        viewModel.delete(label)
    }
}
