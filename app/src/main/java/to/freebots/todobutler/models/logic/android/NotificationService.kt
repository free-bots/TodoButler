package to.freebots.todobutler.models.logic.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import to.freebots.todobutler.MainActivity
import to.freebots.todobutler.R
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.logic.*

class NotificationService : JobIntentService() {

    val CHANNEL_NAME by lazy { application.getString(R.string.channel_name) }
    val CHANNEL_DESCRIPTION by lazy { application.getString(R.string.channel_description) }

    companion object {
        const val CHANNEL_ID = "ReminderChannel"
        const val INTENT_TYPE_EXTRA = "INTENT_TYPE_EXTRA"
        const val INTENT_TASK_ID_EXTRA = "INTENT_TASK_ID_EXTRA"
        private const val JOB_ID = 9999

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, NotificationService::class.java, JOB_ID, work)
        }

        fun cancelNotification(context: Context, type: NotificationType, task: FlatTaskDTO) {
            NotificationManagerCompat.from(context).cancel(notificationId(task, type))
        }

        private fun notificationId(task: FlatTaskDTO, type: NotificationType): Int {
            return when (type) {
                NotificationType.PINNED -> {
                    val id = task.id!!
                    (id.toString().reversed() + id.toString()).toInt() * -1
                }
                NotificationType.REMINDER -> {
                    val id = task.id!!
                    (id.toString().reversed() + id.toString()).toInt()
                }
            }
        }
    }

    private lateinit var flatTaskService: FlatTaskService

    override fun onCreate() {
        super.onCreate()
        flatTaskService = FlatTaskService(
            application,
            TaskService(application, LabelService(application)),
            AttachmentService(application, StorageService((application))),
            LocationService(application),
            ReminderService(application)
        )
    }

    override fun onHandleWork(intent: Intent) {
        println("SERVICE")
        val typeExtra = intent.getStringExtra(INTENT_TYPE_EXTRA) ?: return
        val taskId = intent.getLongExtra(INTENT_TASK_ID_EXTRA, -1)

        if (taskId < 0) {
            return
        }

        val task: FlatTaskDTO

        try {
            task = flatTaskService.findById(taskId)
            when (val type = typeToNotificationType(typeExtra)) {
                NotificationType.PINNED, NotificationType.REMINDER -> {
                    val builder = notificationBuilder(task)
                    createNotificationChannel()
                    showNotification(builder, task, type)
                }
            }
        } catch (e: Exception) {

        }
    }


    private fun notificationBuilder(task: FlatTaskDTO): NotificationCompat.Builder {
        val icon = task.label.icon
        val resource = if (icon.isEmpty()) {
            R.drawable.ic_launcher_foreground
        } else {
            icon.toInt()
        }
        return NotificationCompat.Builder(application, CHANNEL_ID)
            .setSmallIcon(resource)
            .setContentTitle(task.name)
            .setContentText(task.description)
            .setContentIntent(tapAction(task))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(
        builder: NotificationCompat.Builder,
        task: FlatTaskDTO,
        type: NotificationType
    ) {
        NotificationManagerCompat.from(applicationContext)
            .notify(notificationId(task, type), builder.build())
    }

    private fun tapAction(task: FlatTaskDTO): PendingIntent {

        val intent = Intent(application, MainActivity::class.java).putExtra(
            MainActivity.INTENT_TASK_EXTRA,
            task.id
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        println(intent.extras.toString())

        return PendingIntent.getActivity(
            application,
            requestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun requestCode(): Int {
        return Math.random().toInt()
    }


    private fun typeToNotificationType(value: String): NotificationType {
        return NotificationType.valueOf(value)
    }

    enum class NotificationType(value: String) {
        REMINDER("REMINDER"),
        PINNED("PINNED")
    }
}
