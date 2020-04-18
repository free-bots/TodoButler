package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import to.freebots.todobutler.common.entities.BaseEntity
import to.freebots.todobutler.models.entities.Label

class LabelViewModel(application: Application) : BaseViewModel(application), BaseOperations<Label> {

    val labels: LiveData<MutableList<Label>>

    init {
        labels = labelService.findAllLiveData()
    }

    override fun fetchAll() {
//        labels.postValue(Mock.listOfLabels)
    }

    override fun create(e: Label) {
        subscribe(labelService.createRx(e).subscribe())
    }

    override fun update(e: Label) {
        subscribe(labelService.updateRx(e).subscribe())
    }

    override fun delete(e: Label) {
        subscribe(labelService.deleteRx(e).subscribe())
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