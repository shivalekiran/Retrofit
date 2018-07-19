package com.aperotechnologies.retrofit.service;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.aperotechnologies.retrofit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class Reusable_Functions {

    public static final String STORECODE = "storeCode";
    public static final String CASEID = "storeCode";
    public static final int SUBMITTED_VALUE = 1;
    public static final String SUBMITTED = "Submitted";
    public static final int APPROVED_VALUE = 2;
    public static final String APPROVED = "Approved";
    public static final int REJECTED_VALUE = 3;
    public static final String REJECTED = "Rejected";
    private static final String TAG = Reusable_Functions.class.getName();
    public static final String UTF_8 = "UTF-8";
    public static ProgressDialog progressDialog;
    public static final String datePattern_yyyy_MM_dd = "yyyy-MM-dd";


    public static String getStatusString(String statusCode1) {
        try {
            int statusCode = Integer.valueOf(statusCode1);
            switch (statusCode) {
                case Reusable_Functions.SUBMITTED_VALUE:
                    return Reusable_Functions.SUBMITTED;
                case Reusable_Functions.APPROVED_VALUE:
                    return Reusable_Functions.APPROVED;
                case Reusable_Functions.REJECTED_VALUE:
                    return Reusable_Functions.REJECTED;
                default:
                    return "Status";
            }
        } catch (Exception e) {
            Log.e(Reusable_Functions.TAG, "getStatusString: ", e);
            return "Status";
        }
    }

    public static String getStatusValue(String status) {
        try {
            switch (status) {
                case Reusable_Functions.SUBMITTED:
                    return "1";
                case Reusable_Functions.APPROVED:
                    return "2";
                case Reusable_Functions.REJECTED:
                    return "3";
                default:
                    return "0";
            }
        } catch (Exception e) {
            Log.e(Reusable_Functions.TAG, "getStatusString: ", e);
            return "0";
        }
    }

    public static boolean chkStatus(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //noinspection deprecation,deprecation
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //noinspection deprecation,deprecation
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting();
    }

    public static void hDialog() {

        if (Reusable_Functions.progressDialog != null) {
            if (Reusable_Functions.progressDialog.isShowing()) {
                Reusable_Functions.progressDialog.dismiss();
                Reusable_Functions.progressDialog.cancel();
                Reusable_Functions.progressDialog = null;
            }
        }
    }

    public static void sDialog(Context cont, String message) {
        if (Reusable_Functions.progressDialog == null) {
            Reusable_Functions.progressDialog = new ProgressDialog(cont);//R.style.AlertDialog_Theme);
            Reusable_Functions.progressDialog.setIndeterminate(true);
            Reusable_Functions.progressDialog.setMessage(message);
            Reusable_Functions.progressDialog.setCancelable(false);
            if (!Reusable_Functions.progressDialog.isShowing()) {
                Reusable_Functions.progressDialog.show();

            }
        }
    }

//    public static void displayToast(Context context, String msg) {
//
//        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//        View layout = inflater.inflate(R.layout.custom_toast,
//                (ViewGroup) ((Activity) context).findViewById(R.id.custom_container));
//
//        TextView text = layout.findViewById(R.id.text);
//        text.setText(msg);
//
//        Toast toast = Toast.makeText(context, "" + msg, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        toast.setView(layout);
//        toast.show();
//
//    }

//    public static void showSnackbar(View view, String msg) {
//        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
//    }
//
//    public static void showSnackbarError(Context context, View view, String msg) {
//        Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
//        view = snack.getView();
//
//        TextView tv = view.findViewById(id.snackbar_text);
//        tv.setTextColor(Color.parseColor("#ffffff"));
//        view.setBackgroundColor(ContextCompat.getColor(context, R.color.ezfbb_red));
//        snack.show();
//    }
//
//    public static void showSnackbarSuccess(Context context, View view, String msg) {
//        Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
//        view = snack.getView();
//        TextView tv = view.findViewById(id.snackbar_text);
//        tv.setTextColor(Color.parseColor("#ffffff"));
//        view.setBackgroundColor(ContextCompat.getColor(context, R.color.smdm_actionbar));
//
//
//        snack.show();
//    }

    public static void ViewVisible(View view) {
        if (VERSION.SDK_INT >= 21) {

            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            view.setVisibility(View.VISIBLE);
            anim.start();

        } else {
            view.setVisibility(View.VISIBLE);

        }
    }

    public static void ViewGone(final View view) {
        if (VERSION.SDK_INT >= 21) {

            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            float initialRadius = (float) Math.hypot(cx, cy);

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });

            anim.start();

        } else {
            view.setVisibility(View.GONE);

        }
    }

    public static void MakeToast(Context context, String info) {

        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();

    }

    public static void animateScaleOut(final View view) {
        if (!view.isShown()) {
            view.setScaleX(0.2f);
            view.setScaleY(0.2f);
            view.animate()
                    .setStartDelay(200)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1).setListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }


    public static void animateScaleIn(final View view) {
        view.setScaleX(1);
        view.setScaleY(1);
        view.animate()
                .setStartDelay(100)
                .alpha(1)
                .scaleX(0.1f)
                .scaleY(0.1f).setListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public static boolean checkPermission(String strPermission, Context context) {
        if (VERSION.SDK_INT > VERSION_CODES.LOLLIPOP) {
            int result = ContextCompat.checkSelfPermission(context, strPermission);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static Map<String, String> getParan(String bearerToken) {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json");
        params.put("Authorization", "Bearer " + bearerToken);
        return params;
    }

    public static RetryPolicy getPolicy() {
        return new DefaultRetryPolicy(R.integer.web_req_socket_time_out, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public static String getString(String s, String ifNullReturnType) {
        if (s != null && !s.equals("") && s.length() > 0) {
            return s;
        } else {
            return ifNullReturnType;
        }
    }

    /*
    *   input date - dd/MMM/yyyy output - yyyy-MM-dd
    */
    public static String getDateFrom_dd_mmm_yyyyToyyyy_mm_dd(String curDate, String formate) {

        try {
            Date date;
            String resultDate, newDte = "";
            if (curDate.contains(" ") || curDate.contains("-")) {
                if (curDate.contains(" "))
                    newDte = curDate.replace(" ", "/");
                if (curDate.contains("-"))
                    newDte = curDate.replace("-", "/");
            } else {
                newDte = curDate;
            }
            SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MMM/yyyy");
            date = formatter2.parse(newDte);
            Log.e("Reusable Func: ", "getDateFrom_dd_mmm_yyyyToyyyy_mm_dd: " + date);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            resultDate = formatter.format(date);
            Log.e("Reusable Func: ", "getDateFrom_dd_mmm_yyyyToyyyy_mm_dd: " + resultDate);
            return resultDate;
        } catch (ParseException e) {
            Log.e("Reusanle Func: ", "getDateFrom_dd_mmm_yyyyToyyyy_mm_dd: ", e);
        }
        return curDate;
    }


    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static String getRandomDate() {
        GregorianCalendar gc = new GregorianCalendar();

        int year = Reusable_Functions.randBetween(2018, 2018);

        gc.set(Calendar.YEAR, 2018);

        int dayOfYear = Reusable_Functions.randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));

        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);

        Log.e("TAG", "getRandomDate: Random Date: " + gc.get(Calendar.YEAR) + "/" + (gc.get(Calendar.MONTH) + 1) + "/" + gc.get(Calendar.DAY_OF_MONTH));
        return gc.get(Calendar.DAY_OF_MONTH) + "/" + (gc.get(Calendar.MONTH) + 1) + "/" + gc.get(Calendar.YEAR);

    }

    // dd/MM/yyyy
    public static String getDateInMilliSeconds(String date) {
        SimpleDateFormat format;
        if (date.contains("-")) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else if (date.contains("/")) {
            format = new SimpleDateFormat("dd/MM/yyyy");
        } else {
            format = new SimpleDateFormat();
        }
        try {
            Date date1 = format.parse(date);
            long l = date1.getTime();
            return date1.getTime() + "";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    @SuppressLint("DefaultLocale")
    public static String getSingleDecimalFormat(double value)
    {
//        if(value > 0.0f)
//        {
            return String.format("%.1f",value);
//        }
//        else
//        {
//            return "0.0";
//        }

    }


    @SuppressLint("DefaultLocale")
    public static String getTwoDecimalFormat(double value)
    {
//        if(value > 0.0f)
//        {
            return String.format("%.2f",value);
//        }
//        else
//        {
//            return "0.00";
//        }

    }
    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
    public static int isLevelZero(int level)
    {
        if(level == 0)
        {
            return 0;
        }
        else
        {
            return level;
        }

    }
    // for round values input any output round in String format
    public static int getRoundValue(double roundOf) {
        if (roundOf > 0.0f)
            return (int) Math.round(roundOf);
        else return 0;
    }
    // for round values input any output round in int format
    public static int getRoundWithNegativeValue(double roundOf) {
        return (int) Math.round(roundOf);
    }

    public static String getRoundValue(long roundOf) {
        if (roundOf > 0.0f)
            return String.valueOf(Math.round(roundOf));
        else return "0";
    }

    public boolean before(Date date) {

        return false;
    }

    // used to check RS31 barcode scanner device
    public static String getDeviceInfo() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else

        {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getTodaysDate_YYYYMMDD() {
        String today = null;
        try {

            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat(datePattern_yyyy_MM_dd);
            today = format.format(date);
            Log.e(TAG, "getTodaysDate_YYYYMMDD: " + date);
        } catch (Exception e) {
            Log.e(TAG, "getTodaysDate_YYYYMMDD: ", e);
        }
        return today != null ? today : "-";
    }

    public static String getDateAfterToday_YYYYMMDD(int afterDay) {
        String day = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, afterDay);
            Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat(datePattern_yyyy_MM_dd);
            day = format.format(date);
            Log.e(TAG, "getDateAfterToday_YYYYMMDD: " + date);
        } catch (Exception e) {
            Log.e(TAG, "getDateAfterToday_YYYYMMDD: ", e);
        }
        return day != null ? day : "-";
    }

    // input: string date;
    // output: date in milliseconds
    // exception returns Current milliseconds
    public static long getDateInMilliseconds(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(datePattern_yyyy_MM_dd);
            Date longDate = format.parse(date);
            return longDate.getTime();
        } catch (Exception e) {
            Log.e(TAG, "getDateInMilliseconds: ", e);
            return System.currentTimeMillis();
        }
    }



    // to check string is empty or null
    // return false if string is null and empty otherwise true
    public static boolean isStringEmpty(String input) {
        if (input != null) {
            return input.isEmpty() || input.length() == 0;
        } else return true;
    }

    public static Date getDateFromString(String input, String pattern) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.parse(input);
        } catch (ParseException e) {
            Log.e(TAG, "getDateFromString: ", e);
        }
        return date;
    }

    public static long getFirstDayOfYearInLong(int year){
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern_yyyy_MM_dd);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.MONTH, 0);
       return calendar.getTimeInMillis();
    }

    public static RequestQueue getRequestQueue(Context context) {
        final Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        return  new RequestQueue(cache, network);
    }

    public static float convertDpToPixel(int dp, Context context) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    // Filter for input values from given limit
    public static class InputFilterMinMax implements InputFilter {

        private final float min;
        private final float max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Float.parseFloat(min);
            this.max = Float.parseFloat(max);
        }

        public InputFilterMinMax(float min, float max) {
            this.min = min;
            this.max = max;
        }


        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                float input = Float.parseFloat(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(float a, float b, float c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

//    public static UserDetails getUserDetails(SharedPreferences preferences){
//        UserDetails userDetails = new UserDetails();
//        userDetails.setUserId(preferences.getString("userId", ""));
//        userDetails.setBearerToken(preferences.getString("bearerToken", ""));
//        userDetails.setConceptDesc(preferences.getString("conceptDesc", ""));
//        userDetails.setConcept(preferences.getString("concept", ""));
//        userDetails.setGeoLeveLCode(preferences.getString("concept", ""));
//        userDetails.setLobId(preferences.getString("lobid", ""));
//        userDetails.setLobName(preferences.getString("lobname", ""));
//        userDetails.setToken(preferences.getString("push_tokken", ""));
//        userDetails.setHierarchyLevels(preferences.getString("hierarchyLevels", ""));
//        userDetails.setKpiId(preferences.getString("kpiId", "").split(","));
//        return userDetails;
//    }
}
