package com.taxiapp.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.taxiapp.model.business.AppUpdate;
import com.taxiapp.utils.PreferencesUtil;

public class PackageChangedReciever extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.d("Package", "Reciever called");
		if (PreferencesUtil
				.contains(context, PreferencesUtil.APP_UPDATE_RESULT)) {
			Log.d("Package", "Reciever called");
			AppUpdate details = PreferencesUtil.get(context,
					PreferencesUtil.APP_UPDATE_RESULT, AppUpdate.class);
			details.setUpdatePending(false);
			PreferencesUtil.put(context, PreferencesUtil.APP_UPDATE_RESULT,
					details);

		}
	}
}
