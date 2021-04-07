package com.quadrant.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadrant.govolt.CourseDetails_Activity;
import com.quadrant.govolt.R;
import com.quadrant.model.RideListItems;
import com.quadrant.model.TripModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;

public class RideListAdapter extends RecyclerView.Adapter<RideListAdapter.MyViewHolder> {

    private List<TripModel> list;

    String time24;

    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, tv_sub_name, tv_distance, tv_date, tv_price, tv_distanceone, endlocAdd;
        public CardView cardView;
        public ImageView Image_bike;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name_tv);
            tv_sub_name = (TextView) view.findViewById(R.id.sub_name);
            tv_distance = (TextView) view.findViewById(R.id.distance_tv);
            tv_date = (TextView) view.findViewById(R.id.date);
            tv_price = (TextView) view.findViewById(R.id.textprice);
            tv_distanceone = (TextView) view.findViewById(R.id.time_end);
            endlocAdd = (TextView) view.findViewById(R.id.endloc);
            cardView = (CardView) view.findViewById(R.id.cardview);
            Image_bike=view.findViewById(R.id.image_bike);

        }
    }


    public RideListAdapter(ArrayList<TripModel> list) {

        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ride_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TripModel items = list.get(position);
        String start = StringUtils.substringBefore(items.getAddress_start(), ","); //

        String Sc_type = items.getScootertype();

        if(Sc_type.equals("kick")){
            holder.Image_bike.setImageResource(R.drawable.ic_bike_kick);
        } else{

            holder.Image_bike.setImageResource(R.drawable.ic_bike_white_green);
        }
        holder.title.setText(start);

        holder.tv_sub_name.setText(items.getLicenseplate());

        String StartTime = items.getStartTime();


        holder.tv_distance.setText(StartTime);

        holder.tv_distanceone.setText(items.getFinishTime());

        holder.endlocAdd.setText( StringUtils.substringBefore(items.getAddress_finish(), ","));


        holder.tv_date.setText(items.getTempdate());
        // holder.tv_date.setText(items.getDate());
        //holder.tv_price.setText(items.getPrice());

      //  holder.tv_price.setText(String.valueOf(items.getPrice()));
        DecimalFormat form = new DecimalFormat("0.00");
        holder.tv_price.setText( form.format( items.getPrice()));// holder.tv_price.setText(String.valueOf(items.getPrice())) ) );
       /* holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(this, CourseDetails_Activity.class);
                i.putExtra("intVariableName", items.getId());
                context.startActivity(i);


            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
