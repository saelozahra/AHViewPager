package de.amberhome.viewpagerexample;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "de.amberhome.viewpagerexample", "de.amberhome.viewpagerexample.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "de.amberhome.viewpagerexample", "de.amberhome.viewpagerexample.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "de.amberhome.viewpagerexample.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public anywheresoftware.b4a.keywords.Common __c = null;
public static int _type_hello_world = 0;
public static int _type_settings = 0;
public static int _type_listview = 0;
public static int _fill_parent = 0;
public static int _wrap_content = 0;
public static int _currenttheme = 0;
public static int _currentpage = 0;
public de.amberhome.viewpager.PageContainerAdapter _container = null;
public de.amberhome.viewpager.ViewPagerWrapper _pager = null;
public de.amberhome.viewpager.ViewPagerTabsWrapper _tabs = null;
public anywheresoftware.b4a.objects.PanelWrapper _line = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _sptheme = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static class _panelinfo{
public boolean IsInitialized;
public int PanelType;
public boolean LayoutLoaded;
public void Initialize() {
IsInitialized = true;
PanelType = 0;
LayoutLoaded = false;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _activity_create(boolean _firsttime) throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _col = null;
 //BA.debugLineNum = 59;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 62;BA.debugLine="Button1.Initialize(\"Button1\")";
mostCurrent._button1.Initialize(mostCurrent.activityBA,"Button1");
 //BA.debugLineNum = 63;BA.debugLine="Button1.Gravity = Gravity.CENTER";
mostCurrent._button1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 64;BA.debugLine="Activity.AddView(Button1, 88dip, Activity.Height";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._button1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (88)),(int) (mostCurrent._activity.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (44))),(int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (88))-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (88))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 67;BA.debugLine="container.Initialize";
mostCurrent._container.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 68;BA.debugLine="For i = 0 To 5";
{
final int step5 = 1;
final int limit5 = (int) (5);
for (_i = (int) (0) ; (step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5); _i = ((int)(0 + _i + step5)) ) {
 //BA.debugLineNum = 69;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Select i";
switch (_i) {
case 0: {
 //BA.debugLineNum = 73;BA.debugLine="pan = CreatePanel(TYPE_HELLO_WORLD, \"Hello Wor";
_pan = _createpanel(_type_hello_world,"Hello World");
 //BA.debugLineNum = 74;BA.debugLine="container.AddPage(pan,\"Page \" & i)";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"Page "+BA.NumberToString(_i));
 break; }
case 1: {
 //BA.debugLineNum = 76;BA.debugLine="pan = CreatePanel(TYPE_SETTINGS, \"Settings\")";
_pan = _createpanel(_type_settings,"Settings");
 //BA.debugLineNum = 77;BA.debugLine="container.AddPage(pan,\"Settings\")";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"Settings");
 break; }
default: {
 //BA.debugLineNum = 79;BA.debugLine="pan = CreatePanel(TYPE_LISTVIEW, \"ListView \" &";
_pan = _createpanel(_type_listview,"ListView "+BA.NumberToString((_i-1)));
 //BA.debugLineNum = 80;BA.debugLine="container.AddPage(pan,\"ListView \" & i)";
mostCurrent._container.AddPage((android.view.View)(_pan.getObject()),"ListView "+BA.NumberToString(_i));
 break; }
}
;
 }
};
 //BA.debugLineNum = 85;BA.debugLine="pager.Initialize(container, \"Pager\")";
mostCurrent._pager.Initialize(mostCurrent.activityBA,mostCurrent._container,"Pager");
 //BA.debugLineNum = 88;BA.debugLine="tabs.Initialize(pager)";
mostCurrent._tabs.Initialize(mostCurrent.activityBA,mostCurrent._pager);
 //BA.debugLineNum = 89;BA.debugLine="tabs.LineHeight = 5dip";
mostCurrent._tabs.setLineHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 90;BA.debugLine="tabs.UpperCaseTitle = True";
mostCurrent._tabs.setUpperCaseTitle(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 91;BA.debugLine="Activity.AddView(tabs, 0, 0, FILL_PARENT, WRAP_CO";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._tabs.getObject()),(int) (0),(int) (0),_fill_parent,_wrap_content);
 //BA.debugLineNum = 94;BA.debugLine="Dim col As ColorDrawable";
_col = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 95;BA.debugLine="col.Initialize(tabs.LineColorCenter, 0)";
_col.Initialize(mostCurrent._tabs.getLineColorCenter(),(int) (0));
 //BA.debugLineNum = 96;BA.debugLine="line.Initialize(\"\")";
