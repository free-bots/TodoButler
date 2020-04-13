package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import to.freebots.todobutler.common.entities.BaseEntity
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.models.logic.LabelService

class LabelViewModel(application: Application) : BaseViewModel(application), BaseOperations<Label> {

    private val labelService: LabelService by lazy {
        LabelService(application)
    }

    val labels: LiveData<MutableList<Label>>

    init {
        labels = labelService.findAllLiveData()
    }

    override fun fetchAll() {
//        labels.postValue(Mock.listOfLabels)
    }

    override fun create(e: Label) {
        labelService.createAsync(e)
    }

    override fun update(e: Label) {
        labelService.updateAsync(e)
    }

    override fun delete(e: Label) {
        labelService.deleteAsync(e)
    }

    fun newLabelValues(label: Label) {
        labels.value?.let {
            val index = findIndex(label, it as MutableList<BaseEntity>)
            if (index > -1) {
                update(label)
            } else {
                create(label)
            }
        }
    }
}