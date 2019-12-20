package denokela.com.medico.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import denokela.com.medico.PlaceApiAutoComplete;

public class PlaceAutoSuggestAdapter extends ArrayAdapter implements Filterable {
    private static final String TAG = "PlaceAutoSuggestAdapter";
    int resource;
    Context context;

    ArrayList<String> results;
    String location;

    PlaceApiAutoComplete placeApiAutoComplete = new PlaceApiAutoComplete();

    public PlaceAutoSuggestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource = resource;
   }

    @Override
    public int getCount(){
        return results.size();
    }

    @Override
    public String getItem(int pos){
        return results.get(pos);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if(charSequence!=null){
                  Log.d(TAG, "performFiltering: Filtering results");
                    results = placeApiAutoComplete.autoComplete(context,charSequence.toString());
                    Log.d(TAG, "performFiltering: "+results.size());
                    filterResults.values = results;
                    filterResults.count = results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(results!=null && results.size()>0){
                    notifyDataSetChanged();
                }else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
