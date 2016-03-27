package com.ecommerce.activity.personal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecommerce.R;
import com.ecommerce.model.StreamEntity;
import com.ecommerce.model.TAppRelease;
import com.ecommerce.service.DownloadAppService;
import com.ecommerce.service.ServiceUtils;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.Constants;
import com.ecommerce.utils.FileUtil;
import com.ecommerce.utils.VersionUtils;

public class UpgradeVerDialog extends DialogFragment implements
		android.view.View.OnClickListener {
	private static final String TAG = UpgradeVerDialog.class.getSimpleName();
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private File downloadFile;
	private ProgressBar pgbUpgrade;
	private TextView tvUpgradePercent;
	private TextView tvUpgradeContent;
	private TextView tvPackageSize;
	private Button btnUpgradeVerok;
	private Button btnUpgradeVerCancel;
	private TAppRelease appRelease;
	private DownloadAppService downloadAppService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.update_ver_dialog, container);
		initParameters(view);
		initComponents(view);
		initData(view);

		return view;
	}

	private void initParameters(View view) {
		downloadAppService = ServiceUtils.getInstance(
				getActivity().getBaseContext()).getDownloadAppService();
		Bundle args = getArguments();
		if (args != null) {
			appRelease = (TAppRelease) args.getSerializable("key");
		}
	}

	private void initComponents(View view) {
		btnUpgradeVerok = (Button) view.findViewById(R.id.btn_upgrade_ver_ok);
		btnUpgradeVerok.setOnClickListener(this);

		btnUpgradeVerCancel = (Button) view
				.findViewById(R.id.btn_upgrade_ver_cancel);
		btnUpgradeVerCancel.setOnClickListener(this);

		tvPackageSize = (TextView) view.findViewById(R.id.tv_package_size);
		tvUpgradeContent = (TextView) view
				.findViewById(R.id.tv_upgrade_content);

		pgbUpgrade = (ProgressBar) view.findViewById(R.id.pgb_upgrade);
		pgbUpgrade.setMax(100);

		tvUpgradePercent = (TextView) view
				.findViewById(R.id.tv_upgrade_percent);

		if (appRelease != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("当前版本："
					+ VersionUtils.getVerName(getActivity().getBaseContext())
					+ "\r\n");
			sb.append("最新版本：" + appRelease.getName() + "\r\n");
			sb.append("文件大小："
					+ FileUtil.byteToMi(+appRelease.getContentLength())
					+ "M" + "\r\n");
			sb.append("本次升级内容：" + appRelease.getUpgradeDesc() + "\r\n");
			tvUpgradeContent.setText(sb.toString());
		}

	}

	private void initData(View view) {
		String downloadFilePath = FileUtil.getSDPath()
				+ ConfigReader.getInstance(getActivity().getBaseContext())
						.getProperties("upgrade_path") + appRelease.getName()
				+ ".apk";
		downloadFile = new File(downloadFilePath);
		if (!(downloadFile.getParentFile().exists())) {
			downloadFile.getParentFile().mkdirs();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_upgrade_ver_ok:
			downLoadApk(appRelease, downloadFile);
			break;
		case R.id.btn_upgrade_ver_cancel:
			executorService.shutdown();
			dismiss();
			break;
		default:
			break;
		}

	}

	/*private void getAppRelease() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				JsonObj jsonObj = null;
				try {
					jsonObj = downloadAppService
							.getAppRelease(Constants.ANDROID_IDENTITY);
				} catch (Exception e) {
					msg.what = Constants.MSG_WHAT_FAILED;
					jsonObj = new JsonObj(Constants.AJAX_STATUS_FAILED,
							ExceptionHandler.getMessage(e).toString());
				}
				msg.obj = jsonObj;
	//				handlerGetAppRelease.sendMessage(msg);
			}
		});
	}*/

	public void downLoadApk(final TAppRelease appRelease,
			final java.io.File downloadFile) {
		executorService.submit(new Runnable() {
			public void run() {
				StreamEntity streamEntity = downloadAppService
						.getStreamEntity(appRelease.getAppPath());
				copy(streamEntity.getInputStream(),
						downloadFile.getAbsolutePath(),
						streamEntity.getContentLength());
				handler.sendEmptyMessage(Constants.MSG_WHAT_SUCCESS);
			}
		});
	}

	public void copy(InputStream inputStream, String dst, long contentLength) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(inputStream, 16 * 1024);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						16 * 1024);
				byte[] buffer = new byte[16 * 1024];
				int num = 0;
				int count = 0;
				while ((num = in.read(buffer)) != -1) {
					count += num;
					int progress = (int) (((float) count / contentLength) * 100);
					Message msg = new Message();
					msg.what = Constants.MSG_WHAT_PROCESSING;
					Bundle args = new Bundle();
					args.putInt("progress", progress);
					msg.setData(args);
					handler.sendMessage(msg);
					Log.d(TAG, "--progress--" + progress);
					out.write(buffer, 0, num);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void updateProgress(int progress) {
		pgbUpgrade.setProgress(progress);
		tvUpgradePercent.setText(progress + "%");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_WHAT_SUCCESS:
				dismiss();
				VersionUtils.updateVer(getActivity().getBaseContext(),
						downloadFile);
				break;
			case Constants.MSG_WHAT_PROCESSING:
				int progress = msg.getData().getInt("progress");
				updateProgress(progress);
				break;
			/*case Constants.MSG_WHAT_FAILED:
				Log.d(TAG,
						"--error messgage--"
								+ msg.getData().getString(Constants.NOTIFY_MSG));
				Toast.makeText(getActivity().getBaseContext(),
						msg.getData().getString(Constants.NOTIFY_MSG),
						Toast.LENGTH_LONG).show();
				break;*/
			default:
				break;
			}

		}
	};

}
