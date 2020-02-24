package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import to.freebots.todobutler.common.mock.Mock
import to.freebots.todobutler.models.entities.Label

class LabelViewModel(application: Application) : BaseViewModel(application), BaseOperations<Label> {

    val labels: MutableLiveData<MutableList<Label>> = MutableLiveData()

    private var _labels: MutableList<Label> = mutableListOf()

    init {
        fetchAll()
    }

    override fun fetchAll() {
        GlobalScope.runCatching {
            labelDao.findAll()
        }
        _labels = Mock.listOfLabels
        labels.postValue(Mock.listOfLabels)
    }

    override fun create(e: Label) {
        _labels.add(e)
        labels.postValue(_labels)
    }

    override fun update(e: Label) {
        val index = findIndexOfLabel(e);
        if (index > -1) {
            // update existing
            _labels[index] = e
            labels.postValue(_labels)
        }
    }

    override fun delete(e: Label) {
        _labels = _labels.filter { l -> l.id != e.id }.toMutableList()
        labels.postValue(_labels)
    }

    private fun findIndexOfLabel(label: Label): Int {
        return _labels.indexOfFirst { l -> l.id == label.id }
    }

    fun newLabelValues(label: Label) {
        val index = findIndexOfLabel(label);
        if (index > -1) {
            update(label)
        } else {
            create(label)
        }
    }
}