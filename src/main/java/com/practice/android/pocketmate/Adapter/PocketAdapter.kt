
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.android.pocketmate.Model.PocketModel
import com.practice.android.pocketmate.databinding.ItemPocketBinding


class PocketViewHolder(val binding: ItemPocketBinding):
    RecyclerView.ViewHolder(binding.root){
        private val context = binding.root.context

        fun bind(item: PocketModel){
            itemView.setOnClickListener{
                val intent: Intent = Intent(context, PocketActivity::class.java)
                intent.run{context.startActivity(this)}
            }
        }
    }

class PocketAdapter(val Items:MutableList<PocketModel>):
    RecyclerView.Adapter<PocketViewHolder>(){
    override fun getItemCount(): Int = Items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PocketViewHolder =
        PocketViewHolder(ItemPocketBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PocketViewHolder, position: Int) {
        val binding = (holder as PocketViewHolder).binding
        binding.title.text = Items[position].title
        binding.contetnt.text = Items[position].content
        binding.pocketImg.setImageResource(Items[position].image)
    }

}
