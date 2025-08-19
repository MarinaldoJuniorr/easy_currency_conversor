import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.easy_currency_conversor.CurrencyItem
import com.example.easy_currency_conversor.databinding.ItemCurrencyBinding


class CurrencyAdapter(
    context: Context,
    private val items: List<CurrencyItem>
): ArrayAdapter<CurrencyItem>(context,0, items){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemCurrencyBinding
        val view: View

        if (convertView == null) {
            binding = ItemCurrencyBinding.inflate(LayoutInflater.from(context), parent,false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as ItemCurrencyBinding
            view = convertView
        }

        val item = items[position]
        binding.imgFlag.setImageResource(item.flagRes)
        binding.tvCode.text = context.getString(item.code)

        return view
    }

}