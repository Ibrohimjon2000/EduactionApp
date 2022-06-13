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
import uz.mobiler.lesson58_2.adapters.CourseAdapter
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.database.entity.Course
import uz.mobiler.lesson58_2.databinding.FragmentMentorBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MentorFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentMentorBinding
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var courseList: ArrayList<Course>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMentorBinding.inflate(inflater, container, false)
        courseList = ArrayList(appDatabase.courseDao().getAllCourses())
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            if (courseList.isNotEmpty()) {
                lottie.visibility = View.INVISIBLE
            } else {
                lottie.visibility = View.VISIBLE
            }
            courseAdapter = CourseAdapter(courseList, object : CourseAdapter.OnItemClickListener {
                override fun onItemClick(course: Course, position: Int) {
                    val bundle = Bundle()
                    bundle.putLong("position", position.toLong() + 1)
                    Navigation.findNavController(root).navigate(R.id.mentorInfoFragment, bundle)
                }
            })
            rv.adapter = courseAdapter
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MentorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) Navigation.findNavController(binding.root)
            .popBackStack()
        return super.onOptionsItemSelected(item)
    }
}