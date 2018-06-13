package pl.agh.dynamicsearch.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import pl.agh.dynamicsearch.R;
import pl.agh.dynamicsearch.models.Product;

public class MainListAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;
    int mResource;

    public MainListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        products = objects;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = products.get(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_cell,
                    parent, false);

        TextView typeView = (TextView) convertView.findViewById(R.id.cell_type);
        TextView titleView = (TextView) convertView.findViewById(R.id.cell_name);
        TextView priceView = (TextView) convertView.findViewById(R.id.cell_price);


        if (!product.getmName().isEmpty()) {
            typeView.setText("" + product.getmType().charAt(0) + product.getmType().charAt(1));
            titleView.setText(product.getmName());
            priceView.setText(new DecimalFormat("#0.00").format(product.getmPrice()) + "zł");
        }
        return convertView;
    }
}
