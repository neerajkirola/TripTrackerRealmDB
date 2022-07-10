package com.emt.tracker.ui.triplist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.emt.tracker.R
import com.emt.tracker.databinding.ActivityTripListBinding
import com.emt.tracker.db.Trip
import com.emt.tracker.db.TripDao

class TripListActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binder: ActivityTripListBinding
    private val adapter by lazy { TripListAdapter(mutableListOf()) }
    private val tripList = mutableListOf<Trip>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityTripListBinding.inflate(layoutInflater)
        setContentView(binder.root)
        initData()
    }

    private fun initData() {
        binder.rv.adapter = adapter
        TripDao.getAllTrips({
            if (it != null && it.isNotEmpty()) {
                binder.rv.visibility = View.VISIBLE
                binder.tvNotrip.visibility = View.GONE
                tripList.addAll(it)
                adapter.setData(tripList)
            }
        }, {

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvNotrip -> finish()

        }
    }
}