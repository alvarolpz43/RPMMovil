import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R
import com.rpm.rpmmovil.Usermotos.model.Usermoto

class MotosAdapter : ListAdapter<Usermoto, MotosAdapter.MotosViewHolder>(MotosDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MotosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_moto, parent, false)
        return MotosViewHolder(view)
    }

    override fun onBindViewHolder(holder: MotosViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class MotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val motoNombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        private val marcaMotoTextView: TextView = itemView.findViewById(R.id.marcaTextView)
        private val modeloMotoTextView: TextView = itemView.findViewById(R.id.modeloTextView)
        private val cilindrajeMotoTextView: TextView = itemView.findViewById(R.id.cilindrajeTextView)

        fun bind(usermoto: Usermoto) {
            motoNombreTextView.text = usermoto.MotoNombre
            marcaMotoTextView.text = usermoto.MarcaMoto
            modeloMotoTextView.text = usermoto.ModeloMoto
            cilindrajeMotoTextView.text = usermoto.CilindrajeMoto
        }
    }

    class MotosDiffCallback : DiffUtil.ItemCallback<Usermoto>() {
        override fun areItemsTheSame(oldItem: Usermoto, newItem: Usermoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Usermoto, newItem: Usermoto): Boolean {
            return oldItem == newItem
        }
    }
}
