package com.example.masterapp_v4;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.Items.ItemBio;
import com.example.masterapp_v3.R;
import com.example.masterapp_v4.MainActivity.PlaceholderFragment;

public class AboutFragment extends Fragment {

	MainActivity parentActivity;
	LinearLayout aboutPageLinearLayout;
	ArrayList<ItemBio> bios;

	public static PlaceholderFragment newInstance(int sectionNumber) {
		PlaceholderFragment fragment = new PlaceholderFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public AboutFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);
		parentActivity = (MainActivity) getActivity();

		// This allows us to edit the layout
		aboutPageLinearLayout = (LinearLayout) rootView
				.findViewById(R.id.about_fragment_layout);

		bios = new ArrayList<ItemBio>();

		
		//TODO: make bios in text files
		
		ItemBio a = new ItemBio(aboutPageLinearLayout, parentActivity, bios,
				R.drawable.picture, "Cat", "2015", "Catnip", "Mr.Cat",
				"This is a Kool Kat and it looks nice, thank you for reading my cat's bio");

		
		
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}
