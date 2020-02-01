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


        Database.getDatabase(this).let { database ->
                        val labelId: Long = database.labelDAO().create(Label(""))

            database.taskDAO().let { taskDAO ->
                val parentId: Long = taskDAO.create(Task(labelId, null, "parent", "parent", false))
                database.attachmentDAO().create(Attachment(parentId, "", ""))
                taskDAO.create(Task(labelId, parentId, "child", "child", false))
                taskDAO.create(Task(labelId, parentId, "child", "child", false))
                taskDAO.create(Task(labelId, parentId, "child", "child", false))
                taskDAO.create(Task(labelId, parentId, "child", "child", false))
                taskDAO.create(Task(labelId, parentId, "child", "child", false))
                taskDAO.create(Task(labelId, parentId, "child", "child", false))
                taskDAO.create(Task(labelId, parentId, "child", "child", false))
                val childId: Long = taskDAO.create(Task(labelId, parentId, "child", "child", false))
                database.attachmentDAO().create(Attachment(childId, "", ""))
                taskDAO.create(Task(labelId, childId, "child", "child", false))
                taskDAO.create(Task(labelId, childId, "child", "child", false))
            }

            database.taskDAO().findAllDTO().forEach { taskDTO: TaskDTO ->
                println(taskDTO.toString())
            }
            println(database.taskDAO().findDTOById(10).task.id.toString())
            println(database.taskDAO().findAllDTO().size)

            database.taskDAO().findAllFlatTaskDTO().forEach {
                println(it.toString())
            }
        }
    }
}
