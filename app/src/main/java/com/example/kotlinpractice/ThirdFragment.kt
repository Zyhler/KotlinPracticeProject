package com.example.kotlinpractice

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kotlinpractice.models.Friend
import com.example.kotlinpractice.databinding.FragmentThirdBinding
import com.example.kotlinpractice.models.FriendsViewModel
import com.example.kotlinpractice.models.MyAdapter
import com.google.firebase.auth.FirebaseAuth

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    private val friendsViewModel: FriendsViewModel by activityViewModels()
    //var currentUser = FirebaseAuth.getInstance()getCurrentUser()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var friend = Friend(-1,"Name of Friend",null,2000,1,1,null,null,null)

        try {
            val bundle = requireArguments()
            val thirdFragmentArgs: ThirdFragmentArgs = ThirdFragmentArgs.fromBundle(bundle)
            val position = thirdFragmentArgs.position
            friend = friendsViewModel[position.toInt()]!!


        }
        catch (e: Exception)
        {
            Log.e("Error circumvention","${e.message}: This error was used to send id -1 for add friend TODO fix")

            /*
            binding.textviewMessage.text = "Add Friend!"
            binding.buttonAddfriend.setOnClickListener {
                val name = binding.editTextName.text.toString().trim()
                val birthdateUpdate = binding.editTextBirthday.text.toString()
                val birthday = birthdateUpdate.split("/")
                val newFriend = Friend(name,null,birthday[2].toInt(),birthday[1].toInt(),birthday[1].toInt(),null,null,null)
                friendsViewModel.add(newFriend)
                findNavController().popBackStack()
                }*/



        }

        if (friend.id == -1) {
            val userEmail = FirebaseAuth.getInstance().currentUser?.email
            binding.textviewMessage.text = "Add Friend!"
            binding.buttonDelete.visibility = View.GONE
            binding.buttonUpdate.visibility = View.GONE
            binding.buttonAddfriend.setOnClickListener {
                val name = binding.editTextName.text.toString().trim()
                val birthdateUpdate = binding.editTextBirthday.text.toString()
                val birthday = birthdateUpdate.split("/")
                val newFriend = Friend(name,userEmail,birthday[2].toInt(),birthday[1].toInt(),birthday[1].toInt(),null,null,null)
                friendsViewModel.add(newFriend)
                findNavController().popBackStack()
            }

        }
        else
        {
            binding.buttonAddfriend.visibility = View.GONE

            val birthdate = friend.birthDayOfMonth.toString()+"/"+friend.birthMonth.toString()+"/"+friend.birthYear.toString()

            binding.editTextName.setText(friend.name)
            binding.editTextBirthday.setText(birthdate)



            binding.buttonDelete.setOnClickListener {
                friendsViewModel.delete(friend.id)
                findNavController().popBackStack()
            }
            binding.buttonBack.setOnClickListener {

                findNavController().popBackStack()
            }



            binding.buttonUpdate.setOnClickListener {
                val name = binding.editTextName.text.toString().trim()
                val birthdateUpdate = binding.editTextBirthday.text.toString()
                val birthday = birthdateUpdate.split("/")
                //val age = getAge(birthday[2].toInt(),birthday[1].toInt(),birthday[0].toInt())
                // Det viser sig at API'en Regner age ud.

                val updatedFriend = Friend(friend.id,name,friend.userId,birthday[2].toInt(),birthday[1].toInt(),birthday[1].toInt(),null,null,null)
                Log.d("ThirdFragment", "update $updatedFriend")
                friendsViewModel.update(updatedFriend)
                findNavController().popBackStack()
            }
        }




        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    /* API Sørger for Age
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(birthYear: Int, birthMonth: Int, birthDay: Int): Int {
        var age: Int = LocalDateTime.now().year - birthYear

        Log.d("APPLE", LocalDateTime.now().year.toString())
        Log.d("APPLE", birthYear.toString())
        Log.d("APPLE", age.toString())
        if (birthMonth > LocalDateTime.now().monthValue || birthMonth == LocalDateTime.now().monthValue && birthDay > LocalDateTime.now().dayOfMonth) { age-- }
        if (age < 0) { age = 0 }
        return age
    }*/
}