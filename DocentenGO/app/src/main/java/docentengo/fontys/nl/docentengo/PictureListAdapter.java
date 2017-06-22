package docentengo.fontys.nl.docentengo;

/**
 * Created by Jeroe on 16-6-2017.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Business.PersonEntry;

public class PictureListAdapter extends ArrayAdapter<PersonEntry> {

    private final Activity context;

    public PictureListAdapter(Activity context, List<PersonEntry> personen) {
        super(context, R.layout.picturelist, personen);
        this.context = context;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.picturelist, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.text1);
        txtTitle.setText(getItem(position).toString());
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        switch (getItem(position).getTeacher().getDepartment()) {
            case "Team T":
                imageView.setImageResource(R.mipmap.technology);
                break;
            case "Team S":
                imageView.setImageResource(R.mipmap.software);
                break;
            case "Team M":
                imageView.setImageResource(R.mipmap.media);
                break;
            case "Team B":
                imageView.setImageResource(R.mipmap.business);
                break;
        }
        return rowView;
    }

}
