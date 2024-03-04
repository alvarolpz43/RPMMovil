import com.rpm.rpmmovil.Rmotos.model.DataRMotos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RegisterMoto {
    @POST("motos")
    fun PostRegisterMoto(
        @Body dataRMotos: DataRMotos,
        @Header("Authorization") token: HashMap<String, String>
    ): Call<DataRMotos>
}
