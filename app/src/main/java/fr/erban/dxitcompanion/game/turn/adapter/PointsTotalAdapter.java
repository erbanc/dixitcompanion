package fr.erban.dxitcompanion.game.turn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.turn.ScoreRow;

public class PointsTotalAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<ScoreRow> itemsItems;



    public PointsTotalAdapter(Context context, List<ScoreRow> itemsItems) {
        this.mContext = context;
        this.itemsItems = itemsItems;

    }

    @Override
    public int getCount() {
        return itemsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return itemsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View scoreView, ViewGroup parent) {
        ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (scoreView == null) {

            if (inflater != null) {
                scoreView = inflater.inflate(R.layout.score_row, parent, false);
            }
            holder = new ViewHolder();
            if (scoreView != null) {
                holder.name = scoreView.findViewById(R.id.name);
                holder.score = scoreView.findViewById(R.id.score);

                scoreView.setTag(holder);
            }

        } else {
            holder = (ViewHolder) scoreView.getTag();
        }

        final ScoreRow m = itemsItems.get(position);
        holder.name.setText(m.getName());
        holder.score.setText(m.getScore());

        return scoreView;
    }

    static class ViewHolder {

        TextView name;
        TextView score;

    }

}
