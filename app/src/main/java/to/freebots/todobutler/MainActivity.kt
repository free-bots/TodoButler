package to.freebots.todobutler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import to.freebots.todobutler.models.logic.StorageService

class MainActivity : AppCompatActivity() {

    companion object {
        const val INTENT_TASK_EXTRA = "INTENT_TASK_EXTRA"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

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

        findNavController(R.id.nav_host_fragment).navigate(R.id.taskFragment, Bundle().apply {
            putLong("flatTaskDTO", taskId)
        })
    }
}
