package com.example.kotlinpractice


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kotlinpractice.models.Friend
import com.example.kotlinpractice.databinding.FragmentThirdBinding
import com.example.kotlinpractice.models.FriendsViewModel
import com.google.firebase.auth.FirebaseAuth

class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!
    private val friendsViewModel: FriendsViewModel by activityViewModels()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email

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


        //


        try {
            val bundle = requireArguments()
            val thirdFragmentArgs: ThirdFragmentArgs = ThirdFragmentArgs.fromBundle(bundle)
            val position = thirdFragmentArgs.position
            if (position == -1) {
                //var friend = Friend(-1, "Name of Friend", userEmail, 2000, 1, 1, null, null, null)
                binding.textviewMessage.text = "Add friend!"
                binding.buttonDelete.visibility = View.GONE
                binding.buttonUpdate.visibility = View.GONE
                binding.buttonAddFriend.setOnClickListener {
                    var name = binding.editTextName.text.toString().trim()
                    var birthdateUpdate = binding.editTextBirthday.text.toString()
                    var birthday = checkListLength(birthdateUpdate)

                    var comments = binding.editTextRemarks.text.toString()
                    var imageurl = binding.editTextImageUrl.text.toString()

                    val newFriend = Friend(
                        name,
                        userEmail,
                        birthday[2].toInt(),
                        birthday[1].toInt(),
                        birthday[0].toInt(),
                        comments,
                        imageurl,
                        null
                    )
                    friendsViewModel.add(newFriend)
                    findNavController().popBackStack()
                }


                /* FUTURE
                val imageView = view.findViewById<ImageView>(R.id.imageView)


                val imageLoader = ImageLoader.getInstance()


                imageLoader.displayImage(friend.pictureURL, imageView)*/


            } else {

                binding.buttonAddFriend.visibility = View.GONE
                val friend = friendsViewModel[position]
                if (friend != null) {
                    val birthdate =
                        friend.birthDayOfMonth.toString() + "/" + friend.birthMonth.toString() + "/" + friend.birthYear.toString()


                    binding.editTextName.setText(friend.name)
                    binding.editTextBirthday.setText(birthdate)
                    binding.editTextRemarks.setText(friend.remarks)
                    binding.editTextImageUrl.setText(friend.pictureUrl)



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
                        val birthday = checkListLength(birthdateUpdate)

                        val comments = binding.editTextRemarks.text.toString()
                        var imageurl = binding.editTextImageUrl.text.toString()


                        val updatedFriend = Friend(
                            friend.id,
                            name,
                            userEmail,
                            birthday[2].toInt(),
                            birthday[1].toInt(),
                            birthday[0].toInt(),
                            comments,
                            imageurl,
                            null
                        )
                        Log.d("ThirdFragment", "update $updatedFriend")
                        friendsViewModel.update(updatedFriend)
                        findNavController().popBackStack()
                    }

                } else {

                    Toast.makeText(
                        requireContext(),
                        "Error: Found no such friend, redirecting",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()

                }
            }
        } catch (e: Exception) {
            Log.e(
                "Error APPLE",
                "${e.message}:"
            )

        }


    }

    private fun checkListLength(input: String): List<String> {
        val birthdayDefault = "1/1/2000"

        var birthdayList = birthdayDefault.split("/")


        if (input.split("/").size != 3) {
            Toast.makeText(
                requireContext(),
                "Birthdate error, defaulted to 1/1/2000",
                Toast.LENGTH_LONG
            ).show()

        } else {
            birthdayList = input.split("/")

        }
        return birthdayList
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}