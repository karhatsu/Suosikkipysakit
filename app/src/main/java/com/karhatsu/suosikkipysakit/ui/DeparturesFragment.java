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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DeparturesFragment extends ListFragment implements OnHslRequestReady<Stop> {
    private StopRequest stopRequest;

    private ProgressDialog progressDialog;

    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Handler handler = new Handler(Looper.getMainLooper());
    private StopCollection stopCollection;

    public DeparturesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.departures_list, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Stop stop = arguments.getParcelable(Stop.STOP_KEY);
            String stopCode = arguments.getString(Stop.CODE_KEY);
            long collectionId = arguments.getLong(StopCollection.COLLECTION_ID_KEY, 0);
            if (stop != null) {
                setToolbarTitle(stop.getNameByUser());
            } else if (stopCode == null) {
                stopCollection = new StopCollectionDao(getContext()).findById(collectionId);
                setToolbarTitle(stopCollection.getName());
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            stopRequest = new StopRequest(this);
            queryDepartures();
        }
    }

    private void queryDepartures() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        Stop stop = arguments.getParcelable(Stop.STOP_KEY);
        String stopCode = arguments.getString(Stop.CODE_KEY);
        long collectionId = arguments.getLong(StopCollection.COLLECTION_ID_KEY, 0);
        if (stop != null) {
            showDepartures(stop.getDepartures());
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

    private void setToolbarTitle(String title) {
        Activity activity = getActivity();
        if (activity != null) {
            ((AppCompatActivity)activity).getSupportActionBar().setTitle(title);
        }
    }

    private void showDepartures(List<Departure> departures) {
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
        if (progressDialog != null) {
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
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((DepartureListAdapter) getListView().getAdapter()).notifyDataSetChanged();
                    }
                });
            }
        };
        return timerTask;
    }

    @Override
    public void notifyAboutResult(ArrayList<Stop> stops) {
        hideProgressDialog();
        List<Departure> departures = getDeparturesFrom(stops);
        showDepartures(departures);
        if (stopCollection == null && stops.size() == 1) {
            setToolbarTitle(stops.get(0).getName());
        }
    }

    private List<Departure> getDeparturesFrom(ArrayList<Stop> stops) {
        List<Departure> departures = new LinkedList<Departure>();
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

    @Override
    public void onResume() {
        super.onResume();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(createTimerTask(), 100, 5000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgressDialog();
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
