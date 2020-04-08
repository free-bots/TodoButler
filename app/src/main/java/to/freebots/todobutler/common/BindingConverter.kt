package to.freebots.todobutler.common

import androidx.databinding.InverseMethod
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
    }
}