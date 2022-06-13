package uz.mobiler.lesson58_2.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.adapters.CourseAdapter
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.database.entity.Course
import uz.mobiler.lesson58_2.databinding.FragmentCourseBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CourseFragment : Fragment() {
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

    private lateinit var binding: FragmentCourseBinding
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var courseList: ArrayList<Course>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseBinding.inflate(inflater, container, false)
        courseList= ArrayList(appDatabase.courseDao().getAllCourses())
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
                    Navigation.findNavController(root).navigate(R.id.courseInfoFragment, bundle)
                }
            })
            rv.adapter = courseAdapter
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            val mDialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_course_dialog, null)
            val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
            mBuilder.setCancelable(false)
            val mAlterDialog = mBuilder.show()
            mAlterDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            mDialogView.findViewById<TextView>(R.id.save).setOnClickListener {
                val name: String = mDialogView.findViewById<TextView>(R.id.name).text.toString()
                val description: String =
                    mDialogView.findViewById<TextView>(R.id.description).text.toString()
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    val course = Course(name = name, description = description)
                    appDatabase.courseDao().addCourse(course)
                    courseList.add(course)
                    courseAdapter.notifyItemChanged(courseList.size)
                    courseAdapter.notifyDataSetChanged()
                    if (courseList.isNotEmpty()) {
                        binding.lottie.visibility = View.INVISIBLE
                    } else {
                        binding.lottie.visibility = View.VISIBLE
                    }
                    mAlterDialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq kiritilmagan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            mDialogView.findViewById<TextView>(R.id.exit).setOnClickListener {
                mAlterDialog.dismiss()
            }
        } else if (item.itemId == android.R.id.home) {
            Navigation.findNavController(binding.root).popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}