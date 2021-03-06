package monuments.guwahati.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import monuments.guwahati.R;

import java.util.ArrayList;
import java.util.List;


public class SimpleRecyclerAdapter1 extends RecyclerView.Adapter<SimpleRecyclerAdapter1.VersionViewHolder>
{

    List<String> versionModels;
    Boolean isHomeList = false;

    public static List<String> homeActivitiesList = new ArrayList<>();
    public static List<String> homeActivitiesSubList = new ArrayList<>();

    Context context;
    OnItemClickListener clickListener;

    private int pos;


    public void setHomeActivitiesList(Context context)
    {

        String[] listArray = context.getResources().getStringArray(R.array.home_activities);
        String[] subTitleArray = context.getResources().getStringArray(R.array.home_activities_subtitle);

        homeActivitiesList.clear();
        homeActivitiesSubList.clear();

        homeActivitiesList.add(listArray[pos]);
        homeActivitiesSubList.add(subTitleArray[pos]);
    }


    public SimpleRecyclerAdapter1(Context context, int pos)
    {

        isHomeList = true;
        this.context = context;
        this.pos = pos;
        setHomeActivitiesList(context);
    }


    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item1, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i)
    {

        if (isHomeList)
        {
            //versionViewHolder.title.setText(homeActivitiesList.get(i));
            versionViewHolder.subTitle.setText(homeActivitiesSubList.get(i));
        }

        else
        {
            //versionViewHolder.title.setText(versionModels.get(i));
        }
    }


    @Override
    public int getItemCount()
    {

        if (isHomeList)
        {
            return homeActivitiesList == null ? 0 : homeActivitiesList.size();
        }

        else
        {
            return versionModels == null ? 0 : versionModels.size();
        }
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        CardView cardItemLayout;
        //TextView title;
        TextView subTitle;


        public VersionViewHolder(View itemView)
        {

            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            //title = (TextView) itemView.findViewById(R.id.listitem_name);
            subTitle = (TextView) itemView.findViewById(R.id.listitem_subname);

            if (isHomeList)
            {
                itemView.setOnClickListener(this);
            }

            else
            {
                subTitle.setVisibility(View.GONE);
            }
        }


        @Override
        public void onClick(View v)
        {
            clickListener.onItemClick(v, getPosition());
        }
    }


    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }


    public void SetOnItemClickListener(final OnItemClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }
}