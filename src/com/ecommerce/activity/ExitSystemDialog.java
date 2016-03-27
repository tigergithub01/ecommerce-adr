package com.ecommerce.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ecommerce.R;

public class ExitSystemDialog extends DialogFragment implements
		android.view.View.OnClickListener,FragmentEventListener {
	private static final String TAG =  ExitSystemDialog.class.getSimpleName();
	private Button btnExitok;
	private Button btnExitCancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.exit_system_dialog, container);
		initParameters(view);
		initComponents(view);
		initData(view);
		return view;
	}

	@Override
	public void initParameters(View view) {
	}

	@Override
	public void initComponents(View view) {
		btnExitok = (Button) view.findViewById(R.id.btn_exit_system_ok);
		btnExitok.setOnClickListener(this);

		btnExitCancel = (Button) view.findViewById(R.id.btn_exit_system_cancel);
		btnExitCancel.setOnClickListener(this);

	}

	@Override
	public void initData(View view) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exit_system_ok:
			/*NotifyUtils.removeNotification(Constants.NOTITICATION_AUDIO_ID,
					getActivity().getBaseContext());*/
			getActivity().finish();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		case R.id.btn_exit_system_cancel:
			dismiss();
			break;
		default:
			break;
		}

	}

}
