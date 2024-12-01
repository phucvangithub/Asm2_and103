package fpoly.md19304.asm_and103.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import fpoly.md19304.asm_and103.Models.Province;
import fpoly.md19304.asm_and103.R;


public class Adapter_Item_Province_Select_GHN extends ArrayAdapter<Province> {
    private Context context;
    private ArrayList<Province> list;

    public Adapter_Item_Province_Select_GHN(@NonNull Context context, ArrayList<Province> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_province_spinner, parent, false);
        }

        TextView textViewProvinceName = convertView.findViewById(R.id.textViewProvinceName);

        Province currentItem = getItem(position);

        if (currentItem != null) {
            textViewProvinceName.setText(currentItem.getProvinceName());
        }
        return convertView;
    }
}
