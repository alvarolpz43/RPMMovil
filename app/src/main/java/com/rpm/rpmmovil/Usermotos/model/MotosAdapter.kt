import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R

class MotosAdapter(private val motosList: List<Usermoto>) : RecyclerView.Adapter<MotosAdapter.MotoViewHolder>() {

    inner class MotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        private val modeloTextView: TextView = itemView.findViewById(R.id.modeloTextView)
        private val marcaTextView: TextView = itemView.findViewById(R.id.marcaTextView)
        private val consumoTextView: TextView = itemView.findViewById(R.id.consumoTextView)
        private val cilindrajeTextView: TextView = itemView.findViewById(R.id.cilindrajeTextView)

        fun bind(usermoto: Usermoto) {
            Log.d("MotosAdapter", "Bind usermoto: $usermoto")
            nombreTextView.text = usermoto.MotoNombre
            modeloTextView.text = usermoto.ModeloMoto
            marcaTextView.text = usermoto.MarcaMoto
            consumoTextView.text = "Consumo: ${usermoto.ConsumoMotoLx100km} L/100km"
            cilindrajeTextView.text = "Cilindraje: ${usermoto.CilindrajeMoto}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.items_moto, parent, false)
        return MotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MotoViewHolder, position: Int) {
        val usermoto = motosList[position]
        Log.d("MotosAdapter", "Bind usermoto at position $position: $usermoto")
        holder.bind(usermoto)
    }

    override fun getItemCount() = motosList.size
}
