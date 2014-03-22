package com.madisp.fragmentsbroken;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class CrashActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		getSupportFragmentManager().beginTransaction().add(R.id.main, new CrashFragment()).commit();
	}

	public void crash(View v) {
		supportInvalidateOptionsMenu();
		// A wild configuration change appeared!
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
}
