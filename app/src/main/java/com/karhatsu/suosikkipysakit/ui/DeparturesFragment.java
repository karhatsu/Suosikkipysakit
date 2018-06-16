package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.karhatsu.suosikkipysakit.R;
import com.karhatsu.suosikkipysakit.datasource.OnHslRequestReady;
import com.karhatsu.suosikkipysakit.datasource.StopRequest;
import com.karhatsu.suosikkipysakit.db.StopCollectionDao;
import com.karhatsu.suosikkipysakit.db.StopDao;
import com.karhatsu.suosikkipysakit.domain.Departure;
import com.karhatsu.suosikkipysakit.domain.DeparturesComparator;
import com.karhatsu.suosikkipysakit.domain.Stop;
import com.karhatsu.suosikkipysakit.domain.StopCollection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DeparturesFragment extends ListFragment implements OnHslRequestReady<Stop> {
    private static final String DEPARTURES = "departures";
    private static final String TITLE = "title";

    private StopRequest stopRequest;

    private ProgressDialog progressDialog;

    private Timer timer = new Timer();
    private Handler handler = new Handler(Looper.getMainLooper());
    private Calendar calendar;
    private ArrayList<Departure> departures;
    private String title;
    private Stop stop;
    private String stopCode;
    private long collectionId;

    public DeparturesFragment() {
        calendar = new GregorianCalendar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.departures_list, container, false);
        if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                stop = arguments.getParcelable(Stop.STOP_KEY);
                stopCode = arguments.getString(Stop.CODE_KEY);
                collectionId = arguments.getLong(StopCollection.COLLECTION_ID_KEY, 0);
                if (stop != null) {
                    title = stop.getVisibleName();
                    setToolbarTitle();
                } else if (stopCode == null) {
                    StopCollection stopCollection = new StopCollectionDao(getContext()).findById(collectionId);
                    title = stopCollection.getName();
                    setToolbarTitle();
                }
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            queryDepartures();
        } else {
            title = savedInstanceState.getString(TITLE);
            departures = savedInstanceState.getParcelableArrayList(DEPARTURES);
            stop = savedInstanceState.getParcelable(Stop.STOP_KEY);
            stopCode = savedInstanceState.getString(Stop.CODE_KEY);
            collectionId = savedInstanceState.getLong(StopCollection.COLLECTION_ID_KEY, 0);
            if (departures != null) {
                showDepartures();
            } else {
                queryDepartures();
            }
            setToolbarTitle();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, title);
        outState.putParcelableArrayList(DEPARTURES, departures);
        outState.putParcelable(Stop.STOP_KEY, stop);
        outState.putString(Stop.CODE_KEY, stopCode);
        outState.putLong(StopCollection.COLLECTION_ID_KEY, collectionId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        hideProgressDialog();
    }

    private void queryDepartures() {
        stopRequest = new StopRequest(this);
        if (stop != null) {
            departures = new ArrayList<>(stop.getDepartures());
            showDepartures();
        } else if (stopCode != null) {
            showProgressDialog();
            stopRequest.execute(stopCode);
        } else {
            showProgressDialog();
            List<Stop> stops = new StopDao(getContext()).findByCollectionId(collectionId);
            String[] stopCodes = getStopCodesFrom(stops);
            stopRequest.execute(stopCodes);
        }
    }

    private void setToolbarTitle() {
        Activity activity = getActivity();
        if (activity != null) {
            ((AppCompatActivity)activity).getSupportActionBar().setTitle(title);
        }
    }

    private void showDepartures() {
        if (departures.isEmpty()) {
            ToastHelper.showToast(getContext(), R.string.activity_departures_nothing_found);
            return;
        }
        ListView departuresListView = getListView();
        DepartureListAdapter adapter = new DepartureListAdapter(getContext(), departures);
        departuresListView.setAdapter(adapter);
        timer.schedule(createTimerTask(), 5000, 5000);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new PleaseWaitDialog(getContext(), R.string.activity_departures_dialog_title);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private String[] getStopCodesFrom(List<Stop> stops) {
        String[] stopCodes = new String[stops.size()];
        int i = 0;
        for (Stop stop : stops) {
            stopCodes[i] = stop.getCode();
            i++;
        }
        return stopCodes;
    }

    private TimerTask createTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Calendar now = new GregorianCalendar();
                        if (now.get(Calendar.MINUTE) != calendar.get(Calendar.MINUTE)) {
                            ((DepartureListAdapter) getListView().getAdapter()).notifyDataSetChanged();
                        }
                        calendar = now;
                    }
                });
            }
        };
    }

    @Override
    public void notifyAboutResult(ArrayList<Stop> stops) {
        hideProgressDialog();
        departures = getDeparturesFrom(stops);
        showDepartures();
        if (title == null && stops.size() == 1) {
            Stop stop = new StopDao(getContext()).findByCode(stops.get(0).getCode());
            title = stop.getVisibleName();
            setToolbarTitle();
        }
    }

    private ArrayList<Departure> getDeparturesFrom(ArrayList<Stop> stops) {
        departures = new ArrayList<>();
        for (Stop stop : stops) {
            departures.addAll(stop.getDepartures());
        }
        Collections.sort(departures, new DeparturesComparator());
        return departures;
    }

    @Override
    public void notifyConnectionProblem() {
        hideProgressDialog();
        ToastHelper.showToast(getContext(), R.string.connection_problem);
    }
}
