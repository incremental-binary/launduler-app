package kr.ac.kaist.launduler;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kr.ac.kaist.launduler.models.Reservation;
import kr.ac.kaist.launduler.services.LaundulerServiceKt;
import kr.ac.kaist.launduler.services.LaundulerServiceKt.*;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends RxFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int layout = R.layout.fragment_noreservation;
    private long remain = 0;
    private String userId;


    private OnFragmentInteractionListener mListener;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(int param1, long param2) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putLong(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layout = getArguments().getInt(ARG_PARAM1);
            remain = getArguments().getLong(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(layout, container, false);
        /*
        LaundulerServiceKt.getLaundulerService().getReservation(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d("TimerFragment","Success");
                        List<Reservation> result = (List<Reservation>) o;
                        Calendar start, end = Calendar.getInstance();
                        Calendar cur = Calendar.getInstance();
                        long curStamp = cur.getTimeInMillis();
                        int idx;
                        Log.d("TimerFragment",cur.toString());
                        for(idx = 0; idx < result.size();idx++) {
                            start = result.get(idx).getScheduledAt();
                            end = result.get(idx).getEndsAt();
                            Log.d("TimerFragment",String.valueOf(start.getTimeInMillis()));
                            Log.d("TimerFragment",String.valueOf(curStamp));
                            Log.d("TimerFragment", String.valueOf(end.getTimeInMillis()));

                            if(start.getTimeInMillis() < curStamp && end.getTimeInMillis() > curStamp)
                            {
                                Log.d("TimerFragment","Bingo! "+String.valueOf(idx));
                                break;
                            }

                        }
                        if(idx != result.size()) {
                            remain = (end.getTimeInMillis() - curStamp) / 1000 / 60;
                            Log.d("TimerFragment", end.toString());
                            if(remain >= 10)
                                layout = R.layout.fragment_timer;
                            else
                                layout = R.layout.fragment_finished;
                        }
                        else
                            layout = R.layout.fragment_noreservation;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();

                    }
                });*/

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(layout == R.layout.fragment_timer) {
            TextView t = (TextView) getView().findViewById(R.id.text_remaining);
            t.setText(String.valueOf(remain - 10));
        }




        /*List<Reservation> result = null;
        try {
            result = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result != null){
            Reservation reservation = result.get(result.size()-1);
            Calendar calendar = reservation.getEndsAt();
            Log.d("TimerFragment", calendar.toString());

        }*/

    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
