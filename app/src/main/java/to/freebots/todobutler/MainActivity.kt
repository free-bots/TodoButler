package to.freebots.todobutler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import to.freebots.todobutler.models.logic.StorageService

class MainActivity : AppCompatActivity() {

    companion object {
        const val INTENT_TASK_EXTRA = "INTENT_TASK_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskId = intent.getLongExtra(INTENT_TASK_EXTRA, -1)
        navigateToTask(taskId)

        // clean up
        StorageService(application).nuke()

    }


    private fun navigateToTask(taskId: Long) {
        if (taskId < 0) {
            return
        }

        val bundle = Bundle().apply {
            putParcelable("flatTaskDTO", null)
        }

        println("MAINACTIVITY $taskId")

        // todo navigate to task
    }
}
