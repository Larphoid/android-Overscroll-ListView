android-Overscroll-ListView
===========================

An 'Overscrollable' ListView with 'Bounce' effect for android !
The bounce effect can be turned off.
The animation delay can be set via the delay variable, which is in milliseconds. Default = 10. The higher the value, the slower the animation.

Usage:

The bounce effect can be turned on / off by calling listview.setBounce(true / false).

In xml, instead of creating a <ListView .../>, create a <com.larphoid.overscrollinglistview.OverscrollListview .../>

Ofcourse you can put the OverscrollListview.java in your current package and change com.larphoid.overscrollinglistview to your current package name.

When assigning the listview to a variable somewhere in your app, ofcourse you have to make it a OverscrollListview.

IMPORTANT: whenever you populate your listview, when you'r finished populating, don't forget to call listview.initializeValues(),
and set the first visible item to something other than the header or footer view. For example like this: listview.setSelectionFromTop(listview.nHeaders, listview.divHeight)

Thats it, enjoy !
