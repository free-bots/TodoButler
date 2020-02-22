package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.withContext
import to.freebots.todobutler.common.mock.Mock
import to.freebots.todobutler.models.entities.Label

class LabelViewModel(application: Application) : AndroidViewModel(application) {

    val labels: MutableLiveData<MutableList<Label>> = MutableLiveData()

    var _labels: MutableList<Label> = mutableListOf()

    init {
        fetchAll()
    }

    fun fetchAll() {
        _labels = Mock.listOfLabels
        labels.postValue(Mock.listOfLabels)
    }

    fun update(label: Label) {
        val index = _labels.indexOfFirst { l -> l.id == label.id }
        if (index > -1) {
            // update existing
            _labels[index] = label
        } else {
            // create new
            _labels.add(label)
        }
        labels.postValue(_labels)
    }
}