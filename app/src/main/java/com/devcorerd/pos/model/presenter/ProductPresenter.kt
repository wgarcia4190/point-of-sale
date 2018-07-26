package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.service.ProductService
import com.devcorerd.pos.model.table.ProductTable
import ru.arturvasilov.sqlite.core.Where
import ru.arturvasilov.sqlite.rx.RxSQLite

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

    fun getProducts(successCallback: (products: MutableList<Product>) -> Unit,
                    errorCallback: (error: Throwable) -> Unit){

        RxSQLite.get().query(ProductTable.TABLE).subscribe({products: MutableList<Product>? ->
            successCallback(products!!)
        },{error: Throwable ->
            errorCallback(error)

        })
    }

    fun getFavoriteProducts(successCallback: (products: MutableList<Product>) -> Unit,
                    errorCallback: (error: Throwable) -> Unit){

        RxSQLite.get().query(ProductTable.TABLE,
                Where.create().equalTo("isFavorite", 1))
                .subscribe({ products: MutableList<Product>? ->
            successCallback(products!!)
        },{error: Throwable ->
            errorCallback(error)

        })
    }

    fun updateProduct(product: Product, successCallback: () -> Unit,
                      errorCallback: (error: Throwable) -> Unit) {
        RxSQLite.get().update(ProductTable.TABLE,
                Where.create().equalTo("name", product.name), product)
                .subscribe({
                    successCallback()
                },{error: Throwable ->
                    errorCallback(error)

                })
    }

}