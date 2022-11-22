package com.example.asteroidradarapp.main

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.asteroidradarapp.Asteroid
import com.example.asteroidradarapp.Constants
import com.example.asteroidradarapp.R
import com.example.asteroidradarapp.api.getNextSevenDaysFormattedDates
import com.example.asteroidradarapp.databinding.FragmentMainBinding
import com.example.asteroidradarapp.recyclerview.AsteroidAdapter
import com.example.asteroidradarapp.work.AsteroidsWorker
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    lateinit var viewModel: MainViewModel
    private val adapter = AsteroidAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing :
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initialize()

        //Add menu in actionbar :
        addMenu()

        //To confirm that internet is available : (if not : app will show the data that saved in room database ) :
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {

            //Add Photo from Nasa Api To Image view in mainFragment (loading State when upload photo) :
            photoOfTheDay()

            //Add data from Nasa Api to Room Data Base (Work Manager):
//            setupRecurringWork()
        }

        //Adapter :
        setAdapter()

        //Click on any items in the list to go to Detail Fragment :
        detailFragmentItems()

    }

    //Fun Initializing :
    private fun initialize() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    //Fun to Add menu in actionbar :
    private fun addMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.show_week_asteroid -> {
                        viewModel.getWeekAsteroid.observe(viewLifecycleOwner, Observer {
                            adapter.setAsteroidData(it)
                        })
                        return true
                    }
                    R.id.show_today_asteroid -> {
                        viewModel.getTodayAsteroid.observe(viewLifecycleOwner, Observer {
                            adapter.setAsteroidData(it)
                        })
                        return true
                    }
                    R.id.show_all_asteroid -> {
                        viewModel.readAllAsteroidDetails.observe(viewLifecycleOwner, Observer {
                            adapter.setAsteroidData(it)
                        })
                        return true
                    }
                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    //Fun to Add Photo from Nasa Api To Image view in mainFragment :
    private fun photoOfTheDay() {
        viewModel.getPictureFinally(Constants.API_KEY)
        viewModel.customImage.observe(viewLifecycleOwner, Observer {
            val mediaType = it.toData()?.mediaType
            val title = it.toData()?.title
            val url = it.toData()?.url
            if (mediaType == "image") {
                Picasso.Builder(binding.activityMainImageOfTheDay.context).build()
                    .load(url?.toUri()).into(binding.activityMainImageOfTheDay)
                binding.textView.text = title
            }
        })
    }

    // Fun to make a periodic request via work manager to catch the data from nasa api :
    private fun setupRecurringWork() {
        //Wifi and Charging is Enabled :
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).
        setRequiresCharging(true).
        build()

        val repeatingRequest =
            PeriodicWorkRequest.Builder(AsteroidsWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints).build()
        WorkManager.getInstance(requireContext()).enqueue(repeatingRequest)
    }

    //Fun to set data in recycler view through adapter :
    private fun setAdapter() {
        //SetUp viewModel in adapter:
        adapter.setUpViewModel(viewModel)

        //SetUp adapter and read data from roomDataBase in RecyclerView :
        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.readAllAsteroidDetails.observe(viewLifecycleOwner, Observer {
            adapter.setAsteroidData(it)
        })
    }

    //Fun to Click on any items in the list to go to Detail Fragment :
    private fun detailFragmentItems() {
        adapter.setOnItemClickListenerAsteroid(object : AsteroidAdapter.OnClick {
            override fun onItemClickListener(pos: Int) {
                findNavController().navigate(MainFragmentDirections.mainToDetail(adapter.asteroidList[pos]))
            }
        })
    }
}


