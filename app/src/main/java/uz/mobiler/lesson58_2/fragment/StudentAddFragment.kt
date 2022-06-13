package uz.mobiler.lesson58_2.fragment

import android.app.DatePickerDialog
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
import uz.mobiler.lesson58_2.database.entity.Student
import uz.mobiler.lesson58_2.databinding.FragmentStudentAddBinding
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "id"
private const val ARG_PARAM2 = "param2"

class StudentAddFragment : Fragment() {
    private var param1: Long = -1
    private var param2: String? = null
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentStudentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentAddBinding.inflate(inflater, container, false)
        val group = appDatabase.groupDao().getGroupById(param1)
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
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
            addStudent.setOnClickListener {
                val firstName = firstName.text
                val lastName = lastName.text
                val patron = patron.text
                val day = date.text.toString()
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && patron.isNotEmpty() && day.isNotEmpty()) {
                    val student = Student(
                        firstName = firstName.toString(),
                        lastName = lastName.toString(),
                        patron = patron.toString(),
                        registerDate = day,
                        groupId = group.id,
                        mentorId = group.mentorId
                    )
                    appDatabase.studentDao().addStudent(student)
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
        fun newInstance(param1: Long, param2: String) =
            StudentAddFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
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