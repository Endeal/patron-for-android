package me.endeal.patron.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import me.endeal.patron.model.Station;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

public class ButtonStationListener implements OnClickListener
{
    private OnApiExecutedListener listener;

    public ButtonStationListener(OnApiExecutedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.DialogMain);
        final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_stations, null);
        builder.setView(dialogView);
        ListView listStations = (ListView)dialogView.findViewById(R.id.dialogStationsListMain);
        Button buttonCancel = (Button)dialogView.findViewById(R.id.dialogStationsButtonCancel);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String[] stations = new String[Globals.getVendor().getStations().size()];
        for (int i = 0; i < Globals.getVendor().getStations().size(); i++)
        {
            Station station = Globals.getVendor().getStations().get(i);
            stations[i] = station.getName();;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, stations);
        listStations.setAdapter(adapter);
        dialog.show();

        // Set the station when an item is clicked.
        listStations.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Station station = Globals.getVendor().getStations().get(position);
                Globals.getOrder().setStation(station);
                listener.onExecuted();
                dialog.dismiss();
            }
        });

        // Set the button to close the dialog when canceled.
        buttonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
    }
}
