package fr.erban.dxitcompanion.game.turn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.turn.SelectPlayerRow;

public class PlayerSelectionAdapter extends BaseAdapter {

    private final Context mContext;

    private LayoutInflater inflater;

    private final List<SelectPlayerRow> itemsItems;

    public PlayerSelectionAdapter(Context context, List<SelectPlayerRow> itemsItems) {

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
    public View getView(int position, View playerSelectionView, ViewGroup parent) {

        ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (playerSelectionView == null) {

            assert inflater != null;
            playerSelectionView = inflater.inflate(R.layout.player_select_row, parent, false);
            holder = new ViewHolder();
            holder.name = playerSelectionView.findViewById(R.id.name);
            holder.checked = playerSelectionView.findViewById(R.id.checked);

            playerSelectionView.setTag(holder);

        } else {
            holder = (ViewHolder) playerSelectionView.getTag();
        }

        final SelectPlayerRow m = itemsItems.get(position);
        holder.name.setText(m.getName());
        holder.checked.setChecked(m.isChecked());

        return playerSelectionView;
    }

    static class ViewHolder {

        TextView name;

        CheckBox checked;

    }

}
