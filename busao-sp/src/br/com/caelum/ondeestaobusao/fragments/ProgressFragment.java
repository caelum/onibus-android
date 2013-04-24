package br.com.caelum.ondeestaobusao.fragments;

import br.com.caelum.ondeestaobusao.activity.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgressFragment extends Fragment{
	
	private TextView mensagemTv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.progress_bar, null);
		
		this.mensagemTv = (TextView) view.findViewById(R.id.progress_text);
		
		if (getArguments()!=null && getArguments().getInt("messageId", -1) != -1) {
			mudaMensagem(getArguments().getInt("messageId"));
		}
		
		return view;
	}
	
	public static ProgressFragment comMensagem(int stringId) {
		ProgressFragment fragment = new ProgressFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("messageId", stringId);
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	public void mudaMensagem(int stringId) {
		String novaMensagem = getActivity().getResources().getString(stringId);
		mensagemTv.setText(novaMensagem);
	}

}
