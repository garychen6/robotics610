package com.example.masterapp_v4;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.masterapp_v3.R;
import com.example.masterapp_v4.MainActivity.PlaceholderFragment;

public class MainFragment extends Fragment {

	Button gtcButton, hawaiiButton, wcButton;
	MainActivity parentActivity;
	
	public static PlaceholderFragment newInstance(int sectionNumber) {
		PlaceholderFragment fragment = new PlaceholderFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public MainFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		parentActivity = (MainActivity) getActivity();
		
		gtcButton = (Button) parentActivity.findViewById(R.id.greater_toronto_central_Button);
		hawaiiButton = (Button) parentActivity.findViewById(R.id.hawaii_Button);
		wcButton = (Button) parentActivity.findViewById(R.id.champs_Button);
		
		gtcButton.setOnClickListener(new OnClickListener(){

			
			public void onClick(View arg0) {
				
				
			}
			
		});
		
			
			
			
		
		
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);


		
		return rootView;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}
