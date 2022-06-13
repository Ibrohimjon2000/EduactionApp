package uz.mobiler.lesson58_2.fragment

import android.app.DatePickerDialog
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
import uz.mobiler.lesson58_2.database.entity.Student
import uz.mobiler.lesson58_2.databinding.FragmentStudentEditBinding
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "student"
private const val ARG_PARAM2 = "param2"

class StudentEditFragment : Fragment() {
    private lateinit var param1: Student
    private var param2: String? = null
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Student
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentStudentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentEditBinding.inflate(inflater, container, false)
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            firstName.setText(param1.firstName)
            lastName.setText(param1.lastName)
            patron.setText(param1.patron)
            date.setText((param1.registerDate))
            val calendar = Calendar.getInstance()
            val dp = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                val format = "dd/MM/yyyy"
                val simpleDateFormat = SimpleDateFormat(format, Locale.US)
                date.setText(simpleDateFormat.format(calendar.time))
            }
            date.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dp,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            editStudent.setOnClickListener {
                val firstName = firstName.text
                val lastName = lastName.text
                val patron = patron.text
                val day = date.text.toString()
                val student = Student(
                    id = param1.id,
                    firstName = firstName.toString(),
                    lastName = lastName.toString(),
                    patron = patron.toString(),
                    registerDate = day,
                    groupId = param1.groupId,
                    mentorId = param1.mentorId
                )
                appDatabase.studentDao().editStudent(student)
                Navigation.findNavController(binding.root).popBackStack()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Student, param2: String) =
            StudentEditFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
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