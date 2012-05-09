package br.com.caelum.ondeestaobusao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;

public class ListOnibusActivity extends Activity {
	private ListView lvOnibuses;
	private Ponto ponto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_onibus);

		ponto = (Ponto) getIntent().getSerializableExtra(Extras.PONTO);
		
		TextView tituloPonto = (TextView) findViewById(R.id.titulo_ponto_onibus);
		tituloPonto.setText(ponto.getDescricao());
		
		lvOnibuses = (ListView) findViewById(R.id.list_onibuses);
		
		mountAdapter();
	}

	private void mountAdapter() {
		ArrayAdapter<Onibus> onibusAdapter = new ArrayAdapter<Onibus>(this, R.layout.item_onibus, R.id.nome_onibus, ponto.getOnibuses());
		lvOnibuses.setAdapter(onibusAdapter);

		lvOnibuses.setOnItemClickListener(new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> pontos, View view, int pos, long pres) {
				Onibus onibus = (Onibus) pontos.getItemAtPosition(pos);
				
				Intent intent = new Intent(ListOnibusActivity.this, MostraRotaActivity.class);
				intent.putExtra(Extras.ONIBUS, onibus);
				intent.putExtra(Extras.LOCALIZACAO, ponto.getCoordenada());
				
				startActivity(intent);
			}
		});

		hideProgressBar();
	}

	private void hideProgressBar() {
		lvOnibuses.setVisibility(View.VISIBLE);
		findViewById(R.id.progress_bar).setVisibility(View.GONE);
	}

}