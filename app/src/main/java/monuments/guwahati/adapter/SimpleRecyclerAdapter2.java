package monuments.guwahati.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import monuments.guwahati.R;


public class SimpleRecyclerAdapter2 extends RecyclerView.Adapter<SimpleRecyclerAdapter2.VersionViewHolder>
{

    List<String> versionModels;
    Boolean isHomeList = false;

    public static List<String> homeActivitiesList = new ArrayList<>();
    public static List<String> homeActivitiesSubList = new ArrayList<>();


    Context context;
    OnItemClickListener clickListener;


    public void setHomeActivitiesList(Context context)
    {

        String[] listArray = context.getResources().getStringArray(R.array.home_activities);
        String[] subTitleArray = context.getResources().getStringArray(R.array.home_activities_subtitle);

        for (int i = 0; i < listArray.length; ++i)
        {
            homeActivitiesList.add(listArray[i]);
            homeActivitiesSubList.add(subTitleArray[i]);
        }
    }


    public SimpleRecyclerAdapter2(Context context)
    {
        isHomeList = true;
        this.context = context;
        setHomeActivitiesList(context);
    }


    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item2, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i)
    {

        if (isHomeList)
        {

            versionViewHolder.title.setText(homeActivitiesList.get(i));
            versionViewHolder.subTitle.setText(homeActivitiesSubList.get(i));

            TypedArray images = context.getResources().obtainTypedArray(R.array.home_activities_images_thumb);

            versionViewHolder.thumbnail.setImageResource(images.getResourceId(i, -1));
        }

        else
        {
            versionViewHolder.title.setText(versionModels.get(i));
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
        TextView title;
        TextView subTitle;
        ImageView thumbnail;

        public VersionViewHolder(View itemView)
        {

            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.listitem_name);
            subTitle = (TextView) itemView.findViewById(R.id.listitem_subname);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

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