package to.freebots.todobutler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mikepenz.aboutlibraries.LibsBuilder
import to.freebots.todobutler.R

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = LibsBuilder()
            .withFields(R.string::class.java.fields)
            .withAboutSpecial1(getString(R.string.developer_title))
            .withAboutSpecial1Description(getString(R.string.developer))
            .withAboutSpecial2(getString(R.string.icons_title))
            .withAboutSpecial2Description(getString(R.string.icons_license))
            .supportFragment()

        childFragmentManager.beginTransaction().replace(R.id.f_license, fragment).commit()
    }
}
