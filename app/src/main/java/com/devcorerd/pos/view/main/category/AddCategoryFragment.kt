package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.listener.OnCategoryAdded
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.presenter.CategoryPresenter
import kotlinx.android.synthetic.main.add_category_fragment.*
import org.joda.time.DateTime
import petrov.kristiyan.colorpicker.ColorPicker

/**
 * @author Ing. Wilson Garcia
 * Created on 7/27/18
 */
class AddCategoryFragment : FragmentBase() {
    private lateinit var colorPicker: ColorPicker
    private lateinit var listener: OnCategoryAdded

    companion object {
        @JvmStatic
        fun newInstance(listener: OnCategoryAdded):
                AddCategoryFragment {

            val fragmentBase = AddCategoryFragment()
            val layout: Int = R.layout.add_category_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout, CategoryPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
    }

    private fun setupEvents() {

        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        name.addTextChangedListener(object : TextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)

                createCategoryButton.isEnabled = count > 0
            }
        })

        createCategoryButton.setOnClickListener {
            val category = Category(name.text.toString(), colorText.text.toString(),
                    DateTime.now(), DateTime.now())
            (presenter as CategoryPresenter).addCategory(category, {
                listener.onCategoryAdded(category)
                backButton.performClick()
            }, { error: Throwable ->
                error.printStackTrace()
            })
        }

        colorPickerButton.setOnClickListener {
            colorPicker = ColorPicker(activity!!)
            colorPicker
                    .disableDefaultButtons(false)
                    .setColumns(5)
                    .setTitle(context!!.getString(R.string.choose_color))
                    .setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                        override fun onChooseColor(position: Int, color: Int) {
                            colorIndicator.setBackgroundColor(color)
                            colorText.text = String.format("#%06X", 0xFFFFFF and color)
                        }

                        override fun onCancel() {}

                    }).show()
        }
    }
}