mostCurrent._line.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 97;BA.debugLine="line.Background=col";
mostCurrent._line.setBackground((android.graphics.drawable.Drawable)(_col.getObject()));
 //BA.debugLineNum = 98;BA.debugLine="Activity.AddView(line, 0, 35dip, Activity.Width,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._line.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35)),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 101;BA.debugLine="Activity.AddView(pager, 0, 35dip + 2dip, Activity";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pager.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35))+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))),mostCurrent._activity.getWidth(),(int) (mostCurrent._activity.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (48))-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (35))-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 102;BA.debugLine="SetButtonText";
_setbuttontext();
 //BA.debugLineNum = 103;BA.debugLine="SetTheme(CurrentTheme)";
_settheme(_currenttheme);
 //BA.debugLineNum = 106;BA.debugLine="Activity.AddMenuItem(\"Add new Page\", \"NewPage\")";
mostCurrent._activity.AddMenuItem("Add new Page","NewPage");
 //BA.debugLineNum = 107;BA.debugLine="Activity.AddMenuItem(\"Delete current Page\", \"Dele";
mostCurrent._activity.AddMenuItem("Delete current Page","DeletePage");
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 115;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 112;BA.debugLine="pager.GotoPage(CurrentPage, False)";
mostCurrent._pager.GotoPage(_currentpage,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 113;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _pl = null;
int _ret = 0;
int _i = 0;
 //BA.debugLineNum = 227;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 228;BA.debugLine="Dim pl As List";
_pl = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 229;BA.debugLine="Dim ret As Int";
_ret = 0;
 //BA.debugLineNum = 230;BA.debugLine="pl.Initialize";
_pl.Initialize();
 //BA.debugLineNum = 233;BA.debugLine="For i = 0 To container.Count - 1";
{
final int step4 = 1;
final int limit4 = (int) (mostCurrent._container.getCount()-1);
for (_i = (int) (0) ; (step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4); _i = ((int)(0 + _i + step4)) ) {
 //BA.debugLineNum = 234;BA.debugLine="pl.Add(container.GetTitle(i))";
_pl.Add((Object)(mostCurrent._container.GetTitle(_i)));
 }
};
 //BA.debugLineNum = 238;BA.debugLine="ret = InputList(pl, \"Choose page\", pager.CurrentP";
_ret = anywheresoftware.b4a.keywords.Common.InputList(_pl,"Choose page",mostCurrent._pager.getCurrentPage(),mostCurrent.activityBA);
 //BA.debugLineNum = 240;BA.debugLine="If ret = DialogResponse.CANCEL Then";
if (_ret==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 241;BA.debugLine="ToastMessageShow(\"Aborted\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Aborted",anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 244;BA.debugLine="pager.GotoPage(ret, True)";
mostCurrent._pager.GotoPage(_ret,anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.PanelWrapper  _createpanel(int _paneltype,String _title) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
de.amberhome.viewpagerexample.main._panelinfo _pi = null;
anywheresoftware.b4a.objects.LabelWrapper _lab = null;
anywheresoftware.b4a.objects.ListViewWrapper _lv = null;
int _i = 0;
 //BA.debugLineNum = 120;BA.debugLine="Sub CreatePanel(PanelType As Int, Title As String)";
 //BA.debugLineNum = 121;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Dim pi As PanelInfo";
_pi = new de.amberhome.viewpagerexample.main._panelinfo();
 //BA.debugLineNum = 124;BA.debugLine="pi.Initialize";
_pi.Initialize();
 //BA.debugLineNum = 125;BA.debugLine="pi.LayoutLoaded = False";
_pi.LayoutLoaded = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 126;BA.debugLine="pi.PanelType = PanelType";
_pi.PanelType = _paneltype;
 //BA.debugLineNum = 128;BA.debugLine="pan.Initialize(\"\")";
_pan.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 129;BA.debugLine="Select PanelType";
switch (BA.switchObjectToInt(_paneltype,_type_hello_world,_type_listview)) {
case 0: {
 //BA.debugLineNum = 131;BA.debugLine="Dim lab As Label";
_lab = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 133;BA.debugLine="pan.Color = Colors.RGB(Rnd(0, 150), Rnd(0,150),";
_pan.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150)),anywheresoftware.b4a.keywords.Common.Rnd((int) (0),(int) (150))));
 //BA.debugLineNum = 134;BA.debugLine="lab.Initialize(\"\")";
_lab.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 135;BA.debugLine="lab.Text = Title";
_lab.setText((Object)(_title));
 //BA.debugLineNum = 136;BA.debugLine="lab.TextSize = 16";
_lab.setTextSize((float) (16));
 //BA.debugLineNum = 137;BA.debugLine="lab.Gravity = Gravity.CENTER";
_lab.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 138;BA.debugLine="pan.AddView(lab, 0, 0, FILL_PARENT, FILL_PARENT";
_pan.AddView((android.view.View)(_lab.getObject()),(int) (0),(int) (0),_fill_parent,_fill_parent);
 break; }
