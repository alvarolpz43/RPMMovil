package com.rpm.rpmmovil.Routes


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rpm.rpmmovil.ExplorarRutas.model.rutas.RutasResponses
import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.R
import com.rpm.rpmmovil.Routes.apiRoute.ApiService
import com.rpm.rpmmovil.Usermotos.UserMotosActivity
import com.rpm.rpmmovil.databinding.ActivityMapBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@Suppress("DEPRECATION")
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap

    private lateinit var btnCalculate: Button

    private lateinit var pInicioEditText: EditText
    private lateinit var pFinalEditText: EditText

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    fun distanceInKm(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Double {
        val radioDeLaTierra = 6371.0 // Radio de la Tierra en kilómetros

        val startLatRad = Math.toRadians(startLat)
        val startLngRad = Math.toRadians(startLng)
        val endLatRad = Math.toRadians(endLat)
        val endLngRad = Math.toRadians(endLng)

        val deltaLat = endLatRad - startLatRad
        val deltaLng = endLngRad - startLngRad

        val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(startLatRad) * Math.cos(endLatRad) * Math.sin(deltaLng / 2) * Math.sin(
                deltaLng / 2
        )
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return radioDeLaTierra * c
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 0
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id: String = intent.getStringExtra(EXTRA_ID).orEmpty()
        getCordinatesRoute(id)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        btnCalculate = binding.btnCalculateRoute

        binding.iniciar.visibility = View.GONE
        pInicioEditText = binding.pInicio
        pFinalEditText = binding.pFinal

        btnCalculate.setOnClickListener {
            val location1 = pInicioEditText.text.toString()
            val location2 = pFinalEditText.text.toString()

            if (location1.isNotBlank() && location2.isNotBlank()) {
                val geocoder = Geocoder(this@MapActivity)

                try {
                    val addressList1 = geocoder.getFromLocationName(location1, 1)
                    val addressList2 = geocoder.getFromLocationName(location2, 1)

                    if (!addressList1.isNullOrEmpty() && !addressList2.isNullOrEmpty()) {
                        val startAddress: Address = addressList1[0]
                        val endAddress: Address = addressList2[0]
                        val startLatLng = LatLng(startAddress.latitude, startAddress.longitude)
                        val endLatLng = LatLng(endAddress.latitude, endAddress.longitude)

                        createRoute(startLatLng, endLatLng)
                        Toast.makeText(this, "Calculando Ruta", Toast.LENGTH_SHORT).show()
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                        val distanceKm = distanceInKm(
                                startLatLng.latitude,
                                startLatLng.longitude,
                                endLatLng.latitude,
                                endLatLng.longitude
                        )
                        val distanceKmRounded = "%.2f".format(distanceKm)
                        binding.km.text = "${distanceKmRounded} Km"
                        Constains.DISTANCIA_RUTA = distanceKmRounded.toDouble()

               binding.btnsiguiente.setOnClickListener{
                   funcionBtnSiguiente(distanceKmRounded)
               }

                    } else {
                        Toast.makeText(
                                this,
                                "No se encontraron direcciones para los puntos ingresados",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(
                        this,
                        "Por favor, ingrese los puntos de inicio y final",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }

        val bottomSheet = findViewById<FrameLayout>(R.id.desp)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet).apply {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenHeight = displayMetrics.heightPixels
            peekHeight = (screenHeight * 0.1).toInt()
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        var isBottomSheetExpanded = false
        val button = binding.icon
        button.setOnClickListener {
            if (isBottomSheetExpanded) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            isBottomSheetExpanded = !isBottomSheetExpanded
        }

        binding.btnDesp.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun getCordinatesRoute(id: String) {
        lifecycleScope.launch {
            try {
                Log.e("TAG", "${id}", )
                val result = getRetrofit2().getCordinateRoutes(id)
                createUIRoute(result)
            } catch (e: Exception) {
                Log.e("TAG", "${e}", )
            }
        }
    }

    private fun createUIRoute(ruta: RutasResponses) {
        val puntoIniRuta = ruta.ruta.puntoiniruta.split(",")
        val puntoFinalRuta = ruta.ruta.puntofinalruta.split(",")

        if (puntoIniRuta.size != 2 || puntoFinalRuta.size != 2) {
            return
        }

        val startPoint = LatLng(puntoIniRuta[0].toDouble(), puntoIniRuta[1].toDouble())
        val endPoint = LatLng(puntoFinalRuta[0].toDouble(), puntoFinalRuta[1].toDouble())

        createRoute(startPoint, endPoint)

        binding.km.text = ruta.ruta.kmstotruta.toString()
        binding.desp.visibility = View.GONE
        binding.btnSave.visibility = View.GONE
        binding.btnDesp.visibility = View.GONE
        binding.iniciar.visibility = View.VISIBLE
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocation()
    }

    private fun createRoute(startLatLng: LatLng, endLatLng: LatLng) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java)
                    .getRoute(
                            "5b3ce3597851110001cf6248c50b947f1222418498fae123bb1a6114",
                            "${startLatLng.longitude},${startLatLng.latitude}",
                            "${endLatLng.longitude},${endLatLng.latitude}"
                    )

            if (call.isSuccessful) {
                drawRoute(call.body(), startLatLng, endLatLng)
            } else {
                Log.i("aris", "Error al obtener la ruta")
            }
        }
    }

    private fun drawRoute(routeResponse: RouteResponse?, startLatLng: LatLng, endLatLng: LatLng) {
        routeResponse?.features?.firstOrNull()?.geometry?.coordinates?.let { coordinates ->
            val polyLineOptions = PolylineOptions().apply {
                width(10f)
                color(Color.parseColor("#1486cc"))
                coordinates.forEach { coordinate ->
                    add(LatLng(coordinate[1], coordinate[0]))
                }
            }

            runOnUiThread {
                map?.addPolyline(polyLineOptions)
            }

            val startMarkerOptions = MarkerOptions().position(startLatLng).title("Inicio")
            val endMarkerOptions = MarkerOptions().position(endLatLng).title("Fin")

            runOnUiThread {
                map?.addMarker(startMarkerOptions)
                map?.addMarker(endMarkerOptions)

                val builder = LatLngBounds.Builder()
                builder.include(startLatLng)
                builder.include(endLatLng)
                val bounds = builder.build()

                val padding = 100
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                map?.animateCamera(cameraUpdate)

                binding.btnSave.visibility = View.VISIBLE
                binding.km.visibility = View.VISIBLE

                binding.btnSave.setOnClickListener() {
                    val intent = Intent(this, saveRutasActivity::class.java)
                    intent.putExtra("cordenadasInicio", startLatLng.toString())
                    intent.putExtra("cordenadasFinal", endLatLng.toString())
                    startActivity(intent)
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getRetrofit2(): ApiService {
        return Retrofit.Builder()
                .baseUrl("https://rpm-back-end.vercel.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
    }

    private fun isLocationPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionsGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingSuperCall", "MissingPermission")
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun funcionBtnSiguiente(distanceKmRounded: String) {
        val distanceKmRoundedInt = distanceKmRounded.toDouble().toInt()
        val intent = Intent(this, UserMotosActivity::class.java)
        intent.putExtra("distanceKm", distanceKmRoundedInt)
        Toast.makeText(this, "Este es el valor que se está enviando: $distanceKmRoundedInt", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

}

