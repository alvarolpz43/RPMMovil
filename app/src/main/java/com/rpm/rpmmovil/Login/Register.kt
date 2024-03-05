package com.rpm.rpmmovil.Login
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Login.model.ApiServiceRegister
import com.rpm.rpmmovil.Login.model.DtaRegister
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var datePickerDialog: DatePickerDialog

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rpm-back-end.vercel.app/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiServiceRegister::class.java)


    private val DATE_FORMAT = "yyyy/MM/dd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        managerUI()
    }

    private fun managerUI() {
        binding.ETDate.setOnClickListener {
            showDatePicker()
        }
        binding.fecha.setOnClickListener{
            showDatePicker()
        }

        binding.login.setOnClickListener {
            val loginIntent = Intent(this@Register, Login::class.java)
            startActivity(loginIntent)
            finish()
        }

        setListeners()
    }

    private fun setListeners() {
        binding.BtnRegister.setOnClickListener {
            val nombre = binding.ETName.text.toString()
            val email = binding.ETEmail.text.toString()
            val password = binding.ETPassword.text.toString()
            val identificationNumber = binding.ETIdentificacion.text.toString()
            val phoneNumber = binding.ETTelefono.text.toString()
            val dateText: String = binding.ETDate.text.toString()

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || identificationNumber.isEmpty() || phoneNumber.isEmpty() || dateText.isEmpty()) {
                Toast.makeText(applicationContext, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(applicationContext, "Formato de correo electrónico inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(applicationContext, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (identificationNumber.length < 5 || identificationNumber.length > 10 || !identificationNumber.all { it.isDigit() }) {
                Toast.makeText(applicationContext, "El número de identificación debe tener entre 5 y 10 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (phoneNumber.length != 10 || !phoneNumber.all { it.isDigit() }) {
                Toast.makeText(applicationContext, "El número de teléfono debe tener 10 dígitos y ser numérico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val userRegistrationData = DtaRegister(
                Nombres_Mv = nombre,
                Email_Mv = email,
                NumeroIdent_Mv = identificationNumber.toInt(),
                FechaNac_Mv = dateText,
                Contraseña_Mv = password,
                NumeroTel_Mv = phoneNumber.toLong()
            )

            val call: Call<DtaRegister> = apiService.getRegister(userRegistrationData)

            call.enqueue(object : Callback<DtaRegister> {
                override fun onResponse(call: Call<DtaRegister>, response: Response<DtaRegister>) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@Register,Login::class.java)
                        Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DtaRegister>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, day ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, day)
                val sdf = SimpleDateFormat(DATE_FORMAT)
                val formattedDate = sdf.format(selectedCalendar.time)
                binding.ETDate.setText(formattedDate)
            }, year, month, dayOfMonth)

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }
}
