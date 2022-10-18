package com.karhatsu.suosikkipysakit.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.ListFragment;

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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DeparturesFragment extends ListFragment implements OnHslRequestReady<Stop> {
    private static final String DEPARTURES = "departures";
    private static final String TITLE = "title";

    private ProgressDialog progressDialog;

    private Timer timer = new Timer();
    private boolean timerRunning = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ArrayList<Departure> departures;
    private String title;
    private Stop stop;
    private String stopCode;
    private long collectionId;

    public DeparturesFragment() {
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
        } else {
            title = savedInstanceState.getString(TITLE);
            departures = savedInstanceState.getParcelableArrayList(DEPARTURES);
            stop = savedInstanceState.getParcelable(Stop.STOP_KEY);
            stopCode = savedInstanceState.getString(Stop.CODE_KEY);
            collectionId = savedInstanceState.getLong(StopCollection.COLLECTION_ID_KEY, 0);
            if (title != null) {
                setToolbarTitle();
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            queryDepartures(true);
        } else {
            if (departures != null) {
                showDepartures();
            } else {
                queryDepartures(true);
            }
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

    private void queryDepartures(boolean initialQuery) {
        StopRequest stopRequest = new StopRequest(this);
        if (stop != null) {
            departures = new ArrayList<>(stop.getDepartures());
            showDepartures();
            stopCode = stop.getCode(); // in order to use stopCode in timer
            stop = null;
        } else if (stopCode != null) {
            if (initialQuery) {
                showProgressDialog();
            }
            stopRequest.execute(stopCode);
        } else {
            if (initialQuery) {
                showProgressDialog();
            }
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
        if (!timerRunning) {
            timer.schedule(createTimerTask(), 15000, 15000);
        }
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
        timerRunning = true;
        return new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        queryDepartures(false);
                    }
                });
            }
        };
    }

    @Override
    public void notifyAboutResult(ArrayList<Stop> stops) {
        hideProgressDialog();
        departures = getDeparturesFrom(stops);
        if (isSafeFragment()) {
            showDepartures();
            if (title == null && stops.size() == 1) {
                Stop apiStop = stops.get(0);
                Stop dbStop = new StopDao(getContext()).findByCode(apiStop.getCode());
                title = dbStop != null ? dbStop.getVisibleName() : apiStop.getVisibleName();
                setToolbarTitle();
            }
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

    private boolean isSafeFragment() {
        return !(isRemoving() || getActivity() == null || isDetached() || !isAdded() || getView() == null);
    }

    @Override
    public void notifyConnectionProblem() {
        hideProgressDialog();
        if (departures == null) {
            ToastHelper.showToast(getContext(), R.string.connection_problem);
        }
    }
}
