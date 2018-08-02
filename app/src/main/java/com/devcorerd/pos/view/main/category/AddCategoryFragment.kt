package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnCategoryAdded
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.CategoryPresenter
import com.devcorerd.pos.view.viewholder.ProductSelectionViewHolder
import kotlinx.android.synthetic.main.add_category_fragment.*
import org.joda.time.DateTime
import petrov.kristiyan.colorpicker.ColorPicker
import java.util.concurrent.Executors

@Suppress("DEPRECATION")
/**
 * @author Ing. Wilson Garcia
 * Created on 7/27/18
 */
class AddCategoryFragment : FragmentBase() {
    private lateinit var colorPicker: ColorPicker
    private lateinit var listener: OnCategoryAdded
    private var showItems = false

    private var selectedProducts: MutableList<Product> = mutableListOf()

    private lateinit var productList: MutableList<Product>
    private val adapter: Adapter<Product, ProductSelectionViewHolder> by lazy {
        Adapter(productList, productListRV.context, {
            val view: View = LayoutInflater.from(productListRV.context)
                    .inflate(R.layout.category_product_selection_item, productListRV, false)
            ProductSelectionViewHolder(view, null)
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
        fun newInstance(listener: OnCategoryAdded, showItems: Boolean = false):
                AddCategoryFragment {

            val fragmentBase = AddCategoryFragment()
            val layout: Int = R.layout.add_category_fragment

            fragmentBase.listener = listener
            fragmentBase.showItems = showItems
            fragmentBase.createBundle(layout, CategoryPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()


        if (!showItems)
            productsContainer.visibility = View.GONE
        else {
            productsContainer.visibility = View.VISIBLE
            getProducts()
        }
    }

    private fun getProducts() {
        (presenter as CategoryPresenter).getProducts({
            productList = it

            productListRV.setHasFixedSize(false)
            productListRV.layoutManager = LinearLayoutManager(context!!)
            productListRV.adapter = adapter

            if (productList.isEmpty())
                productsContainer.visibility = View.GONE
        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error cargando productos", error.message!!)
            error.printStackTrace()
        })
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            removeFragment()
        }

        name.addTextChangedListener(object : TextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)

                if (count > 0) {
                    createCategoryButton.isEnabled = true
                    createCategoryButton.setTextColor(resources.getColor(R.color.white))
                } else {
                    createCategoryButton.isEnabled = false
                    createCategoryButton.setTextColor(resources.getColor(R.color.deselected))
                }
            }
        })

        createCategoryButton.setOnClickListener {
            val category = Category(name.text.toString(), colorText.text.toString(),
                    productList.size, DateTime.now(), DateTime.now())
            (presenter as CategoryPresenter).addCategory(category, {

                if (productList.isNotEmpty()) {
                    Executors.newSingleThreadExecutor().submit {
                        (presenter as CategoryPresenter).updateProducts(productList, category) {
                            activity!!.runOnUiThread {
                                listener.onCategoryAdded(category)
                                backButton.performClick()
                            }
                        }
                    }

                } else {
                    listener.onCategoryAdded(category)
                    backButton.performClick()
                }

            }, { error: Throwable ->
                UIHelper.showMessage(context!!, "Error creando categoria", error.message!!)
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