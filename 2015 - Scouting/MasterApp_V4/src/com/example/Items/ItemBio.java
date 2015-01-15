package com.example.Items;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.masterapp_v3.R;
import com.example.masterapp_v4.MainActivity;

/**
 * The ItemBio Class initializes all of the XML views into Java. It displays a
 * picture, name, position, favourite food, and other important info
 * 
 * @author williamgill
 * @version 3.0
 * @since 2015-01-13
 */
public class ItemBio extends RelativeLayout {

	ImageView portraitIV;
	TextView positionTV, rookieYearTV, favouriteFoodTV, nameTV, bioTV;

	MainActivity activity;
	RelativeLayout root;

	ArrayList<ItemBio> parentArrayList = new ArrayList<ItemBio>();

	/**
	 * Initializes all of the views adds the corresponding values and adds the itemBio to the arrayList
	 * @since 2015-01-13
	 * @param parent - The Parent Layout
	 * @param context - the main activity
	 * @param parentArrayList - an arraylist that holds the item bios
	 * @param portraitId - The view containing the portrait
	 * @param position - the view containing the position
	 * @param rookieYear - the view containing the rookie year
	 * @param favouriteFood - the view containing the favourite food
	 * @param name - the view containing the name
	 * @param bio - the view containing the bio
	 */
	public ItemBio(final LinearLayout parent, MainActivity context,
			ArrayList<ItemBio> parentArrayList, int portraitId,
			String position, String rookieYear, String favouriteFood,
			String name, String bio) {
		super(context);
		this.parentArrayList = parentArrayList;

		activity = (MainActivity) context;
		LayoutInflater inflater = LayoutInflater.from(activity);
		inflater.inflate(R.layout.item_bio, this, true);

		this.root = (RelativeLayout) findViewById(R.id.item_bio_layout);

		portraitIV = (ImageView) findViewById(R.id.image);
		positionTV = (TextView) findViewById(R.id.position_TV);
		rookieYearTV = (TextView) findViewById(R.id.rookie_year_TV);
		favouriteFoodTV = (TextView) findViewById(R.id.favourite_food_TV);
		nameTV = (TextView) findViewById(R.id.name_TV);
		bioTV = (TextView) findViewById(R.id.bio_TV);

		
		
		
		
		portraitIV.setImageResource(portraitId);
		positionTV.setText("Position: " + position);
		rookieYearTV.setText("Rookie Year: " + rookieYear);
		favouriteFoodTV.setText("Favourite Food: " + favouriteFood);
		nameTV.setText("Name: " + name);		
		bioTV.setText("Bio: " + bio);

		parentArrayList.add(this);
		addToParent(parent);

	}

	/**
	 * Add this View to the Parent
	 * @since 2015-01-13
	 * @param parent The Parent Layout
	 */
	public void addToParent(LinearLayout parent) {

		parent.addView(this, parent.getChildCount(), new android.view.ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

	}

}
