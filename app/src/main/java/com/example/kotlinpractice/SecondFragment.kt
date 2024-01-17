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
import com.example.kotlinpractice.SecondFragmentDirections
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val friendsViewModel: FriendsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.visibility = View.VISIBLE
        binding.progressbar.visibility = View.GONE


        // Set click listener for the FAB
        binding.fab.setOnClickListener {
            findNavController().navigate(SecondFragmentDirections.actionSecondFragmentToThirdFragment("-1"))
        }
        /*val adapter = MyAdapter<Friend>(emptyList()) { position ->
            val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment(position.toString())
            findNavController().navigate(action)
        }*/
        val adapter = MyAdapter<Friend>(
            emptyList(),
            { position ->
                // Your logic for item click, e.g., navigation or other actions
                val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment(position.toString())
                findNavController().navigate(action)
            },
            { binding.progressbar.visibility = View.GONE }
        )
        var columns = 2
        val currentOrientation = this.resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 4
        } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            columns = 2
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this.context,columns)
        binding.recyclerView.adapter = adapter

        binding.progressbar.visibility = View.VISIBLE
        friendsViewModel.friendsLiveData.observe(viewLifecycleOwner) { friends ->
            //binding.root.visibility = View.GONE
            binding.recyclerView.visibility = if (friends == null) View.GONE else View.VISIBLE
            if (friends != null) {
                adapter.updateData(friends)
                /*binding.recyclerView.layoutManager = GridLayoutManager(this.context, columns)
                  binding.recyclerView.adapter = adapter

                 */
                for(friend: Friend in friends) {
                    Log.d("APPLE", friend.toString())
                }
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

        friendsViewModel.friendsLiveData.observe(viewLifecycleOwner) { friends ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, friends)
            binding.spinnerFriends.adapter = adapter

        }

        binding.buttonShowDetails.setOnClickListener {
            val position = binding.spinnerFriends.selectedItemPosition
            val action =
                SecondFragmentDirections.actionSecondFragmentToThirdFragment(position.toString())
            findNavController().navigate(action)
        }

        binding.buttonSort.setOnClickListener {
            when (binding.spinnerSorting.selectedItemPosition) {
                0 -> friendsViewModel.sortByName()
                1 -> friendsViewModel.sortByNameDescending()
               /* 2 -> friendsViewModel.sortByPrice()
                3 -> friendsViewModel.sortByPriceDescending()*/
            }
        }

        binding.buttonFilter.setOnClickListener {
            val name = binding.edittextFilterName.text.toString().trim()
            /* if (title.isBlank()) {
                 binding.edittextFilterTitle.error = "No title"
                 return@setOnClickListener
             }*/
            friendsViewModel.filterByName(name)
        }
        
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}