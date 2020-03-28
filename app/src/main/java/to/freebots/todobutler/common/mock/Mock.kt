package to.freebots.todobutler.common.mock

import to.freebots.todobutler.models.database.Database
import to.freebots.todobutler.models.dto.AttachmentDAO
import to.freebots.todobutler.models.dto.LabelDAO
import to.freebots.todobutler.models.dto.TaskDAO
import to.freebots.todobutler.models.entities.Attachment
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.models.entities.Task

class Mock {

    companion object {

        fun applyToDatabase(database: Database) {
            val taskDAO = database.taskDAO()
            val labelDAO = database.labelDAO()
            val attachmentDAO = database.attachmentDAO()

            val basicLabels = createLabels(labelDAO)

            val labelId: Long = basicLabels[0].id!!
            // add mock data

            val parentTask = createTask(taskDAO, Task(labelId, null, "parent", "parent", false, false,null,""))

            val children = createSubTaskOf(
                taskDAO, parentTask, listOf(
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, ""),
                    Task(labelId, null, "child", "child", false,false, null, "")
                )
            )

            val parentId: Long = parentTask.id!!
            val attachmentOfParent = createAttachment(attachmentDAO, Attachment(parentId, "", "", ""))

            val attachmentOfChild =
                createAttachment(attachmentDAO, Attachment(children[0].id!!, "", "", ""))


            val childrenOfChildern = createSubTaskOf(
                taskDAO, children[0], listOf(
                    Task(basicLabels[1].id!!, null, "child", "child", false, false,null, ""),
                    Task(basicLabels[1].id!!, null, "child", "child", false, false, null,""),
                    Task(basicLabels[2].id!!, null, "child", "child", false, false, null, ""),
                    Task(basicLabels[2].id!!, null, "child", "child", false, false, null,"")
                )
            )
        }

        private fun createLabels(labelDAO: LabelDAO): List<Label> {
            val labels: MutableList<Label> = mutableListOf()

            for (i in 0..100) {
                labels.add(createLabel(labelDAO, Label("# $i", "")))
            }

            return labels
        }

        private fun createLabel(labelDAO: LabelDAO, label: Label): Label {
            return labelDAO.findByRowIndex(labelDAO.create(label))
        }

        private fun createTask(taskDAO: TaskDAO, task: Task): Task {
            return taskDAO.findByRowId(taskDAO.create(task))
        }

        private fun createAttachment(
            attachmentDAO: AttachmentDAO,
            attachment: Attachment
        ): Attachment {
            return attachmentDAO.findByRowIndex(attachmentDAO.create(attachment))
        }

        // parent to apply
        // children -> to create
        private fun createSubTaskOf(
            taskDAO: TaskDAO,
            parent: Task,
            children: List<Task>
        ): List<Task> {
            return children.map { task ->
                createTask(
                    taskDAO,
                    task.apply { parentTaskId = parent.id })
            }
        }


        val listOfLabels = mutableListOf(
            Label("Todo",""),
            Label("Todo",""),
            Label("Todo",""),
            Label("Todo",""),
            Label("Todo","")
        )


        val listOfFlatTaskDTO = mutableListOf(
            FlatTaskDTO(
                Label("label name",""),
                null,
                "name",
                "desc",
                false,
                false,
                null,
                "",
                mutableListOf(),
                mutableListOf(),
                id = 10
            ),
            FlatTaskDTO(
                Label("label name", ""),
                null,
                "name",
                "desc",
                false,
                false,
                null,
                "",
                mutableListOf(),
                mutableListOf(),
                id = 11
            ),
            FlatTaskDTO(
                Label("label name", ""),
                null,
                "name",
                "desc",
                false,
                false,
                null,
                "",
                mutableListOf(),
                mutableListOf(),
                id = 12
            ),
            FlatTaskDTO(
                Label("label name", ""),
                null,
                "name",
                "desc",
                false,
                false,
                null,
                "",
                mutableListOf(
                    FlatTaskDTO(
                        Label("label name", ""),
                        13,
                        "name",
                        "desc",
                        false,
                        false,
                        null,
                        "",
                        mutableListOf(),
                        mutableListOf(),
                        id = 14
                    )
                ),
                mutableListOf(),
                id = 13
            ),
            FlatTaskDTO(
                Label("label name", ""),
                null,
                "name",
                "desc",
                false,
                false,
                null,
                "",
                mutableListOf(
                    FlatTaskDTO(
                        Label("label name", ""),
                        15,
                        "name",
                        "desc",
                        false,
                        false,
                        null,
                        "",
                        mutableListOf(
                            FlatTaskDTO(
                                Label("label name", ""),
                                16,
                                "name",
                                "desc",
                                false,
                                false,
                                null,
                                "",
                                mutableListOf(
                                    FlatTaskDTO(
                                        Label("label name", ""),
                                        17,
                                        "name",
                                        "desc",
                                        false,
                                        false,
                                        null,
                                        "",
                                        mutableListOf(),
                                        mutableListOf(),
                                        id = 18
                                    )
                                ),
                                mutableListOf(),
                                id = 17
                            )
                        ),
                        mutableListOf(),
                        id = 16
                    )
                ),
                mutableListOf(),
                id = 15
            )
        )

        var flatTaskDTOWithSubTask = FlatTaskDTO(
            Label("label name", ""),
            null,
            "name",
            "desc",
            false,
            false,
            null,
            "",
            listOfFlatTaskDTO.map { flatTaskDTO ->
                flatTaskDTO.parentTaskId = 19
                return@map flatTaskDTO
            }.toMutableList(),
            mutableListOf(),
            id = 19
        )
    }
}