package to.freebots.todobutler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_label_info.*
import to.freebots.todobutler.R
import to.freebots.todobutler.models.entities.Label


class LabelDialogFragment : BottomSheetDialogFragment() {

    var editListener: EditListener? = null

    var label: Label? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_label_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        label = arguments?.getParcelable<Label>("label")

        label?.let {
            labelNameET.setText(it.name)
        }
    }

    override fun onDetach() {
        editListener?.let { editListener ->
            label?.let {
                editListener.labelInfo(it)
            }
        }

        editListener = null

        super.onDetach()
    }

    interface EditListener {
        fun labelInfo(label: Label)
    }

    companion object {
        fun instance(label: Label, listener: EditListener? = null): BottomSheetDialogFragment {
            val bundle = Bundle()
            bundle.putParcelable("label", label)

            return LabelDialogFragment().apply {
                arguments = bundle
                editListener = listener
            }
        }
    }
}
