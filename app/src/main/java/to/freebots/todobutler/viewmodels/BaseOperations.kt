package to.freebots.todobutler.viewmodels

import to.freebots.todobutler.common.entities.BaseEntity

interface BaseOperations <E: BaseEntity> {
    fun fetchAll()
    fun create(e: E)
    fun update(e: E)
    fun delete(e: E)
}