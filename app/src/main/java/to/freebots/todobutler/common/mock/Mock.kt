package to.freebots.todobutler.common.mock

import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label

class Mock {

    companion object {

        val listOfLabels = mutableListOf(
            Label("Todo"),
            Label("Todo"),
            Label("Todo"),
            Label("Todo"),
            Label("Todo"),
            Label("Todo"),
            Label("Todo")
        )


        val listOfFlatTaskDTO = mutableListOf(
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(),
                mutableListOf()
            ),
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(),
                mutableListOf()
            ),
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(),
                mutableListOf()
            ),
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(
                    FlatTaskDTO(
                        Label("label name"),
                        null,
                        "name",
                        "desc",
                        false,
                        mutableListOf(),
                        mutableListOf()
                    )
                ),
                mutableListOf()
            ),
            FlatTaskDTO(
                Label("label name"),
                null,
                "name",
                "desc",
                false,
                mutableListOf(
                    FlatTaskDTO(
                        Label("label name"),
                        null,
                        "name",
                        "desc",
                        false,
                        mutableListOf(
                            FlatTaskDTO(
                                Label("label name"),
                                null,
                                "name",
                                "desc",
                                false,
                                mutableListOf(
                                    FlatTaskDTO(
                                        Label("label name"),
                                        null,
                                        "name",
                                        "desc",
                                        false,
                                        mutableListOf(),
                                        mutableListOf()
                                    )
                                ),
                                mutableListOf()
                            )
                        ),
                        mutableListOf()
                    )
                ),
                mutableListOf()
            )
        )

        var flatTaskDTOWithSubTask = FlatTaskDTO(
            Label("label name"),
            null,
            "name",
            "desc",
            false,
            listOfFlatTaskDTO,
            mutableListOf()
        )
    }
}