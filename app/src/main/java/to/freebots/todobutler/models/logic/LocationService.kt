package to.freebots.todobutler.models.logic

import android.app.Application
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Location

class LocationService(application: Application) : BaseLogicService<Location>(application) {
    override fun findAll(): MutableList<Location> {
        return locationDAO.findAll()
    }

    override fun findById(id: Long): Location {
        return locationDAO.findLocationById(id)
    }

    override fun create(e: Location): Location {
        return locationDAO.createLocation(e)
    }

    override fun update(e: Location): Location {
        return locationDAO.updateLocation(e)
    }

    override fun delete(e: Location): Location {
        locationDAO.delete(e)
        return e
    }

    fun createCopy(e: Location?): Location? {
        return if (e == null) {
            null
        } else {
            locationDAO.createCopy(e)
        }
    }
}
