package com.taxiapp.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amu on 26/04/15.
 */
public class SetupForm2Fragment extends Fragment {

    public static final String NUM_OTP_RESEND = "numOtpResend";
    public static final String NUM_RETRIES = "numRetries2";
    public static final String APP_LOCK_TIME = "appLockTime2";
    private Handler handler;
    private long appLockTime;
    private TextView error;
    private Button nextButton;


    private BroadcastReceiver incomingSmsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                        String regex = "Please\\s*enter\\s*this\\s*OTP\\s*([\\d]{6})\\s*in\\s*the\\s*TaxiApp\\s*App";
                        Matcher m = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(message);
                        if (m.find()) {
                            String otp = m.group(1);
                            new OTPVerificationTask(getActivity()).execute(otp);
                        }
                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }

        }
    };


    private Runnable appLockChecker = new Runnable() {
        @Override
        public void run() {
            if (appLockTime + AppConstants.APP_LOCK_TIME_INTERVAL > System.currentTimeMillis()) {
                handler.postDelayed(appLockChecker, 2000);
                error.setVisibility(View.VISIBLE);
                nextButton.setEnabled(false);
                resendOtpButton.setEnabled(false);
            } else {
                handler.removeCallbacks(appLockChecker);
                error.setVisibility(View.GONE);
                nextButton.setEnabled(true);
                resendOtpButton.setEnabled(true);
                PreferencesUtil.remove(getActivity(), APP_LOCK_TIME);
                PreferencesUtil.remove(getActivity(), NUM_RETRIES);
                PreferencesUtil.remove(getActivity(), NUM_OTP_RESEND);
            }
        }
    };

    private EditText otp;
    private TextView verifyOtp;
    //private String phoneNumber;
    private ViewGroup rootView;
    private TextView resendOtpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_setup_2, container, false);

        setupForm(rootView);
        preProcessForm();
        getActivity().registerReceiver(incomingSmsReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(incomingSmsReceiver);
        handler.removeCallbacks(appLockChecker);
    }

    private void setupForm(ViewGroup rootView) {

        verifyOtp = (TextView) rootView.findViewById(R.id.verifyOtpText);

        otp = (EditText) rootView.findViewById(R.id.setup_otp);

        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && otp.getError() != null) {
                    otp.setError(null);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nextButton = (Button) rootView.findViewById(R.id.setup_next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpNumber = otp.getText().toString();
                if (StringUtils.isBlank(otpNumber)) {
                    otp.requestFocus();
                    otp.setError("OTP field is mandatory");

                    return;
                }

                if (StringUtils.length(otpNumber) < 4) {
                    otp.requestFocus();
                    otp.setError("OTP should be 4 digits.");
                    return;
                }
                new OTPVerificationTask(getActivity()).execute(otpNumber);

            }
        });

        resendOtpButton = (TextView) rootView.findViewById(R.id.resend_otp_button);
        resendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTP();
            }
        });

        error = (TextView) rootView.findViewById(R.id.error);
        handler = new Handler();
    }

    private void preProcessForm() {
        String number = PreferencesUtil.getString(getActivity(), AppConstants.PHONE_NUMBER_KEY, "");
        //verifyOtp.setText("Verify OTP for " + number);

        appLockTime = PreferencesUtil.getLong(getActivity(), APP_LOCK_TIME, -1);
        if (appLockTime != -1) {
            handler.post(appLockChecker);
        }

    }

    private void resendOTP() {
        long numRetries = PreferencesUtil.getLong(getActivity(), NUM_OTP_RESEND, 0);

        if (numRetries < 3) {
            new ResendOtpTask(getActivity()).execute();
        } else {
            applyLockRules();
        }
    }


    private class ResendOtpTask extends ProgressAsyncTask {

        private String number;
        private String message = "No response from Server";

        public ResendOtpTask(Context ctx) {
            super(ctx);
        }

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                number = PreferencesUtil.getString(getActivity(), AppConstants.PHONE_NUMBER_KEY, "");
                if (!Utils.hasNetwork(ctx)) {
                    message = "Please connect to internet and retry";
                    return NO_NETWORK;
                }
//                NetResponse response = APIUtils.receiveOtp(number);
//                if (response == null) {
//                    return FAILED;
//                }
//
//                if (response.getStatusCode() == APIStatusCodes.ERR_NO_RESULT.getCode()) {
//                    message = response.getStatusDetails();
//                    return FAILED;
//                }
//
//                if (StringUtils.isBlank(response.getJwt())) {
//                    message = "Error retrying for new OTP.";
//                    return FAILED;
//                }
                UserOtpToken token = new UserOtpToken();
                token.setEncodedToken("1234");
                token.setToken("1234");
                token.setOperatorType("D");
                token.setPhone(number);


                //UserOtpToken token = PreferencesUtil.get(ctx, AppConstants.USER_INFO, UserOtpToken.class);

                if (token == null || StringUtils.isBlank(token.getEncodedToken())) {
                    message = "Unable to verify the response";
                    return FAILED;
                }
                PreferencesUtil.put(ctx, AppConstants.USER_INFO, token);
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
                Utils.showPrompt(getActivity(), "Error", message);
                return;
            }
            long numRetries = PreferencesUtil.getLong(getActivity(), NUM_OTP_RESEND, 0);
            numRetries++;
            PreferencesUtil.putLong(getActivity(), NUM_OTP_RESEND, numRetries);

            Utils.showPrompt(getActivity(), "OTP Sent", "OTP has been resent on your Registered mobile number");
        }
    }


    private class OTPVerificationTask extends ProgressAsyncTask {

        private String number;
        private String message = "Unknown error occured. Please try again";

        public OTPVerificationTask(Context ctx) {
            super(ctx);
        }

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                number = strings[0];

                UserOtpToken token = PreferencesUtil.get(ctx, AppConstants.USER_INFO, UserOtpToken.class);

                String otp = number;
                if (StringUtils.equalsIgnoreCase(token.getEncodedToken(), otp)) {
                    PreferencesUtil.putBool(ctx, AppConstants.APP_INITIALIZED, true);
                    return SUCCESS;
                }

                message = "OTP is invalid/expired.";

                return FAILED;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return FAILED;
        }

        public void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result == FAILED) {
                applyLockRules();
                Utils.showPrompt(getActivity(), "Error", message);
                otp.setText("");
                return;
            }

            Utils.showToast(ctx, "Activing app... ");
            ((SetupActivity) getActivity()).setupComplete();
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
