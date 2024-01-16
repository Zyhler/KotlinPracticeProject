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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import com.example.kotlinpractice.models.Friend
import com.example.kotlinpractice.databinding.FragmentThirdBinding
import com.example.kotlinpractice.models.FriendsViewModel
import com.example.kotlinpractice.models.MyAdapter
class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    private val friendsViewModel: FriendsViewModel by activityViewModels()

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

        val bundle = requireArguments()
        val thirdFragmentArgs: ThirdFragmentArgs = ThirdFragmentArgs.fromBundle(bundle)
        val position = thirdFragmentArgs.position

        val friend = friendsViewModel[position]

        if (friend == null) {
            binding.textviewMessage.text = "No such friend!"
            return
        }
        val birthdate = friend.birthDayOfMonth.toString()+friend.birthMonth.toString()+friend.birthYear.toString()
        binding.editTextName.setText(friend.name)

        binding.editTextBirthday.setText(birthdate)

        binding.buttonBack.setOnClickListener {

            findNavController().popBackStack()
        }

        binding.buttonDelete.setOnClickListener {
            friendsViewModel.delete(friend.id)
            findNavController().popBackStack()
        }

        binding.buttonUpdate.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val birthdateUpdate = binding.editTextBirthday.text.toString()
            val birthday = birthdateUpdate.split("-")
            val age = getAge(birthday[0].toInt(),birthday[1].toInt(),birthday[2].toInt())

            val updatedFriend = Friend(name,null,birthday[0].toInt(),birthday[1].toInt(),birthday[2].toInt(),null,null,age)
            Log.d("APPLE", "update $updatedFriend")
            friendsViewModel.update(updatedFriend)
            findNavController().popBackStack()
        }


        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(birthYear: Int, birthMonth: Int, birthDay: Int): Int {
        var age: Int = LocalDateTime.now().year - birthYear
        if (birthMonth > LocalDateTime.now().monthValue || birthMonth == LocalDateTime.now().monthValue && birthDay > LocalDateTime.now().dayOfMonth) { age-- }
        if (age < 0) { age = 0 }
        return age
    }
}