import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DataMotosResponse( @SerializedName("motos") val moto:List<DataItemMotos>)
data class DataItemMotos(
   @SerializedName("MotoNombre") val motonom: String,
   @SerializedName("MarcaMoto") val motomarca: String,
   @SerializedName("ModeloMoto") val motomodel: String,
   @SerializedName("VersionMoto") val motovers: Number,
   @SerializedName("ConsumoMotoLx100km") val consumokmxg: Number,
   @SerializedName("CilindrajeMoto") val cilimoto: String,
   @SerializedName("FotoMoto") val imagemoto: String
) : Parcelable {
   constructor(parcel: Parcel) : this(
      parcel.readString() ?: "",
      parcel.readString() ?: "",
      parcel.readString() ?: "",
      parcel.readValue(Number::class.java.classLoader) as Number,
      parcel.readValue(Number::class.java.classLoader) as Number,
      parcel.readString() ?: "",
      parcel.readString() ?: ""
   )

   override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeString(motonom)
      parcel.writeString(motomarca)
      parcel.writeString(motomodel)
      parcel.writeValue(motovers)
      parcel.writeValue(consumokmxg)
      parcel.writeString(cilimoto)
      parcel.writeString(imagemoto)
   }

   override fun describeContents(): Int {
      return 0
   }

   companion object CREATOR : Parcelable.Creator<DataItemMotos> {
      override fun createFromParcel(parcel: Parcel): DataItemMotos {
         return DataItemMotos(parcel)
      }

      override fun newArray(size: Int): Array<DataItemMotos?> {
         return arrayOfNulls(size)
      }
   }
}
