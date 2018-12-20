package com.convalida.user.jsonparsing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Convalida on 7/7/2017.
 */

public class GroceryFragment extends Fragment {
    View view;
    private static final String TAG="GroceryFragment";
  //  ConstraintLayout constraintLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.grocery_fragment, container, false);


        return view;
    }



   /** public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
   //         Toast.makeText(getActivity(),"Sorry, no data currently", Toast.LENGTH_SHORT).show();
//          ConstraintLayout constraintLayout= (ConstraintLayout) view.findViewById(R.id.groceryConstraint);
 //           Snackbar.make(constraintLayout,"Internet connection is required",Snackbar.LENGTH_LONG).show();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Sorry no data currently")
                    .create()
                    .show();

        }
    }**/
}
