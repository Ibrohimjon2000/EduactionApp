package uz.mobiler.lesson58_2.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.adapters.MentorAdapter
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.database.entity.Mentor
import uz.mobiler.lesson58_2.databinding.FragmentMentorInfoBinding

private const val ARG_PARAM1 = "position"

class MentorInfoFragment : Fragment() {
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

    private lateinit var binding: FragmentMentorInfoBinding
    private lateinit var mentorAdapter: MentorAdapter
    private lateinit var mentorList: ArrayList<Mentor>
    private var courseId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMentorInfoBinding.inflate(inflater, container, false)
        val course = appDatabase.courseDao().getCourseById(param1)
        courseId = course.id
        mentorList = ArrayList(appDatabase.mentorDao().getAllMentors(courseId))
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.title = course.name
            if (mentorList.isNotEmpty()) {
                lottie.visibility = View.INVISIBLE
            } else {
                lottie.visibility = View.VISIBLE
            }
            mentorAdapter = MentorAdapter(mentorList, object : MentorAdapter.OnItemClickListener {
                override fun onItemEdit(mentor: Mentor, position: Int) {
                    val mDialogView =
                        LayoutInflater.from(requireContext())
                            .inflate(R.layout.edit_mentor_dialog, null)
                    val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
                    mBuilder.setCancelable(false)
                    val mAlterDialog = mBuilder.show()
                    val firstNameMentor = mDialogView.findViewById<TextView>(R.id.firstNameMentor)
                    val lastNameMentor = mDialogView.findViewById<TextView>(R.id.lastNameMentor)
                    val patronMentor = mDialogView.findViewById<TextView>(R.id.patronMentor)
                    mAlterDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    firstNameMentor.text = mentor.firstName
                    lastNameMentor.text = mentor.lastName
                    patronMentor.text = mentor.patron
                    mDialogView.findViewById<TextView>(R.id.editMentor).setOnClickListener {
                        val f: String =
                            firstNameMentor.text.toString()
                        val l: String =
                            lastNameMentor.text.toString()
                        val p: String =
                            patronMentor.text.toString()
                        val m = Mentor(
                            id = mentor.id,
                            firstName = f,
                            lastName = l,
                            patron = p,
                            courseId = mentor.courseId
                        )
                        appDatabase.mentorDao().editMentor(m)
                        mentorList[position] = m
                        mentorAdapter.notifyDataSetChanged()
                        mAlterDialog.dismiss()
                    }
                    mDialogView.findViewById<TextView>(R.id.exitMentor).setOnClickListener {
                        mAlterDialog.dismiss()
                    }
                }

                override fun onItemDelete(mentor: Mentor, position: Int) {
                    appDatabase.mentorDao().deleteMentor(mentor)
                    mentorList.remove(mentor)
                    if (mentorList.isNotEmpty()) {
                        lottie.visibility = View.INVISIBLE
                    } else {
                        lottie.visibility = View.VISIBLE
                    }
                    mentorAdapter.notifyItemRemoved(position)
                    mentorAdapter.notifyItemRangeChanged(position, mentorList.size)

                }
            })
            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            rv.addItemDecoration(dividerItemDecoration)
            rv.adapter = mentorAdapter
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            MentorInfoFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            val bundle = Bundle()
            bundle.putLong("id", courseId)
            Navigation.findNavController(binding.root).navigate(R.id.addMentorFragment, bundle)
        } else if (item.itemId == android.R.id.home) {
            Navigation.findNavController(binding.root).popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}