package com.rpm.rpmmovil.Routes


import android.Manifest
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition

import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rpm.rpmmovil.ExplorarRutas.model.rutas.RutasResponses
import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.R
import com.rpm.rpmmovil.Routes.RecyclerPlaces.PlaceViewHolder
import com.rpm.rpmmovil.Routes.RecyclerPlaces.PlacesAdapter
import com.rpm.rpmmovil.Routes.RecyclerPlaces.PlacesAdapter2

import com.rpm.rpmmovil.Routes.apiRoute.ApiService
import com.rpm.rpmmovil.Usermotos.UserMotosActivity
import com.rpm.rpmmovil.databinding.ActivityMapBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@Suppress("DEPRECATION")
class MapActivity : AppCompatActivity(), OnMapReadyCallback, OnMyLocationButtonClickListener {

    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap

    //location
    private val locationService: LocationService = LocationService()



    //lugares calve
    private lateinit var placesClient: PlacesClient

    private lateinit var btnCalculate: Button

    private lateinit var pInicioEditText: EditText
    private lateinit var pFinalEditText: EditText

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    fun distanceInKm(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Double {
        val radioDeLaTierra = 6371.0 // Radio de la Tierra en km

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

        // Inicializar Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.GoogleKey))
        }
        placesClient = Places.createClient(this)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)





        btnCalculate = binding.btnCalculateRoute

        binding.iniciar.visibility = View.GONE






        btnCalculate.setOnClickListener {
            val pInicio = binding.pInicio.text.toString()
            val pFinal = binding.pFinal.text.toString()

            if (pInicio.isNotBlank() && pFinal.isNotBlank()) {
                val geocoder = Geocoder(this@MapActivity)


                try {
                    val addressList1 = geocoder.getFromLocationName(pInicio, 3)
                    val addressList2 = geocoder.getFromLocationName(pFinal, 3)

                    if (!addressList1.isNullOrEmpty() && !addressList2.isNullOrEmpty()) {

                        if (addressList1.isNotEmpty()) {
                            println("Resultados para $pInicio:")
                            for (i in addressList1.indices) {
                                val address = addressList1[i]
                                println("${i + 1}. ${address.getAddressLine(0)}") // Imprimir la dirección
                            }
                        } else {
                            println("No se encontraron resultados para $pInicio")
                        }
                        Log.i("Lugares", "${addressList1}")
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

                        binding.btnsiguiente.setOnClickListener {
                            funcionBtnSiguiente(distanceKmRounded)
                        }
                    } else {
                        // Si no se encontraron direcciones exactas, intenta buscar lugares relacionados
                        val relatedPlaces = geocoder.getFromLocationName(
                            pInicio,
                            5
                        ) // Obtener hasta 5 lugares relacionados
                        if (relatedPlaces.isNullOrEmpty()) {
                            Toast.makeText(
                                this,
                                "No se encontraron direcciones para el punto de inicio, intenta ingresar un nombre más específico",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Imprimir la lista de lugares relacionados
                            for (place in relatedPlaces) {
                                Log.i(
                                    "RelatedPlace",
                                    "${place.featureName}, ${place.adminArea}, ${place.countryName}"
                                )
                            }
                        }
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
            peekHeight = (screenHeight * 0.0).toInt()
            state = BottomSheetBehavior.STATE_COLLAPSED
        }




        binding.btnCancel.setOnClickListener {
            val newState = if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                BottomSheetBehavior.STATE_COLLAPSED
            } else {
                BottomSheetBehavior.STATE_EXPANDED
            }
            bottomSheetBehavior.state = newState
        }




        binding.search.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED


        }




        val adapter = PlacesAdapter(emptyList())
        val adapter2 = PlacesAdapter2(emptyList())
        binding.recyclerLugares.adapter = adapter



        binding.pInicio.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                buscarLugaresSimilares(query) { lugares ->
                    // Actualizar la lista de lugares en el adaptador
                    adapter.updatePlacesList(lugares)
                    binding.recyclerLugares.layoutManager = LinearLayoutManager(this@MapActivity)
                    binding.recyclerLugares.adapter = adapter


                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se usa
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se usa
            }
        })



        binding.pFinal.addTextChangedListener(object : TextWatcher {

            //Cuando cambia
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                buscarLugaresSimilares(query) { lugares ->
                    // Actualizar la lista de lugares en el adaptador
                    adapter2.updatePlacesList(lugares)
                    binding.recyclerLugares.layoutManager = LinearLayoutManager(this@MapActivity)
                    binding.recyclerLugares.adapter = adapter2


                    println(lugares)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se usa por q? nose
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se usa
            }
        })

