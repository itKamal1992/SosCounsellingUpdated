package com.example.soscounsellingapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.soscounsellingapp.R

/**
 * Created by Kamal on 18-04-2018.
 */
//Class for ViewPager
class ViewPagerAdapter(//Declaration
    private val context: Context
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    //Resource Provider - Images in Array
    private val images = arrayOf<Int>(
        R.drawable.common_img,
        R.drawable.sos_akola_birla_two,
        R.drawable.sos_akola_birla_three,
        R.drawable.sos_akola_birla_four,
        R.drawable.sos_akola_birla_five
    )

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.custom_layout, null)
        val imageView = view.findViewById(R.id.imageView) as ImageView
        imageView.setImageResource(images[position])
        view.setOnClickListener(View.OnClickListener { })
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}
