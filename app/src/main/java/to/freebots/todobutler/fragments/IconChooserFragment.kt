package to.freebots.todobutler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_icon.*
import to.freebots.iconhelper.IconHelper
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.IconAdapter

class IconChooserFragment : DialogFragment() {
    var selectionListener: SelectionListener? = null

    companion object {
        val icons: List<Int> = IconHelper.labelIcons()

        fun instance(listener: SelectionListener? = null): IconChooserFragment {
            return IconChooserFragment().apply {
                selectionListener = listener
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_icon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_icons.adapter = IconAdapter().apply {
            items = icons.toMutableList()
            action = object : IconAdapter.Action {
                override fun onOpen(icon: Int) {
                    selectionListener?.onIcon(icon.toString())

                    dismiss()
                }
            }
        }
    }

    override fun onDetach() {

        selectionListener = null
        super.onDetach()
    }

    interface SelectionListener {
        fun onIcon(icon: String)
    }
}