        binding.btnLimpiar.setOnClickListener{
            binding.pInicio.text=null
            binding.pFinal.text=null


        }

        binding.myLocation.setOnClickListener {
            lifecycleScope.launch {
                val location = locationService.getUserLocation(this@MapActivity)
                withContext(Dispatchers.Main) {
                    if (location != null) {
                        val latitude = location.latitude.toString()
                        val longitude = location.longitude.toString()
                        val locationString = "$latitude, $longitude"


                        binding.pInicio.setText(locationString)


                        binding.pInicio.selectAll()
                    }
                    else{
                        Toast.makeText(this@MapActivity, " Tienes Activada la ubi?? Parece que no", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }



    }

    private fun getCordinatesRoute(id: String) {
        lifecycleScope.launch {
            try {
                Log.e("TAG", "${id}")
                val result = getRetrofit2().getCordinateRoutes(id)
                createUIRoute(result)
            } catch (e: Exception) {
                Log.e("TAG", "${e}")
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

        binding.iniciar.visibility = View.VISIBLE

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationClickListener { this }
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

                val padding = 110
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                map?.animateCamera(cameraUpdate)

                binding.btnSave.visibility = View.VISIBLE
                binding.km.visibility = View.VISIBLE
                binding.btnsiguiente.visibility = View.VISIBLE

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


    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionsGranted()) {
            //Si Esta en Rojo es Asi tranquilo Es Parte de Plan
            map.isMyLocationEnabled = true

            lifecycleScope.launch {
                //obtengo lo location del usuario
                val result = locationService.getUserLocation(this@MapActivity)

                if (result != null) {


                    val myLocation = LatLng(
                        result.latitude,
                        result.longitude
                    ) // estas no las cambies es la ubicacion en tiempo real

                    val cameraPosition = CameraPosition.Builder()
                        .target(myLocation)
                        .zoom(14F)
                        .build()

                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                }

            }


        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                this,
                "Ve a ajustes y Acepta los Permisos de Localizacion",
                Toast.LENGTH_SHORT
            ).show()


        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }


    //Location permisos

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Si Esta en Rojo es Asi tranquilo Es Parte de Plan
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "Para activar la localización ve a ajustes y acepta los permisos",

                    Toast.LENGTH_SHORT
                ).show()


            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!isLocationPermissionsGranted()) {
            if (!::map.isInitialized) return
            //Si Esta en Rojo es Asi tranquilo Es Parte de Plan
            map.isMyLocationEnabled = false
            Toast.makeText(
                this,
                "Para activar la localización ve a ajustes y acepta los permisos",

                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return true
    }


    private fun funcionBtnSiguiente(distanceKmRounded: String) {
        val distanceKmRoundedInt = distanceKmRounded.toDouble().toInt()
        val intent = Intent(this, UserMotosActivity::class.java)
        intent.putExtra("distanceKm", distanceKmRoundedInt)
        Toast.makeText(
            this,
            "Este es el valortirris que se está enviando: $distanceKmRoundedInt",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(intent)
    }




    private fun buscarLugaresSimilares(query: String, callback: (List<String>) -> Unit) {
        val lugaresEncontrados = mutableListOf<String>()

        // Verificar si la consulta no está vacía
        if (query.isNotEmpty()) {
            // Establecer límites de búsqueda para América Latina
            val bounds = RectangularBounds.newInstance(
                LatLng(-56.0, -180.0), // Extremo sur y oeste de América Latina
                LatLng(33.0, -34.0)    // Extremo norte y este de América Latina
            )

            // Crear la solicitud de búsqueda
            val request = FindAutocompletePredictionsRequest.builder()
                .setCountry("CO") // Establecer país (opcional)
                .setLocationBias(bounds) // Establecer límites de búsqueda
                .setQuery(query) // Establecer la consulta de búsqueda
                .build()

            // Realizar la solicitud de búsqueda
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    for (prediction: AutocompletePrediction in response.autocompletePredictions) {
                        val lugar = prediction.getFullText(null)
                        lugaresEncontrados.add(lugar.toString())
                    }

                    // Llamar al callback con la lista de lugares encontrados
                    callback(lugaresEncontrados)
                }
                .addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        // Manejar errores de la API
                        Log.e("MapActivity", "Error al buscar lugares: ${exception.statusCode}")
                    }

                    // Llamar al callback con una lista vacía en caso de fallo
                    callback(emptyList())
                }
        } else {
            // Llamar al callback con una lista vacía si la consulta está vacía
            callback(emptyList())
        }
    }







}





