package retailer.tekmeda.com.tekmedaretailer.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import retailer.tekmeda.com.tekmedaretailer.R;
import retailer.tekmeda.com.tekmedaretailer.bean.Stockists;

public class StockistListAdapter extends ArrayAdapter<Stockists> {

    public StockistListAdapter(@NonNull Context context, @NonNull List<Stockists> stockistsList) {
        super(context, 0, stockistsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.stockistlistview, parent, false);

        Stockists stockists =getItem(position);
        TextView enterpriseName = (TextView)convertView.findViewById(R.id.tvStockistName);
        TextView area = (TextView)convertView.findViewById(R.id.tvStockistArea);
        enterpriseName.setText(stockists.getEnterpriseName());
        area.setText(stockists.getArea());

        return convertView;

    }
}
