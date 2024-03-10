import com.rpm.rpmmovil.SelectMoto.model.ApiSelectMotos
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SelectMotoClient {
    private const val BASE_URL = "https://rpm-back-end.vercel.app/api/"

    val apiService: ApiSelectMotos by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiSelectMotos::class.java)
    }
}
