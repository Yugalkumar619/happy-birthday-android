package com.app.happy_birthday.data_source.local_db

import android.content.Context
import com.app.happy_birthday.helper.Extensions.printLog

object LocalDB {

    fun Context.getDao() : AppDatabase {
       return AppDatabase.getAppDatabase(this)
    }

    fun Context.addToCartInDB(products: ProductDataDbModel): ArrayList<ProductDataDbModel?> {
        val db = AppDatabase.getAppDatabase(this).cartDao()
        if (db?.isProductPresentInCart(products.entityID) == true) {
            val qty = db.getQtyInCart(products.entityID)
            db.updateProductsInCart(qty + 1, products.entityID)
        } else {
            db?.addProductToCart(products)
        }
        ("Here i am add to cart product 333  ${db?.getAllCartProducts()}").printLog()
        return db?.getAllCartProducts() as ArrayList<ProductDataDbModel?>
    }

    fun Context.getAllCartListFromDB(): ArrayList<ProductDataDbModel?>? {
        val db = AppDatabase.getAppDatabase(this).cartDao()
        return db?.getAllCartProducts() as ArrayList<ProductDataDbModel?>?
    }
    fun Context.deleteCartTableFromDB() {
        val db = AppDatabase.getAppDatabase(this).cartDao()
        return db?.deleteCartTable() ?: Unit
    }

    fun Context.updateCartInDb(
        productID: String? = "0",
        qty: String? = "1"
    ): ArrayList<ProductDataDbModel?> {
        ("Here i am update cart qty   $productID, $qty").printLog()
        val db = AppDatabase.getAppDatabase(this).cartDao()
        if (db?.isProductPresentInCart(productID) == true) {
            db.updateProductsInCart(qty?.toInt(), productID)
        }
        return db?.getAllCartProducts() as ArrayList<ProductDataDbModel?>
    }

    fun Context.deleteCartInDb(productEntityID: String? = "0"): ArrayList<ProductDataDbModel?>? {
        ("Here i am delete cart id   $productEntityID").printLog()
        val db = AppDatabase.getAppDatabase(this).cartDao()
        db?.deleteProductFromCart(productEntityID)
        return db?.getAllCartProducts() as ArrayList<ProductDataDbModel?>?
    }
}