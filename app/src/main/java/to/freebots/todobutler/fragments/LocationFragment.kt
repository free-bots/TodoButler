package to.freebots.todobutler.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationParams
import io.nlopez.smartlocation.location.providers.LocationManagerProvider
import kotlinx.android.synthetic.main.fragment_location.*
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MinimapOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import to.freebots.todobutler.R
import to.freebots.todobutler.viewmodels.TaskViewModel


/**
 * A simple [Fragment] subclass.
 */
class LocationFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
        ).get(TaskViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.getLong("flatTaskDTO")?.let{
            viewModel.getUpdated(it)
        }

        Configuration.getInstance().load(context, activity?.getPreferences(Context.MODE_PRIVATE))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_location, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                viewModel.deleteLocation()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()

        addLocationListener()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun initMap() {
        map.overlays.clear()

        map.setTileSource(TileSourceFactory.MAPNIK)
//        map.setBuiltInZoomControls(true) // not visible with the action button
        map.setMultiTouchControls(true)

        val mapController = map.controller
        mapController.setZoom(9.5)
        val startPoint = GeoPoint(48.8583, 2.2944)
        mapController.setCenter(startPoint)


        // compass
        val complass = CompassOverlay(context, InternalCompassOrientationProvider(context), map)
        complass.enableCompass()
        map.overlays.add(complass)


        // rotation
        val rotation = RotationGestureOverlay(context, map)
            .apply {
                isEnabled = true
            }
        map.overlays.add(rotation)


        // minimap
        val minimap = MinimapOverlay(context, map.tileRequestCompleteHandler)
            .apply {
                width = map.width / 5
                height = map.height / 5
            }
        map.overlays.add(minimap)

        initCenterOverlay(map)

        initTaskLocationOverlay(map)
    }

    private fun initCenterOverlay(map: MapView) {
        val centerMarker = Marker(map).apply { position = GeoPoint(map.mapCenter) }

        map.overlays.add(centerMarker)

        map.addMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                event?.source?.let {
                    centerMarker.position = GeoPoint(it.mapCenter)
                }
                return true
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                return true
            }
        })
    }

    private fun initTaskLocationOverlay(map: MapView) {
        val taskMarker = Marker(map).apply { position = GeoPoint(map.mapCenter) }

        map.overlays.add(taskMarker)

        val controller = map.controller

        viewModel.location.observe(viewLifecycleOwner) {
            it?.let { location ->
                taskMarker.setVisible(true)
                val point = GeoPoint(location.latitude, location.longitude)
                taskMarker.position = point
                controller.setCenter(point)
            } ?: run {
                taskMarker.setVisible(false)
            }
        }
    }

    private fun addLocationListener() {
        currentLocation.setOnClickListener {
            SmartLocation.with(context)
                .location(LocationManagerProvider())
                .config(LocationParams.NAVIGATION)
                .oneFix()
                .start {
                    viewModel.updateLocation(it.latitude, it.longitude)
                    println("location update")
                    println(it.toString())
                }
        }

        addLocation.setOnClickListener {
            val location = map.mapCenter
            viewModel.updateLocation(location.latitude, location.longitude)
        }
    }
}
