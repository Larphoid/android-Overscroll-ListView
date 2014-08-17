android-Overscroll-ListView
===========================

An 'Overscrollable' ListView with 'Bounce' effect for android !

Usage:
======
The bounce effect can be turned on / off with listview.setBounce(true / false).

The animation delay can be set via the delay variable, which is in milliseconds. Default = 10. The higher the value, the slower the animation.

The bounce length can be set with listview.setElasticity(float), see source file for help.

The break speed can be set with listview.setBreakspeed(float), see source file for help.

In xml, instead of creating a ListView, create a com.larphoid.overscrollinglistview.OverscrollListview

Ofcourse you can put the OverscrollListview.java in your current package and change com.larphoid.overscrollinglistview to your current package name.

When assigning the listview to a variable somewhere in your app, ofcourse you have to make it a OverscrollListview.

IMPORTANT: whenever you populate your listview, when you'r finished populating, don't forget to call listview.initializeValues().

CHANGES:
after populating the listview it is not necesary anymore to set the first visible item to something other than the header or footer view (for example like this: listview.setSelectionFromTop(listview.nHeaders, listview.divHeight)), this is now done in initializeValues().

Thats it, enjoy !

Example:
========
The following is an example of how to include Overscrollable in your project.
```Java
package com.example.tutorial;


import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import com.larphoid.overscrolllistview.OverscrollListview;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		OverscrollListview listView = (OverscrollListview)findViewById(R.id.list);

		// Defined Array values to show in ListView
		String[] values = new String[] { 
				"List item 1", 
				"List item 2",
				"List item 3",
				"List item 4", 
				"List item 5", 
				"List item 6", 
				"List item 7", 
				"List item 8" 
		};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);
		listView.setAdapter(adapter); 
	}
}

```

```XML
<!-- activity_main.xml -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <com.larphoid.overscrolllistview.OverscrollListview
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" >
    </com.larphoid.overscrolllistview.OverscrollListview>

</LinearLayout>
```

Be sure to include the 'com.larphoid.overscrolllistview' package in your Android
Project src file and you're all set.


LICENSE
=======

This projected is licensed under the terms of the GPL license.
