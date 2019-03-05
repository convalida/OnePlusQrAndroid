package com.convalida.user.jsonparsing;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>  {

    Context context;
    ArrayList<HashMap<String,String>> Originaldata;

    ImageLoader imageLoader;
    LayoutInflater infalter;
    HashMap<String,String> result=new HashMap<String, String>();
    HashMap<String, String> resultDist;
    String emptyData;
    private static final String TAG="DataAdapter";

    public DataAdapter(Context context,ArrayList<HashMap<String,String>> mainArrayList){
        this.context=context;
        Originaldata=mainArrayList;
        if(Originaldata.isEmpty()){
            Log.e(TAG,"No restaurants in this range");
         //   Toast.makeText(context,"No restaurants in this range",Toast.LENGTH_SHORT).show();
        }
        imageLoader=new ImageLoader(context);
    }

    public DataAdapter(Context context,String empty) {
        this.context=context;
        emptyData=empty;
        Log.e(TAG,"No rest");
    }

    //    private final View.OnClickListener mOnClickListener= new MyOnClickListener();

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    //   if(Originaldata.isEmpty()){
           // Log.e(TAG,"No restaurants in this range");
          // holder.emptyResult.setText("No data in this range");
      //  }
        //else {
        result=Originaldata.get(position);
     //
            holder.name.setText(result.get(MainActivity.NAME));
            holder.add1.setText(result.get(MainActivity.ADDRESS1));
            holder.add2.setText(result.get(MainActivity.ADDRESS2));
            holder.add3.setText(result.get(MainActivity.ADDRESS3)+" ");
            holder.zipCode.setText(result.get(MainActivity.ZIP));
            holder.dist.setText(result.get(MainActivity.DISTANCE) + " miles");
            Picasso.with(context).load(Uri.parse(result.get(MainActivity.LOGO))).into(holder.logoImg);
            holder.pointsText.setText(result.get(MainActivity.POINT));

       // }



    }

    @Override
    public int getItemCount() {
        if(Originaldata!=null) {
            return Originaldata.size();
        }
        else{
            return 0;
        }

    }

    public void setmFilter(ArrayList<HashMap<String,String>> items){
        Originaldata=new ArrayList<>();
        Originaldata.addAll(items);
        notifyDataSetChanged();


    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,add1,add2,add3,zipCode,dist,pointsText,emptyResult;
        public ImageView logoImg;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.name);
            add1= (TextView) itemView.findViewById(R.id.addressA);
            add2= (TextView) itemView.findViewById(R.id.addressB);
            add3= (TextView) itemView.findViewById(R.id.addressC);
            dist= (TextView) itemView.findViewById(R.id.distText);
            zipCode= (TextView) itemView.findViewById(R.id.zp);
            logoImg= (ImageView) itemView.findViewById(R.id.logo);
            cardView= (CardView) itemView.findViewById(R.id.cv);
            pointsText= (TextView) itemView.findViewById(R.id.pointsNum);
          //  emptyResult= (TextView) itemView.findViewById(R.id.empty);


        }

    }

}
