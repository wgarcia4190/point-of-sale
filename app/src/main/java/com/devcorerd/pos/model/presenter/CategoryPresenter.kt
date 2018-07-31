package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.service.CustomerService
import com.devcorerd.pos.model.table.CategoryTable
import com.devcorerd.pos.model.table.ProductTable
import org.joda.time.DateTime
import ru.arturvasilov.sqlite.core.Where
import ru.arturvasilov.sqlite.rx.RxSQLite
import android.content.ContentValues
import com.devcorerd.pos.helper.ConstantsHelper


/**
 * Created by wgarcia on 7/26/2018.
 */
class CategoryPresenter(private val context: Context?,
                        private var mainService:
                        CustomerService? = CustomerService(context)) : Presenter() {

    constructor() : this(null, null)

    override fun initService(context: AppCompatActivity?) {
        super.initService(context)
        mainService = CustomerService(context)
    }

    fun addCategory(category: Category, successCallback: () -> Unit,
                    errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().insert(CategoryTable.TABLE, category).subscribe({
            successCallback()
        }, { error: Throwable ->
            errorCallback(error)
        }).dispose()
    }

    fun getCategory(successCallback: (category: Category?) -> Unit) {

        RxSQLite.get().query(CategoryTable.TABLE).subscribe({ categories: MutableList<Category>? ->
            if (categories != null && categories.isNotEmpty())
                successCallback(categories[0])
            else
                successCallback(null)
        }, { error: Throwable ->
            error.printStackTrace()
            successCallback(null)
        }).dispose()
    }

    fun getCategories(successCallback: (categories: MutableList<Category>) -> Unit,
                      errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(CategoryTable.TABLE).subscribe({ categories: MutableList<Category>? ->
            successCallback(categories!!)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()
    }

    fun updateCategory(category: Category, successCallback: () -> Unit,
                       errorCallback: (error: Throwable) -> Unit){
        RxSQLite.get().update(CategoryTable.TABLE, Where.create()
                .equalTo(CategoryTable.name, category.name), category).subscribe({
            successCallback()
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)

        })
    }

    fun getProductsByCategory(categoryName: String, successCallback: (products: MutableList<Product>?) -> Unit,
                              errorCallback: (error: Throwable) -> Unit) {
        RxSQLite.get().query(ProductTable.TABLE, Where.create()
                .equalTo(ProductTable.category, categoryName))
                .subscribe({ products: MutableList<Product>? ->
                    successCallback(products!!)
                }, { error: Throwable ->
                    error.printStackTrace()
                    errorCallback(error)
                }).dispose()
    }

    fun getProducts(successCallback: (products: MutableList<Product>) -> Unit,
                    errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(ProductTable.TABLE).subscribe({ products: MutableList<Product>? ->
            successCallback(products!!)
        }, { error: Throwable ->
            errorCallback(error)

        })
    }

    fun updateProducts(products: MutableList<Product>, category: Category, successCallback: () -> Unit) {
        for (product in products) {
            product.modificationDate = DateTime.now()
            product.category = category.name
            product.categoryColor = category.color

            RxSQLite.get().querySingle(CategoryTable.TABLE, Where.create()
                    .equalTo(CategoryTable.name, product.category)).subscribe {
                it.totalItems -= 1
                it.modificationDate = DateTime.now()

                RxSQLite.get().update(CategoryTable.TABLE, Where.create().equalTo(CategoryTable.name,
                        it.name), it).subscribe {

                    RxSQLite.get().update(ProductTable.TABLE, Where.create()
                            .equalTo(ProductTable.name, product.name), product).subscribe {
                        successCallback()
                    }
                }

            }
        }
    }

    fun deleteCategory(category: Category, successCallback: () -> Unit,
                       errorCallback: (error: Throwable) -> Unit){

        RxSQLite.get().delete(CategoryTable.TABLE, Where.create().equalTo(CategoryTable.name,
                category.name)).subscribe({

            val cv = ContentValues()
            val args = arrayOf(category.name)
            cv.put(ProductTable.category, ConstantsHelper.defaultCategoryName)

            ProductTable.database.update(ProductTable.TABLE.tableName, cv,
                    "category = ?", args)
            successCallback()
        },{error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        })
    }
}
