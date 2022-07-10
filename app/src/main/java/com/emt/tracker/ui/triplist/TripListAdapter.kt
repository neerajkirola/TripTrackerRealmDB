package com.emt.tracker.ui.triplist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emt.tracker.TrackerApplication
import com.emt.tracker.databinding.ItemTripListBinding
import com.emt.tracker.db.Trip
import com.emt.tracker.util.DateFormater
import com.emt.tracker.util.ToastUtils
import com.emt.tracker.util.Utils

class TripListAdapter(private val list: MutableList<Trip>) :
    RecyclerView.Adapter<TripListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTripListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(_item: MutableList<Trip>) {
        list.clear()
        list.addAll(_item)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binder: ItemTripListBinding) :
        RecyclerView.ViewHolder(binder.root), View.OnClickListener {
        fun bind(item: Trip, position: Int) {
            val address = Utils.getStartEndAddressTrip(item)
            binder.tvStartDate.text =
                " Start at: ${DateFormater.getFormattedDate(item.startTime!!)}"
            binder.tvEndDate.text = Utils.getEndTime(item)
            binder.tvStartAddress.text = "From: ${address?.first}"
            binder.tvEndAddress.text = "To: ${address?.second}"
            binder.tvTitle.text = (position + 1).toString()
            binder.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            ToastUtils.showShortMessage("Under Development...", TrackerApplication.App.getContext())
        }

    }

}