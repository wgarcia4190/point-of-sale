package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.service.ProductService
import com.devcorerd.pos.model.table.CategoryTable
import com.devcorerd.pos.model.table.ProductTable
import org.joda.time.DateTime
import org.json.JSONObject
import ru.arturvasilov.sqlite.core.Where
import ru.arturvasilov.sqlite.rx.RxSQLite
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author Ing. Wilson Garcia
 * Created on 7/23/18
 */
class ProductPresenter(private val context: Context?,
                       private var mainService:
                       ProductService? = ProductService(context)) : Presenter() {

    constructor() : this(null, null)

    override fun initService(context: AppCompatActivity?) {
        super.initService(context)
        mainService = ProductService(context)
    }

    fun addProduct(product: Product, successCallback: () -> Unit,
                   errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().insert(ProductTable.TABLE, product).subscribe({
            successCallback()
        }, { error: Throwable ->
            errorCallback(error)

        }).dispose()
    }

    fun addProducts(products: MutableList<Product>) {

        RxSQLite.get().insert(ProductTable.TABLE, products).subscribe().dispose()
    }

    fun getProducts(successCallback: (products: MutableList<Product>) -> Unit,
                    errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(ProductTable.TABLE).subscribe({ products: MutableList<Product>? ->
            successCallback(products!!)
        }, { error: Throwable ->
            errorCallback(error)

        })
    }

    fun getProductByBarCode(barcode: String, successCallback: (product: Product?) -> Unit,
                            errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(ProductTable.TABLE, Where.create().equalTo(ProductTable.barcode, barcode))
                .subscribe({
                    if (it != null && it.isNotEmpty())
                        successCallback(it[0])
                    else
                        successCallback(null)
                }, { error: Throwable ->
                    errorCallback(error)

                })
    }

    fun getFavoriteProducts(successCallback: (products: MutableList<Product>) -> Unit,
                            errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(ProductTable.TABLE,
                Where.create().equalTo("isFavorite", 1))
                .subscribe({ products: MutableList<Product>? ->
                    successCallback(products!!)
                }, { error: Throwable ->
                    errorCallback(error)

                })
    }

    fun updateProduct(product: Product, successCallback: () -> Unit,
                      errorCallback: (error: Throwable) -> Unit) {
        RxSQLite.get().update(ProductTable.TABLE,
                Where.create().equalTo("name", product.name), product)
                .subscribe({
                    successCallback()
                }, { error: Throwable ->
                    errorCallback(error)

                })
    }

    fun updateCategoryProduct(category: Category) {
        RxSQLite.get().update(CategoryTable.TABLE,
                Where.create().equalTo("name", category.name), category)
                .subscribe({})
    }

    fun getProductsFromServer(successCallback: (products: MutableList<Product>) -> Unit,
                               errorCallback: (error: Throwable) -> Unit) {

        manageSubscription(subscription = mainService!!.API.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val jsonObject = JSONObject(it.body().string())
                    val productsArray = jsonObject.optJSONArray("items")
                    val products = mutableListOf<Product>()

                    for (index in 0 until productsArray.length()) {
                        val productJson = productsArray.getJSONObject(index)

                        val product = Product(productJson.optString("nombre"),
                                productJson.optString("nombre"),
                                ConstantsHelper.defaultCategoryName,
                                ConstantsHelper.defaultCategoryColor,
                                'L', productJson.optDouble("precio"),
                                productJson.optString("idprod"), "", "",
                                false, false,
                                DateTime.now(), DateTime.now())
                        products.add(product)
                    }

                    addProducts(products)
                    successCallback(products)

                    UIHelper.hideLoadingDialog()
                }, { error: Throwable ->
                    error.printStackTrace()
                    UIHelper.hideLoadingDialog()
                    errorCallback(error)
                }), showLoading = true)

    }

}