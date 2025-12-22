package com.idol.prank.call.chat.video.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.idol.prank.call.chat.video.R

class ImagePagerAdapter(private val context: Context) : PagerAdapter() {

    private val imageSets = arrayOf(
        intArrayOf(R.drawable.onb_one),
        intArrayOf(R.drawable.onb_two),
        intArrayOf(R.drawable.onb_three),
        intArrayOf(R.drawable.onb_four),


    )

    private val titles = arrayOf(
        "Make Funny Prank Calls",
        "Realistic Video Calling",
        "Chat With Your Friends",
        "Enjoy Safe Entertainment"
    )

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.viewpager_layout_new, container, false)
        val imageView1 = layout.findViewById<ImageView>(R.id.image1)
        val textView = layout.findViewById<TextView>(R.id.tvTitle)

        imageView1.setImageResource(imageSets[position][0])
        textView.text = titles[position]
        /*imageView1.setImageResource(imageSets[position][1])
        imageView1.setImageResource(imageSets[position][2])*/
        /* imageView4.setImageResource(imageSets[position][3])
         imageView5.setImageResource(imageSets[position][4])
         imageView6.setImageResource(imageSets[position][5])*/

        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getCount(): Int {
        return imageSets.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }
}