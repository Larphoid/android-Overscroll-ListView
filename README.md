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
