package com.example.asteroidradarapp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asteroidradarapp.Asteroid
import com.example.asteroidradarapp.R
import com.example.asteroidradarapp.databinding.AsteroidRecyclerviewCardBinding
import com.example.asteroidradarapp.main.MainViewModel

class AsteroidAdapter : RecyclerView.Adapter<AsteroidAdapter.MyViewHolder>() {

    var asteroidList = emptyList<Asteroid>()
    private lateinit var asteroidViewModel: MainViewModel

    //make a Listener :
    private lateinit var asteroidListener: OnClick

    //Make an OnClick interface :
    interface OnClick {
        fun onItemClickListener(pos: Int)
    }

    fun setOnItemClickListenerAsteroid(listener: OnClick) {
        asteroidListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AsteroidRecyclerviewCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), asteroidListener
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedAsteroid = asteroidList[position]
        holder.binding.nameCard.text = selectedAsteroid.codename
        holder.binding.dateCard.text = selectedAsteroid.closeApproachDate
        if (selectedAsteroid.isPotentiallyHazardous) {
            holder.binding.imageCard.setImageResource(R.drawable.ic_status_potentially_hazardous)
        } else {
            holder.binding.imageCard.setImageResource(R.drawable.ic_status_normal)
        }
    }

    override fun getItemCount(): Int {
        return asteroidList.size
    }

    class MyViewHolder(var binding: AsteroidRecyclerviewCardBinding, listener: OnClick) :
        RecyclerView.ViewHolder(binding.root) {

        //init listener (when press on any position on card layout in recyclerView list) :
        init {
            binding.cardLayout.setOnClickListener {
                listener.onItemClickListener(adapterPosition)
            }
        }
    }

    //To set data in adapter :
    fun setAsteroidData(apiData: List<Asteroid>) {
        this.asteroidList = apiData
        notifyDataSetChanged()
    }

    //fun to Setup ViewModel in adapter :
    fun setUpViewModel(vm: MainViewModel) {
        asteroidViewModel = vm
    }
}