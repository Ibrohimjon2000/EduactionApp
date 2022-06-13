package uz.mobiler.lesson58_2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import uz.mobiler.lesson58_2.R
import uz.mobiler.lesson58_2.adapters.ViewPager2FragmentAdapter
import uz.mobiler.lesson58_2.database.AppDatabase
import uz.mobiler.lesson58_2.databinding.FragmentGroupInfoBinding

private const val ARG_PARAM1 = "position"

class GroupInfoFragment : Fragment() {
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

    private lateinit var binding: FragmentGroupInfoBinding
    private lateinit var viewPager2FragmentAdapter: ViewPager2FragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupInfoBinding.inflate(inflater, container, false)
        val course = appDatabase.courseDao().getCourseById(param1)
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.setDisplayShowHomeEnabled(true)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.title = course.name
            work()
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 0) {
                        toolbar.menu.clear()
                    } else {
                        toolbar.inflateMenu(R.menu.add_menu)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }
        return binding.root
    }

    private fun work() {
        val list = ArrayList<String>()
        list.add("1")
        list.add("2")
        viewPager2FragmentAdapter = ViewPager2FragmentAdapter(requireParentFragment(), list, param1)
        binding.viewPager.adapter = viewPager2FragmentAdapter
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Ochilgan guruhlar"
                    1 -> tab.text = "Ochilayotgan guruhlar"
                }
            }
        tabLayoutMediator.attach()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Long) =
            GroupInfoFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM1, param1)
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            val bundle = Bundle()
            bundle.putLong("id", param1)
            Navigation.findNavController(binding.root).navigate(R.id.addGroupFragment, bundle)
        } else if (item.itemId == android.R.id.home) Navigation.findNavController(binding.root)
            .popBackStack()
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        work()
    }
}