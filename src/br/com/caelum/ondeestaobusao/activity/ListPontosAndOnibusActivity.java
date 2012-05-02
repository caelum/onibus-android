package br.com.caelum.ondeestaobusao.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import br.com.caelum.ondeestaobusao.adapter.PontosEOnibusAdapter;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.AsyncResultDealer;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;

public class ListPontosAndOnibusActivity extends Activity implements AsyncResultDealer<List<Ponto>> {
	private ExpandableListView lvPontos;
	private Coordenada atual;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_ponto);

		lvPontos = (ExpandableListView) findViewById(R.id.listPonto);

		atualizar();
	}

	public void dealWithResult(final List<Ponto> pontos) {
		lvPontos.setAdapter(new PontosEOnibusAdapter(pontos, this));

		lvPontos.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				Onibus onibus = pontos.get(groupPosition).getOnibuses().get(childPosition);

				Intent intent = new Intent(ListPontosAndOnibusActivity.this, MostraRotaActivity.class);
				intent.putExtra(Extras.ONIBUS, onibus);
				intent.putExtra(Extras.MINHA_LOCALIZACAO, atual);

				startActivity(intent);

				return false;
			}
		});

		hideProgressBar();
	}

	private void hideProgressBar() {
		lvPontos.setVisibility(View.VISIBLE);
		findViewById(R.id.progress_bar).setVisibility(View.GONE);
	}

	public void atualizaLista(View view) {
		findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
		lvPontos.setVisibility(View.GONE);
		atualizar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.procura_no_mapa) {
			Intent intent = new Intent(ListPontosAndOnibusActivity.this, MostraPontosActivity.class);
			intent.putExtra(Extras.MINHA_LOCALIZACAO, atual);

			startActivity(intent);
			return false;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (this.atual != null) {
			MenuItem item = menu.findItem(R.id.procura_no_mapa);
			item.setEnabled(true);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}

	private void atualizar() {
		new GPSControl(this).execute();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return this.lvPontos;
	}

	public void setAtual(Coordenada atual) {
		this.atual = atual;
	}

	@Override
	public void dealWithError() {
		new AlertDialogBuilder(this).build().show();
	}
}
