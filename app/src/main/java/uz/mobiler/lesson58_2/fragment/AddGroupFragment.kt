package uz.mobiler.lesson58_2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.adapters.SpinnerMentorAdapter
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.database.entity.Group
import uz.mobiler.lesson58_2.database.entity.Mentor
import uz.mobiler.lesson58_2.databinding.FragmentAddGroupBinding

private const val ARG_PARAM1 = "id"
private const val ARG_PARAM2 = "param2"

class AddGroupFragment : Fragment() {
    private var param1: Long = -1
    private var param2: Long = -1
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getLong(ARG_PARAM1)
            param2 = it.getLong(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentAddGroupBinding
    private lateinit var mentorList: ArrayList<Mentor>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        mentorList = ArrayList(appDatabase.mentorDao().getAllMentors(param1))
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            var groupName: String
            var mentorIds: Long = -1
            var dateTime = ""
            var dateTaype = ""
            val adapter0 = SpinnerMentorAdapter(requireContext(), mentorList)
            mentor.adapter = adapter0
            mentor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    mentorIds = mentorList[position].id
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
            val adapter1 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.date,
                android.R.layout.simple_spinner_item
            )
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            date.adapter = adapter1
            date.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    dateTime = p0?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

            val adapter2 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.dateType,
                android.R.layout.simple_spinner_item
            )
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dateType.adapter = adapter2
            dateType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    dateTaype = p0?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
            saveGroup.setOnClickListener {
                groupName = groupN.text.toString()
                if (groupName.isNotEmpty() && dateTime.isNotEmpty() && dateTaype.isNotEmpty() && mentorIds != -1L) {
                    val group = Group(
                        name = groupName,
                        isOpen = "Open",
                        date = dateTime,
                        dateType = dateTaype,
                        courseId = param1,
                        mentorId = mentorIds
                    )
                    appDatabase.groupDao().addGroup(group)
                    Navigation.findNavController(binding.root)
                        .popBackStack()
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
        fun newInstance(param1: Long, param2: Long) =
            AddGroupFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                    putLong(ARG_PARAM2, param2)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) Navigation.findNavController(binding.root)
            .popBackStack()
        return super.onOptionsItemSelected(item)
    }
}
