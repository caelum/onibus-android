package br.com.caelum.ondeestaobusao.generic.fragment;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.activity.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GenericFragmentManager {
	private List<ViewState> stack = new ArrayList<ViewState>();
	private final Activity activity;
	private GenericFragment currentFragment;
	private TextView fragmentName;
	private View mapView;

	public GenericFragmentManager(Activity activity) {
		this.activity = activity;
		fragmentName = (TextView) this.activity.findViewById(R.id.fragment_name);
	}
	
	public GenericFragmentManager replace(int fragmentId, GenericFragment fragment) {
		return this.replace(fragmentId, fragment, false);
	}
	
	public GenericFragmentManager replace(int fragmentId, GenericFragment fragment, boolean addToBackStack) {
		ViewGroup parent  = (ViewGroup) activity.findViewById(fragmentId);

		if (currentFragment != null) currentFragment.finish();
		
		if (addToBackStack) {
			stack.add(0, new ViewState(currentFragment, parent));
		}
		
		parent.removeAllViews();
		
		View view;
		if (fragment instanceof GenericMapFragment)  {
			if (mapView == null) {
				mapView = fragment.onCreateView(activity, parent);
			}
			view = mapView;
		} else {
			view = fragment.onCreateView(activity, parent);
		}
		
		fragmentName.setText(fragment.getName());
		parent.addView(view);
		currentFragment = fragment;
		currentFragment.resume();
		
		return this;
	}

	public boolean back() {
		if (!stack.isEmpty()) {
			currentFragment.finish();
			ViewState state = stack.remove(0);
			state.restoreLast();
			fragmentName.setText(state.getFragment().getName());
			return true;
		}
		
		return false;
	}
	
	private class ViewState {
		private GenericFragment fragment;
		private ViewGroup parent;
		private List<View> currentChildren = new ArrayList<View>();
		
		public ViewState(GenericFragment fragment, ViewGroup parent) {
			this.fragment = fragment;
			this.parent = parent;
			extractChildren(this.parent);
		}
		
		private void extractChildren(ViewGroup group) {
			for (int i = 0; i < group.getChildCount(); i++) {
				currentChildren.add(group.getChildAt(i));
			}
		}

		public void restoreLast() {
			parent.removeAllViews();
			for (View view : currentChildren) {
				parent.addView(view);
			}
			fragment.resume();
		}
		
		public GenericFragment getFragment() {
			return fragment;
		}
	}
	
}
