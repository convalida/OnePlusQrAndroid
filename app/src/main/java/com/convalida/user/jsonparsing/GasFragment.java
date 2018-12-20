package com.convalida.user.jsonparsing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Convalida on 7/7/2017.
 */

public class GasFragment extends Fragment  {
    View view;
    private static final String TAG="GasFragment";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gas_fragment, container, false);
    //    Toast.makeText(getActivity(),"Sorry, no data currently",Toast.LENGTH_SHORT).show();
        return view;
    }
  /**   public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
    //        Toast.makeText(getActivity(),"Sorry, no data currently", Toast.LENGTH_SHORT).show();
        }
    }**/

}
