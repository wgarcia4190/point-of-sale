package com.devcorerd.pos.view.main.item

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.*
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.BarcodePresenter
import com.devcorerd.pos.model.presenter.ProductPresenter
import com.devcorerd.pos.view.main.barcode.BarcodeReaderFragment
import com.devcorerd.pos.view.main.category.CategorySelectorFragment
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import kotlinx.android.synthetic.main.update_product_fragment.*
import me.dm7.barcodescanner.zbar.Result
import org.apache.commons.codec.binary.Base64
import org.json.JSONObject
import java.io.File
import java.util.concurrent.Executors

/**
 * Created by wgarcia on 7/31/2018.
 */
class UpdateProductFragment: FragmentBase(), OnScanCompleted, OnCategorySelected {

    private var measureUnit: Char = 'U'
    private lateinit var updateListener: OnProductUpdated
    private lateinit var deleteListener: OnProductDeleted
    private lateinit var selectedProduct: Product
    private lateinit var imagePicker: ImagePicker
    private var selectedCategory: Category? = null
    private var selectedImage: String? = null
    private var position = 0


    private val barcodePresenter: BarcodePresenter = BarcodePresenter()

    companion object {
        @JvmStatic
        fun newInstance(updateListener: OnProductUpdated, deleteListener: OnProductDeleted,
                        product: Product, position: Int): UpdateProductFragment {
            val fragmentBase = UpdateProductFragment()
            val layout: Int = R.layout.update_product_fragment

            fragmentBase.updateListener = updateListener
            fragmentBase.deleteListener = deleteListener
            fragmentBase.selectedProduct = product
            fragmentBase.position = position
            fragmentBase.createBundle(layout, ProductPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagePicker = UIHelper.createImagePicker(this)
        barcodePresenter.initService(activity!! as AppCompatActivity?)
        setupEvents()

        toolbarTitle.text = selectedProduct.name
        categoryName.text = selectedProduct.category

        name.setText(selectedProduct.name)
        description.setText(selectedProduct.description)
        price.setText(selectedProduct.price.toString())
        sku.setText(selectedProduct.sku)
        barcodeText.setText(selectedProduct.barcode)
        soldByGroup.check(if (selectedProduct.soldBy == 'U') R.id.unityRadio else R.id.weightRadio )


        if(!selectedProduct.hasImage)
            productImage.setBackgroundColor(Color.parseColor(selectedProduct.categoryColor))
        else
            productImage.setImageBitmap(Helper.getBitmapFromString(selectedProduct.representation))
    }

    private fun setupEvents() {
        setupTextWatcher(name, price, sku)
        backButton.setOnClickListener {
            removeFragment()
        }

        updateProductButton.setOnClickListener {
            val hasImage = selectedImage != null

            selectedProduct.name = name.text.toString()
            selectedProduct.description = description.text.toString()
            selectedProduct.price = price.text.toString().toDouble()
            selectedProduct.soldBy = measureUnit
            selectedProduct.representation = if (hasImage) selectedImage!! else ""
        }

        soldByGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            measureUnit = radio.tag.toString().single()
        }

        productImage.setOnClickListener { _: View? ->
            if (!Helper.checkPermissions(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                        ConstantsHelper.writeReadCode)
            } else
                imagePicker.start()
        }

        barcodeButton.setOnClickListener {
            if (!Helper.checkPermissions(context!!, Manifest.permission.CAMERA)) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),
                        ConstantsHelper.cameraCode)
            } else
                stackFragmentToTop(BarcodeReaderFragment.newInstance(this), R.id.mainContainer,
                        false)
        }

        categoryButton.setOnClickListener {
            stackFragmentToTop(CategorySelectorFragment.newInstance(this), R.id.mainContainer,
                    false)
        }
    }

    private fun setupTextWatcher(vararg editTexts: EditText) {
        for (editText in editTexts)
            editText.addTextChangedListener(object : TextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)

                    var activateButton = true
                    for (innerEditText in editTexts) {
                        if (innerEditText.text.toString().isEmpty()) {
                            activateButton = false
                            break
                        }
                    }
                    updateProductButton.isEnabled = activateButton
                }
            })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ConstantsHelper.writeReadCode -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    UIHelper.showMessage(context!!, "Error",
                            context!!.getString(R.string.not_file_permission_granted))
                else
                    imagePicker.start()

            }
            ConstantsHelper.cameraCode -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    UIHelper.showMessage(context!!, "Error",
                            context!!.getString(R.string.not_file_permission_granted))
                else
                    stackFragmentToTop(BarcodeReaderFragment.newInstance(this), R.id.mainContainer,
                            false)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image: Image? = ImagePicker.getFirstImageOrNull(data)

            if (image != null) {
                UIHelper.addLoadingToImage(context, productImage)

                Executors.newSingleThreadExecutor().submit {
                    val file = File(image.path)
                    val base64String = Helper.getFileAsBase64(file)

                    activity!!.runOnUiThread {
                        if (base64String != null) {
                            selectedImage = base64String
                            UIHelper.addBitmapToImage(file, productImage)
                        }
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onScanComple(barcode: Result?) {
        barcodeText.setText(barcode?.contents)
        if (Helper.isInternetAvailable(context!!)) {

            barcodePresenter.getProductInformation(barcode!!.contents,
                    String(Base64.encodeBase64(Helper.computeHash(barcode.contents))),
                    { responseJson: JSONObject ->
                        description.setText(responseJson.optString("description", ""))
                        UIHelper.loadImage(context!!, responseJson.optString("image", ""),
                                productImage, true) {
                            selectedImage = Helper.getBitmapAsBase64(productImage.drawingCache)
                        }
                    }, { error: Throwable ->
                error.printStackTrace()
                UIHelper.showMessage(context!!, "Error obteniendo información del producto", error.message!!)
            })
        }
    }

    override fun onCategorySelected(category: Category) {
        selectedCategory = category
        categoryName.text = category.name

        selectedProduct.categoryColor = category.color
        selectedProduct.category = category.name
        productImage.setBackgroundColor(Color.parseColor(category.color))
    }



}