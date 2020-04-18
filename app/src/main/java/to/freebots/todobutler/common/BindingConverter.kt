package to.freebots.todobutler.common

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import com.google.android.material.textfield.TextInputEditText
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import to.freebots.iconhelper.IconHelper
import to.freebots.todobutler.R
import to.freebots.todobutler.models.entities.Priority

class BindingConverter {
    companion object {
        @InverseMethod("idToPriority")
        @JvmStatic
        fun priorityToId(
            value: Priority?
        ): Int? {
            return when (value) {
                Priority.LOW -> R.id.rb_low
                Priority.MID -> R.id.rb_mid
                Priority.HIGH -> R.id.rb_high
                else -> null
            }
        }

        @JvmStatic
        fun idToPriority(
            value: Int
        ): Priority {
            return when (value) {
                R.id.rb_low -> Priority.LOW
                R.id.rb_mid -> Priority.MID
                R.id.rb_high -> Priority.HIGH
                else -> Priority.LOW
            }
        }



        @BindingAdapter("strikethrough")
        @JvmStatic
        fun strikethrough(view: TextInputEditText, show: Boolean) {
            view.paintFlags = if (show) {
                view.paintFlags or STRIKE_THRU_TEXT_FLAG
            } else {
                view.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

//        @BindingAdapter("strikethroughTextView")
//        @JvmStatic
//        fun strikethroughTextView(view: TextView, show: Boolean) {
//            view.paintFlags = if (show) {
//                view.paintFlags or STRIKE_THRU_TEXT_FLAG
//            } else {
//                view.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
//            }
//        }


        fun showEmptyIcon(view: ImageView?, list: MutableList<*>?, textView: TextView?, text: String) {
            if (list == null || list.isEmpty()) {
                view?.setImageResource(IconHelper.randomEmptyIcon())
                view?.visibility = View.VISIBLE
                textView?.text = text
                textView?.visibility = View.VISIBLE
                view?.fadeIn(500)
                textView?.fadeIn(500)
            } else {
                view?.fadeOut(500)
                textView?.fadeOut(500)
                view?.visibility = View.GONE
                textView?.visibility = View.GONE
            }
        }
    }
}