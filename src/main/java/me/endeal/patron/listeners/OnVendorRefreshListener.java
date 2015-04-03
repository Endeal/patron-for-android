package me.endeal.patron.listeners;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.felipecsl.quickreturn.library.AbsListViewQuickReturnAttacher;
import com.felipecsl.quickreturn.library.QuickReturnAttacher;
import com.felipecsl.quickreturn.library.widget.QuickReturnAdapter;
import com.felipecsl.quickreturn.library.widget.QuickReturnTargetView;

import me.endeal.patron.bind.VendorBinder;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

public class OnVendorRefreshListener implements OnApiExecutedListener
{
    private ListView listLocations;
    private Button buttonFindNearest;

    public OnVendorRefreshListener(ListView listLocations, Button buttonFindNearest)
    {
        this.listLocations = listLocations;
        this.buttonFindNearest = buttonFindNearest;
    }

    @Override
    public void onExecuted()
    {
    	if (Globals.getVendors() == null || Globals.getVendors().isEmpty())
    	{
            return;
        }
        List<Map<String, String>> locations = new ArrayList<Map<String, String>>();

        String[] from = {"textName",
                "textPhone",
                "textAddress",
                "toggleButtonFavorite"};

        int[] to = {R.id.locationListItemTextName,
                R.id.locationListItemTextPhone,
                R.id.locationListItemTextAddress,
                R.id.locationListItemToggleButtonFavorite};
        sortVendors();
        addMappings(locations);
        SimpleAdapter adapter = new SimpleAdapter(listLocations.getContext(), locations,
            R.layout.list_item_location, from, to);
        adapter.setViewBinder(new VendorBinder());
        adapter.notifyDataSetChanged();

        // Quick Return
        int space = (int)Globals.convertDpToPixel(49, listLocations.getContext());
        listLocations.setAdapter(new QuickReturnAdapter(adapter));
        QuickReturnAttacher quickReturnAttacher = QuickReturnAttacher.forView((listLocations));
        QuickReturnTargetView targetView = quickReturnAttacher.addTargetView(buttonFindNearest, QuickReturnTargetView.POSITION_TOP, space);
        targetView.setAnimatedTransition(true);
        final AbsListViewQuickReturnAttacher attacher = (AbsListViewQuickReturnAttacher) quickReturnAttacher;
        attacher.setOnItemClickListener(new ListItemVendorListener());
    }


    public void addMappings(List<Map<String, String>> locations)
    {
        for (int i = 0; i < Globals.getVendors().size(); i++)
        {
            Map<String, String> mapping = new HashMap<String, String>();
            Vendor currentLocation = Globals.getVendors().get(i);
            mapping.put("textName", currentLocation.getName());
            mapping.put("textPhone", currentLocation.getPhone());
            mapping.put("textAddress", currentLocation.getAddress() +
                        ", " + currentLocation.getCity() +
                        ", " + currentLocation.getState() +
                        currentLocation.getZip());
            mapping.put("toggleButtonFavorite", "" + i);
            locations.add(mapping);
        }
    }

    private void sortVendors()
    {
      List<Vendor> vendors = new ArrayList<Vendor>();
      // Put favorites on top
      for (int i = 0; i < Globals.getVendors().size(); i++)
      {
          Vendor currentLocation = Globals.getVendors().get(i);
          if (Globals.getUser().hasFavoriteVendor(currentLocation.getId()))
          {
            vendors.add(currentLocation);
          }
      }

      // Sort the rest by distance
      for (int i = 0; i < Globals.getVendors().size(); i++)
      {
        Vendor currentLocation = Globals.getVendors().get(i);
        if (!Globals.getUser().hasFavoriteVendor(currentLocation.getId()))
        {
          vendors.add(currentLocation);
        }
      }
      Globals.setVendors(vendors);
    }
}
