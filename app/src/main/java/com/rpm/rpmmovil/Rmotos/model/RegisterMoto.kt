import com.rpm.rpmmovil.Rmotos.model.DataMotosResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RegisterMoto {
    @POST("motos")
    fun PostRegisterMoto(
        @Body dataRMotos: DataMotosResponse,
        @Header("Authorization") token: HashMap<String, String>
    ): Call<DataMotosResponse>
}
