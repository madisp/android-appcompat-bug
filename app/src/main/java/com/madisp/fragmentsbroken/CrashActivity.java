package com.madisp.fragmentsbroken;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * A small toy app to demonstrate a bug in appcompat which may result in a crash.
 *
 * If a fragment with options menu items is added to the activity then in some cases the fragment
 * may receive onCreateOptionsMenu callbacks whilst the fragment is detached. This will often lead
 * to a crash as it is quite common to load resources in the callback - for instance to set a title
 * for a menu option. This will yield an IllegalStateException from Fragment.getResources() as the
 * fragment isn't attached any more. This happens iff your app happens to call the
 * supportInvalidateOptionsMenu() method.
 *
 * The specifics:
 *
 * 1) When ActionBarActivity.supportInvalidateOptionsMenu is called it posts an invalidate runnable
 *    to DecorView (method ActionBarActivityDelegateBase.supportInvalidateOptionsMenu)
 * 2) This runnable will make FragmentActivity (super of ActionBarActivity) dispatch the
 *    onCreateOptionsMenu callbacks on all the fragments. Note that this is usually the desired
 *    functionality...
 *
 *    *unless*
 *
 * 3) Due to some reason, say a configuration change, our activity instance was destroyed before the
 *    posted runnable had a chance to run. In this case the onCreateOptionsMenu callbacks will still
 *    be dispatched and the app will crash.
 *
 * This class exhibits the described case.
 */
public class CrashActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// just a framelayout with id=@id/main
		setContentView(R.layout.main);
		// set up our toolbar
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		// add a standard fragment with a few views and a menu option
		getSupportFragmentManager().beginTransaction().add(R.id.main, new CrashFragment()).commit();
	}

	public void crash(View v) {
		// the order of these is not really important, although I could get better reproducability
		// if I called supportInvalidateOptionsMenu *after* the setRequestOrientation. (It does
		// crash both ways, I just had a harder time reproducing it on an emulator if I called
		// invalidate first.

		// A wild configuration change appeared!
		// Configuration change used crash. It's super effective!
		supportInvalidateOptionsMenu();
		recreate();
	}
}
