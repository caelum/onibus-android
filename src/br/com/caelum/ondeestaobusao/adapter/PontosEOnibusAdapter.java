package br.com.caelum.ondeestaobusao.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;

public class PontosEOnibusAdapter extends BaseExpandableListAdapter{
	private final Activity activity;
	private final List<Ponto> pontos;

	public PontosEOnibusAdapter(List<Ponto> pontos, Activity activity) {
		this.pontos = pontos;
		this.activity = activity;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return pontos.get(groupPosition).getOnibuses().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return pontos.get(groupPosition).getOnibuses().get(childPosition).getId();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		Onibus onibus = pontos.get(groupPosition).getOnibuses().get(childPosition);
		
		TextView view = (TextView) activity.getLayoutInflater().inflate(R.layout.item_onibus, null);
		view.setText(onibus.getNome());
		
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return pontos.get(groupPosition).getOnibuses().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return pontos.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return pontos.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return pontos.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		Ponto ponto = pontos.get(groupPosition);
		
		View view = activity.getLayoutInflater().inflate(R.layout.item_ponto, null);
		
		TextView nomePonto = (TextView) view.findViewById(R.id.nomePonto);
		TextView distancia = (TextView) view.findViewById(R.id.distancia);
		
		nomePonto.setText(ponto.getDescricao());
		distancia.setText(ponto.getDistancia());
		
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
