package com.example.masterapp_v4;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.masterapp_v3.R;
import com.example.masterapp_v4.MainActivity.PlaceholderFragment;

public class TournamentFragment extends Fragment{
	
	
	
	
	public static PlaceholderFragment newInstance(int sectionNumber) {
		PlaceholderFragment fragment = new PlaceholderFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public TournamentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);


		String[] teams = {"610", "1114", "254"};
		ListAdapter theAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, teams);
		
		ListView theListView= (ListView) this.getActivity().findViewById(R.id.list_view);
		theListView.setAdapter(theAdapter);
		
		theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long l) {
				
				//TODO: show the averages of the selected team
				
				String teamPicked = "Team picked" + String.valueOf(adapterView.getItemAtPosition(position));
				Toast.makeText(getActivity(), teamPicked, Toast.LENGTH_SHORT).show();
				
			}
		});
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}