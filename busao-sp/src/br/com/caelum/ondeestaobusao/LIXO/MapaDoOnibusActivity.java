//package br.com.caelum.ondeestaobusao.LIXO;
//
//import java.util.List;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.os.AsyncTask;
//import android.os.AsyncTask.Status;
//import android.os.Bundle;
//import br.com.caelum.ondeestaobusao.activity.R;
//import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
//import br.com.caelum.ondeestaobusao.cache.Cache;
//import br.com.caelum.ondeestaobusao.constants.Extras;
//import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
//import br.com.caelum.ondeestaobusao.gps.LocationObserver;
//import br.com.caelum.ondeestaobusao.model.Coordenada;
//import br.com.caelum.ondeestaobusao.model.Onibus;
//import br.com.caelum.ondeestaobusao.model.Ponto;
//import br.com.caelum.ondeestaobusao.model.Veiculo;
//import br.com.caelum.ondeestaobusao.progressbar.ProgressBarAdministrator;
//import br.com.caelum.ondeestaobusao.task.PontosDoOnibusTask;
//import br.com.caelum.ondeestaobusao.task.VeiculoEmTempoRealTask;
//import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
//import br.com.caelum.ondeestaobusao.util.CancelableAssynTask;
//import br.com.caelum.ondeestaobusao.util.GeoCoderUtil;
//import br.com.caelum.ondeestaobusao.util.Mapa;
//import br.com.caelum.ondeestaobusao.widgets.actionbar.BusaoNoMapa;
//
//import com.actionbarsherlock.app.ActionBar;
//import com.actionbarsherlock.app.ActionBar.Tab;
//import com.actionbarsherlock.app.SherlockFragmentActivity;
//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;
//
//public class MapaDoOnibusActivity extends SherlockFragmentActivity implements AsyncResultDelegate<List<Ponto>>,
//		LocationObserver {
//
//	private Mapa mapa;
//	private Onibus onibus;
//	private BusaoApplication application;
//	private GeoCoderUtil geoCoderUtil;
//	private AsyncTask<Integer, Void, List<Veiculo>> veiculosTask;
//	private AsyncTask<Long, Void, List<Ponto>> pontosDoOnibusTask;
//	private ActionBar actionBar;
//	private AlertDialog.Builder dialog;
//	private List<Ponto> pontos;
//	private List<Veiculo> veiculos;
//	private ProgressBarAdministrator progressBarAdministrator;
//	private Cache cache;
//	private Coordenada coordenada;
//
//	@Override
//	protected void onCreate(Bundle bundle) {
//		super.onCreate(bundle);
//		setContentView(R.layout.mapa);
//		application = (BusaoApplication) getApplication();
//		geoCoderUtil = new GeoCoderUtil(this);
//	}
//	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		
////		progressBarAdministrator = new ProgressBarAdministrator(this.get);
////		progressBarAdministrator.showWithText(R.string.colocando_pontos_mapa);
//
//		onibus = (Onibus) getIntent().getSerializableExtra(Extras.ONIBUS);
//
//		application.getCentralNotificacoes().registerObserver(this);
//		cache = application.getCache();
//
//		initializeSherlockActionBar();
//	}
//
//	private void initializeSherlockActionBar() {
//		this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		this.getSupportActionBar().setTitle(onibus.getLetreiro());
//		this.getSupportActionBar().setSubtitle(onibus.getSentido().toString());
//		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		
//		createTab();
//	}
//
//	private void createTab() {
//		BusaoNoMapaListener listener = new BusaoNoMapaListener(this, mapa);
//		actionBar = this.getSupportActionBar();
//
//		actionBar.removeAllTabs();
//
//		Tab itinerario = actionBar.newTab();
//		itinerario.setText("Itinerarios");
//		itinerario.setTag(BusaoNoMapa.ITINERARIO);
//		itinerario.setTabListener(listener);
//		actionBar.addTab(itinerario, true);
//
//		Tab tempoReal = actionBar.newTab();
//		tempoReal.setText("Tempo real");
//		tempoReal.setTag(BusaoNoMapa.TEMPO_REAL);
//		tempoReal.setTabListener(listener);
//		actionBar.addTab(tempoReal);
//	}
//
//
//	@Override
//	public void callback(Coordenada coordenada) {
//		//TODO colocar um PIN com a nossa posicao no mapa
//		
//		pontosDoOnibusTask = new PontosDoOnibusTask(cache, this).execute(onibus.getId());
//		veiculosTask = new VeiculoEmTempoRealTask(assync).execute(onibus.getCodigoGPS());
//	}
//
//	@Override
//	public void dealWithResult(List<Ponto> pontos) {
//		this.pontos = pontos;
//		exibePontosNoMapa();
//	}
//
//	public void exibePontosNoMapa() {
//		boolean actionBar = verifyActionBar(BusaoNoMapa.ITINERARIO);
//		if (pontosDoOnibusTask != null && pontosDoOnibusTask.getStatus().equals(Status.RUNNING) && actionBar) {
////			progressBarAdministrator.showWithText(R.string.colocando_pontos_mapa);
//		}
//
//		if (actionBar) {
//			//TODO colcor os itens no mapa
//			
//			mapa.adicionaCamada(null);
//			mapa.centralizaNa(coordenada);
//			mapa.redesenha();
//
////			progressBarAdministrator.hide();
//		}
//	}
//
//	public void exibeVeiculosNoMapa() {
//		boolean actionBar = verifyActionBar(BusaoNoMapa.TEMPO_REAL);
//
//		if (veiculosTask != null && veiculosTask.getStatus().equals(Status.RUNNING) && actionBar) {
//			progressBarAdministrator.showWithText(R.string.colocando_veiculos_mapa);
//		}
//		
//		mapa.limpa();
//
//		if (actionBar) {
//			// TODO colocar veiculos no mapa
//			mapa.adicionaCamada(null);
//			progressBarAdministrator.hide();
//		}
//	}
//
//	private boolean verifyActionBar(BusaoNoMapa busaoNoMapa) {
//		return actionBar != null && actionBar.getSelectedTab() != null
//				&& busaoNoMapa.equals(actionBar.getSelectedTab().getTag());
//	}
//
//	public void error() {
//		if (dialog == null) {
//			cancelAssyncTasks();
//			dialog = AlertDialogBuilder.builder(this);
//			dialog.setPositiveButton(this.getResources().getString(R.string.tente_novamente), new OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					pontosDoOnibusTask = new PontosDoOnibusTask(cache, MapaDoOnibusActivity.this).execute(onibus.getId());
//					veiculosTask = new VeiculoEmTempoRealTask(assync).execute(onibus.getCodigoGPS());
//				}
//			});
//			dialog.setNegativeButton(this.getResources().getString(R.string.cancelar), new OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					onBackPressed();
//				}
//
//			});
//			dialog.show();
//		}
//	}
//
//	@Override
//	public void dealWithError() {
//		this.error();
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		progressBarAdministrator.hide();
//		application.getCentralNotificacoes().unRegisterObserver(this);
//
//		cancelAssyncTasks();
//
//		this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//
//		mapa.limpa();
//	}
//
//	private void cancelAssyncTasks() {
//		CancelableAssynTask.cancel(pontosDoOnibusTask);
//		CancelableAssynTask.cancel(veiculosTask);
//	}
//
//	private AsyncResultDelegate<List<Veiculo>> assync = new AsyncResultDelegate<List<Veiculo>>() {
//
//		@Override
//		public void dealWithResult(List<Veiculo> vecs) {
//			veiculos = vecs;
//			exibeVeiculosNoMapa();
//		}
//
//		@Override
//		public void dealWithError() {
//			MapaDoOnibusActivity.this.error();
//		}
//	};
//
//	public boolean onOptionsItemSelected(MenuItem item) {
////		if (item.getItemId() == android.R.id.home) {
////			onBackPressed();
////			return true;
////		} 
//		
//		if (item.getItemId() == R.id.menu_atualizar) {
//			if (!CancelableAssynTask.isRunning(veiculosTask)) {
//				boolean actionBar = verifyActionBar(BusaoNoMapa.TEMPO_REAL);
//
//				veiculosTask = new VeiculoEmTempoRealTask(assync).execute(onibus.getCodigoGPS());
//				if (veiculosTask != null && veiculosTask.getStatus().equals(Status.RUNNING) && actionBar) {
//					progressBarAdministrator.showWithText(R.string.colocando_veiculos_mapa);
//				}
//				return true;
//			}
//		}
//		
//		return super.onOptionsItemSelected(item);
//	};
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getSupportMenuInflater().inflate(R.menu.menu_refresh_busao, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//}
