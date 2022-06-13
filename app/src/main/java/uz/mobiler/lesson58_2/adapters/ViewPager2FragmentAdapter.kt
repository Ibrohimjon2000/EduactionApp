package uz.mobiler.lesson58_2.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.mobiler.lesson58_2.fragment.GroupCreateFragment

class ViewPager2FragmentAdapter(
    fragment: Fragment,
    private val list: List<String>,
    private val id: Long
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return GroupCreateFragment.newInstance(list[position], id)
    }
}