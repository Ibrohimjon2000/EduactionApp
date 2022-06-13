package uz.mobiler.lesson58_2.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.adapters.GroupAdapter
import uz.mobiler.lesson58_2.adapters.SpinnerMentorAdapter
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.database.entity.Group
import uz.mobiler.lesson58_2.database.entity.Mentor
import uz.mobiler.lesson58_2.database.entity.Student
import uz.mobiler.lesson58_2.databinding.FragmentGroupCreateBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupCreateFragment : Fragment() {
    private var param1: String? = null
    private var param2: Long = -1
    val appDatabase: AppDatabase by lazy {

        AppDatabase.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getLong(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentGroupCreateBinding
    private lateinit var groupList: ArrayList<Group>
    private lateinit var groupOpenList: ArrayList<Group>
    private lateinit var groupClosedList: ArrayList<Group>
    private lateinit var studentList: ArrayList<Student>
    private lateinit var mentorList: ArrayList<Mentor>
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val list = arrayOf(
            "09:00–11:00",
            "11:00–13:00",
            "14:00–16:00",
            "16:00–18:00",
            "18:00–20:00",
            "20:00–22:00"
        )
        groupList = ArrayList(appDatabase.groupDao().getAllIdGroup(param2))
        groupOpenList = ArrayList(appDatabase.groupDao().getOpenAllIdGroup(param2))
        groupClosedList = ArrayList(appDatabase.groupDao().getClosedAllIdGroup(param2))
        mentorList = ArrayList(appDatabase.mentorDao().getAllMentors(param2))
        binding = FragmentGroupCreateBinding.inflate(inflater, container, false)
        var mentorIds: Long = -1
        var dateTimes = ""
        binding.apply {
            when (param1.toString()) {
                "1" -> {
                    if (groupClosedList.isNotEmpty()) {
                        lottie.visibility = View.INVISIBLE
                    } else {
                        lottie.visibility = View.VISIBLE
                    }
                    groupAdapter =
                        GroupAdapter(groupClosedList, object : GroupAdapter.OnItemClickListener {
                            override fun onItemClick(group: Group, position: Int) {
                                val bundle = Bundle()
                                bundle.putSerializable("group", group)
                                Navigation.findNavController(root)
                                    .navigate(R.id.groupBeginFragment, bundle)
                            }

                            override fun onItemEdit(group: Group, position: Int) {
                                val mDialogView =
                                    LayoutInflater.from(requireContext())
                                        .inflate(R.layout.edit_group_dialog, null)
                                val mBuilder =
                                    AlertDialog.Builder(requireContext()).setView(mDialogView)
                                mBuilder.setCancelable(false)
                                val mAlterDialog = mBuilder.show()
                                val groupName =
                                    mDialogView.findViewById<TextView>(R.id.editGroupName)
                                val mentorId =
                                    mDialogView.findViewById<Spinner>(R.id.editMentorName)
                                val dateTime = mDialogView.findViewById<Spinner>(R.id.editTime)
                                mAlterDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                val adapter0 = SpinnerMentorAdapter(requireContext(), mentorList)
                                mentorId.adapter = adapter0
                                mentorId.setSelection(group.mentorId.toInt() - 1)
                                groupName.text = group.name
                                mentorId.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
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
                                dateTime.adapter = adapter1
                                var index = -1;
                                for (i in list.indices) {
                                    if (group.date == list[i]) {
                                        index = i
                                    }
                                }
                                dateTime.setSelection(index)
                                dateTime.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            p0: AdapterView<*>?,
                                            p1: View?,
                                            position: Int,
                                            p3: Long
                                        ) {
                                            dateTimes = p0?.getItemAtPosition(position).toString()
                                        }

                                        override fun onNothingSelected(p0: AdapterView<*>?) {

                                        }
                                    }
                                mDialogView.findViewById<TextView>(R.id.editMentor)
                                    .setOnClickListener {
                                        val g: String =
                                            groupName.text.toString()
                                        val m = Group(
                                            id = group.id,
                                            name = g,
                                            isOpen = group.isOpen,
                                            date = dateTimes,
                                            dateType = group.dateType,
                                            courseId = group.courseId,
                                            mentorId = mentorIds
                                        )
                                        appDatabase.groupDao().editGroup(m)
                                        groupClosedList[position] = m
                                        groupAdapter.notifyDataSetChanged()
                                        mAlterDialog.dismiss()
                                    }
                                mDialogView.findViewById<TextView>(R.id.exitMentor)
                                    .setOnClickListener {
                                        mAlterDialog.dismiss()
                                    }
                            }

                            override fun onItemDelete(group: Group, position: Int) {
                                appDatabase.groupDao().deleteGroup(group)
                                groupClosedList.remove(group)
                                if (groupClosedList.isNotEmpty()) {
                                    lottie.visibility = View.INVISIBLE
                                } else {
                                    lottie.visibility = View.VISIBLE
                                }
                                groupAdapter.notifyItemRemoved(position)
                                groupAdapter.notifyItemRangeChanged(position, groupClosedList.size)
                            }
                        }, requireContext())
                    val dividerItemDecoration =
                        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                    rv.addItemDecoration(dividerItemDecoration)
                    rv.adapter = groupAdapter
                }
                "2" -> {
                    if (groupOpenList.isNotEmpty()) {
                        lottie.visibility = View.INVISIBLE
                    } else {
                        lottie.visibility = View.VISIBLE
                    }
                    groupAdapter =
                        GroupAdapter(groupOpenList, object : GroupAdapter.OnItemClickListener {
                            override fun onItemClick(group: Group, position: Int) {
                                val bundle = Bundle()
                                bundle.putSerializable("group", group)
                                Navigation.findNavController(root)
                                    .navigate(R.id.groupStudentFragment, bundle)
                            }

                            override fun onItemEdit(group: Group, position: Int) {
                                val mDialogView =
                                    LayoutInflater.from(requireContext())
                                        .inflate(R.layout.edit_group_dialog, null)
                                val mBuilder =
                                    AlertDialog.Builder(requireContext()).setView(mDialogView)
                                mBuilder.setCancelable(false)
                                val mAlterDialog = mBuilder.show()
                                val groupName =
                                    mDialogView.findViewById<TextView>(R.id.editGroupName)
                                val mentorId =
                                    mDialogView.findViewById<Spinner>(R.id.editMentorName)
                                val dateTime = mDialogView.findViewById<Spinner>(R.id.editTime)
                                val adapter0 = SpinnerMentorAdapter(requireContext(), mentorList)
                                mAlterDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                mentorId.adapter = adapter0
                                mentorId.setSelection(group.mentorId.toInt() - 1)
                                groupName.text = group.name
                                mentorId.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
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
                                dateTime.adapter = adapter1
                                var index = -1;
                                for (i in list.indices) {
                                    if (group.date == list[i]) {
                                        index = i
                                    }
                                }
                                dateTime.setSelection(index)
                                dateTime.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            p0: AdapterView<*>?,
                                            p1: View?,
                                            position: Int,
                                            p3: Long
                                        ) {
                                            dateTimes = p0?.getItemAtPosition(position).toString()
                                        }

                                        override fun onNothingSelected(p0: AdapterView<*>?) {

                                        }
                                    }
                                mDialogView.findViewById<TextView>(R.id.editMentor)
                                    .setOnClickListener {
                                        val g: String =
                                            groupName.text.toString()
                                        val m = Group(
                                            id = group.id,
                                            name = g,
                                            isOpen = group.isOpen,
                                            date = dateTimes,
                                            dateType = group.dateType,
                                            courseId = group.courseId,
                                            mentorId = mentorIds
                                        )
                                        appDatabase.groupDao().editGroup(m)
                                        groupOpenList[position] = m
                                        groupAdapter.notifyDataSetChanged()
                                        mAlterDialog.dismiss()
                                    }
                                mDialogView.findViewById<TextView>(R.id.exitMentor)
                                    .setOnClickListener {
                                        mAlterDialog.dismiss()
                                    }
                            }

                            override fun onItemDelete(group: Group, position: Int) {
                                appDatabase.groupDao().deleteGroup(group)
                                groupOpenList.remove(group)
                                if (groupOpenList.isNotEmpty()) {
                                    lottie.visibility = View.INVISIBLE
                                } else {
                                    lottie.visibility = View.VISIBLE
                                }
                                groupAdapter.notifyItemRemoved(position)
                                groupAdapter.notifyItemRangeChanged(position, groupOpenList.size)
                            }
                        }, requireContext())
                    val dividerItemDecoration =
                        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                    rv.addItemDecoration(dividerItemDecoration)
                    rv.adapter = groupAdapter
                }
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Long) =
            GroupCreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putLong(ARG_PARAM2, param2)
                }
            }
    }
}