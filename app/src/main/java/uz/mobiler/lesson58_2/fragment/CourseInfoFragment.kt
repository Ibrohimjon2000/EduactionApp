package uz.mobiler.lesson58_2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.databinding.FragmentCourseInfoBinding

private const val ARG_PARAM1 = "position"

class CourseInfoFragment : Fragment() {
    private var param1: Long = 1
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

    private lateinit var binding: FragmentCourseInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseInfoBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            val course = appDatabase.courseDao().getCourseById(param1)
            actionBar?.title = course.name
            description.text = course.description
            addStudent.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("id", course.id)
                Navigation.findNavController(root).navigate(R.id.addStudentFragment, bundle)
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            CourseInfoFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            Navigation.findNavController(binding.root).popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}