package it.poliba.giorgiobasile.prescriptionwriter.activities;

import java.util.ArrayList;

import it.poliba.giorgiobasile.prescriptionwriter.CheckedLinearLayout;
import it.poliba.giorgiobasile.prescriptionwriter.Patient;
import it.poliba.giorgiobasile.prescriptionwriter.R;
import it.poliba.giorgiobasile.prescriptionwriter.dialogs.DialogRemoveFragment;

import it.poliba.giorgiobasile.prescriptionwriter.settings.SettingsActivity;
import it.poliba.giorgiobasile.prescriptionwriter.utils.AboutFragment;
import it.poliba.giorgiobasile.prescritpionwriter.fragments.DoctorFragment;
import it.poliba.giorgiobasile.prescritpionwriter.fragments.MyPrescriptionsFragment;
import it.poliba.giorgiobasile.prescritpionwriter.fragments.MyPatientsFragment;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		DialogRemoveFragment.OnSelectedListener {

	private FloatingActionButton mFab;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	NfcAdapter adapter;
	DrawerAdapter drawerAdapter;
	PendingIntent pendingIntent;
	IntentFilter writeTagFilters[];
	boolean writeMode;
	Tag mytag;
	int oldPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		adapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);
		tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
		writeTagFilters = new IntentFilter[] { tagDetected };

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		drawerAdapter = new DrawerAdapter(this, getResources().getStringArray(
				R.array.drawer_options));
		mDrawerList.setAdapter(drawerAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mFab = (FloatingActionButton) findViewById(R.id.fabbutton);
		mFab.setColor(getResources().getColor(android.R.color.holo_red_light));

		if (savedInstanceState == null) {
			mDrawerList.setItemChecked(0, true);
			drawerAdapter.notifyDataSetChanged();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new MyPrescriptionsFragment())
					.commit();
		} else {
			oldPosition = savedInstanceState.getInt("position");
			mDrawerList.setItemChecked(oldPosition, true);
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("position", oldPosition);
	}

	public Tag getTag() {
		return mytag;
	}

	public void setTag(Tag tag) {
		mytag = tag;
	}

	@Override
	public void onPause() {
		super.onPause();
		WriteModeOff();
	}

	@Override
	public void onResume() {
		super.onResume();
		WriteModeOn();
	}

	private void WriteModeOn() {
		writeMode = true;
		adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters,
				null);
	}

	private void WriteModeOff() {
		writeMode = false;
		adapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Toast.makeText(this, getString(R.string.tag_detected),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		/*
		 * if (id == R.id.action_settings) { return true; }
		 */
		return super.onOptionsItemSelected(item);
	}

	public void hideFab(View view) {
		mFab.hide(true);
		// getActionBar().hide();
	}

	public void changeFab(int drawable, OnClickListener l, ListView v) {
		mFab.setDrawable(getResources().getDrawable(drawable));
		mFab.setOnClickListener(l);
		mFab.listenTo(v);
		// getActionBar().hide();
	}

	public void changeFab(int drawable, OnClickListener l) {
		mFab.setDrawable(getResources().getDrawable(drawable));
		mFab.setOnClickListener(l);
		// getActionBar().hide();
	}

	public void showFab(View view) {
		mFab.hide(false);
		// getActionBar().show();
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		if (position == 0) {
			if ((getSupportFragmentManager().findFragmentById(R.id.container)
					.getClass() != MyPrescriptionsFragment.class)) {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new MyPrescriptionsFragment())
						.commit();
			}
			oldPosition = position;
			mDrawerLayout.closeDrawer(mDrawerList);
			mDrawerList.setItemChecked(position, true);
		} else if (position == 1) {
			if ((getSupportFragmentManager().findFragmentById(R.id.container)
					.getClass() != MyPatientsFragment.class)) {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new MyPatientsFragment()).commit();
			}
			oldPosition = position;
			mDrawerLayout.closeDrawer(mDrawerList);
			mDrawerList.setItemChecked(position, true);
		} else if (position == 2) {
			if ((getSupportFragmentManager().findFragmentById(R.id.container)
					.getClass() != DoctorFragment.class)) {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new DoctorFragment()).commit();
			}
			mDrawerLayout.closeDrawer(mDrawerList);
			mDrawerList.setItemChecked(position, true);
			oldPosition = position;
		} else if (position == 3) {
			startActivity(new Intent(this, SettingsActivity.class));
			// mDrawerLayout.closeDrawer(mDrawerList);
			// int pos = mDrawerList.getCheckedItemPosition();
			mDrawerList.setItemChecked(position, false);
			mDrawerList.setItemChecked(oldPosition, true);
		} else if (position == 4) {
			AboutFragment prog = AboutFragment.newInstance();
			prog.show(getSupportFragmentManager(), AboutFragment.fragment_tag);
			// mDrawerLayout.closeDrawer(mDrawerList);
			// int pos = mDrawerList.getCheckedItemPosition();
			mDrawerList.setItemChecked(position, false);
			mDrawerList.setItemChecked(oldPosition, true);
		}
		// Highlight the selected item, update the title, and close the drawer
		// mDrawerList.setItemChecked(position, true);
		// setTitle(mPlanetTitles[position]);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title.toString();
		getActionBar().setTitle(mTitle);
	}

	@Override
	public void onSelected(int position) {
		if (getSupportFragmentManager().findFragmentById(R.id.container)
				.getClass() == MyPrescriptionsFragment.class) {
			MyPrescriptionsFragment frag = (MyPrescriptionsFragment) getSupportFragmentManager()
					.findFragmentById(R.id.container);
			frag.removeItem(position);
		} else if ((getSupportFragmentManager()
				.findFragmentById(R.id.container).getClass() == MyPatientsFragment.class)) {
			MyPatientsFragment frag = (MyPatientsFragment) getSupportFragmentManager()
					.findFragmentById(R.id.container);
			frag.removeItem(position);
		}
	}

	static class DrawerAdapter extends BaseAdapter {

		private Context context;
		private String[] items;

		public DrawerAdapter(Context context, String[] items) {
			this.context = context;
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.drawer_list_item,
						parent, false);
			}
			String p = items[position];
			// DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			CheckedTextView text = ((CheckedTextView) convertView
					.findViewById(android.R.id.text1));
			text.setText(p);
			ImageView icon = (ImageView) convertView
					.findViewById(R.id.icon_item);
			int icon_id = 0;
			switch (position) {
			case 0:
				icon_id = R.drawable.prescription_icon;
				break;
			case 1:
				icon_id = R.drawable.thermo_icon;
				break;
			case 2:
				icon_id = R.drawable.steto_icon;
				break;
			case 3:
				icon_id = R.drawable.settings;
				break;
			case 4:
				icon_id = R.drawable.cursor;
				break;
			}

			icon.setImageResource(icon_id);

			return convertView;
		}
	}
}
