package com.example.kotlinpractice

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinpractice.databinding.FragmentSecondBinding
import com.example.kotlinpractice.models.FriendsViewModel
import com.example.kotlinpractice.models.MyAdapter
import com.example.kotlinpractice.models.Friend
import com.example.kotlinpractice.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth




class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var firebaseAuth: FirebaseAuth
    private val friendsViewModel: FriendsViewModel by activityViewModels()
    var columns = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth = FirebaseAuth.getInstance()
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            (activity as MainActivity).logout()
        }
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.visibility = View.VISIBLE
        binding.progressbar.visibility = View.GONE

        binding.fab.setOnClickListener {
            findNavController().navigate(SecondFragmentDirections.actionSecondFragmentToThirdFragment(-1))
        }

        val adapter = MyAdapter<Friend>(
            emptyList(),
            { position ->

                val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment(position)
                findNavController().navigate(action)
            },
            { binding.progressbar.visibility = View.GONE }
        )
        val currentOrientation = this.resources.configuration
        onConfigurationChanged(currentOrientation)
        binding.recyclerView.layoutManager = GridLayoutManager(this.context,columns)
        binding.recyclerView.adapter = adapter


        binding.progressbar.visibility = View.VISIBLE
        friendsViewModel.friendsLiveData.observe(viewLifecycleOwner) { friends ->

            //binding.root.visibility = View.GONE
            binding.recyclerView.visibility = if (friends == null) View.GONE else View.VISIBLE
            if (friends != null) {

                adapter.updateData(friends)

            }
        }

        friendsViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textviewMessage.text = errorMessage
        }

        friendsViewModel.reload()

        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = true
            friendsViewModel.reload()
            binding.swiperefresh.isRefreshing = false // TODO too early
        }




        binding.buttonSort.setOnClickListener {
            when (binding.spinnerSorting.selectedItemPosition) {
                0 -> friendsViewModel.sortByName()
                1 -> friendsViewModel.sortByNameDescending()
                2 -> friendsViewModel.sortByAge()
                3 -> friendsViewModel.sortByAgeDescending()
                4 -> friendsViewModel.sortByBirthday()
            }
        }

        binding.buttonFilter.setOnClickListener {
            val input = binding.edittextFilterName.text.toString().trim().lowercase()
            try{
                input.toInt()
                friendsViewModel.filterByAge(input)
                Log.d("second fragment", "try ran with name.toInt() -> filterByAge")


            }
            catch(e:Exception){
                friendsViewModel.filterByName(input)
                Log.d("second fragment", "Catch $e \n filterByName ran")

                }

        }
        binding.buttonResetFilter.setOnClickListener{
            friendsViewModel.filterByName("")
            binding.edittextFilterName.setText("")
        }
        
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            columns = 4
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            columns = 2

        }
        binding.recyclerView.layoutManager = GridLayoutManager(this.context,columns)
        friendsViewModel.reload()



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}