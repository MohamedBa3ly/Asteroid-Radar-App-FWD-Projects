package com.example.asteroidradarapp.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.asteroidradarapp.R
import com.example.asteroidradarapp.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    val args: DetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initializing :
        initialize()


        //Display Astronomical Unit Explanation Dialog :
        displayAstronomicalUnitExplanationDialog()

    }

    //Initialize :
    private fun initialize(){
        binding.lifecycleOwner = this
        val asteroid = arguments?.let { DetailFragmentArgs.fromBundle(it).selectedAsteroid }
        binding.asteroid = asteroid
    }

    //Display Astronomical Unit Explanation Dialog :
    private fun displayAstronomicalUnitExplanationDialog() {
        //Show when press on help Button :
        binding.helpButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
                .setMessage(getString(R.string.astronomica_unit_explanation))
                .setPositiveButton(android.R.string.ok, null)
            builder.create().show()
        }
    }

}