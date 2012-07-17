package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.adapter.PontosEOnibusAdapter;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.gps.LocationObserver;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
import br.com.caelum.ondeestaobusao.widget.PontoExpandableListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class PontosProximosFragment extends SherlockFragment implements AsyncResultDelegate<List<Ponto>>, LocationObserver {
	private PontoExpandableListView pontosExpandableListView;
	private List<Ponto> pontos;
	private AsyncTask<Coordenada, Void, List<Ponto>> pontosEOnibusTask;
	private Coordenada coordenada;

	public PontosProximosFragment() {
		super();
		setHasOptionsMenu(true);	
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
		if (pontosExpandableListView == null) {
			pontosExpandableListView = (PontoExpandableListView) inflater.inflate(R.layout.pontos_e_onibuses, parent,
					false);
			hide();
			BusaoApplication application = (BusaoApplication) getActivity().getApplication();
			application.getLocation().registerObserver(this);
		}
		return pontosExpandableListView;
	}


	public void hide() {
		pontosExpandableListView.setVisibility(View.GONE);
	}

	public void callback(Coordenada coordenada) {
		this.coordenada = coordenada;
		pontosEOnibusTask = new PontosEOnibusTask(this).execute(coordenada);
		
		BusaoApplication application = (BusaoApplication) getActivity().getApplication();
		application.getProgressBar().changedText(R.string.buscando_pontos_proximos);
	}

	@Override
	public void dealWithResult(final List<Ponto> pontos) {
		this.pontos = pontos;
		pontosExpandableListView.setAdapter(new PontosEOnibusAdapter(pontos, getActivity()));

		pontosExpandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Onibus onibus = pontos.get(groupPosition).getOnibuses().get(childPosition);
				BusaoActivity activity = ((BusaoActivity) getActivity());
				
				MapaDoOnibusFragment mapaDoOnibusFragment = (MapaDoOnibusFragment) activity.getSupportFragmentManager().findFragmentByTag("mapaDoOnibusFragment");
				
				if (mapaDoOnibusFragment == null) {
					mapaDoOnibusFragment = new MapaDoOnibusFragment();
				}
				
				Bundle bundle = new Bundle();
				bundle.putSerializable(Extras.ONIBUS, onibus);

				mapaDoOnibusFragment.setArguments(bundle);
				
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, mapaDoOnibusFragment, "mapaDoOnibusFragment").addToBackStack("mapaDoOnibusFragment").commit();
				
				return false;
			}
		});
		pontosExpandableListView.setVisibility(View.VISIBLE);
		BusaoApplication application = (BusaoApplication) getActivity().getApplication();
		application.getProgressBar().hide();		
	}

	@Override
	public void dealWithError() {
		final Activity activity = getActivity();
		AlertDialog dialog = AlertDialogBuilder.builder(activity);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.tente_novamente), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				pontosEOnibusTask = new PontosEOnibusTask(PontosProximosFragment.this).execute(coordenada);
			}

		});
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, activity.getResources().getString(R.string.cancelar), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				activity.onBackPressed();
			}

		});
		dialog.show();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		super.onDestroyOptionsMenu();
		
		if (pontosEOnibusTask != null && Status.RUNNING.equals(pontosEOnibusTask.getStatus())) {
			pontosEOnibusTask.cancel(true);
		}
		
		ViewGroup parent = (ViewGroup) pontosExpandableListView.getParent();
		if (parent != null) {
			parent.removeView(pontosExpandableListView);
		}
	}

	public List<Ponto> getPontos() {
		return pontos;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getSherlockActivity().getSupportActionBar().setTitle(getString(R.string.pontos_proximos));
		getSherlockActivity().getSupportActionBar().setSubtitle(null);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_split_principal, menu);
	}
	
}
