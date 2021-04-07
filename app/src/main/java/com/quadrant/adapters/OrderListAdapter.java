package com.quadrant.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quadrant.govolt.R;
import com.quadrant.model.OrderModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    private List<OrderModel> list;
    String time24;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView status, tv_creditamt, tv_category, tv_rewardamt, tv_time, tv_createtime, tv_tilltime;
        public CardView cardView;
        public ImageView iv_circlecolor;

        public MyViewHolder(View view) {
            super(view);
            status = (TextView) view.findViewById(R.id.status);
            tv_rewardamt = (TextView) view.findViewById(R.id.rewardamt);
            tv_creditamt = (TextView) view.findViewById(R.id.creditamt);
            tv_tilltime = (TextView) view.findViewById(R.id.till_time);
            tv_category = (TextView) view.findViewById(R.id.category);
            iv_circlecolor = (ImageView) view.findViewById(R.id.circlecolor);
           /* tv_rewardamt = (TextView) view.findViewById(R.id.rewardamt);
            tv_creditamt = (TextView) view.findViewById(R.id.creditamt);
            tv_time= (TextView) view.findViewById(R.id.time);
            tv_createtime= (TextView) view.findViewById(R.id.createtime);*/
        }
    }

    public OrderListAdapter(ArrayList<OrderModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OrderModel items = list.get(position);
        holder.status.setText(items.getStatus());
        if (items.getStatus().equals("PENDING")) {
            holder.status.setTextColor(Color.parseColor("#FF0000"));
            holder.status.setText("In attesa di conferma");
        } else if (items.getStatus().equals("PAID")) {

            holder.status.setTextColor(Color.parseColor("#37B12B"));

            holder.status.setText("Confermato");
        }
        int creditprice = Integer.parseInt(items.getCreditprice());
        int cp = creditprice / 100;
        holder.tv_creditamt.setText(String.valueOf("€" + " " + creditprice / 100));

        String sss = holder.tv_creditamt.getText().toString();

        if (cp == 9) {
            holder.tv_category.setText("Pacchetto Friends");
            holder.iv_circlecolor.setImageResource(R.drawable.ic_circle_yellow);
        } else if (cp == 17) {
            holder.tv_category.setText("Pacchetto Mates");
            holder.iv_circlecolor.setImageResource(R.drawable.ic_circle_pink);
        } else if (cp == 40) {
            holder.tv_category.setText("Pacchetto Lovers");
            holder.iv_circlecolor.setImageResource(R.drawable.ic_meroon);
        } else if (cp == 75) {
            holder.tv_category.setText("Pacchetto Govolters");
            holder.iv_circlecolor.setImageResource(R.drawable.ic_circle_green);
        }
        int rewardprice = Integer.parseInt(items.getRewardpriceval());
        holder.tv_rewardamt.setText(String.valueOf("€" + " " + rewardprice / 100));

        String str = items.getDatatime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        try {
            Date date = format.parse(str
                    .replaceAll("Z$", "+0000"));

            System.out.println(date.toString());

            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Calendar calendar = Calendar.getInstance();

            TimeZone localTZ = calendar.getTimeZone();

            sdf.setTimeZone(localTZ);

            System.out.println("DATE===" + sdf.format(date));

            String finalstr = sdf.format(date);

            System.out.println("dvhbdbbhd" + finalstr);

            holder.tv_tilltime.setText(finalstr);


        } catch (ParseException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();


        }


       /* int creditprice = Integer.parseInt(items.getCreditprice());
        holder.tv_creditamt.setText(String.valueOf("€"+creditprice / 100));
        int rewardprice = Integer.parseInt(items.getRewardpriceval());
        holder.tv_rewardamt.setText(String.valueOf("€"+rewardprice / 100));
        if (items.getStatus().equals("PENDING")) {
            holder.status.setTextColor(Color.parseColor("#FF0000"));
            holder.status.setText("in attesa di");
        } else if (items.getStatus().equals("SUCCESS")) {

            holder.status.setTextColor(Color.parseColor("#37B12B"));

            holder.status.setText("Pagato");
        }

        String Fromtime = items.getFromtime();
        String Tilltime = items.getTilltime();
        if (Fromtime.equals("UNBOUNDED") && Tilltime.equals("UNBOUNDED")) {
            holder.tv_time.setText("Tutta la vita" + " "+"to" +" "+ "Tutta la vita");
        } else {
            if(Fromtime.equals("UNBOUNDED")){
                Fromtime="Tutta la vita";
            }
            holder.tv_time.setText(Fromtime + "-" + Tilltime);
        }

        String str =items.getDatatime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        try {
            Date date = format.parse(str
                    .replaceAll("Z$", "+0000"));

            System.out.println(date.toString());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            Calendar calendar = Calendar.getInstance();

            TimeZone localTZ = calendar.getTimeZone();

            sdf.setTimeZone(localTZ);

            System.out.println("DATE==="+sdf.format(date));

            String finalstr =sdf.format(date);

            System.out.println("dvhbdbbhd"+finalstr);

            holder.tv_createtime.setText(finalstr);


        } catch (ParseException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();

        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
