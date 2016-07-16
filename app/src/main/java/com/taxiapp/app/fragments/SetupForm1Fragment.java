package com.taxiapp.app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.taxiapp.app.activities.SetupActivity;
import com.taxiapp.model.UserOtpToken;
import com.taxiapp.utils.AppConstants;
import com.taxiapp.utils.PreferencesUtil;
import com.taxiapp.utils.ProgressAsyncTask;
import com.taxiapp.utils.Utils;
import com.taxiapp.vendor.app.R;

import org.apache.commons.lang.StringUtils;

/**
 * Created by amu on 26/04/15.
 */
public class SetupForm1Fragment extends Fragment {


    public static final String NUM_RETRIES = "numRetries";
    public static final String APP_LOCK_TIME = "appLockTime";
    private EditText phoneNumber;
    private Handler handler;
    private long appLockTime;
    private TextView error;
    private Button nextButton;
    private Runnable appLockChecker = new Runnable() {
        @Override
        public void run() {
            if (appLockTime + AppConstants.APP_LOCK_TIME_INTERVAL > System.currentTimeMillis()) {
                handler.postDelayed(appLockChecker, 2000);
                error.setVisibility(View.VISIBLE);
                nextButton.setEnabled(false);
            } else {
                handler.removeCallbacks(appLockChecker);
                error.setVisibility(View.GONE);
                nextButton.setEnabled(true);
                PreferencesUtil.remove(getActivity(), APP_LOCK_TIME);
                PreferencesUtil.remove(getActivity(), NUM_RETRIES);
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_setup_1, container, false);

        setupForm(rootView);
        preProcessForm();
        return rootView;
    }

    private void setupForm(ViewGroup rootView) {
        // setExitTransition(TransitionU);

        error = (TextView) rootView.findViewById(R.id.error);

        phoneNumber = (EditText) rootView.findViewById(R.id.setup_phone);

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && phoneNumber.getError() != null)
                    phoneNumber.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nextButton = (Button) rootView.findViewById(R.id.setup_next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = phoneNumber.getText().toString();
                if (StringUtils.isBlank(number) || StringUtils.length(number) < 10) {
                    phoneNumber.requestFocus();
                    phoneNumber.setError("Mobile number is invalid");
                    return;
                }

                confirmToSendSMS(number);
            }
        });


        handler = new Handler();
    }

    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(appLockChecker);
    }

    private void preProcessForm() {
        appLockTime = PreferencesUtil.getLong(getActivity(), APP_LOCK_TIME, -1);
        if (appLockTime != -1) {
            handler.post(appLockChecker);
        }
    }

    private void confirmToSendSMS(final String number) {
        Utils.showPrompt(getActivity(), "Confirm Number", "Click OK to receive OTP on " + number, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == Dialog.BUTTON_POSITIVE) {
                    new SetupAsyncTask(getActivity()).execute(number);
                }
            }
        }, "OK", "Cancel");

    }

    private class SetupAsyncTask extends ProgressAsyncTask {

        private String number;
        private String message = "No response from Server";

        public SetupAsyncTask(Context ctx) {
            super(ctx);
        }

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                number = strings[0];

                if (!Utils.hasNetwork(ctx)) {
                    message = "Please connect to internet and retry";
                    return NO_NETWORK;
                }

                //NetResponse response = APIUtils.receiveOtp(number);
                //PreferencesUtil.putString(ctx, AppConstants.PREFS_KEY, "1234");
//                if (response == null) {
//                    return FAILED;
//                }

//                if (response.getStatusCode() == APIStatusCodes.ERR_NO_RESULT.getCode()) {
//                    message = response.getStatusDetails();
//                    return FAILED;
//                }
//
//                if (response.getStatusCode() != APIStatusCodes.SUCCESS.getCode()) {
//                    message = "Phone number is invalid. Please try again.";
//                    return FAILED;
//                }
//
//
//                if (StringUtils.isBlank(response.getJwt())) {
//                    message = "Could not authenticate with Server";
//                    return FAILED;
//                }


                //UserOtpToken token = WebSession.gson.fromJson(JWTUtils.decrypt(response.getJwt()), UserOtpToken.class);
                UserOtpToken token = new UserOtpToken();
                token.setEncodedToken("1234");
                token.setToken("1234");
                token.setOperatorType("D");
                token.setPhone(number);

                if (token == null || StringUtils.isBlank(token.getEncodedToken())) {
                    message = "Unable to verify the response";
                    return FAILED;
                }
                PreferencesUtil.put(ctx, AppConstants.USER_INFO, token);
                PreferencesUtil.putString(ctx, AppConstants.PHONE_NUMBER_KEY, number);
                return SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return FAILED;
        }

        public void onPostExecute(Integer result) {

            if (result == NO_NETWORK) {
                super.onPostExecute(100);
                Utils.showPrompt((Activity) ctx, "Error", "Please connect to internet and retry", true);
                return;
            }

            super.onPostExecute(result);

            if (result == FAILED) {
                applyLockRules();
                Utils.showPrompt(getActivity(), "Error", message);
                phoneNumber.setText("");
                return;
            }

            ((SetupActivity) getActivity()).goToStep2();
            PreferencesUtil.putString(getActivity(), AppConstants.PHONE_NUMBER_KEY, number);

        }
    }

    private void applyLockRules() {
        long numRetries = PreferencesUtil.getLong(getActivity(), NUM_RETRIES, 0);
        numRetries++;
        PreferencesUtil.putLong(getActivity(), NUM_RETRIES, numRetries);

        if (numRetries >= 3) {
            PreferencesUtil.putLong(getActivity(), APP_LOCK_TIME, System.currentTimeMillis());
            appLockTime = System.currentTimeMillis();
            handler.post(appLockChecker);
        }

    }

}
