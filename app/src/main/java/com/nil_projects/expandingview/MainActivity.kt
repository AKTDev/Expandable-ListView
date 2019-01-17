package com.nil_projects.expandingview


import android.app.AlertDialog
import android.content.DialogInterface
import android.media.Image
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import com.diegodobelo.expandingview.ExpandingItem
import com.diegodobelo.expandingview.ExpandingList


class MainActivity : AppCompatActivity() {
    private var mExpandingList: ExpandingList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mExpandingList = findViewById(R.id.expanding_list_main)
        createItems()
    }

    private fun createItems() {
        addItem("John", arrayOf("House", "Boat", "Candy", "Collection", "Sport", "Ball", "Head"), R.color.pink, R.drawable.ic_ghost)
        addItem("Mary", arrayOf("Dog", "Horse", "Boat"), R.color.blue, R.drawable.ic_ghost)
        addItem("Ana", arrayOf("Cat"), R.color.purple, R.drawable.ic_ghost)
        addItem("Peter", arrayOf("Parrot", "Elephant", "Coffee"), R.color.yellow, R.drawable.ic_ghost)
        addItem("Joseph", arrayOf(), R.color.orange, R.drawable.ic_ghost)
        addItem("Paul", arrayOf("Golf", "Football"), R.color.green, R.drawable.ic_ghost)
        addItem("Larry", arrayOf("Ferrari", "Mazda", "Honda", "Toyota", "Fiat"), R.color.blue, R.drawable.ic_ghost)
        addItem("Moe", arrayOf("Beans", "Rice", "Meat"), R.color.yellow, R.drawable.ic_ghost)
        addItem("Bart", arrayOf("Hamburger", "Ice cream", "Candy"), R.color.purple, R.drawable.ic_ghost)
    }

    private fun addItem(title: String, subItems: Array<String>, colorRes: Int, iconRes: Int) {
        //Let's create an item with R.layout.expanding_layout
        val item = mExpandingList!!.createNewItem(R.layout.expanding_layout)

        //If item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes)
            item.setIndicatorIconRes(iconRes)
            //It is possible to get any view inside the inflated layout. Let's set the text in the item
            (item.findViewById(R.id.title) as TextView).text = title

            //We can create items in batch.
            item.createSubItems(subItems.size)
            for (i in 0 until item.subItemsCount) {
                //Let's get the created sub item by its index
                val view = item.getSubItemView(i)

                //Let's set some values in
                configureSubItem(item, view, subItems[i])
            }
            item.findViewById<ImageView>(R.id.add_more_sub_items).setOnClickListener(View.OnClickListener {
                showInsertDialog(object : OnItemCreated {
                    override fun itemCreated(title: String) {
                        val newSubItem = item.createSubItem()
                        configureSubItem(item, newSubItem!!, title)
                    }
                })
            })

            item.findViewById<ImageView>(R.id.remove_item)
                    .setOnClickListener(View.OnClickListener { mExpandingList!!.removeItem(item) })
        }
    }

    private fun configureSubItem(item: ExpandingItem?, view: View, subTitle: String) {
        (view.findViewById(R.id.sub_title) as TextView).text = subTitle
        view.findViewById<ImageView>(R.id.remove_sub_item)
                .setOnClickListener(View.OnClickListener { item!!.removeSubItem(view) })
    }

    private fun showInsertDialog(positive: OnItemCreated) {
        val text = EditText(this)
        val builder = AlertDialog.Builder(this)
        builder.setView(text)
        builder.setTitle(R.string.enter_title)
        builder.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which -> positive.itemCreated(text.text.toString()) })
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    internal interface OnItemCreated {
        fun itemCreated(title: String)
    }
}