case 1: {
 //BA.debugLineNum = 140;BA.debugLine="Dim lv As ListView";
_lv = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 142;BA.debugLine="lv.Initialize(\"LV\")";
_lv.Initialize(mostCurrent.activityBA,"LV");
 //BA.debugLineNum = 144;BA.debugLine="For i = 0 To 30";
{
final int step19 = 1;
final int limit19 = (int) (30);
for (_i = (int) (0) ; (step19 > 0 && _i <= limit19) || (step19 < 0 && _i >= limit19); _i = ((int)(0 + _i + step19)) ) {
 //BA.debugLineNum = 145;BA.debugLine="lv.AddSingleLine(\"List Entry \" & i)";
_lv.AddSingleLine("List Entry "+BA.NumberToString(_i));
 }
};
 //BA.debugLineNum = 148;BA.debugLine="pan.AddView(lv, 0, 0, FILL_PARENT, FILL_PARENT)";
_pan.AddView((android.view.View)(_lv.getObject()),(int) (0),(int) (0),_fill_parent,_fill_parent);
 break; }
}
;
 //BA.debugLineNum = 151;BA.debugLine="pan.Tag = pi";
_pan.setTag((Object)(_pi));
 //BA.debugLineNum = 152;BA.debugLine="Return pan";
if (true) return _pan;
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return null;
}
public static String  _deletepage_click() throws Exception{
 //BA.debugLineNum = 178;BA.debugLine="Sub DeletePage_Click";
 //BA.debugLineNum = 180;BA.debugLine="container.DeletePage(pager.CurrentPage)";
mostCurrent._container.DeletePage(mostCurrent._pager.getCurrentPage());
 //BA.debugLineNum = 182;BA.debugLine="tabs.NotifyDataChange";
mostCurrent._tabs.NotifyDataChange();
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 45;BA.debugLine="Dim container As AHPageContainer";
mostCurrent._container = new de.amberhome.viewpager.PageContainerAdapter();
 //BA.debugLineNum = 46;BA.debugLine="Dim pager As AHViewPager";
mostCurrent._pager = new de.amberhome.viewpager.ViewPagerWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Dim tabs As AHViewPagerTabs";
mostCurrent._tabs = new de.amberhome.viewpager.ViewPagerTabsWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Dim line As Panel";
mostCurrent._line = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Dim Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Dim spTheme As Spinner";
mostCurrent._sptheme = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static String  _newpage_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _names = null;
int _ret = 0;
 //BA.debugLineNum = 155;BA.debugLine="Sub NewPage_Click";
 //BA.debugLineNum = 156;BA.debugLine="Dim names As List";
_names = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 157;BA.debugLine="Dim ret As Int";
_ret = 0;
 //BA.debugLineNum = 159;BA.debugLine="names.Initialize";
_names.Initialize();
 //BA.debugLineNum = 160;BA.debugLine="names.Add(\"Hello world\")";
_names.Add((Object)("Hello world"));
 //BA.debugLineNum = 161;BA.debugLine="names.Add(\"Settings\")";
_names.Add((Object)("Settings"));
 //BA.debugLineNum = 162;BA.debugLine="names.Add(\"Listview\")";
_names.Add((Object)("Listview"));
 //BA.debugLineNum = 164;BA.debugLine="ret = InputList(names, \"Choose page type\", -1)";
_ret = anywheresoftware.b4a.keywords.Common.InputList(_names,"Choose page type",(int) (-1),mostCurrent.activityBA);
 //BA.debugLineNum = 166;BA.debugLine="If ret = DialogResponse.CANCEL Then";
if (_ret==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 167;BA.debugLine="ToastMessageShow(\"Aborted\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Aborted",anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 170;BA.debugLine="container.AddPageAt(CreatePanel(ret + 1, names.G";
mostCurrent._container.AddPageAt((android.view.View)(_createpanel((int) (_ret+1),BA.ObjectToString(_names.Get(_ret))).getObject()),BA.ObjectToString(_names.Get(_ret)),(int) (mostCurrent._pager.getCurrentPage()+1));
 //BA.debugLineNum = 172;BA.debugLine="tabs.NotifyDataChange";
mostCurrent._tabs.NotifyDataChange();
 //BA.debugLineNum = 174;BA.debugLine="pager.GotoPage(pager.CurrentPage + 1, True)";
mostCurrent._pager.GotoPage((int) (mostCurrent._pager.getCurrentPage()+1),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}
public static String  _pager_pagechanged(int _position) throws Exception{
 //BA.debugLineNum = 187;BA.debugLine="Sub Pager_PageChanged (Position As Int)";
 //BA.debugLineNum = 188;BA.debugLine="Log (\"Page Changed to \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("Page Changed to "+BA.NumberToString(_position));
 //BA.debugLineNum = 189;BA.debugLine="CurrentPage = Position";
_currentpage = _position;
 //BA.debugLineNum = 190;BA.debugLine="SetButtonText";
_setbuttontext();
 //BA.debugLineNum = 191;BA.debugLine="End Sub";
return "";
}
public static String  _pager_pagecreated(int _position,Object _page) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pan = null;
de.amberhome.viewpagerexample.main._panelinfo _pi = null;
 //BA.debugLineNum = 196;BA.debugLine="Sub Pager_PageCreated (Position As Int, Page As Ob";
 //BA.debugLineNum = 197;BA.debugLine="Log (\"Page created \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("Page created "+BA.NumberToString(_position));
 //BA.debugLineNum = 199;BA.debugLine="Dim pan As Panel";
_pan = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 200;BA.debugLine="Dim pi As PanelInfo";
_pi = new de.amberhome.viewpagerexample.main._panelinfo();
 //BA.debugLineNum = 202;BA.debugLine="pan = Page";
_pan.setObject((android.view.ViewGroup)(_page));
 //BA.debugLineNum = 203;BA.debugLine="pi = pan.Tag";
_pi = (de.amberhome.viewpagerexample.main._panelinfo)(_pan.getTag());
 //BA.debugLineNum = 205;BA.debugLine="Select pi.PanelType";
switch (BA.switchObjectToInt(_pi.PanelType,_type_settings)) {
case 0: {
 //BA.debugLineNum = 207;BA.debugLine="If Not(pi.LayoutLoaded) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_pi.LayoutLoaded)) { 
 //BA.debugLineNum = 208;BA.debugLine="pan.LoadLayout(\"settings\")";
_pan.LoadLayout("settings",mostCurrent.activityBA);
 //BA.debugLineNum = 209;BA.debugLine="pi.LayoutLoaded = True";
_pi.LayoutLoaded = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 210;BA.debugLine="spTheme.Add(\"Dark\")";
mostCurrent._sptheme.Add("Dark");
 //BA.debugLineNum = 211;BA.debugLine="spTheme.Add(\"Light\")";
mostCurrent._sptheme.Add("Light");
 //BA.debugLineNum = 212;BA.debugLine="spTheme.SelectedIndex = CurrentTheme";
mostCurrent._sptheme.setSelectedIndex(_currenttheme);
 };
 break; }
}
;
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
public static String  _pager_pagedestroyed(int _position,Object _page) throws Exception{
 //BA.debugLineNum = 218;BA.debugLine="Sub Pager_PageDestroyed (Position As Int, Page As";
 //BA.debugLineNum = 219;BA.debugLine="Log(\"Page destroyed \" & Position)";
anywheresoftware.b4a.keywords.Common.Log("Page destroyed "+BA.NumberToString(_position));
 //BA.debugLineNum = 220;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 26;BA.debugLine="Dim TYPE_HELLO_WORLD As Int : TYPE_HELLO_WORLD =";
_type_hello_world = 0;
 //BA.debugLineNum = 26;BA.debugLine="Dim TYPE_HELLO_WORLD As Int : TYPE_HELLO_WORLD =";
_type_hello_world = (int) (1);
 //BA.debugLineNum = 27;BA.debugLine="Dim TYPE_SETTINGS As Int : TYPE_SETTINGS = 2";
_type_settings = 0;
 //BA.debugLineNum = 27;BA.debugLine="Dim TYPE_SETTINGS As Int : TYPE_SETTINGS = 2";
_type_settings = (int) (2);
 //BA.debugLineNum = 28;BA.debugLine="Dim TYPE_LISTVIEW As Int : TYPE_LISTVIEW = 3";
_type_listview = 0;
 //BA.debugLineNum = 28;BA.debugLine="Dim TYPE_LISTVIEW As Int : TYPE_LISTVIEW = 3";
_type_listview = (int) (3);
 //BA.debugLineNum = 30;BA.debugLine="Dim FILL_PARENT As Int : FILL_PARENT = -1";
_fill_parent = 0;
 //BA.debugLineNum = 30;BA.debugLine="Dim FILL_PARENT As Int : FILL_PARENT = -1";
_fill_parent = (int) (-1);
 //BA.debugLineNum = 31;BA.debugLine="Dim WRAP_CONTENT As Int : WRAP_CONTENT = -2";
_wrap_content = 0;
 //BA.debugLineNum = 31;BA.debugLine="Dim WRAP_CONTENT As Int : WRAP_CONTENT = -2";
_wrap_content = (int) (-2);
 //BA.debugLineNum = 34;BA.debugLine="Type PanelInfo (PanelType As Int, LayoutLoaded As";
;
 //BA.debugLineNum = 36;BA.debugLine="Dim CurrentTheme As Int : CurrentTheme = 0";
_currenttheme = 0;
 //BA.debugLineNum = 36;BA.debugLine="Dim CurrentTheme As Int : CurrentTheme = 0";
_currenttheme = (int) (0);
 //BA.debugLineNum = 37;BA.debugLine="Dim CurrentPage As Int : CurrentPage = 0";
_currentpage = 0;
 //BA.debugLineNum = 37;BA.debugLine="Dim CurrentPage As Int : CurrentPage = 0";
_currentpage = (int) (0);
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _setbuttontext() throws Exception{
 //BA.debugLineNum = 222;BA.debugLine="Sub SetButtonText";
 //BA.debugLineNum = 223;BA.debugLine="Button1.Text = \"Page \" & pager.CurrentPage";
mostCurrent._button1.setText((Object)("Page "+BA.NumberToString(mostCurrent._pager.getCurrentPage())));
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public static String  _settheme(int _theme) throws Exception{
 //BA.debugLineNum = 248;BA.debugLine="Sub SetTheme(Theme As Int)";
 //BA.debugLineNum = 249;BA.debugLine="Select Theme";
switch (_theme) {
case 0: {
 //BA.debugLineNum = 251;BA.debugLine="tabs.Color = Colors.Black";
mostCurrent._tabs.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 252;BA.debugLine="tabs.BackgroundColorPressed = Colors.Blue";
mostCurrent._tabs.setBackgroundColorPressed(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 253;BA.debugLine="tabs.LineColorCenter = Colors.Green";
mostCurrent._tabs.setLineColorCenter(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 254;BA.debugLine="tabs.TextColor = Colors.LightGray";
mostCurrent._tabs.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 255;BA.debugLine="tabs.TextColorCenter = Colors.Green";
mostCurrent._tabs.setTextColorCenter(anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 256;BA.debugLine="line.Color = Colors.Green";
mostCurrent._line.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);
 break; }
case 1: {
 //BA.debugLineNum = 258;BA.debugLine="tabs.Color = Colors.White";
mostCurrent._tabs.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 259;BA.debugLine="tabs.BackgroundColorPressed = Colors.Blue";
mostCurrent._tabs.setBackgroundColorPressed(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 260;BA.debugLine="tabs.LineColorCenter = Colors.DarkGray";
mostCurrent._tabs.setLineColorCenter(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 261;BA.debugLine="tabs.TextColor = Colors.LightGray";
mostCurrent._tabs.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 262;BA.debugLine="tabs.TextColorCenter = Colors.DarkGray";
mostCurrent._tabs.setTextColorCenter(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 263;BA.debugLine="line.Color = Colors.DarkGray";
mostCurrent._line.setColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 break; }
}
;
 //BA.debugLineNum = 265;BA.debugLine="End Sub";
return "";
}
public static String  _sptheme_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 267;BA.debugLine="Sub spTheme_ItemClick (Position As Int, Value As O";
 //BA.debugLineNum = 268;BA.debugLine="CurrentTheme = Position";
_currenttheme = _position;
 //BA.debugLineNum = 269;BA.debugLine="SetTheme(CurrentTheme)";
_settheme(_currenttheme);
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
return "";
}
}
