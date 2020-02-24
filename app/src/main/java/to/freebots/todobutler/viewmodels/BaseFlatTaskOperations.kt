package to.freebots.todobutler.viewmodels

import to.freebots.todobutler.models.entities.FlatTaskDTO

interface BaseFlatTaskOperations {
    fun fetchAll_DTO()
    fun create(e: FlatTaskDTO)
    fun update(e: FlatTaskDTO)
    fun delete(e: FlatTaskDTO)
}