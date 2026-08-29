package com.app.happy_birthday.data_source.local_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    fun addProductToCart(vararg products: ProductDataDbModel)

    @Query("UPDATE ProductDataDbModel SET quantity =:strQty WHERE entityID = :productEntityID")
    fun updateProductsInCart(strQty: Int?, productEntityID: String?)

    @Query("UPDATE ProductDataDbModel SET quantity =:strQty,productID =:productID,entityID =:productEntityID," +
            "configOptions = :configurableOptionModelJson WHERE entityID = :productEntityID")
    fun updateProductsInCartOffline(
        strQty: String?,
        productID: String?,
        productEntityID: String?,
        configurableOptionModelJson: String?
    )

    @Query("DELETE from ProductDataDbModel where entityID=:productEntityID")
    fun deleteProductFromCart(productEntityID: String?)

    @Query("SELECT DISTINCT quantity FROM ProductDataDbModel WHERE entityID IN (:productEntityID)")
    fun getQtyInCart(productEntityID: String?): Int

    @Query("SELECT * FROM ProductDataDbModel")
    fun getTotalCartProductCount(): List<ProductDataDbModel>

    @Query("SELECT entityID FROM ProductDataDbModel WHERE entityID=:productEntityID")
    fun isProductPresentInCart(productEntityID: String?): Boolean

    @Query("SELECT * FROM ProductDataDbModel")
    fun getAllCartProducts(): List<ProductDataDbModel>

    @Query("DELETE FROM ProductDataDbModel")
    fun deleteCartTable()
}