android-appcompat-bug
=====================

A small toy app to demonstrate a bug in appcompat which may result in a crash.

If a fragment with options menu items is added to the activity then in some
cases the fragment may receive `onCreateOptionsMenu` callbacks whilst the
fragment is detached. This will often lead to a crash as it is quite common to
load resources in the callback - for instance to set a title for a menu option.

This will yield an `IllegalStateException` from `Fragment.getResources()` as the
fragment isn't attached any more. This happens iff your app happens to call the
`supportInvalidateOptionsMenu()` method.

The specifics:

* When `ActionBarActivity.supportInvalidateOptionsMenu` is called it posts an
  invalidate runnable to DecorView (method
  `ActionBarActivityDelegateBase.supportInvalidateOptionsMenu`)
* This runnable will make FragmentActivity (super of ActionBarActivity) dispatch
  the `onCreateOptionsMenu` callbacks on all the fragments. Note that this is
  usually the desired functionality...

  *...unless...*

* ...due to some reason, say a configuration change, our activity instance was
  destroyed before the posted runnable had a chance to run. In this case the
  `onCreateOptionsMenu` callbacks will still be dispatched and the app will
  crash.

This toy app exhibits the described case.
