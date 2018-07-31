package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.listener.OnCategoryDeleted
import com.devcorerd.pos.listener.OnCategoryUpdated
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.CategoryPresenter
import com.devcorerd.pos.view.viewholder.ProductSelectionViewHolder
import kotlinx.android.synthetic.main.update_category_fragment.*
import org.joda.time.DateTime
import petrov.kristiyan.colorpicker.ColorPicker
import java.util.concurrent.Executors

/**
 * Created by wgarcia on 7/31/2018.
 */
class UpdateCategoryFragment : FragmentBase() {
    private lateinit var colorPicker: ColorPicker
    private lateinit var updateListener: OnCategoryUpdated
    private lateinit var deleteListener: OnCategoryDeleted
    private lateinit var selectedCategory: Category

    private var position: Int = 0
    private var selectedProducts: MutableList<Product> = mutableListOf()

    private lateinit var productList: MutableList<Product>
    private val adapter: Adapter<Product, ProductSelectionViewHolder> by lazy {
        Adapter(productList, productListRV.context, {
            val view: View = LayoutInflater.from(productListRV.context)
                    .inflate(R.layout.category_product_selection_item, productListRV, false)
            ProductSelectionViewHolder(view, selectedCategory)
        }, object : OnClickListener<Product> {
            override fun onClick(entity: Product?, `object`: Any?) {
                val isChecked: Boolean = `object` as Boolean
                if (isChecked)
                    selectedProducts.add(entity!!)
                else
                    selectedProducts.remove(entity!!)
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(updateListener: OnCategoryUpdated, deleteListener: OnCategoryDeleted,
                        position: Int, selectedCategory: Category):
                UpdateCategoryFragment {

            val fragmentBase = UpdateCategoryFragment()
            val layout: Int = R.layout.update_category_fragment

            fragmentBase.updateListener = updateListener
            fragmentBase.deleteListener = deleteListener
            fragmentBase.selectedCategory = selectedCategory
            fragmentBase.position = position
            fragmentBase.createBundle(layout, CategoryPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()

        name.setText(selectedCategory.name)

        productsContainer.visibility = View.VISIBLE
        getProducts()

    }

    private fun getProducts() {
        (presenter as CategoryPresenter).getProducts({
            productList = it

            productListRV.setHasFixedSize(false)
            productListRV.layoutManager = LinearLayoutManager(context!!)
            productListRV.adapter = adapter
        }, { error: Throwable ->
            error.printStackTrace()
        })
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        name.addTextChangedListener(object : TextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                updateCategoryButton.isEnabled = count > 0
            }
        })

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

        updateCategoryButton.setOnClickListener {
            selectedCategory.totalItems = productList.size
            selectedCategory.modificationDate = DateTime.now()

            (presenter as CategoryPresenter).updateCategory(selectedCategory, {
                Executors.newSingleThreadExecutor().submit {
                    (presenter as CategoryPresenter).updateProducts(productList, selectedCategory) {
                        activity!!.runOnUiThread {
                            updateListener.onCategoryUpdated(position, selectedCategory)
                            backButton.performClick()
                        }
                    }
                }
            }, { error: Throwable ->
                error.printStackTrace()
            })
        }

        deleteCategoryButton.setOnClickListener {
            (presenter as CategoryPresenter).deleteCategory(selectedCategory, {
                deleteListener.onCategoryDeleted(position)
                backButton.performClick()
            }, { error: Throwable ->
                error.printStackTrace()
            })
        }
    }
}