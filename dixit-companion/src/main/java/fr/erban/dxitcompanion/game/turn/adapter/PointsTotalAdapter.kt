package fr.erban.dxitcompanion.game.turn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import fr.erban.dxitcompanion.databinding.ScoreRowBinding
import fr.erban.dxitcompanion.game.turn.ScoreRow

class PointsTotalAdapter(
    context: Context,
    private val items: List<ScoreRow>
) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getCount() = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ScoreRowBinding.inflate(inflater, parent, false).also { it.root.tag = it }
        } else {
            convertView.tag as ScoreRowBinding
        }
        val item = items[position]
        binding.name.text = item.name
        binding.score.text = item.score
        return binding.root
    }
}
