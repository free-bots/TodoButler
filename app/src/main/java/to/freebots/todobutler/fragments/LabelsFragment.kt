package to.freebots.todobutler.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_labels.*
import kotlinx.android.synthetic.main.fragment_labels.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.LabelsAdapter
import to.freebots.todobutler.models.entities.Label

/**
 * [Fragment] for browse and editing labels.
 */
class LabelsFragment : Fragment() {

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

        labelsAdapter.labels = listOf(
            Label("Todo"),
            Label("Todo"),
            Label("Todo"),
            Label("Todo"),
            Label("Todo"),
            Label("Todo"),
            Label("Todo")
        )

        rv_label.adapter = labelsAdapter

        addLabelFab.setOnClickListener {
            Snackbar.make(it, "Todo",Snackbar.LENGTH_LONG).show()
        }
    }
}
