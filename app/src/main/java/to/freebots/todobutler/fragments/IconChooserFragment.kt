package to.freebots.todobutler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_icon.*
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.IconAdapter

class IconChooserFragment : DialogFragment() {
    var selectionListener: SelectionListener? = null

    companion object {
        // todo add icons
        val icons: List<Int> = listOf(
            R.drawable.ic_add_24px,
            R.drawable.ic_attach_file_black_18dp,
            R.drawable.ic_color_lens_black_18dp,
            R.drawable.ic_delete_outline_black_18dp,
            R.drawable.ic_file_copy_black_18dp,
            R.drawable.ic_gps_fixed_black_18dp,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_list_add_black_18dp,
            R.drawable.ic_location_on_black_18dp,
            R.drawable.ic_notifications_none_black_18dp,
            R.drawable.ic_notifications_off_black_18dp
        )


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
            action = object : IconAdapter.Action{
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
