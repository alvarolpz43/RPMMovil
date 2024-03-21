    package com.rpm.rpmmovil.Usermotos


    import MotosAdapter
    import Usermoto
    import android.content.Context
    import android.content.SharedPreferences
    import android.os.Bundle
    import android.util.Log
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.rpm.rpmmovil.Model.Constains
    import com.rpm.rpmmovil.Usermotos.model.ApiServiceMotouser
    import com.rpm.rpmmovil.Usermotos.model.MotosResponse
    import com.rpm.rpmmovil.Usermotos.model.RetrofitClient
    import com.rpm.rpmmovil.databinding.ActivityUserMotosBinding
    import com.rpm.rpmmovil.presupuesto.Presupuesto
    import com.rpm.rpmmovil.presupuesto.model.PresupuestoService
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext


    class UserMotosActivity : AppCompatActivity(),MotosAdapter.OnMotoClickListener  {
        private lateinit var binding: ActivityUserMotosBinding
        private lateinit var motosAdapter: MotosAdapter
        private lateinit var apiService: ApiServiceMotouser
        private lateinit var presupuestoService: PresupuestoService
        private lateinit var sharedPreferences: SharedPreferences
        private var distanceKm: Int = 0
        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityUserMotosBinding.inflate(layoutInflater)
            setContentView(binding.root)
            //-----------------------------------------------------------------------------
            val intent = intent
            distanceKm = intent.getIntExtra("distanceKm", 0)
            Log.d("MyTag", "Este es el valor que se está recibiendo: $distanceKm")
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            apiService = RetrofitClient.apiService
            presupuestoService = RetrofitClient.presupuestoService
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

            val token = sharedPreferences.getString("token", null)

            if (token != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response: MotosResponse? = apiService.getMotos(token)

                        withContext(Dispatchers.Main) {
                            response?.let { motosResponse ->
                                val motosList: List<Usermoto> = motosResponse.motos
                                if (motosList.isEmpty()) {
                                    Toast.makeText(this@UserMotosActivity, "El usuario no tiene motos registradas", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Crear y adjuntar adaptador al RecyclerView
                                    // Dentro de la actividad UserMotosActivity
                                    motosAdapter = MotosAdapter(motosList, this@UserMotosActivity,
                                        distanceKm!!
                                    )

                                    binding.recyclerView.adapter = motosAdapter
                                }
                            } ?: run {
                                Toast.makeText(this@UserMotosActivity, "No se encontraron datos de motos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@UserMotosActivity, "Error al obtener datos de motos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Manejar la situación donde el token no está disponible en las preferencias compartidas
                Toast.makeText(this@UserMotosActivity, "Token no encontrado en las preferencias compartidas", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onMotoClicked(consumoMotoLx100km: Int, distanceKm: Int) {
            enviarConsumoAlPresupuesto(distanceKm.toInt(),consumoMotoLx100km )
        }

        private fun enviarConsumoAlPresupuesto(distanceKm: Int, consumoMotoLx100km: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Aquí envías ambos valores al servidor
                    val presupuestoRequest = Presupuesto(distanceKm, consumoMotoLx100km)
                    val response = presupuestoService.enviarPresupuesto(presupuestoRequest)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val presupuestoRedondeado = String.format("%.2f", response.body()?.presupuesto ?: 0.0)
                            Constains.VALOR_PPTO = presupuestoRedondeado.toInt()

                            // Si la solicitud fue exitosa, mostrar el presupuesto obtenido
                            Toast.makeText(this@UserMotosActivity, "Presupuesto: $$presupuestoRedondeado", Toast.LENGTH_SHORT).show()
                            Log.d("MyTag", "distanceKm: $distanceKm, consumoMotoLx100km: $consumoMotoLx100km")

                        } else {
                            // Si la solicitud no fue exitosa, mostrar un mensaje de error
                            Toast.makeText(this@UserMotosActivity, "Error al obtener el presupuesto", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UserMotosActivity, "Error al enviar el consumo al presupuesto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }