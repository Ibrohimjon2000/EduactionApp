package uz.mobiler.lesson58_2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.database.entity.Mentor
import uz.mobiler.lesson58_2.databinding.FragmentAddMentorBinding

private const val ARG_PARAM1 = "id"

class AddMentorFragment : Fragment() {
    private var param1: Long = -1
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentAddMentorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMentorBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            val courseId = param1
            addMentor.setOnClickListener {
                val firstName = firstName.text.toString()
                val lastName = lastName.text.toString()
                val patron = patron.text.toString()
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && patron.isNotEmpty()) {
                    val mentor = Mentor(
                        firstName = firstName,
                        lastName = lastName,
                        patron = patron,
                        courseId = courseId
                    )
                    appDatabase.mentorDao().addMentor(mentor)
                    Navigation.findNavController(binding.root).popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq kiritilmagan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            AddMentorFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) Navigation.findNavController(binding.root)
            .popBackStack()
        return super.onOptionsItemSelected(item)
    }
}