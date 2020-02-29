package to.freebots.todobutler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import to.freebots.todobutler.models.database.Database
import to.freebots.todobutler.models.entities.Attachment
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.entities.TaskDTO

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
